package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.*;
import com.nis.galaxy.domain.dict.NtcServiceDict;
import com.nis.galaxy.domain.dict.UiAreaDictInfo;
import com.nis.galaxy.domain.dict.UiCarrierDictInfo;
import com.nis.galaxy.domain.dict.UiTagDictInfo;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.dao.BlockingTopNDao;
import com.nis.galaxy.web.service.BlockingTopNService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BlockingTopNServiceImpl implements BlockingTopNService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BlockingTopNDao blockingTopNDao;
    @Autowired
    MyLoad myLoad;
    @Value("${Language.Chose}")
    private int languageChose;
    @Value("${Open.False.Data}")
    private boolean OPENFALSEDATA;

    //地域表
    public Map<String, UiAreaDictInfo> listArea() {
        return myLoad.listUiAreaDicInfo;
    }

    //运营商表
    public Map<String, UiCarrierDictInfo> listUiCarrierDictInfo() {
        return myLoad.listUiCarrierDictInfo;
    }

    //标签表
    public Map<String, UiTagDictInfo> listUiTagDicInfo() {
        return myLoad.listUiTagDicInfo;
    }

    //业务表
    public Map<String, NtcServiceDict> listNtcService() {
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
        if (list.isEmpty()) {
            logger.warn("1小时内全国阻断URLTOP10\tNTC_REJECT_URL_STATISTIC\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                String lastTime = blockingTopNDao.getBlockTop10URLOf1hLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = blockingTopNDao.getBlockTop10URLOf1h(time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("1小时内全国阻断URLTOP10\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("1小时内全国阻断URLTOP10------->测试数据");
            int i = (int) (Math.random() * 200);
            list.add(new NtcRejectUrlStatistic(start, "www.bing.com/fd/ls/lsp.aspx", i + 20));
            list.add(new NtcRejectUrlStatistic(start, "www.bing.com/fd/ls/lsp.aspx?", i + 18));
            list.add(new NtcRejectUrlStatistic(start, "www.badidu.com/xx/ee/ee.a2d", i + 15));
            list.add(new NtcRejectUrlStatistic(start, "www.bing.com/ffdsd/ls/lsp.dfd", i + 13));
            list.add(new NtcRejectUrlStatistic(start, "www.bing.com/dfs/ls/lsp.aspx", i + 11));
            list.add(new NtcRejectUrlStatistic(start, "www.baidu.com/er/r/r.rje", i + 10));
            list.add(new NtcRejectUrlStatistic(start, "www.bing.com/fdd/ls/lsp.asrpx", i + 7));
            list.add(new NtcRejectUrlStatistic(start, "www.bing.com/fd/ls/dsf.fsd", i + 6));
            list.add(new NtcRejectUrlStatistic(start, "www.bing.com/fdd/sdf/lsp.fds?0", i + 5));
            list.add(new NtcRejectUrlStatistic(start, "www.bing.com/fdd/af/sd.041werf", i + 1));
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
            logger.error("1小时内全国阻断网站TOP10\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            logger.warn("1小时内全国阻断网站TOP10\tNTC_REJECT_WEBSITE_STATISTIC\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                String lastTime = blockingTopNDao.getBlockTop10WebsiteOf1hLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = blockingTopNDao.getBlockTop10WebsiteOf1h(time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("1小时内全国阻断网站TOP10\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("1小时内全国阻断网站TOP10------->测试数据");
            int i = (int) (Math.random() * 200);
            list.add(new NtcRejectWebsiteStatistic(start, "7.au.download.windowsupdate.com", i + 40));
            list.add(new NtcRejectWebsiteStatistic(start, "3.au.download.windowsupdate.com", i + 33));
            list.add(new NtcRejectWebsiteStatistic(start, "au.download.windowsupdate.com", i + 30));
            list.add(new NtcRejectWebsiteStatistic(start, "3.au.download.windowsupdate.com", i + 23));
            list.add(new NtcRejectWebsiteStatistic(start, "store-vidoe.s-microsoft.com", i + 20));
            list.add(new NtcRejectWebsiteStatistic(start, "store-chaange.s-microsoft.com", i + 16));
            list.add(new NtcRejectWebsiteStatistic(start, "store-images.s-microsoft.com", i + 6));
            list.add(new NtcRejectWebsiteStatistic(start, "au-download.s-microsoft.com", i + 5));
            list.add(new NtcRejectWebsiteStatistic(start, "ps-check.s-microsoft.com", i + 3));
            list.add(new NtcRejectWebsiteStatistic(start, "store-pa.s-microsoft.com", i));
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
        Map<String, UiCarrierDictInfo> listCarrier = listUiCarrierDictInfo();
        Map<String, NtcServiceDict> listNtcService = listNtcService();
        try {
            //查分钟报
            list = blockingTopNDao.getAreaBlockTop10ProtocolOf24hOnly(start, end);
        } catch (Exception e) {
            logger.error("24小时区域阻断协议TOP10\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            logger.warn("24小时区域阻断协议TOP10\tNTC_ENTRANCE_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                String lastTime = blockingTopNDao.getAreaBlockTop10ProtocolOf24hLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOfday(time);
                list = blockingTopNDao.getAreaBlockTop10ProtocolOf24hOnly(time, DateUtils.getAfter24h(time));
            } catch (Exception e) {
                logger.error("24小时区域阻断协议TOP10\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (!list.isEmpty()) {
            for (AreaBlockProtocol areaBlockProtocol : list) {
                String type = areaBlockProtocol.getType();
                String area = areaBlockProtocol.getArea();
                UiCarrierDictInfo uiCarrierDictInfo = listCarrier.get(area);
                NtcServiceDict ntcServiceDict = listNtcService.get(type);
                if (uiCarrierDictInfo != null) {
                    areaBlockProtocol.setArea(uiCarrierDictInfo.getItemValue());
                }
                if (ntcServiceDict == null) {
                    continue;
                }
                if (languageChose == 2) {
                    areaBlockProtocol.setType(ntcServiceDict.getServiceNameEn());
                } else if (languageChose == 3) {
                    areaBlockProtocol.setType(ntcServiceDict.getServiceNameRu());
                } else {
                    areaBlockProtocol.setType(ntcServiceDict.getServiceNameZh());
                }
            }
        }
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("24小时区域阻断协议TOP10------->测试数据");
            int i = (int) (Math.random() * 400);
            list.add(new AreaBlockProtocol(start, "Kazakhtelecom", "ssl_reject", i + 40));
            list.add(new AreaBlockProtocol(start, "kazTransCom", "file_transfer_digest_reject", i + 31));
            list.add(new AreaBlockProtocol(start, "Kazakhtelecom", "p2p_reject", i + 20));
            list.add(new AreaBlockProtocol(start, "kazTransCom", "encrypted_tunnel_behavior_reject", i + 18));
            list.add(new AreaBlockProtocol(start, "Kazakhtelecom", "website_dns_monit", i + 17));
            list.add(new AreaBlockProtocol(start, "Kazakhtelecom", "mail_monit", i + 16));
            list.add(new AreaBlockProtocol(start, "kazTransCom", "ipsec_monit", i + 10));
            list.add(new AreaBlockProtocol(start, "Kazakhtelecom", "socks_monit", i + 8));
            list.add(new AreaBlockProtocol(start, "kazTransCom", "bgp_monit", i + 7));
        }
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
        if (list.isEmpty()) {
            logger.warn("5分钟内全国阻断URLTOP10\tNTC_REJECT_URL_STATISTIC\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                String lastTime = blockingTopNDao.getBlockTop10URLOf5mLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingTopNDao.getBlockTop10URLOf5m(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("5分钟内全国阻断URLTOP10\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("5分钟内全国阻断URLTOP10------->测试数据");
            int i = (int) (Math.random() * 100);
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/fd/ls/lsp.aspx", i + 70));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/fd/ls/lsp.aspx?", i + 64));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/cnhp/life?mkt=zh-CN&ensearch=0&IID=SERP.5044&IG=A5994504E8DF4E49AD7E27B89274399", i + 60));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/cnhp/life?mkt=zh-CN&enseaE8DF4E49AD7E27B8927439o9", i + 30));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/cnhp/life?yuyt9", i + 20));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/fd/life?yuytu?TYUytu", i + 15));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/xg/dfd", i + 15));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/cnhp/ww", i + 15));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/cnhp/4ttre", i + 15));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/cnhp/retr", i + 15));
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
        if (list == null || list.isEmpty()) {
            logger.warn("5分钟内全国阻断website\tNTC_REJECT_WEBSITE_STATISTIC\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                String lastTime = blockingTopNDao.getBlockTop10WebsiteOf5mLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingTopNDao.getBlockTop10WebsiteOf5m(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("5分钟内全国阻断website\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("5分钟内全国阻断website------->测试数据");
            int i = (int) (Math.random() * 100);
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/fd/ls/ls", i + 10));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/fd/ls/ldfs", i + 9));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/cnhp/lidghfsdf", i + 6));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/cnhp/ligfhgfhgft4f", i + 5));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/cnhp/lg345if", i + 5));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/fd/lif7e?", i + 4));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/xg/d4564d", i + 3));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "p3.qhimgs0.com", i + 2));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/cnhp/4t78t", i + 1));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "7.au.download.windowsupdate.com", i));
            list.add(new NtcUrlIpCount(start, "204.79.197.200", "www.bing.com/cnhp/rde23t", i));
        }
        return list;
    }

    /**
     * 1h内全国阻断标签TOP100
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NtcTagReport> getBlockTop100Tag1h(String start, String end) {
        List<NtcTagReport> list = new ArrayList<>();
        Map<String, UiTagDictInfo> listUiTagDicInfo = listUiTagDicInfo();
        try {
            list = blockingTopNDao.getBlockTop10Tag5mOnly(start, end);
        } catch (Exception e) {
            logger.error("1h内全国阻断标签TOP100\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            logger.warn("1h内全国阻断标签TOP100\tNTC_TAG_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                String lastTime = blockingTopNDao.getBlockTop10Tag5mOnlyLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = blockingTopNDao.getBlockTop10Tag5mOnly(time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("1h内全国阻断标签TOP100\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (!list.isEmpty()) {
            for (NtcTagReport ntcTagReport : list) {
                String type = ntcTagReport.getType();
                UiTagDictInfo uiTagDicInfo = listUiTagDicInfo.get(type);
                if (uiTagDicInfo == null) {
                    continue;
                }
                ntcTagReport.setType(uiTagDicInfo.getItemValue());
            }
        }
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("1h内全国阻断标签TOP100------->测试数据");
            int i = (int) (Math.random() * 200);
            list.add(new NtcTagReport(start, "News", i + 30));
            list.add(new NtcTagReport(start, "kill", i + 30));
            list.add(new NtcTagReport(start, "golden Three fat", i + 30));
            list.add(new NtcTagReport(start, "SaDam", i + 30));
            list.add(new NtcTagReport(start, "Two Sessions", i + 30));
        }
        return list;
    }
}
