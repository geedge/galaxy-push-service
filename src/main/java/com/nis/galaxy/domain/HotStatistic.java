package com.nis.galaxy.domain;

import com.nis.galaxy.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author GouGe
 * @data 2018/8/23 19:19
 */


@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class HotStatistic {
    private String time;   // 时间
    private String area;// 区域
    private String src_ip; // 源IP
    private String dst_ip;   // 目的IP
    private String type;  // 类型，如：url封堵、网站封堵等
    private long count;
}
