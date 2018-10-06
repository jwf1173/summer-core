package cn.cerc.jdb.core;

public abstract class DataQuery extends DataSet {
    private static final long serialVersionUID = 7316772894058168187L;
    // 批次保存模式，默认为post与delete立即保存
    private boolean batchSave = false;
    protected SqlText commandText;
    protected boolean active = false;
    protected IHandle handle;

    public DataQuery(IHandle handle) {
        this.handle = handle;
        commandText = new SqlText();
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
        commandText.add(sql);
        return this;
    }

    public DataQuery add(String format, Object... args) {
        commandText.add(format, args);
        return this;
    }

    public String getCommandText() {
        return this.commandText.getSelect();
    }

    /**
     * 将commandText 置为 null
     * 
     * @return 返回自身
     */
    public DataQuery emptyCommand() {
        this.commandText.clear();
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
