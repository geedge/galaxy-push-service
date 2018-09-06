package com.nis.galaxy.util;

import javafx.application.Application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author GouGe
 * @data 2018/8/27 10:57
 */
public class PropertiesFile {
    Properties prop = new Properties();
    /**
     * 获取加载配置文件
     * @return
     */
    public static Properties getPropertiesIn() {
        Properties prop = new Properties();
        try {

            // 读取配置文件
            FileInputStream inputStream = new FileInputStream(

                    "C:\\Users\\lenovo\\Desktop\\galaxy\\src\\main\\resources\\application.properties");
            // 加载
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    /**
     * 获取输出配置文件
     * @return
     */
    public static FileOutputStream getPropertiesOut() {
        FileOutputStream outputStream = null;
        try {
            //C:\Users\lenovo\Desktop\galaxy\src\main\resources\
            outputStream = new FileOutputStream("C:\\Users\\lenovo\\Desktop\\galaxy\\src\\main\\resources\\application.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }
}
