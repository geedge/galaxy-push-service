package com.nis.galaxy.domain;

import com.nis.galaxy.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author GouGe
 * @data 2018/8/29 11:08
 */


@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class CfgTypeCount {
    private String time;
    private String type;
    private int count;
    public String getTime() {
        return time= DateUtils.getRangeOfday(DateUtils.getDateTime());
    }
}
