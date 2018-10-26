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
public class SystemTotal {
    private long totalNum;     // 总流量
    private long configNum;   // 配置总数
    private long blockNum;  // 阻断数量
    private long monitorNum; // 监测数量
    private long discardedNum; // 丢弃数量
    private long refluxNum;//回流数量
}
