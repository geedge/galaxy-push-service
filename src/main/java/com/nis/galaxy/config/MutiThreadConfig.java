package com.nis.galaxy.config;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

@Configuration // 开启对异步任务的支持
public class MutiThreadConfig implements AsyncConfigurer {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${SCHEDULED.THREAD.POOL}")
    private int SCHEDULEDTHREADPOOL;

    @Bean
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(SCHEDULEDTHREADPOOL);
        //ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // // 线程池核心线程的数量
        // executor.setCorePoolSize(SCHEDULEDTHREADPOOL);
        // // 线程池维护线程的最大数量
        // executor.setMaxPoolSize(2*SCHEDULEDTHREADPOOL);
        // // 线程池所使用的缓冲队列
        // executor.setQueueCapacity(SCHEDULEDTHREADPOOL);
        // // 设置默认线程名称
        // executor.setThreadNamePrefix("MyAsync-");
        // // 线程空闲后的最大存活时间
        // //executor.setKeepAliveSeconds(5);
        // // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        // // 设置拒绝策略
        // executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // // 等待所有任务结束后再关闭线程池
        // executor.setWaitForTasksToCompleteOnShutdown(true);
        // executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new MyAsyncExceptionHandler();
    }

    class MyAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
            logger.info("Exception message - " + throwable.getMessage(),throwable);
            logger.info("Method name - " + method.getName());
            for (Object param : obj) {
                logger.info("Parameter value - " + param);
            }
        }
    }
}
