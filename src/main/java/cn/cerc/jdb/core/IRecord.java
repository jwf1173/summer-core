package cn.cerc.jdb.core;

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

}
