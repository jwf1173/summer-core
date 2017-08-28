package cn.cerc.jdb.core;

public interface IConfig {
    public String getProperty(String key, String def);

    default public String getProperty(String key) {
        return this.getProperty(key, null);
    }
}
