package com.nis.galaxy.config;

import com.nis.galaxy.domain.dict.*;
import com.nis.galaxy.template.ForBlockingInterface;
import com.nis.galaxy.web.service.DictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.reflect.annotation.ExceptionProxy;

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


    private int a =1;

    @Autowired
    DictService dictService;
    //局点表
    public HashMap<String, UiSysDictInfo> listDictUiSys = null;
    //业务表
    public HashMap<String, NtcServiceDict> listNtcService = null;
    //协议表
    public HashMap<String, String> listProtocolDict = null;
    //标签表
    public HashMap<String, UiTagDicInfo> listUiTagDicInfo = null;


    public HashMap<String, UiTagDicInfo> getListUiTagDicInfo() {
        return listUiTagDicInfo;
    }

    public HashMap<String, String> getListProtocolDict() {
        return listProtocolDict;
    }

    public HashMap<String, UiSysDictInfo> getListDictUiSys() {
        return listDictUiSys;
    }

    public HashMap<String, NtcServiceDict> getListNtcService() {
        return listNtcService;
    }

    @Scheduled(cron = "${dict.scheduled.cron.plan}")
    public void runMyLoad() {
        logger.info("执行初始化加载数据：\t局点字典表\t业务字典表\t协议字典表\t标签字典表");

        //局点表的预加载(中英俄)
        listDictUiSys = new HashMap<>();
        List<UiSysDictInfo> uiSysDictInfo = null;
        try {
            uiSysDictInfo = dictService.getUiSysDictInfo();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("局点表预加载出现异常");
        }
        if (uiSysDictInfo != null && uiSysDictInfo.size()>0) {
            for (UiSysDictInfo uiSysDictInfoZh : uiSysDictInfo) {
                listDictUiSys.put(String.valueOf(uiSysDictInfoZh.getSysDictId()), uiSysDictInfoZh);
            }
        }
        logger.info("局点表的预加载结果\t" + listDictUiSys);

        //业务字典表的预加载(中英俄)
        listNtcService = new HashMap<>();
        List<NtcServiceDict> ntcServiceDict = null;
        try {
            ntcServiceDict = dictService.getNtcServiceDict();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("业务字典表预加载出现异常");
        }
        if (ntcServiceDict != null && ntcServiceDict.size()>0) {
            for (NtcServiceDict serviceDict : ntcServiceDict) {
                listNtcService.put(String.valueOf(serviceDict.getServiceCode()), serviceDict);
            }
        }
        logger.info("业务字典表的预加载结果\t" + listNtcService);

        //协议字典表的预加载
        listProtocolDict = new HashMap<>();
        List<ProtocolDict> protocolDict = null;
        try {
            protocolDict = dictService.getProtocolDict();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("协议字典表的预加载出现异常");
        }
        if (protocolDict != null && protocolDict.size()>0) {
            for (ProtocolDict dict : protocolDict) {
                listProtocolDict.put(String.valueOf(dict.getViewCode()), dict.getProtocolType());
            }
        }
        logger.info("协议字典表的预加载结果为\t" + listProtocolDict);

        //标签表的预加载
        listUiTagDicInfo = new HashMap<>();
        List<UiTagDicInfo> uiTagDicInfo = null;
        try {
            uiTagDicInfo = dictService.getUiTagDicInfo();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("标签表的预加载出现异常");
        }
        if (uiTagDicInfo != null && uiTagDicInfo.size()>0) {
            for (UiTagDicInfo tagDicInfo : uiTagDicInfo) {
                listUiTagDicInfo.put(String.valueOf(tagDicInfo.getServiceDictId()), tagDicInfo);
            }
        }
        logger.info("标签字典表的预加载结果为\t" + uiTagDicInfo);
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
