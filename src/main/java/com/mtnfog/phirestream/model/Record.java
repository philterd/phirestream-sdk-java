package com.mtnfog.phirestream.model;

import com.google.gson.annotations.SerializedName;

public class Record {

    @SerializedName("key")
    private String key;

    @SerializedName("value")
    private String value;

    public Record() {

    }

    public Record(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
