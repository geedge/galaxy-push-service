package com.nis.galaxy.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class StringCount {
    private String type;
    private BigInteger count;

}
