package com.nis.galaxy.domain;


import com.nis.galaxy.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class NetflowMonitor {
    private String type;
    private String url;    // 图片url
    private String time;    // 时间

    public void setTime(Date time) {
        this.time = DateUtils.formatDateTime(time);
    }
}
