package com.nis.galaxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author GouGe
 * @data 2018/9/8 14:45
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class AbnormalBean {

    private String name;
    private String ip;
}
