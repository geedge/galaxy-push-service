package com.nis.galaxy.domain;

import com.nis.galaxy.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author GouGe
 * @data 2018/8/28 19:45
 */


@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class PointMap {
    private String time;      // 时间
    private String src_ip;    //  源IP
    private String dst_ip;//  目的IP
    private String type; //    类型
    public String getTime() {
        return DateUtils.getDateTime();
    }
}
