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
public class EventsLetterCount {
    private String time;  // 时间
    private String letter;    // 来函
    private long num;
    public void setTime(String time) {
        if (time != null && time.indexOf(".") > -1) {
            this.time = time.substring(0, time.indexOf("."));
        }else {
            this.time=time;
        };
    }
}
