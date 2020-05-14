package com.bigdata.visualization.stayAnalyse.controller;


import com.bigdata.visualization.heatMap.pojo.Coord;
import com.bigdata.visualization.heatMap.service.GetCoordData;
import com.bigdata.visualization.stayAnalyse.service.GetCoords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/stayAnalyse")
public class StayAnalyseController {

    @Autowired
    private GetCoords getCoords;


    @RequestMapping("/stayAnalyse.html")
    public ModelAndView stayAnalyse()
    {
        ModelAndView mv = new ModelAndView();
        List<Coord> coords = getCoords.getCoords();

        mv.addObject("coords",coords);
        mv.setViewName("stayAnalyse/stayAnalyse.html");
        return mv;
    }
    @ResponseBody
    @RequestMapping("/stayAnalyseData")
    public List<Coord> stayAnalyseData()
    {
        List<Coord> coords = getCoords.getCoords();
        return coords;
    }
}
