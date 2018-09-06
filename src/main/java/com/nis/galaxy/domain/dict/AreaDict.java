package com.nis.galaxy.domain.dict;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author GouGe
 * @data 2018/9/5 22:50
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class AreaDict {
    private String areaId;
    private String areaName;
}
