package com.alibaba.alink.pojo;

public class Result {

    private String id;

    private Double time;

    private Double distance;

    private Double speed;

    private Integer sort;

    public Result(String id, Double time, Double distance, Double speed, Integer sort) {
        this.id = id;
        this.time = time;
        this.distance = distance;
        this.speed = speed;
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
