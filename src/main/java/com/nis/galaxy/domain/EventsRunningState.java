package com.nis.galaxy.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class EventsRunningState {

    private long visitsNum;// 访问次数
    private long controlNum;  // 管控次数
    private long replaceNum; // 替换次数
}
