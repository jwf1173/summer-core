package cn.cerc.jdb.core;

public interface ISyncDataSet {
    public void process(Record src, Record tar) throws SyncUpdateException;
}
