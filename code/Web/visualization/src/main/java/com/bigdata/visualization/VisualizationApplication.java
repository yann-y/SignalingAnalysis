package com.bigdata.visualization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class VisualizationApplication  {

    public static void main(String[] args) {
        SpringApplication.run(VisualizationApplication.class, args);
    }

}
