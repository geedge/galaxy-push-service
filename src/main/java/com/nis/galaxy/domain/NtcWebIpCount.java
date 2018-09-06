package com.nis.galaxy.domain;

import com.nis.galaxy.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author GouGe
 * @data 2018/8/28 17:29
 */


@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class NtcWebIpCount {
    private String time;
    private String ip;
    private String type;
    private long count;

}
