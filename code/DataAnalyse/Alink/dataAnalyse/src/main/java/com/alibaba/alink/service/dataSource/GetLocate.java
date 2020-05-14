package com.alibaba.alink.service.dataSource;

import com.alibaba.alink.mapper.LocateMapper;
import com.alibaba.alink.pojo.Locate;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetLocate {




    public static HashMap<String,String> getLocate()
    {

        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

        SqlSessionFactory sqlSessionFactory = null;
        try {
            sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("sqlMapConfig.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        SqlSession sqlSession =  sqlSessionFactory.openSession();

        LocateMapper locateMapper = sqlSession.getMapper(LocateMapper.class);

        List<Locate> locates = locateMapper.getLocate();
        HashMap<String,String> map = new HashMap<>();
        locates.forEach(c->{

            map.put(c.getLongitude()+"-"+c.getLatitude(),c.getMode());

        });
        return map;
    }

}
