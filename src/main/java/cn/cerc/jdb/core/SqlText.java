package cn.cerc.jdb.core;

import java.util.ArrayList;

public class SqlText {
    // 从数据库每次加载的最大笔数
    private int batchSize = 50000;
    private int offset = 0;
    // sql 指令
    private String sqlText;

    public SqlText() {

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
        if (sqlText == null)
            sqlText = sql;
        else
            sqlText = sqlText + " " + sql;
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
        String sql = this.sqlText;
        if (sql == null || sql.equals(""))
            throw new RuntimeException("[SqlText]CommandText is null ！");

        sql = sql + String.format(" limit %d,%d", offset, this.batchSize);
        return sql;
    }

    public SqlText clear() {
        this.sqlText = null;
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

}
