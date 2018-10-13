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
public class EvevtsTargetNetwork {

    private String account;  // 账号
    private String type;     // 协议类型
    private String srcip;    // 源ip
    private String dstip;    // 目的ip
    private String time;    // 时间

}
