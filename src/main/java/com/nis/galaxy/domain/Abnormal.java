package com.nis.galaxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author GouGe
 * @data 2018/8/29 11:31
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class Abnormal {
     private int nmsServer_Id;
     private String hostname;
     private String ip;
}
