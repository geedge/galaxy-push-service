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
public class NetWaterMonitor {
    private String time;
    private String area;
    private String type;
    private int count;

    public String getArea() {
        return "astana";
    }

    public String getType() {
        return "OCTETS";
    }
}
