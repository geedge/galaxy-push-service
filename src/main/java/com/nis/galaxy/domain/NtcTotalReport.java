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

  private long rejectNum;//封堵数量
  private long monitorNum;//监测数量
  private long c2sByteLen;//字节数
  private long s2cByteLen;
  private long newUniConnNum;//新建连接数
  private long liveConnNum;//活跃连接数
  private long dropConnNum;//丢弃连接数
  private String reportTime;//统计时间
  private long loopConnNum;//回流数

}
