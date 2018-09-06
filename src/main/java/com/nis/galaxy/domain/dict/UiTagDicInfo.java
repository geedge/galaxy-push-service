package com.nis.galaxy.domain.dict;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author GouGe
 * @data 2018/9/5 21:34
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class UiTagDicInfo {

    private int serviceDictId;
    private String itemValue;
    private int itemType;
}
