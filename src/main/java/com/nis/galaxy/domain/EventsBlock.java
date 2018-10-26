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
public class EventsBlock {
    private String event;   // 事件
    private long ipNum;   // ip数量
    private long httpNum; // http数量
    private long mailNum;  // 邮件数量
    private long vedioNum; // 视频次数
    private long agentNum;  // 代理次数
    private String time;      // 时间
}
