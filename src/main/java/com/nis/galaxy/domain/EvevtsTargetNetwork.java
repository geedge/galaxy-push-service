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
    private String nas_ip;    // 接入IP
    private long num;    // 次数
    private String time;    // 时间
}
