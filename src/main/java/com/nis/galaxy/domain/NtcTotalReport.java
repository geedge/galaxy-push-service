package com.nis.galaxy.domain;

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
@Accessors(chain=true)
public class NtcTotalReport implements Serializable {

  private int rejectNum;//封堵数量
  private int monitorNum;//监测数量
  private int c2sByteLen;//字节数
  private int s2cByteLen;
  private int newUniConnNum;//新建连接数
  private int liveConnNum;//活跃连接数
  private int dropConnNum;//丢弃连接数
  private String reportTime;//统计时间
  private int loopConnNum;//回流数
  public int getRejectNum() {
    return rejectNum;
  }

  public int getLoopConnNum() {
    return loopConnNum;
  }

  public void setRejectNum(int rejectNum) {
    this.rejectNum = rejectNum;
  }

  public int getMonitorNum() {
    return monitorNum;
  }

  public void setMonitorNum(int monitorNum) {
    this.monitorNum = monitorNum;
  }

  public int getC2sByteLen() {
    return c2sByteLen;
  }

  public void setC2sByteLen(int c2sByteLen) {
    this.c2sByteLen = c2sByteLen;
  }

  public int getS2cByteLen() {
    return s2cByteLen;
  }

  public void setS2cByteLen(int s2cByteLen) {
    this.s2cByteLen = s2cByteLen;
  }

  public int getNewUniConnNum() {
    return newUniConnNum;
  }

  public void setNewUniConnNum(int newUniConnNum) {
    this.newUniConnNum = newUniConnNum;
  }

  public int getLiveConnNum() {
    return liveConnNum;
  }
  public void setLiveConnNum(int liveConnNum) {
    this.liveConnNum = liveConnNum;
  }

  public int getDropConnNum() {
    return dropConnNum;
  }

  public void setDropConnNum(int dropConnNum) {
    this.dropConnNum = dropConnNum;
  }
  public String getReportTime() {
    return reportTime;
  }

  public void setReportTime(String reportTime) {
       this.reportTime = reportTime.substring(0,reportTime.length()-2);
   }
}
