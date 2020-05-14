package com.alibaba.alink.mapper;

import com.alibaba.alink.pojo.Locate;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LocateMapper {


    @Select("select latitude,longitude,mode from a")
    public List<Locate> getLocate();

}
