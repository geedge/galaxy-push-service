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
public class EventsImageMonitor {
    private long imageId;     // 节目id
    private int num;    // 次数
    private String url;   // 现场片段url

}
