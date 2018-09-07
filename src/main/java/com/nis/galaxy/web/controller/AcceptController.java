package com.nis.galaxy.web.controller;

import com.nis.galaxy.util.JsonMapper;
import com.nis.galaxy.web.service.AcceptService;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GouGe
 * @data 2018/8/29 14:11
 */

@RestController
public class AcceptController {
    @Autowired
    AcceptService acceptService;

    /**
     * 系统监控接口
     * @param request
     */
    @RequestMapping(value = "/accept" ,method = RequestMethod.POST)
    public  void acceptMonitoringSystem(@RequestBody Map<String ,Object> request) {
        acceptService.acceptMonitoringSystem(request);
    }
}

