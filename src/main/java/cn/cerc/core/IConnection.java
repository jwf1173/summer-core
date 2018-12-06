package cn.cerc.core;

public interface IConnection extends AutoCloseable {
    String getClientId();

    // 返回会话
    public Object getClient();
}
