package com.nis.galaxy.web.controller;


import com.nis.galaxy.domain.NmsServerBean;
import com.nis.galaxy.web.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author GouGe
 * @data 2018/9/8 14:53
 */

@RestController
public class MonitorController {


    @Autowired
    MonitorService monitorService;

    @RequestMapping(value = "/getMonitorOf1m", method = RequestMethod.GET)
    public List<NmsServerBean> getMonitorOf1m() {
        return monitorService.getMonitorOf1m();
    }

}
