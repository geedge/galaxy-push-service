package com.nis.galaxy.web.controller;

import com.nis.galaxy.domain.*;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.service.BlockingTopNService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BlockingTopNController {

    @Autowired
    BlockingTopNService blockingTopNService;

    /**
     * 1小时内全国阻断URLTOP10
     * @return List<NtcRejectUrlStatistic>
     */
    @RequestMapping(value = "/getBlockTop10URLOf1h/{start}",method = RequestMethod.GET)
    public List<NtcRejectUrlStatistic> getBlockTop10URLOf1h(@PathVariable("start") String start){
        String end = DateUtils.getAfter1h(start);
        int times =1;
        return blockingTopNService.getBlockTop10URLOf1h(start,end,times);
    }

    /**
     * 1小时内全国阻断网站TOP10
     * @return
     */
    @RequestMapping(value = "/getBlockTop10WebsiteOf1h/{start}",method = RequestMethod.GET)
    public List<NtcRejectWebsiteStatistic> getBlockTop10WebsiteOf1h(@PathVariable("start") String start){
        String end = DateUtils.getAfter1h(start);
        int times =1;
        return blockingTopNService.getBlockTop10WebsiteOf1h(start,end,times);
    }

    /**
     * 24小时内封堵标签TOP10
     * @return
     */
    @RequestMapping(value = "/getBlockTop10Tag24h/{start}",method = RequestMethod.GET)
    public List<NtcTagReport> getBlockTop10Tag24h(@PathVariable("start") String start){
        String end = DateUtils.getAfter1h(start);
        int times =1;
        return blockingTopNService.getBlockTop10Tag24h(start,end,times);
    } /**
     * 5min内封堵标签TOP10
     * @return
     */
    @RequestMapping(value = "/getBlockTop10Tag5m/{start}",method = RequestMethod.GET)
    public List<NtcTagReport> getBlockTop10Tag5m(@PathVariable("start") String start){
        String end = DateUtils.getAfter5min(start);
        int times =1;
        return blockingTopNService.getBlockTop10Tag5m(start,end,times);
    }

    /**
     * 24小时区域阻断协议TOP10
     * @return
     */
    @RequestMapping(value = "/getAreaBlockTop10ProtocolOf24h/{start}",method = RequestMethod.GET)
    public List<AreaBlockProtocol> getAreaBlockTop10ProtocolOf24(@PathVariable("start") String start){
        String end = DateUtils.getAfter24h(start);
        int times =1;
        return blockingTopNService.getAreaBlockTop10ProtocolOf24(start,end,times);
    }

    /**
     * 5分钟内全国阻断URL TOP10
     * @return
     */
    @RequestMapping(value = "/getBlockTop10URLOf5m/{start}",method = RequestMethod.GET)
    public List<NtcUrlIpCount> getBlockTop10URLOf5m(@PathVariable("start")String start){
        String end = DateUtils.getAfter5min(start);
        int times =1;
        return blockingTopNService.getBlockTop10URLOf5m(start,end,times);
    }
    /**
     * 5分钟内全国阻断website TOP10
     * @return
     */
    @RequestMapping(value = "/getBlockTop10WebsiteOf5m/{start}",method = RequestMethod.GET)
    public List<NtcUrlIpCount> getBlockTop10WebsiteOf5m(@PathVariable("start") String start){
        String end = DateUtils.getAfter5min(start);
        int times=1;
        return blockingTopNService.getBlockTop10WebsiteOf5m(start,end,times);
    }
}
