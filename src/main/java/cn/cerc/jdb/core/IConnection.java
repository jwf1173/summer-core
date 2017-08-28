package cn.cerc.jdb.core;

public interface IConnection {

    // 设置连接
    public void setConfig(IConfig config);

    // 初始化
    public void init();

    // 返回会话
    public Object getSession();

}
