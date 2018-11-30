package com.nis.galaxy.config;


import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.ScheduledLockConfiguration;
import net.javacrumbs.shedlock.spring.ScheduledLockConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
public class ShedlockConfig {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${SCHEDULED.THREAD.POOL}")
    private int SCHEDULEDTHREADPOOL;

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            jdbcTemplate.execute("SELECT COUNT(0) from shedlock");
        } catch (Exception e) {
            logger.warn("shedlock表不存在\t" + e + "\t开始执行创建sql");
            jdbcTemplate.execute("CREATE TABLE shedlock(\n" +
                    "    name VARCHAR(64), \n" +
                    "    lock_until TIMESTAMP(3) NULL, \n" +
                    "    locked_at TIMESTAMP(3) NULL, \n" +
                    "    locked_by  VARCHAR(255), \n" +
                    "    PRIMARY KEY (name)\n" +
                    ")");
            logger.info("shedlock表创建成功");
        }
        return new JdbcTemplateLockProvider(dataSource);
    }

    @Bean
    public ScheduledLockConfiguration scheduledLockConfiguration(LockProvider lockProvider) {
        ScheduledLockConfiguration build = ScheduledLockConfigurationBuilder
                .withLockProvider(lockProvider)
                .withPoolSize(SCHEDULEDTHREADPOOL)
                .withDefaultLockAtMostFor(Duration.ofMinutes(4))
                .build();
        return build;
    }
}