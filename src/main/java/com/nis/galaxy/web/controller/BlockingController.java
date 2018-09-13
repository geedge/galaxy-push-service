package com.nis.galaxy.web.controller;


import com.nis.galaxy.domain.*;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.service.BlockingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class BlockingController {

    @Autowired
    BlockingService blockingService;
    /**
     * 封堵信息总体显示
     * @return
     */
    @RequestMapping(value = "/getAllBlockInformation/{start}",method = RequestMethod.GET)
    public List<NtcTypeCount> getAllBlockInformation(@PathVariable("start") String start){
        String end = DateUtils.getAfter5min(start);
        int times = 1;

        return blockingService.getAllBlockInformation(start,end,times);
    }

    /**
     * 封堵信息分布显示
     * @return
     */
    @RequestMapping(value = "/getDisBlockInformation/{start}",method = RequestMethod.GET)
    public List<NtcDisReport> getDisBlockInformation(@PathVariable("start") String start){
        String end = DateUtils.getAfter5min(start);
        int times=1;
        return blockingService.getDisBlockInformation(start,end,times);
    }


    /**
     * DNS欺骗信息显示
     * @return
     */
    @RequestMapping(value = "/getDNSDeceiveInformation/{start}",method = RequestMethod.GET)
    public List<NtcEntranceReport> getDNSDeceiveInformation(@PathVariable("start") String start){
        String end = DateUtils.getAfter5min(start);
        int times =1;
        return blockingService.getDNSDeceiveInformation(start,end,times);
    }

    /**
     * 24小时全国范围阻断协议信息
     * @return
     */
    @RequestMapping(value = "/get24hAllBlockEachProtocol/{start}",method = RequestMethod.GET)
    public List<TimeTypeCount > get24hAllBlockEachProtocol(@PathVariable("start")String  start){
        String end = DateUtils.getAfter5min(start);
        int times =1;
        return blockingService.get24hAllBlockEachProtocol(start,end,times);
    }/**
     /**
     * 24h全国热力图 5min
     * @return
     */
    @RequestMapping(value = "/getAllHotStatistis/{start}",method = RequestMethod.GET)
    public List<HotStatistic > getAllHotStatistis(@PathVariable("start") String start){
        String end = DateUtils.getAfter5min(start);
        int times =1;
        return blockingService.getAllHotStatistis(start,end,times);

    }

    /**
     * 实时打点图 10s推送一次
     */
    @RequestMapping(value = "/getPointMapOf10s", method = RequestMethod.GET)
    public List<PointMap> getPointMapOf10s() {
        return blockingService.getPointMapOf10s();
    }
    /**
     * 热力图接口 5min
     */
    @RequestMapping(value = "/getDisBlockMap/{start}", method = RequestMethod.GET)
    public List<AreaCount> getDisBlockMap(@PathVariable("start") String start) {
        String end = DateUtils.getAfter5min(start);
        int times= 1;
        List<AreaCount> disBlockMap = blockingService.getDisBlockMap(start, end,times);
        return disBlockMap;
    }
    /**
     * 所有配置数量
     */
    @RequestMapping(value = "/getAllCfgInfo", method = RequestMethod.GET)
    public List<CfgTypeCount> getAllCfgInfo() {
        return blockingService.getAllCfgInfo();
    }
}