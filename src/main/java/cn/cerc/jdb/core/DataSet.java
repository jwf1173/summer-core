package cn.cerc.jdb.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class DataSet extends CustomDataSet implements Serializable {
    // private static final Logger log = Logger.getLogger(DataSet.class);

    private static final long serialVersionUID = 873159747066855363L;

    private Record head = null;
    private FieldDefs head_defs = null;
    //
    // public DataSet() {
    // init(null);
    // }

    @Override
    public void close() {
        if (this.head != null)
            this.head.clear();
        if (this.head_defs != null)
            this.head_defs.clear();
        super.close();
    }

    public Record getHead() {
        if (head_defs == null)
            head_defs = new FieldDefs();
        if (head == null)
            head = new Record(head_defs);
        return head;
    }

    public String getJSON() {
        return this.getJSON(0, this.size() - 1);
    }

    public String getJSON(int beginLine, int endLine) {
        StringBuilder builder = new StringBuilder();

        builder.append("{");
        if (head != null) {
            if (head.size() > 0) {
                builder.append("\"head\":").append(head.toString());
            }
            if (head.size() > 0 && this.size() > 0) {
                builder.append(",");
            }
        }
        if (this.size() > 0) {
            List<String> fields = this.getFieldDefs().getFields();
            Gson gson = new Gson();
            builder.append("\"dataset\":[").append(gson.toJson(fields));
            for (int i = 0; i < this.size(); i++) {
                Record record = this.getRecords().get(i);
                if (i < beginLine || i > endLine)
                    continue;
                Map<String, Object> tmp1 = record.getItems();
                Map<String, Object> tmp2 = new LinkedHashMap<String, Object>();
                for (String field : fields) {
                    Object obj = tmp1.get(field);
                    if (obj == null)
                        tmp2.put(field, "{}");
                    else if (obj instanceof TDateTime)
                        tmp2.put(field, obj.toString());
                    else if (obj instanceof Date)
                        tmp2.put(field, (new TDateTime((Date) obj)).toString());
                    else
                        tmp2.put(field, obj);
                }
                builder.append(",").append(gson.toJson(tmp2.values()).toString());
            }
            builder.append("]");
        }
        builder.append("}");
        // Logger.debug(getClass(),"getJson == "+ buffer.toString());
        return builder.toString();
    }

    public boolean setJSON(String json) {
        if (json == null || json.equals("")) {
            return false;
        }
        if (json.equals("")) {
            this.close();
            return true;
        }
        // JSONObject jsonobj;
        // try {
        // jsonobj = JSONObject.fromObject(json);
        // } catch (JSONException e) {
        // this.close();
        // log.info("JSON format error: " + json);
        // return false;
        // }

        Gson gson = new GsonBuilder().serializeNulls().create();
        Map<String, Object> jsonmap = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());

        if (jsonmap.containsKey("head")) {
            this.getHead().setJSON(jsonmap.get("head"));
        }

        if (jsonmap.containsKey("dataset")) {
            @SuppressWarnings("rawtypes")
            ArrayList dataset = (ArrayList) jsonmap.get("dataset");
            if (dataset != null && dataset.size() > 1) {
                @SuppressWarnings("rawtypes")
                ArrayList fields = (ArrayList) dataset.get(0);
                for (int i = 1; i < dataset.size(); i++) {
                    @SuppressWarnings("rawtypes")
                    ArrayList Recordj = (ArrayList) dataset.get(i);
                    Record record = this.append().getCurrent();
                    for (int j = 0; j < fields.size(); j++) {
                        Object obj = Recordj.get(j);
                        if (obj instanceof Double) {
                            double tmp = (double) obj;
                            if (tmp >= Integer.MIN_VALUE && tmp <= Integer.MAX_VALUE) {
                                Integer val = (int) tmp;
                                if (tmp == val)
                                    obj = val;
                            }
                        }
                        record.setField(fields.get(j).toString(), obj);
                    }
                    this.post();
                }
                this.first();
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return getJSON();
    }

    /**
     * 
     * @param source
     *            要复制的数据源
     * @param includeHead
     *            是否连头部一起复制
     */
    public void appendDataSet(DataSet source, boolean includeHead) {
        this.appendDataSet(source);
        if (includeHead) {
            this.getHead().copyValues(source.getHead(), source.getHead().getFieldDefs());
        }
    }

    // 支持对象序列化
    private void writeObject(ObjectOutputStream out) throws IOException {
        String json = this.getJSON();
        int strLen = json.length();
        out.writeInt(strLen);
        out.write(json.getBytes(Charset.forName("UTF-8")));
    }

    // 支持对象序列化
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int strLen = in.readInt();
        byte[] strBytes = new byte[strLen];
        in.readFully(strBytes);
        String json = new String(strBytes, Charset.forName("UTF-8"));
        this.setJSON(json);
    }

    @Override
    public DataSet appendDataSet(CustomDataSet source) {
        super.appendDataSet(source);
        return this;
    }
}
