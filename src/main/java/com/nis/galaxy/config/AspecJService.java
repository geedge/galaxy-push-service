package com.nis.galaxy.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author GouGe
 * @data 2018/9/3 20:00
 */
@Component
@Aspect
public class AspecJService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 切入点表达式
     */
    @Pointcut("execution(* com.nis.galaxy.web.service.*.*(..))")
    public void declarePointCut() {
    }

    /**
     * 前置通知
     *
     * @param joinPoint
     */
    @Before("declarePointCut()")
    public void beforeMethod(JoinPoint joinPoint) {
        logger.info("service\t前置通知\t被代理类对象：" + joinPoint.getTarget());
        logger.info("service\t方法名：" + joinPoint.getSignature().getName() + "\t参数：" + Arrays.asList(joinPoint.getArgs()));
    }

    @After("declarePointCut()")
    public void afterMethod(JoinPoint joinPoint) {
        logger.info("service\t后置通知\t出" + joinPoint.getSignature().getName());
    }

    @AfterReturning(value = "declarePointCut()", returning = "result")
    public void afterReturnMethod(Object result) {
        if (result != null) {
            logger.info("service\t返回通知\t返回结果是：" + result.toString());
        }
    }

    @AfterThrowing(value = "declarePointCut()", throwing = "e")
    public void afterThrowingMethod(Exception e) {
        logger.info("service\t异常通知\t异常是：" + e.toString());
    }

}
