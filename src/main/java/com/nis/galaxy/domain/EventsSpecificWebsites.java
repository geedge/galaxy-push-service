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
public class EventsSpecificWebsites {
    private String time;    // 时间
    private String name;    // 网站名称
    private int num;    //数量

}
