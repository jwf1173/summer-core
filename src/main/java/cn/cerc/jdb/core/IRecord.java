package cn.cerc.jdb.core;

import java.util.Map;

public interface IRecord {

    public boolean exists(String field);

    public boolean getBoolean(String field);

    public int getInt(String field);

    public double getDouble(String field);

    public String getString(String field);

    public TDate getDate(String field);

    public TDateTime getDateTime(String field);

    public IRecord setField(String field, Object value);

    public Object getField(String field);

    default public boolean equalsValues(Map<String, Object> values) {
        for (String field : values.keySet()) {
            Object obj1 = getField(field);
            String value = obj1 == null ? "null" : obj1.toString();
            Object obj2 = values.get(field);
            String compareValue = obj2 == null ? "null" : obj2.toString();
            if (!value.equals(compareValue))
                return false;
        }
        return true;
    }
}
