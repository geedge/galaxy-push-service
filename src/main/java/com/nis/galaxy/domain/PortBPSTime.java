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

    private int port;
    private int bps;
    private String time;

    public String getTime() {
        return time= time.substring(0, time.length() - 2);
    }
}
