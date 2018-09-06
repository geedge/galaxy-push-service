package com.nis.galaxy.web.controller;


import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.ForTestBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
public class ForTest {


    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @RequestMapping(value = "/v1/blocking",method = RequestMethod.POST)
    public void acceptInterface(HttpServletResponse response,HttpServletRequest request){

        logger.info(Thread.currentThread().getName()+request+"接收完毕");
    }
}
