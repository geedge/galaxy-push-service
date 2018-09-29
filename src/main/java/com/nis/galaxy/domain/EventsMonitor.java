package com.nis.galaxy.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class EventsMonitor {
    private String event;    // 事件
    private int ipNum;   // ip数量
    private int httpNum;  // http数量
    private int mailNum;  // 邮件数量
    private int vedioNum; // 视频次数
    private int agentNum;  // 代理次数
    private String time;      // 时间

}
