package com.bigdata.visualization.modeSplit.service;


import com.bigdata.visualization.modeSplit.pojo.AverData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GetAverData {



    List[] getAverData(String mmeans);

}
