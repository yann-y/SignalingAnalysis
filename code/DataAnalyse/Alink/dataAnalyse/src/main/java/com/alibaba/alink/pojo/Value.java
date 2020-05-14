package com.alibaba.alink.pojo;

public class Value {

    public String time;

    public String distance;

    public String speed;

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public Value(){}

    public Value(String time, String distance,String speed) {
        this.time = time;
        this.speed = speed;
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Value{" +
                "time='" + time + '\'' +
                ", distance='" + distance + '\'' +
                ", speed='" + speed + '\'' +
                '}';
    }
}
