package com.nis.galaxy.domain.dict;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class UiCarrierDictInfo {
    private String CarrierId;
    private String itemValue;
}
