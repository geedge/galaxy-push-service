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
public class NetflowMonitor {
    private String type;
    private String url;    // 图片url
    private String time;    // 时间

    public void setTime(String time) {
        this.time = time.substring(0, time.length() - 2);
    }
}
