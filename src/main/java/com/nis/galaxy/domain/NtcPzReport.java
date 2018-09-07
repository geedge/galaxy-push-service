package com.nis.galaxy.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class NtcPzReport implements Serializable //必须序列化
{
    private Integer statId;
    private Integer cfgId;
    private Integer service;
    private Integer sum;
    private String reportTime;

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime.substring(0,reportTime.length()-2);
    }
}
