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
public class IpActiveInt {
    private String time;// 时间
    private String area;// 区域
    private String IP;// 活跃IP
    private Integer count;//  流量
}
