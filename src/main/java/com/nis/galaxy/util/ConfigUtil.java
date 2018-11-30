package com.nis.galaxy.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author GouGe
 * @data 2018/11/28 17:29
 */

@Component
public class ConfigUtil {
    /**
     * 推送失败后再次推送时间间隔单位ms
     */
    public static int time;
    /**
     * 测试数据接口开关
     */
    public static boolean testData;
    /**
     * 累加、实时数据接口开关
     */
    public static boolean actualData;
    /**
     * 趋势数据接口开关
     */
    public static boolean tendencyData;
    /**
     * 无特殊说明数据接口开关
     */
    public static boolean recentData;
    /**
     * 推送语言选择参数
     * 1.中文、2.英文、3.俄语
     */
    public static int languageChose;


    @Value("${Language.Chose}")
    public void setLanguageChose(String languageChose) {
        if (languageChose == null || "".equals(languageChose)) {
            ConfigUtil.languageChose = 3;
            return;
        }
        try {
            ConfigUtil.languageChose = Integer.parseInt(languageChose.trim());
            if (ConfigUtil.languageChose != 1 && ConfigUtil.languageChose != 2 && ConfigUtil.languageChose != 3) {
                ConfigUtil.languageChose = 3;
            }
        } catch (Exception e) {
            ConfigUtil.languageChose = 3;
        }
    }

    @Value("${Open.Recent.Data}")
    public void setRecentData(String recentData) {
        ConfigUtil.recentData = Boolean.parseBoolean(recentData);
    }

    @Value("${Open.Recent.Data.Tendency}")
    public void setTendency(String tendency) {
        ConfigUtil.tendencyData = Boolean.parseBoolean(tendency);
        ;
    }

    @Value("${Open.Recent.Data.Actual}")
    public void setActualData(String actual) {
        ConfigUtil.actualData = Boolean.parseBoolean(actual);
    }

    @Value("${Open.False.Data}")
    public void setTestData(String testData) {
        ConfigUtil.testData = Boolean.parseBoolean(testData);
    }

    @Value("${t_loop}")
    public void setTime(String time) {
        if (time == null || "".equals(time)) {
            ConfigUtil.time = 120000;
            return;
        }
        try {
            ConfigUtil.time = Integer.parseInt(time.trim());
        } catch (Exception e) {
            ConfigUtil.time = 120000;
        }
    }
}
