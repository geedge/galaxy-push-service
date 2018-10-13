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
    private int totalNum;     // 总流量
    private int configNum;   // 配置总数
    private int blockNum;  // 阻断数量
    private int monitorNum; // 监测数量
    private int discardedNum; // 丢弃数量
    private int refluxNum;//回流数量
}
