package com.nis.galaxy.domain.dict;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author GouGe
 * @data 2018/8/30 16:22
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class UiSysDictInfo {
    private int sysDictId;//字典ID
    private int itemType;//字典类型
    private String itemDesc;//汉语描述信息
    private String itemValue;//英文
}
