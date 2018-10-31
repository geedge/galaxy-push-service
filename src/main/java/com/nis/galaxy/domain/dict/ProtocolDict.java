package com.nis.galaxy.domain.dict;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author GouGe
 * @data 2018/9/5 21:06
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class ProtocolDict {
    private String protocolType;
    private String viewCode;
}
