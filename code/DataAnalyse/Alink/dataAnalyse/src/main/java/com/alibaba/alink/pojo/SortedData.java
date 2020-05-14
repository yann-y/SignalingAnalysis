package com.alibaba.alink.pojo;

public class SortedData {

    private String method;


    private String coords;

    public SortedData(){}

    public SortedData(String method, String cooreds) {
        this.method = method;
        this.coords = cooreds;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String cooreds) {
        this.coords = cooreds;
    }
}
