package com.nis.galaxy.config;


import com.nis.galaxy.util.DateUtils;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.ScheduledLockConfiguration;
import net.javacrumbs.shedlock.spring.ScheduledLockConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShedlockConfig {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${SCHEDULED.THREAD.POOL}")
    private int SCHEDULEDTHREADPOOL;

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `shedlock` (\n" +
                    "  `name` varchar(64) NOT NULL COMMENT '锁的名字',\n" +
                    "  `lock_until` timestamp(3) NULL DEFAULT NULL COMMENT '锁的释放时间',\n" +
                    "  `locked_at` timestamp(3) NULL DEFAULT NULL COMMENT '锁的开始时间',\n" +
                    "  `locked_by` varchar(255) DEFAULT '' COMMENT '产生锁的主机名字',\n" +
                    "  PRIMARY KEY (`name`)\n" +
                    ")");
            long timeLock1 = 0, timeLock2 = 0, subTime = 0, count = 0;
            for (; ; ) {
                count++;
                Map<String, Object> timeLockempt1 = new HashMap<String, Object>();
                try {
                    try {
                        timeLockempt1 = jdbcTemplate.queryForMap("SELECT locked_at FROM shedlock WHERE `name` = 'PushServiceMonitor'");
                    } catch (EmptyResultDataAccessException ex) {
                        logger.warn("PushServiceMonitor\t不存在\t应为服务首次" + ex);
                        break;
                    }
                    Date lockEmpt1 = (Date) timeLockempt1.get("locked_at");
                    Thread.sleep(13 * 1000);
                    Map<String, Object> timeLockempt2 = jdbcTemplate.queryForMap("SELECT locked_at FROM shedlock WHERE `name` = 'PushServiceMonitor'");
                    Date lockEmpt2 = (Date) timeLockempt2.get("locked_at");
                    timeLock1 = lockEmpt1.getTime();
                    timeLock2 = lockEmpt2.getTime();
                    subTime = (timeLock2 - timeLock1) / 1000;
                    if (subTime == 0) {
                        logger.warn("大屏推送服务未运行\t跳出循环\t启动此服务");
                        break;
                    }
                    logger.info("大屏推送服务ing\t此程序暂不需要启动\t监测次数：" + count);
                } catch (Exception ex) {
                    logger.info("大屏推送服务启动监测查询出现异常\t监测次数：" + count, ex);
                }
            }
            String date = DateUtils.getAfter1h(DateUtils.getDateTime());
            if (count == 1) {
                logger.warn("需开启大屏全部接口首次推送");
                jdbcTemplate.execute("DELETE FROM shedlock WHERE name LIKE 'FirstPush%'");
            } else {
                logger.warn("不需开启大屏全部接口首次推送");
                jdbcTemplate.update("UPDATE shedlock SET lock_until = ? WHERE name LIKE 'FirstPush%'", new Object[]{date});
            }
        } catch (Exception ex) {
            logger.warn("启动查询监测执行异常", ex);
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