package com.nis.galaxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author GouGe
 * @data 2018/9/8 15:12
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class NmsServiceBeanTmp {
    private String time;
    private String area;
    private String total;
    private String normal;
    private String abnormal;
    private long id;
}
