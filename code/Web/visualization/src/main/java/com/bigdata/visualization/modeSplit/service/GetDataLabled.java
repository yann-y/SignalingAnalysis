package com.bigdata.visualization.modeSplit.service;

import com.bigdata.visualization.modeSplit.pojo.DataLabled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GetDataLabled {

    List<DataLabled> getDataLabled(String means);
}
