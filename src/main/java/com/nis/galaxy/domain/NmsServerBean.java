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
@Accessors(chain=true)
public class NmsServerBean {
    private String time;
    private String system;
    private String area;
    private int total;
    private int normal;
    private int abnormal;
    private List<AbnormalBean> abnormalmachines;
    public void setTime(String time) {
        this.time = time.substring(0,time.length()-2);
    }
    public String getSystem() {
        return this.system = "ntc";
    }
}
