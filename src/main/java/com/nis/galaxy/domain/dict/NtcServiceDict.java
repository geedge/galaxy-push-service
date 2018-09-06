package com.nis.galaxy.domain.dict;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author GouGe
 * @data 2018/8/31 9:32
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class NtcServiceDict {
    private int serviceCode;
    private String serviceNameZh;
    private String serviceNameEn;
    private String serviceNameRu;
}
