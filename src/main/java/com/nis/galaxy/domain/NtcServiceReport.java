package com.nis.galaxy.domain;


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
public class NtcServiceReport {
    private String time;
    private String type;
    private Integer count;
    public void setTime(String time) {
        this.time = time.substring(0,time.length()-2);
    }
}
