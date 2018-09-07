package com.nis.galaxy.domain;

import com.nis.galaxy.util.DateUtils;

/**
 * @author GouGe
 * @data 2018/8/29 13:57
 */
public class CfgInfo {
    private String time;
    private String type;
    private long count;
    public String getTime() {
        return time= DateUtils.getDateTime();
    }
}
