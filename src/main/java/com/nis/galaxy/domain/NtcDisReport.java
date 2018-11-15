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
public class NtcDisReport {
    private String time;         // 时间
    private String area;         // 地区
    private long controlNum;  // 管控数量
    private long monitorNum; // 监测数量
    private long networkFlowNum; // 网络流量数量
    private long upstreamNum; // 上行数量
    private long downstreamNum; // 上行数量
    private long newLinkNum;   // 新建连接数
    private long activeConNum;   // 活跃连接数
    private long dropConNum;// 丢弃连接数
    private long refluxConNum;  // 回流连接数

    public NtcDisReport(String time, String area, long controlNum, long monitorNum, long networkFlowNum, long upstreamNum, long downstreamNum) {
        this.time = time;
        this.area = area;
        this.controlNum = controlNum;
        this.monitorNum = monitorNum;
        this.networkFlowNum = networkFlowNum;
        this.upstreamNum = upstreamNum;
        this.downstreamNum = downstreamNum;
    }
}
