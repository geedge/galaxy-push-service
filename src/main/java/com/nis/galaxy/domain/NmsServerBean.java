package com.nis.galaxy.domain;

import com.nis.galaxy.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
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

    public void setTime(Date time) {
        this.time =DateUtils.formatDateTime(time);
    }

    public String getSystem() {
        return this.system = "ntc";
    }

    public String getTime() {
        return time=DateUtils.getDateTime();
    }
}
