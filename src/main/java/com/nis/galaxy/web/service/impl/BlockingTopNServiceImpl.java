package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.*;
import com.nis.galaxy.domain.dict.NtcServiceDict;
import com.nis.galaxy.domain.dict.UiSysDictInfo;
import com.nis.galaxy.domain.dict.UiTagDicInfo;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.dao.BlockingTopNDao;
import com.nis.galaxy.web.service.BlockingTopNService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class BlockingTopNServiceImpl implements BlockingTopNService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

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
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NtcRejectUrlStatistic> getBlockTop10URLOf1h(String start, String end) {
        List<NtcRejectUrlStatistic> list = new ArrayList<>();
        try {
            list = blockingTopNDao.getBlockTop10URLOf1h(start, end);
        } catch (Exception e) {
            logger.error("1小时内全国阻断URLTOP10\t查询异常\tserviceImpl", e);
        }
        //如果此次没有查询到数据查上一个1h的数据
        if (list == null || list.size() == 0) {
            try {
                String lastTime = blockingTopNDao.getBlockTop10URLOf1hLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = blockingTopNDao.getBlockTop10URLOf1h(time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("1小时内全国阻断URLTOP10\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        return list;
    }

    /**
     * 1小时内全国阻断网站TOP10
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NtcRejectWebsiteStatistic> getBlockTop10WebsiteOf1h(String start, String end) {
        List<NtcRejectWebsiteStatistic> list = new ArrayList<>();
        try {
            list = blockingTopNDao.getBlockTop10WebsiteOf1h(start, end);
        } catch (Exception e) {
            logger.error("全国阻断网站TOP10\t查询异常\tserviceImpl", e);
        }
        //如果此次没有查询到数据查上一个1h的数据
        if (list == null || list.size() == 0) {
            try {
                String lastTime = blockingTopNDao.getBlockTop10WebsiteOf1hLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = blockingTopNDao.getBlockTop10WebsiteOf1h(time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("全国阻断网站TOP10\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        return list;
    }

    /**
     * 封堵标签TOP10
     *
     * @return
     */
    @Override
    public List<NtcTagReport> getBlockTop10Tag24h(String start, String end) {
        List<NtcTagReport> list = new ArrayList<>();
        HashMap<String, UiTagDicInfo> listUiTagDicInfo = listUiTagDicInfo();
        try {
            list = blockingTopNDao.getBlockTop10Tag24hOnly(start, end);
        } catch (Exception e) {
            logger.error("封堵标签TOP10\t查询异常\tserviceImpl", e);
        }
        //如果此次没有查询到数据查上一个1h的数据
        if (list == null || list.size() == 0) {
            try {
                String lastTime = blockingTopNDao.getBlockTop10Tag24hLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = blockingTopNDao.getBlockTop10Tag24hOnly(time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("封堵标签TOP10\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list != null && list.size() != 0) {
            for (NtcTagReport ntcTagReport : list) {
                String type = ntcTagReport.getType();
                UiTagDicInfo uiTagDicInfo = listUiTagDicInfo.get(type);
                if (type != null && uiTagDicInfo != null) {
                    int itemType = uiTagDicInfo.getItemType();
                    if (itemType == 3) {
                        //取字典表中的字段值
                        ntcTagReport.setType(uiTagDicInfo.getItemValue());
                    }
                }
            }
        }
        return list;
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
        List<AreaBlockProtocol> list = new ArrayList<>();
        HashMap<String, UiSysDictInfo> listDictUiSys = listDictUiSys();
        HashMap<String, NtcServiceDict> listNtcService = listNtcService();
        try {
            //查分钟报
            list = blockingTopNDao.getAreaBlockTop10ProtocolOf24hOnly(start, end);
        } catch (Exception e) {
            logger.error("24小时区域阻断协议TOP10分钟报表\t查询异常\tserviceImpl", e);
        }
        //查询数据为空则查询上一天数据
        if (list == null || list.size() == 0) {
            try {
                String lastTime = blockingTopNDao.getAreaBlockTop10ProtocolOf24hLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOfday(time);
                list = blockingTopNDao.getAreaBlockTop10ProtocolOf24hOnly(time, DateUtils.getAfter24h(time));
            } catch (Exception e) {
                logger.error("24小时区域阻断协议TOP10分钟报表\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list != null && list.size() != 0) {
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
                            } else if (languageChose == 1) {
                                //中文
                                areaBlockProtocol.setType(ntcServiceDict.getServiceNameZh());
                                areaBlockProtocol.setArea(uiSysDictInfo.getItemDesc());
                            } else {
                                //俄文
                                areaBlockProtocol.setType(ntcServiceDict.getServiceNameRu());
                                areaBlockProtocol.setArea(uiSysDictInfo.getItemValue());
                            }
                        }
                    }
                }
            }
        }
        //假数据
        //int i = 1;
        //for (AreaBlockProtocol areaBlockProtocol : list) {
        //    try {
        //        Integer.parseInt(areaBlockProtocol.getArea());
        //        areaBlockProtocol.setArea("Astana" + i++);
        //    } catch (Exception e) {
        //        logger.info("找到对应字典表", e);
        //    }
        //    try {
        //        Integer.parseInt(areaBlockProtocol.getType());
        //        areaBlockProtocol.setType("ftp_monit" + i);
        //    } catch (Exception e) {
        //        logger.info("找到对应字典表", e);
        //    }
        //}
        return list;
    }

    /**
     * 5分钟内全国阻断URLTOP10
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NtcUrlIpCount> getBlockTop10URLOf5m(String start, String end) {
        List<NtcUrlIpCount> list = new ArrayList<>();
        try {
            list = blockingTopNDao.getBlockTop10URLOf5m(start, end);

        } catch (Exception e) {
            logger.error("5分钟内全国阻断URLTOP10\t查询异常\tserviceImpl", e);
        }
        //查询不到时，继续向前一段时间
        if (list == null || list.size() == 0) {
            try {
                String lastTime = blockingTopNDao.getBlockTop10URLOf5mLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingTopNDao.getBlockTop10URLOf5m(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("5分钟内全国阻断URLTOP10\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        return list;
    }

    /**
     * 5分钟内全国阻断websiteTOP10
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NtcUrlIpCount> getBlockTop10WebsiteOf5m(String start, String end) {
        List<NtcUrlIpCount> list = new ArrayList<>();
        try {
            list = blockingTopNDao.getBlockTop10WebsiteOf5m(start, end);
        } catch (Exception e) {
            logger.error("5分钟内全国阻断website\t查询异常\tserviceImpl", e);
        }
        //查询不到时，继续向前一段时间
        if (list == null || list.size() == 0) {
            try {
                String lastTime = blockingTopNDao.getBlockTop10WebsiteOf5mLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingTopNDao.getBlockTop10WebsiteOf5m(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("5分钟内全国阻断website\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        return list;
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
        List<NtcTagReport> list = new ArrayList<>();
        HashMap<String, UiTagDicInfo> listUiTagDicInfo = listUiTagDicInfo();
        try {
            list = blockingTopNDao.getBlockTop10Tag5mOnly(start, end);
        } catch (Exception e) {
            logger.error("5分钟内全国阻断标签TOP10\t查询异常\tserviceImpl", e);
        }
        //查询不到继续向前一段时间
        if (list == null || list.size() == 0) {
            try {
                String lastTime = blockingTopNDao.getBlockTop10Tag5mOnlyLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingTopNDao.getBlockTop10Tag5mOnly(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("5分钟内全国阻断标签TOP10\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list != null && list.size() != 0) {
            for (NtcTagReport ntcTagReport : list) {
                String type = ntcTagReport.getType();
                if (type != null && listUiTagDicInfo != null) {
                    UiTagDicInfo uiTagDicInfo = listUiTagDicInfo.get(type);
                    if (uiTagDicInfo != null) {
                        int itemType = uiTagDicInfo.getItemType();
                        //条件是属性item=3的
                        if (itemType == 3) {
                            ntcTagReport.setType(uiTagDicInfo.getItemValue());
                        }
                    }
                }
            }
        }
        return list;
    }
}
