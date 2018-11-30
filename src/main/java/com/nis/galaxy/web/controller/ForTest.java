package com.nis.galaxy.web.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class ForTest {


    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @RequestMapping(value = "/v1/blocking",method = RequestMethod.POST)
    public void blocking(@RequestBody Map<String,Object> map){
        logger.info("接收数据\n"+map);
    }
    @RequestMapping(value = "/v1/netflow",method = RequestMethod.POST)
    public void netflow(@RequestBody Map<String,Object> map){
        logger.info("接收数据\n"+map);
    }
    @RequestMapping(value = "/v1/system_monitoring",method = RequestMethod.POST)
    public void monitor(@RequestBody Map<String,Object> map){
        logger.info("接收数据\n"+map);
    }
    @RequestMapping(value = "/v1/events",method = RequestMethod.POST)
    public void events(@RequestBody Map<String,Object> map){
        logger.info("接收数据\n"+map);
    }
}
