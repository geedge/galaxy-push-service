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
public class EventsKeyPointSafaguardThing {
    private String time;
    private String taskName;
    private String taskTime;
    private int letterNum;// 来函件数
    private int configNum;// 配置总数
    private int monitorNum;// 监测数量
    private int controlNum;  // 管控数量
}
