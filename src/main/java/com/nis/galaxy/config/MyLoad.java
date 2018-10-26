package com.nis.galaxy.config;

import com.nis.galaxy.domain.dict.*;
import com.nis.galaxy.web.service.DictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    //业务表
    public HashMap<String, NtcServiceDict> listNtcService = new HashMap<>();
    //协议表
    public HashMap<String, String> listProtocolDict = new HashMap<>();
    //标签表
    public HashMap<String, UiTagDictInfo> listUiTagDicInfo = new HashMap<>();
    //地域表
    public HashMap<String, UiAreaDictInfo> listUiAreaDicInfo = new HashMap<>();
    //运营商表
    public HashMap<String, UiCarrierDictInfo> listUiCarrierDictInfo = new HashMap<>();

    public HashMap<String, UiCarrierDictInfo> getListUiCarrierDictInfo() {
        return listUiCarrierDictInfo;
    }

    public HashMap<String, UiAreaDictInfo> getListUiAreaDicInfo() {
        return listUiAreaDicInfo;
    }

    public HashMap<String, UiTagDictInfo> getListUiTagDicInfo() {
        return listUiTagDicInfo;
    }

    public HashMap<String, String> getListProtocolDict() {
        return listProtocolDict;
    }

    public HashMap<String, NtcServiceDict> getListNtcService() {
        return listNtcService;
    }

    @Scheduled(cron = "${dict.scheduled.cron.plan}")
    public void runMyLoad() {
        logger.info("执行初始化字典表：地域表\t运营商表\t业务表\t协议表\t标签表");


        //地域表的预加载
        List<UiAreaDictInfo> uiAreaDictInfo = new ArrayList<>();
        try {
            uiAreaDictInfo = dictService.getUiAreaDictInfo();
        } catch (Exception e) {
            logger.error("地域表预加载出现异常", e);
        }
        for (UiAreaDictInfo areaDictInfo : uiAreaDictInfo) {
            listUiAreaDicInfo.put(areaDictInfo.getAreaId(), areaDictInfo);
        }
        logger.info("地域表的预加载结果\t" + listUiAreaDicInfo);

        //运营商表的预加载
        List<UiCarrierDictInfo> uiCarrierDictInfo = new ArrayList<>();
        try {
            uiCarrierDictInfo = dictService.getUiCarrierDictInfo();
        } catch (Exception e) {
            logger.error("运营商表预加载出现异常", e);
        }
        for (UiCarrierDictInfo carrierDictInfo : uiCarrierDictInfo) {
            listUiCarrierDictInfo.put(carrierDictInfo.getCarrierId(), carrierDictInfo);
        }

        logger.info("运营商表的预加载结果\t" + listUiCarrierDictInfo);

        //业务表的预加载(中英俄)
        List<NtcServiceDict> ntcServiceDict = new ArrayList<>();
        try {
            ntcServiceDict = dictService.getNtcServiceDict();
        } catch (Exception e) {
            logger.error("业务表预加载出现异常", e);
        }
        if (!ntcServiceDict.isEmpty()) {
            for (NtcServiceDict serviceDict : ntcServiceDict) {
                listNtcService.put(String.valueOf(serviceDict.getServiceCode()), serviceDict);
            }
        }
        logger.info("业务表的预加载结果\t" + listNtcService);

        //协议表的预加载
        List<ProtocolDict> protocolDict = new ArrayList<>();
        try {
            protocolDict = dictService.getProtocolDict();
        } catch (Exception e) {
            logger.error("协议表的预加载出现异常", e);
        }
        if (!protocolDict.isEmpty()) {
            for (ProtocolDict dict : protocolDict) {
                listProtocolDict.put(String.valueOf(dict.getViewCode()), dict.getProtocolType());
            }
        }
        logger.info("协议表的预加载结果为\t" + listProtocolDict);

        //标签表的预加载
        List<UiTagDictInfo> uiTagDicInfo = new ArrayList<>();
        try {
            uiTagDicInfo = dictService.getUiTagDicInfo();
        } catch (Exception e) {
            logger.error("标签表的预加载出现异常", e);
        }
        if (!uiTagDicInfo.isEmpty()) {
            for (UiTagDictInfo tagDicInfo : uiTagDicInfo) {
                listUiTagDicInfo.put(tagDicInfo.getTagId(), tagDicInfo);
            }
        }
        logger.info("标签表的预加载结果为\t" + listUiTagDicInfo);
        logger.info("初始化字典表操作执行完成");
    }
    @Scheduled(fixedRate = 999999999)
    public void run() {
        if (a++ > 1) {
            return;
        }
        runMyLoad();
    }
}
