package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.*;
import com.nis.galaxy.domain.dict.NtcServiceDict;
import com.nis.galaxy.domain.dict.UiSysDictInfo;
import com.nis.galaxy.domain.dict.UiTagDicInfo;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.dao.BlockingTopNDao;
import com.nis.galaxy.web.service.BlockingTopNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class BlockingTopNServiceImpl implements BlockingTopNService {

    @Autowired
    BlockingTopNDao blockingTopNDao;
    @Autowired
    MyLoad myLoad;
    @Value("${Area1.entranceId}")
    private String area1EntranceId;
    @Value("${Area2.entranceId}")
    private String area2EntranceId;

    //局点表
    public HashMap<String, UiSysDictInfo> listDictUiSys() {
        return myLoad.listDictUiSys;
    }

    //标签表
    public HashMap<String, UiTagDicInfo> listUiTagDicInfo() {
        return myLoad.listUiTagDicInfo;
    }

    //业务表
    public HashMap<String, NtcServiceDict> listNtcService() {
        return myLoad.listNtcService;
    }

    @Override
    public List<NtcRejectUrlStatistic> getBlockTop10URLOf1h(String start, String end) {

        return blockingTopNDao.getBlockTop10URLOf1h(start, end);
    }

    @Override
    public List<NtcRejectWebsiteStatistic> getBlockTop10WebsiteOf1h(String start, String end) {

        return blockingTopNDao.getBlockTop10WebsiteOf1h(start, end);
    }

    /**
     * 24小时内封堵标签TOP10
     *
     * @return
     */
    @Override
    public List<NtcTagReport> getBlockTop10Tag24h(String start, String end) {
        List<NtcTagReport> result = new ArrayList<>();
        List<NtcTagReport> list = blockingTopNDao.getBlockTop10Tag24hOnly(start, end);
        HashMap<String, UiTagDicInfo> listUiTagDicInfo = listUiTagDicInfo();
        for (NtcTagReport ntcTagReport : list) {
            String type = ntcTagReport.getType();
            UiTagDicInfo uiTagDicInfo = listUiTagDicInfo.get(type);
            if (type != null && uiTagDicInfo != null) {
                int itemType = uiTagDicInfo.getItemType();
                if (itemType == 3) {
                    ntcTagReport.setType(uiTagDicInfo.getItemValue());
                    result.add(ntcTagReport);
                }
            }
        }
        return result;
     }

    /**
     * 24小时区域阻断协议TOP10
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<AreaBlockProtocol> getAreaBlockTop10ProtocolOf24(String start, String end) {
        HashMap<String, UiSysDictInfo> listDictUiSys = listDictUiSys();
        HashMap<String, NtcServiceDict> listNtcService = listNtcService();
        ArrayList<AreaBlockProtocol> result = new ArrayList<>();
        //查询日报表
        List<AreaBlockProtocol> list = null;
        list = blockingTopNDao.getAreaBlockTop10ProtocolOf24hDayOnly(start, end, area1EntranceId, area2EntranceId);
        if (list == null) {
            //查分钟报
            list = blockingTopNDao.getAreaBlockTop10ProtocolOf24hOnly(start, end, area1EntranceId, area2EntranceId);
        }

        //映射
        for (AreaBlockProtocol areaBlockProtocol : list) {
            String type = areaBlockProtocol.getType();
            String area = areaBlockProtocol.getArea();
            if (type != null && area != null) {
                NtcServiceDict ntcServiceDict = listNtcService.get(type);
                UiSysDictInfo uiSysDictInfo = listDictUiSys.get(area);
                if (ntcServiceDict != null && uiSysDictInfo != null) {
                    //设置英文字段
                    areaBlockProtocol.setType(ntcServiceDict.getServiceNameEn());
                    areaBlockProtocol.setArea(uiSysDictInfo.getItemValue());
                    result.add(areaBlockProtocol);
                }
            }
        }
        return result;

    }

    @Override
    public List<NtcUrlIpCount> getBlockTop10URLOf5m(String start, String end) {
        return blockingTopNDao.getBlockTop10URLOf5m(start, end);
    }

    @Override
    public List<NtcUrlIpCount> getBlockTop10WebsiteOf5m(String start, String end) {
        return blockingTopNDao.getBlockTop10WebsiteOf5m(start, end);
    }

    /**
     * 5分钟内全国阻断标签TOP10
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NtcTagReport> getBlockTop10Tag5m(String start, String end) {
        List<NtcTagReport> result= new ArrayList<>();
        HashMap<String, UiTagDicInfo> listUiTagDicInfo = myLoad.listUiTagDicInfo;
        List<NtcTagReport> list = blockingTopNDao.getBlockTop10Tag5mOnly(start,end);
        for (NtcTagReport ntcTagReport : list) {
            String type = ntcTagReport.getType();
            if(type!=null && listUiTagDicInfo!=null){
                UiTagDicInfo uiTagDicInfo = listUiTagDicInfo.get(type);
                if(uiTagDicInfo!=null){
                    int itemType = uiTagDicInfo.getItemType();
                     if(itemType==3){
                        ntcTagReport.setType(uiTagDicInfo.getItemValue());
                        result.add(ntcTagReport);
                    }
                }
            }
        }
        return  result;
     }
}
