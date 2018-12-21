package com.nis.galaxy.web.controller;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.template.ForEventsInterface;
import com.nis.galaxy.util.ConfigUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author GouGe
 * @data 2018/12/21 9:42
 */

@Controller
public class CfgParam {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ForEventsInterface forEventsInterface;
    @Autowired
    MyLoad myLoad;

    @RequestMapping("/cfgParam")
    public String upImage() {
        return "index";
    }

    /**
     * 语言选择配置
     *
     * @param language
     * @return
     */
    @RequestMapping(value = "/cfgLanguageParam", method = RequestMethod.GET)
    @ResponseBody
    public String cfgLanguageParam(@Param("language") int language) {
        ConfigUtil.languageChose = language;
        myLoad.MyloadArea();
        return "SUCCEED";
    }

    /**
     * 设备参数配置修改
     *
     * @param request
     * @return
     */
//    @RequestMapping(value = "/cfgDeviceParamData", method = RequestMethod.POST)
//    @ResponseBody
//    public String cfgDeviceParamData(HttpServletRequest request) {
//        return "SUCCEED";
//    }
}
