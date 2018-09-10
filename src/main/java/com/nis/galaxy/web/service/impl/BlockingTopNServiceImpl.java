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
    @Value("${Language.Chose}")
    private int languageChose;


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

    /**
     * 1小时内全国阻断URLTOP10
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NtcRejectUrlStatistic> getBlockTop10URLOf1h(String start, String end) {

        List<NtcRejectUrlStatistic> result = blockingTopNDao.getBlockTop10URLOf1h(start, end);
        //如果此次没有查询到数据查上一个1h的数据
        if(result.size()==0){
            return getBlockTop10URLOf1h(DateUtils.getBeforeNminOfTime(start,-60),DateUtils.getBeforeNminOfTime(end,-60));
        }
        return result;
    }

    /**
     * 1小时内全国阻断网站TOP10
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NtcRejectWebsiteStatistic> getBlockTop10WebsiteOf1h(String start, String end) {
        List<NtcRejectWebsiteStatistic> result = blockingTopNDao.getBlockTop10WebsiteOf1h(start, end);
        //如果此次没有查询到数据查上一个1h的数据
        if(result.size()==0){
            return getBlockTop10WebsiteOf1h(DateUtils.getBeforeNminOfTime(start,-60),DateUtils.getBeforeNminOfTime(end,-60));
        }
        return result;
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
                    //取字典表中的字段值
                    ntcTagReport.setType(uiTagDicInfo.getItemValue());
                    result.add(ntcTagReport);
                }
            }
        }
        //如果此次没有查询到数据查上一个1h的数据
        if(result.size()==0){
            return getBlockTop10Tag24h(DateUtils.getBeforeNminOfTime(start,-60),DateUtils.getBeforeNminOfTime(end,-60));
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
        list = blockingTopNDao.getAreaBlockTop10ProtocolOf24hDayOnly(start, end);
        if (list == null) {
            //查分钟报
            list = blockingTopNDao.getAreaBlockTop10ProtocolOf24hOnly(start, end);
        }
        //映射
        for (AreaBlockProtocol areaBlockProtocol : list) {
            String type = areaBlockProtocol.getType();
            String area = areaBlockProtocol.getArea();
            if (type != null && area != null) {
                NtcServiceDict ntcServiceDict = listNtcService.get(type);
                UiSysDictInfo uiSysDictInfo = listDictUiSys.get(area);
                if (ntcServiceDict != null && uiSysDictInfo != null) {
                    //获取itemType=1位地域
                    int itemType = uiSysDictInfo.getItemType();
                    if (itemType == 1) {
                        if (languageChose == 2) {
                            //设置英文字段
                            areaBlockProtocol.setType(ntcServiceDict.getServiceNameEn());
                            areaBlockProtocol.setArea(uiSysDictInfo.getItemValue());
                            result.add(areaBlockProtocol);
                        } else if (languageChose == 1) {
                            //中文
                            areaBlockProtocol.setType(ntcServiceDict.getServiceNameZh());
                            areaBlockProtocol.setArea(uiSysDictInfo.getItemDesc());
                            result.add(areaBlockProtocol);
                        } else {
                            //俄文
                            areaBlockProtocol.setType(ntcServiceDict.getServiceNameRu());
                            areaBlockProtocol.setArea(uiSysDictInfo.getItemValue());
                            result.add(areaBlockProtocol);
                        }
                    }
                }
            }
        }
        //查询数据为空则查询上一天数据
        if(result.size()==0){
            return getAreaBlockTop10ProtocolOf24(DateUtils.getBeforeNminOfTime(start,-60*24),DateUtils.getBeforeNminOfTime(end,-60*24));
        }
        return result;

    }

    /**
     * 5分钟内全国阻断URLTOP10
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NtcUrlIpCount> getBlockTop10URLOf5m(String start, String end) {
        List<NtcUrlIpCount> result = blockingTopNDao.getBlockTop10URLOf5m(start, end);
        //查询不到时，继续向前一段时间
        if(result.size()==0){
            return getBlockTop10URLOf5m(DateUtils.getBeforeNminOfTime(start,-5),DateUtils.getBeforeNminOfTime(end,-5));
        }
        return result;
    }

    /**
     * 5分钟内全国阻断website
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NtcUrlIpCount> getBlockTop10WebsiteOf5m(String start, String end) {
        List<NtcUrlIpCount> result = blockingTopNDao.getBlockTop10WebsiteOf5m(start, end);
        //查询不到时，继续向前一段时间
        if(result.size()==0){
            return getBlockTop10WebsiteOf5m(DateUtils.getBeforeNminOfTime(start,-5),DateUtils.getBeforeNminOfTime(end,-5));
        }
        return result;
    }

    /**
     * 5分钟内全国阻断标签TOP10
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NtcTagReport> getBlockTop10Tag5m(String start, String end) {
        List<NtcTagReport> result = new ArrayList<>();
        HashMap<String, UiTagDicInfo> listUiTagDicInfo = myLoad.listUiTagDicInfo;
        List<NtcTagReport> list = blockingTopNDao.getBlockTop10Tag5mOnly(start, end);
        for (NtcTagReport ntcTagReport : list) {
            String type = ntcTagReport.getType();
            if (type != null && listUiTagDicInfo != null) {
                UiTagDicInfo uiTagDicInfo = listUiTagDicInfo.get(type);
                if (uiTagDicInfo != null) {
                    int itemType = uiTagDicInfo.getItemType();
                    //条件是属性item=3的
                    if (itemType == 3) {
                        ntcTagReport.setType(uiTagDicInfo.getItemValue());
                        result.add(ntcTagReport);
                    }
                }
            }
        }
        //查询不到继续向前一段时间
        if(result.size()==0){
            return getBlockTop10Tag5m(DateUtils.getBeforeNminOfTime(start,-5),DateUtils.getBeforeNminOfTime(end,-5));
        }
        return result;
    }
}
