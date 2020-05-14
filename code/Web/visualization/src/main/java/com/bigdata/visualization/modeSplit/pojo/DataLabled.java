package com.bigdata.visualization.modeSplit.pojo;

public class DataLabled {


    private String method;

    private String coords;


    public DataLabled() {
    }

    public DataLabled(String method, String coords) {
        this.method = method;
        this.coords = coords;
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

    public void setCoords(String coords) {
        this.coords = coords;
    }


    @Override
    public String toString() {
        return "DataLabled{" +
                "method='" + method + '\'' +
                ", coords='" + coords + '\'' +
                '}';
    }
}
