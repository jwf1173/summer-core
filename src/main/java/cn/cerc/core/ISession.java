package cn.cerc.core;

public interface ISession {
    String getSessionId();

    // 关闭会话
    public void closeSession();
}
