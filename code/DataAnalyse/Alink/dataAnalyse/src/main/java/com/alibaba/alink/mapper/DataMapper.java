package com.alibaba.alink.mapper;

import com.alibaba.alink.pojo.SortedData;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface DataMapper {
    @Insert("insert into gaussianmixture values(#{sortedData.method},#{sortedData.coords})")
    public void addGaussianMixtureData(@Param("sortedData") SortedData sortedData);

    @Delete("delete from gaussianmixture")
    public void deleteGaussianMixtureData();

    @Insert("insert into  kmeans values(#{sortedData.method},#{sortedData.coords})")
    public void addKMeansData(@Param("sortedData") SortedData sortedData);

    @Delete("delete from  kmeans")
    public void deleteKMeansData();

    @Insert("insert into bisectingkmeans values(#{sortedData.method},#{sortedData.coords})")
    public void addBisectingKMeansData(@Param("sortedData") SortedData sortedData);

    @Delete("delete from bisectingkmeans")
    public void deleteBisectingKMeansData();




}
