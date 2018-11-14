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
    private long letterNum;// 来函件数
    private long configNum;// 配置总数
    private long monitorNum;// 监测数量
    private long controlNum;  // 管控数量
}
