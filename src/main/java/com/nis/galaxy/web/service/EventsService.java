package com.nis.galaxy.web.service;

import com.nis.galaxy.domain.*;

import java.util.List;

public interface EventsService {
    /**
     * 系统总计
     * @param start
     * @param end
     * @return
     */
    List<SystemTotal> getSystemTotalOf1Day(String start, String end);

    /**
     * 特定APP访问监控
     * @param start
     * @param end
     * @return
     */
    List<EventsAppMonitor> getSpecifiedAPPMonitor(String start, String end);

    /**
     * 来函统计报表
     * @return
     */
    List<EventsLetterCount> getLetterCount();

    /**
     * 业务类型统计报表
     * @return
     */
    List<EventsServiceCount> getServiceCount(String start,String end);

    /**
     * 重点保障事件总览
     * @param start
     * @return
     */
    List<EventsKeyPointSafaguardThing> getKeyPointSafaguardThing(String start);

    /**
     * 特定网站监控
     * @return
     */
    List<EventsSpecificWebsites> getSpecificWebsites(String end);

    /**
     * 事件监测
     * @param end
     * @return
     */
    List<EventsMonitor> getEventsMonitor(String end);

    /**
     * 事件封堵
     * @param end
     * @return
     */
    List<EventsBlock> getEventsBlock(String end);

    /**
     * 音频监控
     * @param start
     * @param end
     * @return
     */
    List<EventsAudioMonitor> getEventsAudioMonitor(String start, String end);

    /**
     * 视频监控
     * @param start
     * @param end
     * @return
     */
    List<EventsVedioMonitor> getVedioMonitor(String start, String end);

    /**
     * 图片监控
     * @param start
     * @param end
     * @return
     */
    List<EventsImageMonitor> getImageMonitor(String start, String end);

    /**
     *VOIP监控
     * @param start
     * @param end
     * @return
     */
    List<EventsVoipMonitor> getVoipMonitor(String start, String end);

    /**
     * 网络行为预警
     * @param start
     * @param end
     * @return
     */
    List<EventsDdosAlarm> getDdosAlarm(String start, String end);

    /**
     * 目标行为网络监测
     * @param start
     * @param end
     * @return
     */
    List<EvevtsTargetNetwork> getTargetNetwork(String start, String end);

    /**
     * 部署设备数量
     * @param end
     * @return
     */
    List<EventsDeviceNum> getDeviceNum(String end);

    /**
     * 运行状态
     * @param start
     * @param end
     * @return
     */
    List<EventsRunningState> getRunningState(String start,String end);


}
