package com.alibaba.alink.pojo;

public class InfoSorted extends Info{

    private String sort;

    public InfoSorted(Info info)
    {
        super(info.getLongitude(),info.getLatitude(),info.getTimeStamp());
    }

    public InfoSorted(String longitude, String latitude, String timeStamp, String sort) {
        super(longitude, latitude, timeStamp);
        this.sort = sort;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "InfoSorted{" +
                "sort='" + sort + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
