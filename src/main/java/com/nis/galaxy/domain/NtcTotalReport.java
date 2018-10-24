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

}
