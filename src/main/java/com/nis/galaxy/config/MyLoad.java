package com.nis.galaxy.config;

import com.nis.galaxy.domain.dict.*;
import com.nis.galaxy.util.ListUtils;
import com.nis.galaxy.web.service.DictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author GouGe
 * @data 2018/8/30 16:45
 */


@Component
@Order(1)
public class MyLoad {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private int a = 1;
    @Value("${dict.moment.switch}")
    private boolean dictMomentSwitch;
    @Autowired
    DictService dictService;
    /**
     * 业务表
     */
    public Map<String, NtcServiceDict> listNtcService = new ConcurrentHashMap<>();
    /**
     * 协议表
     */
    public Map<String, String> listProtocolDict = new ConcurrentHashMap<>();
    ;
    /**
     * 标签表
     */
    public Map<String, UiTagDictInfo> listUiTagDicInfo = new ConcurrentHashMap<>();
    /**
     * 地域表
     */
//    public Map<String, UiAreaDictInfo> listUiAreaDicInfo = new ConcurrentHashMap<>();
    /**
     * 运营商表
     */
    public Map<String, UiCarrierDictInfo> listUiCarrierDictInfo = new ConcurrentHashMap<>();

    public Map<String, UiCarrierDictInfo> getListUiCarrierDictInfo() {
        return listUiCarrierDictInfo;
    }

//    public Map<String, UiAreaDictInfo> getListUiAreaDicInfo() {
//        return listUiAreaDicInfo;
//    }

    public Map<String, UiTagDictInfo> getListUiTagDicInfo() {
        return listUiTagDicInfo;
    }

    public Map<String, String> getListProtocolDict() {
        return listProtocolDict;
    }

    public Map<String, NtcServiceDict> getListNtcService() {
        return listNtcService;
    }

//    @Scheduled(cron = "${dict.scheduled.cron.plan}")
//    public void runMyLoadArea() {
//        logger.info("执行初始化字典表：地域表\t运营商表\t业务表\t协议表");
//        List<UiAreaDictInfo> uiAreaDictInfo = new ArrayList<>();
//        try {
//            uiAreaDictInfo = dictService.getUiAreaDictInfo();
//        } catch (Exception e) {
//            logger.error("地域表预加载出现异常", e);
//        }
//        for (UiAreaDictInfo areaDictInfo : uiAreaDictInfo) {
//            listUiAreaDicInfo.put(areaDictInfo.getAreaId(), areaDictInfo);
//        }
//        logger.info("地域表的预加载结果\t" + listUiAreaDicInfo);
//    }

    @Scheduled(cron = "${dict.service.cron.plan}")
    public void MyLoadService() {
        List<NtcServiceDict> ntcServiceDict = new ArrayList<>();
        Map<String, NtcServiceDict> mapNtcService = new HashMap<>();
        try {
            ntcServiceDict = dictService.getNtcServiceDict();
        } catch (Exception e) {
            logger.error("业务表加载出现异常", e);
        }
        if (!ntcServiceDict.isEmpty()) {
            for (NtcServiceDict serviceDict : ntcServiceDict) {
                mapNtcService.put(String.valueOf(serviceDict.getServiceCode()), serviceDict);
            }
            listNtcService = mapNtcService;
        }
        if (listNtcService != null) {
            logger.info("业务类型加载个数为\t" + listNtcService.size());
        } else {
            logger.info("listNtcService为null");
        }
    }

    @Scheduled(cron = "${dict.entrance.cron.plan}")
    public void MyLoadEntrance() {
        List<UiCarrierDictInfo> uiCarrierDictInfo = new ArrayList<>();
        Map<String, UiCarrierDictInfo> mapUiCarrierDictInfo = new HashMap<>();
        try {
            uiCarrierDictInfo = dictService.getUiCarrierDictInfo();
        } catch (Exception e) {
            logger.error("局点表加载出现异常", e);
        }
        if (!ListUtils.isEmptyList(uiCarrierDictInfo)) {
            for (UiCarrierDictInfo carrierDictInfo : uiCarrierDictInfo) {
                mapUiCarrierDictInfo.put(carrierDictInfo.getCarrierId(), carrierDictInfo);
            }
        }
        if (dictMomentSwitch) {
            mapUiCarrierDictInfo.put("0", new UiCarrierDictInfo("0", "Астана"));
            mapUiCarrierDictInfo.put("1", new UiCarrierDictInfo("1", "Астана"));
            mapUiCarrierDictInfo.put("2", new UiCarrierDictInfo("2", "Алматы"));
            mapUiCarrierDictInfo.put("3", new UiCarrierDictInfo("3", "Астана"));
        }
        if (mapUiCarrierDictInfo != null && mapUiCarrierDictInfo.size() > 0) {
            listUiCarrierDictInfo = mapUiCarrierDictInfo;
        }
        if (listNtcService != null) {
            logger.info("局点加载个数为\t" + listUiCarrierDictInfo.size());
        } else {
            logger.info("listUiCarrierDictInfo为null");
        }
    }

    @Scheduled(cron = "${dict.protocol.cron.plan}")
    public void MyLoadProtocol() {
        List<ProtocolDict> protocolDict = new ArrayList<>();
        Map<String, String> mapProtocolDict = new HashMap<>();
        try {
            protocolDict = dictService.getProtocolDict();
        } catch (Exception e) {
            logger.error("协议表加载出现异常", e);
        }
        if (!protocolDict.isEmpty()) {
            for (ProtocolDict dict : protocolDict) {
                mapProtocolDict.put(dict.getViewCode(), dict.getProtocolType());
            }
            listProtocolDict = mapProtocolDict;
        }
        if (listNtcService != null) {
            logger.info("协议加载个数为\t" + listProtocolDict.size());
        } else {
            logger.info("listNtcService为null");
        }
    }

    @Scheduled(cron = "${dict.tag.cron.plan}")
    public void MyLoadTag() {
        List<UiTagDictInfo> uiTagDicInfo = new ArrayList<>();
        Map<String, UiTagDictInfo> mapUiTagDicInfo = new HashMap<>();
        try {
            uiTagDicInfo = dictService.getUiTagDicInfo();
        } catch (Exception e) {
            logger.error("标签表加载出现异常", e);
        }
        if (!uiTagDicInfo.isEmpty()) {
            for (UiTagDictInfo tagDicInfo : uiTagDicInfo) {
                mapUiTagDicInfo.put(tagDicInfo.getTagId(), tagDicInfo);
            }
            listUiTagDicInfo = mapUiTagDicInfo;
        }
        if (listUiTagDicInfo != null) {
            logger.info("标签加载个数为\t" + listUiTagDicInfo.size());
        } else {
            logger.info("listUiTagDicInfo为null");
        }
    }

    @Scheduled(fixedRate = 999999999)
    public void run() {
        if (a++ > 1) {
            return;
        }
        MyLoadService();
        MyLoadEntrance();
        MyLoadProtocol();
        MyLoadTag();
    }
}
