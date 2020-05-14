package com.bigdata.visualization.heatMap.controller;


import com.bigdata.visualization.heatMap.pojo.Coord;
import com.bigdata.visualization.heatMap.service.GetCoordData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/heatMap")
public class HeatMapController {

    @Autowired
    private GetCoordData getCoordData;
    @RequestMapping("/heatMapBySecond.html")
    public ModelAndView heatMapBySecond() {

        ModelAndView mv = new ModelAndView();
        List<Coord> coords = getCoordData.getCoordDataBySecond();
        mv.addObject("coords", coords);
        mv.setViewName("heatmap/heatMapBySecond.html");
        return mv;
    }
    @RequestMapping("/heatMapByMinute.html")
    public ModelAndView heatMapByMinute()
    {
        ModelAndView mv = new ModelAndView();
        List<Coord> coords = getCoordData.getCoordDataByMinute();
        mv.addObject("coords",coords);
        mv.setViewName("heatmap/heatMapByMinute.html");
        return mv;
    }
    @RequestMapping("/heatMapByHour.html")
    public ModelAndView heatMapByHour()
    {
        ModelAndView mv = new ModelAndView();
        List<Coord> coords  = getCoordData.getCoordDataByHour();
        mv.addObject("coords",coords);
        mv.setViewName("heatmap/heatMapByHour.html");
        return mv;
    }

    @RequestMapping("/heatMapDataBySecond")
    @ResponseBody
    public List<Coord> getHeatMapDataBySecond() {
        List<Coord> coords = getCoordData.getCoordDataBySecond();
        return coords;
    }

    @RequestMapping("/heatMapDataByMinute")
    @ResponseBody
    public List<Coord> getHeatMapDataByMinute()
    {
        List<Coord> coords = getCoordData.getCoordDataByMinute();
        System.out.println(coords);
        return coords;
    }
    @RequestMapping("/heatMapDataByHour")
    @ResponseBody
    public List<Coord> getHeatMapByHour()
    {
        List<Coord>  coords = getCoordData.getCoordDataByHour();
        return coords;
    }

    @RequestMapping("/index")
    public String index()
    {
        return "index";
    }
}
