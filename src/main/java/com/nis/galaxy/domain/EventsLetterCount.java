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
public class EventsLetterCount {
    private String time;  // 时间
    private String letterNo;    // 来函编号
    private String topic;    // 主题
    private long num;

    public void setTime(Date time) {
        this.time = DateUtils.formatDateTime(time);
    }
}
