package com.nis.galaxy.domain;

import com.nis.galaxy.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class NtcTotalReport implements Serializable {

    private String time;        // 时间
    private long controlNum; // 管控数量
    private long monitorNum;// 监测数量
    private long networkFlowNum; // 网络流量数量
    private long upstreamNum;// 上行数量
    private long downstreamNum; // 上行数量
    private long newLinkNum;// 新建连接数
    private long activeConNum;// 活跃连接数
    private long dropConNum; // 丢弃连接数
    private long refluxConNum; // 回流连接数

    public void setNetworkFlowNum(double networkFlowNum) {
        this.networkFlowNum = (long) networkFlowNum;
    }

    public void setUpstreamNum(double upstreamNum) {
        this.upstreamNum = (long) upstreamNum;
    }

    public void setDownstreamNum(double downstreamNum) {
        this.downstreamNum = (long) downstreamNum;
    }

}
