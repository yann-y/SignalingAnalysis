package service;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import mapper.LocateMapper;
import pojo.Locate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class GetLocateMap {



    public static HashMap<String,String> getLocateMap()
    {

        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();


        InputStream inputStream = null;


        try {
            inputStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);


        SqlSession sqlSession = sqlSessionFactory.openSession();


        LocateMapper locateMapper = sqlSession.getMapper(LocateMapper.class);


        List<Locate> locates = locateMapper.getLocate();

        HashMap<String,String> map = new HashMap<>();
        locates.forEach(c->map.put(c.getLaci(),c.getLongitude()+"-"+c.getLatitude()));

        return map;
    }

}
