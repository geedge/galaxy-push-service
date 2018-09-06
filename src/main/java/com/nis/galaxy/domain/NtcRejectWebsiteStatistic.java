package com.nis.galaxy.domain;


import com.nis.galaxy.util.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@SuppressWarnings("serial")
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class NtcRejectWebsiteStatistic {
    private String time;
    private String type;
    private Integer count;

}
