package com.nis.galaxy.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author GouGe
 * @data 2018/9/4 9:49
 */


public class AspectJTemplate {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 切入点表达式
     */
    @Pointcut("execution(* com.nis.galaxy.template.*.*(..))")
    public void declarePointCut() {
    }
    /**
     * 前置通知
     * @param joinPoint
     */
    @Before("declarePointCut()")
    public void beforeMethod(JoinPoint joinPoint) {
        logger.info("template\t前置通知\t被代理类对象："+joinPoint.getTarget());
        logger.info("template\t方法名："+joinPoint.getSignature().getName());
    }
    @After("declarePointCut()")
    public void afterMethod(JoinPoint joinPoint){
        logger.info("template\t后置通知\t出"+joinPoint.getSignature().getName());
    }
    @AfterThrowing(value = "declarePointCut()",throwing = "e")
    public void afterThrowingMethod(Exception e){
        logger.info("template\t异常通知\t异常是："+e.toString());
    }
}
