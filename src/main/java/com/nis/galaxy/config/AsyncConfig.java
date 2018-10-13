package com.nis.galaxy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 * @author GouGe
 * @data 2018/9/5 11:48
 */
@Configuration
public class AsyncConfig implements SchedulingConfigurer {

    @Value("${SCHEDULED.THREAD.POOL}")
    private int SCHEDULEDTHREADPOOL;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(SCHEDULEDTHREADPOOL));
    }
}