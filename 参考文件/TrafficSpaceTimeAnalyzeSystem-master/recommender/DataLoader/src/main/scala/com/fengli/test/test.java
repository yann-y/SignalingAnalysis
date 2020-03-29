package com.fengli.test;


import com.alibaba.fastjson.JSONObject;
import com.fengli.util.AllSeagen;

import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        JSONObject jsonObject = AllSeagen.getJsonObject("123.4396362", "41.80488968");
        System.out.println(jsonObject);
    }
}
