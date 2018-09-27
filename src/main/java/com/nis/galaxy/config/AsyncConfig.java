package com.nis.galaxy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author GouGe
 * @data 2018/9/5 11:48
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Value("${Asyn.corePoolSize}")
    private int corePoolSize;
    @Value("${Asyn.maxPoolSize}")
    private int maxPoolSize;
    @Value("${Asyn.queueCapacity}")
    private int queueCapacity;
    @Value("${Asyn.keepAliveSeconds}")
    private int keepAliveSeconds;


    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.initialize();
        // 设置默认线程名称
        executor.setThreadNamePrefix("My-Thread-");
        return executor;
    }
}