package com.nis.galaxy.config;

import com.nis.galaxy.domain.dict.*;
import com.nis.galaxy.util.ConfigUtil;
import com.nis.galaxy.web.service.DictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Map<String, AreaDictInfo> listAreaDictInfo = new HashMap<>();

    public Map<String, AreaDictInfo> getlistAreaDictInfo() {
        return listAreaDictInfo;
    }


    public Map<String, UiTagDictInfo> getListUiTagDicInfo() {
        return listUiTagDicInfo;
    }

    public Map<String, String> getListProtocolDict() {
        return listProtocolDict;
    }

    @Scheduled(cron = "${dict.service.cron.plan}")
    public Map<String, NtcServiceDict> getListNtcService() {
        return listNtcService;
    }


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
        if (ConfigUtil.languageChose == 3) {
            listAreaDictInfo.put("1", new AreaDictInfo("1", "Астана"));
            listAreaDictInfo.put("2", new AreaDictInfo("1", "Алматы"));
        } else if (ConfigUtil.languageChose == 2) {
            listAreaDictInfo.put("1", new AreaDictInfo("1", "Astana"));
            listAreaDictInfo.put("2", new AreaDictInfo("2", "Almaty"));
        } else {
            listAreaDictInfo.put("1", new AreaDictInfo("1", "阿斯塔纳市"));
            listAreaDictInfo.put("2", new AreaDictInfo("2", "阿拉木图市"));
        }
        logger.info("目前地域表为：" + listAreaDictInfo);
        MyLoadService();
        MyLoadProtocol();
        MyLoadTag();
    }
}
