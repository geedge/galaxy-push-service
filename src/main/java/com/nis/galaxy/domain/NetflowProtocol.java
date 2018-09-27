package com.nis.galaxy.domain;


import com.nis.galaxy.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class NetflowProtocol {
    private String time; // 时间
    private String type;        // 协议类型
    private long linknum;// 链路数
    private long pakeagenum;  //  数据包数
    private long bytes;// 字节数


}