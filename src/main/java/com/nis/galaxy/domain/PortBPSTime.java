package com.nis.galaxy.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class PortBPSTime {

    private String port;
    private long bps;
    private String time;

    public void setBps(double bps) {
        this.bps =(long) bps;
    }
}
