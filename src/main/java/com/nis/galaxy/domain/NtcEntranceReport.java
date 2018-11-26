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
@Accessors(chain=true)
public class NtcEntranceReport {
    private String time;
    private String area;
    private long count;

    public String getTime() {
        time = DateUtils.getRangeOf5min(DateUtils.getDateTime());
        return time = DateUtils.getBeforeNminOfTime(time, -5);
    }
}