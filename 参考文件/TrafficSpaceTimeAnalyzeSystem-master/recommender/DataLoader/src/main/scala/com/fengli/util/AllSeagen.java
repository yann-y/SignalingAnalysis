package com.fengli.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllSeagen {
    /**
     * 调用百度地图api 查询经纬度具体地区信息
     * @param lng 经度
     * @param lat 维度
     * @return 地区json 数据
     * @throws IOException 请求异常
     */
    public static JSONObject getJsonObject(String lng, String lat) throws IOException {
        // 创建默认http连接
        HttpClient client = HttpClients.createDefault();
        // 创建一个post请求
        HttpPost post = new HttpPost("http://api.map.baidu.com/reverse_geocoding/v3/");
        // 参数集合
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        //传递的参数
        paramList.add(new BasicNameValuePair("ak", "hG2ixIzny0aAWG0gy4a6xnkr8M0QOERj"));
        paramList.add(new BasicNameValuePair("output", "json"));
        paramList.add(new BasicNameValuePair("location", lat + "," + lng));

        // 把参转码后放入请求实体中
        HttpEntity httpEntity = new UrlEncodedFormEntity(paramList, "utf-8");
        // 把请求实体放post请求中
        post.setEntity(httpEntity);
        // 用http连接去执行get请求并且获得http响应
        HttpResponse response = client.execute(post);
        // 从response中取到响实体
        HttpEntity entity = response.getEntity();
        // 把响应实体转成文本
        String json = EntityUtils.toString(entity);

        return JSONObject.parseObject(json);
    }
}
