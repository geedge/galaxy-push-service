package com.nis.galaxy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.io.IOException;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Value("${Image.Path}")
    private String IMAGEPATH;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (IMAGEPATH == null || "".equals(IMAGEPATH)) {
            try {
                IMAGEPATH = new File("").getCanonicalPath() + "/image/";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //将所有/image/** 访问都映射到某磁盘目录下
        registry.addResourceHandler("/image/**").addResourceLocations("file:"+IMAGEPATH);
        super.addResourceHandlers(registry);
    }
}