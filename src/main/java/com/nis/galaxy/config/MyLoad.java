package com.nis.galaxy.config;

import com.nis.galaxy.domain.dict.*;
import com.nis.galaxy.web.service.DictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * @author GouGe
 * @data 2018/8/30 16:45
 */


@Component
public class MyLoad implements CommandLineRunner {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    DictService dictService;
    //局点表
    public HashMap<String, UiSysDictInfo> listDictUiSys = null;
    //业务表
    public HashMap<String, NtcServiceDict> listNtcService = null;
    //协议表
    public HashMap<String, String> listProtocolDict = null;
    //标签表
    public HashMap<String , UiTagDicInfo> listUiTagDicInfo = null;
    //地域表的加载
    public HashMap<String, AreaDict>  listAreaDict = null;


    public HashMap<String, AreaDict> getListAreaDict() {
        return listAreaDict;
    }

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

    @Override
    public void run(String... args) throws Exception {
        logger.info("执行初始化加载数据：\t局点字典表\t业务字典表\t协议字典表\t标签字典表\t地域字典表");
        //局点表的预加载(中英俄)
        listDictUiSys = new HashMap<>();
        List<UiSysDictInfo> uiSysDictInfo = dictService.getUiSysDictInfo();
        for (UiSysDictInfo uiSysDictInfoZh : uiSysDictInfo) {
            listDictUiSys.put(String.valueOf(uiSysDictInfoZh.getSysDictId()), uiSysDictInfoZh);
        }
        logger.info("局点表的预加载\t" + listDictUiSys);
        //业务字典表的预加载(中英俄)
        listNtcService = new HashMap<>();
        List<NtcServiceDict> ntcServiceDict = dictService.getNtcServiceDict();
        for (NtcServiceDict serviceDict : ntcServiceDict) {
            listNtcService.put(String.valueOf(serviceDict.getServiceCode()), serviceDict);
        }
        logger.info("业务字典表的预加载\t" + listNtcService);

        listProtocolDict = new HashMap<>();
        List<ProtocolDict> protocolDict = dictService.getProtocolDict();
        for (ProtocolDict dict : protocolDict) {
            listProtocolDict.put(String.valueOf(dict.getViewCode()),dict.getProtocolType());
        }
        logger.info("协议字典表的预加载\t" + listProtocolDict);

        listUiTagDicInfo = new HashMap<>();
        List<UiTagDicInfo> uiTagDicInfo = dictService.getUiTagDicInfo();
        for (UiTagDicInfo tagDicInfo : uiTagDicInfo) {
            listUiTagDicInfo.put(String.valueOf(tagDicInfo.getServiceDictId()),tagDicInfo);
        }
        logger.info("标签字典表的预加载\t" + listProtocolDict);

        listAreaDict =new  HashMap<>();

        List<AreaDict> areaDict = dictService.getAreaDict();
        for (AreaDict dict : areaDict) {
            listAreaDict.put(dict.getAreaId(),dict);
        }
        logger.info("地域字典表的预加载\t" + listProtocolDict);

        logger.info("初始化字典表操作执行完成");
    }
}
