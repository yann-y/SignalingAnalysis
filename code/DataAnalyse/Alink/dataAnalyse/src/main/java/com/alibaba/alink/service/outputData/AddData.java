package com.alibaba.alink.service.outputData;


import com.alibaba.alink.mapper.DataMapper;
import com.alibaba.alink.pojo.SortedData;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

public class AddData {


    private SqlSessionFactoryBuilder sqlSessionFactoryBuilder;

    private SqlSessionFactory sqlSessionFactory;

    private SqlSession sqlSession;

    public AddData() {
        sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

        try {
            sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("sqlMapConfig.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSession = sqlSessionFactory.openSession();
    }

    public void deleteData(String means) {
        DataMapper dataMapper = sqlSession.getMapper(DataMapper.class);

        if (means.equals("GaussianMixture")) {
            dataMapper.deleteGaussianMixtureData();
        } else if (means.equals("KMeans")) {
            dataMapper.deleteKMeansData();
        } else {
            dataMapper.deleteBisectingKMeansData();
        }

        sqlSession.commit();
    }

    public void addData(SortedData sortedData, String means) {

        DataMapper dataMapper = sqlSession.getMapper(DataMapper.class);

        if (means.equals("GaussianMixture")) {
            dataMapper.addGaussianMixtureData(sortedData);

        } else if (means.equals("KMeans")) {

            dataMapper.addKMeansData(sortedData);
        } else {
            dataMapper.addBisectingKMeansData(sortedData);

        }

        sqlSession.commit();

    }

}
