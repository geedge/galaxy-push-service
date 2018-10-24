package com.nis.galaxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author GouGe
 * @data 2018/9/1 11:18
 */

@SuppressWarnings("serial")
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class NtcTypeCount {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public NtcTypeCount(String type, int count) {
        this.type = type;
        this.count = count;
    }

    private int count;

}
