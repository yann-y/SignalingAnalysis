package com.bigdata.visualization.modeSplit.service;


import com.bigdata.visualization.modeSplit.pojo.ModeData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GetModeData {


    List<ModeData> getModeData(String means);
}
