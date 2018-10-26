package com.nis.galaxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author GouGe
 * @data 2018/9/8 14:45
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class NmsServerBean {
    private String time;
    private String system;
    private String area;
    private long total;
    private long normal;
    private long abnormal;
    private List<AbnormalBean> abnormalmachines;

    public void setTime(String time) {
        if (time != null && time.indexOf(".") > -1) {
            this.time = time.substring(0, time.indexOf("."));
        }else {
            this.time=time;
        }
    }

    public String getSystem() {
        return this.system = "ntc";
    }
}
