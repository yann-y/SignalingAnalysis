package com.bigdata.visualization.modeSplit.pojo;


import com.bigdata.visualization.heatMap.pojo.Coord;

import java.util.List;

public class ModeData {


    private String method;

    private List<Coord> coords;


    public ModeData(){}

    public ModeData(String method, List<Coord> coords) {
        this.method = method;
        this.coords = coords;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Coord> getCoords() {
        return coords;
    }

    public void setCoords(List<Coord> coords) {
        this.coords = coords;
    }

    @Override
    public String toString() {
        return "ModeData{" +
                "method='" + method + '\'' +
                ", coords=" + coords +
                '}';
    }
}
