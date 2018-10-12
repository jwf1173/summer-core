package cn.cerc.jdb.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

public class SqlText {
    public static int PUBLIC = 1;
    public static int PRIVATE = 2;
    public static int PROTECTED = 4;
    // 从数据库每次加载的最大笔数
    public static final int MAX_RECORDS = 50000;
    private int maximum = MAX_RECORDS;
    private int offset = 0;
    // sql 指令
    private String text;
    private String tableId = null;
    private String baseSelect = null;

    public SqlText() {
        super();
    }

    public SqlText(Class<?> clazz) {
        super();
        for (Annotation anno : clazz.getAnnotations()) {
            if (anno instanceof Entity) {
                Entity entity = (Entity) anno;
                if (!"".equals(entity.name()))
                    tableId = entity.name();
            }
            if (anno instanceof Select) {
                Select obj = (Select) anno;
                if (!"".equals(obj.value())) {
                    baseSelect = obj.value();
                    this.text = obj.value();
                }
            }
        }
        if (baseSelect != null) 
            return;

        if (tableId == null)
            throw new RuntimeException("entity.name or select not define");

        StringBuffer sb = new StringBuffer();
        List<String> fieldItems = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            Column column = null;
            for (Annotation item : field.getAnnotations()) {
                if (item instanceof Column) {
                    column = (Column) item;
                    break;
                }
            }
            if (column != null) {
                String fieldCode = field.getName();
                if (!"".equals(column.name()))
                    fieldCode = column.name();
                if (field.getModifiers() == PUBLIC)
                    fieldItems.add(fieldCode);
                else if (field.getModifiers() == PRIVATE || field.getModifiers() == PROTECTED) {
                    field.setAccessible(true);
                    fieldItems.add(fieldCode);
                }
            }
        }

        sb.append("select ");
        int count = fieldItems.size();
        for (int i = 0; i < count; i++) {
            if (i > 0)
                sb.append(",");
            sb.append(fieldItems.get(i));
        }
        sb.append(" from ").append(tableId);
        this.text = sb.toString();
    }

    public SqlText(String commandText) {
        add(commandText);
    }

    public SqlText(String format, Object... args) {
        add(format, args);
    }

    public SqlText add(String sql) {
        if (sql == null)
            throw new RuntimeException("sql not is null");
        if (text == null)
            text = sql;
        else
            text = text + " " + sql;
        return this;
    }

    public SqlText add(String format, Object... args) {
        ArrayList<Object> items = new ArrayList<>();
        for (Object arg : args) {
            if (arg instanceof String) {
                items.add(Utils.safeString((String) arg));
            } else {
                items.add(arg);
            }
        }
        return this.add(String.format(format, items.toArray()));
    }

    public String getSelect() {
        return getSelect(this.offset);
    }

    public String getSelect(int offset) {
        String sql = this.text;
        if (sql == null || sql.equals(""))
            throw new RuntimeException("[SqlText]CommandText is null ！");

        sql = sql + String.format(" limit %d,%d", offset, this.maximum);
        return sql;
    }

    public SqlText clear() {
        this.text = null;
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getText() {
        return text;
    }

    public String getCommand() {
        String sql = this.getText();
        if (sql == null || sql.equals(""))
            throw new RuntimeException("SqlText.text is null ！");

        if (sql.indexOf("call ") > -1)
            return sql;

        if (this.offset > 0) {
            if (this.maximum < 0)
                sql = sql + String.format(" limit %d,%d", this.offset, MAX_RECORDS + 1);
            else
                sql = sql + String.format(" limit %d,%d", this.offset, this.maximum + 1);
        } else if (this.maximum == MAX_RECORDS) {
            sql = sql + String.format(" limit %d", this.maximum + 2);
        } else if (this.maximum > -1) {
            sql = sql + String.format(" limit %d", this.maximum + 1);
        } else if (this.maximum == 0) {
            sql = sql + String.format(" limit %d", 0);
        }
        return sql;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        if (maximum > MAX_RECORDS)
            throw new RuntimeException(String.format("本次请求的记录数超出了系统最大笔数为  %d 的限制！", MAX_RECORDS));
        this.maximum = maximum;
    }

    public String getTableId() {
        return tableId;
    }

    public String getBaseSelect() {
        return baseSelect;
    }
}
