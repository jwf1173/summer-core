package cn.cerc.jdb.core;

import java.util.ArrayList;

public abstract class DataQuery extends DataSet {
    private static final long serialVersionUID = 7316772894058168187L;
    // 批次保存模式，默认为post与delete立即保存
    private boolean batchSave = false;
    protected String commandText;
    protected boolean active = false;
    protected IHandle handle;

    public DataQuery(IHandle handle) {
        this.handle = handle;
    }

    // 打开数据集
    public abstract DataQuery open();

    // 批量保存
    public abstract void save();

    // 返回保存操作工具
    public abstract IDataOperator getOperator();

    // 是否批量保存
    public boolean isBatchSave() {
        return batchSave;
    }

    public void setBatchSave(boolean batchSave) {
        this.batchSave = batchSave;
    }

    /**
     * 增加sql指令内容，调用此函数需要自行解决sql注入攻击！
     * 
     * @param sql
     *            要增加的sql指令内容
     * @return 返回对象本身
     */
    public DataQuery add(String sql) {
        if (commandText == null)
            commandText = sql;
        else
            commandText = commandText + " " + sql;
        return this;
    }

    public DataQuery add(String format, Object... args) {
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

    /**
     * 设置要执行的sql指令（已停用，请改使用add函数，以防止注入攻击）
     * 
     * @param sql
     *            要执行的sql指令
     */
    @Deprecated
    public void setCommandText(String sql) {
        this.commandText = sql;
    }

    public String getCommandText() {
        return this.commandText;
    }

    /**
     * 将commandText 置为 null
     * 
     * @return 返回自身
     */
    public DataQuery emptyCommand() {
        this.commandText = null;
        return this;
    }

    public boolean getActive() {
        return active;
    }

    public DataQuery setActive(boolean active) {
        this.active = active;
        return this;
    }
}
