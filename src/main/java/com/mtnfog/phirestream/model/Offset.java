package com.mtnfog.phirestream.model;

import com.google.gson.annotations.SerializedName;

public class Offset {

    @SerializedName("offset")
    private long offset;

    @SerializedName("partition")
    private long partition;

    public Offset(long offset, long partition) {
        this.offset = offset;
        this.partition = partition;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getPartition() {
        return partition;
    }

    public void setPartition(long partition) {
        this.partition = partition;
    }

}
