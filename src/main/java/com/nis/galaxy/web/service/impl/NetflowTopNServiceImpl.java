package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.Entrance;
import com.nis.galaxy.domain.IpActiveInt;
import com.nis.galaxy.domain.NetFlowWater;
import com.nis.galaxy.domain.NetFlowWaterPush;
import com.nis.galaxy.domain.dict.UiAreaDictInfo;
import com.nis.galaxy.domain.dict.UiCarrierDictInfo;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.dao.NetflowTopNDao;
import com.nis.galaxy.web.service.NetflowTopNService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NetflowTopNServiceImpl implements NetflowTopNService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NetflowTopNDao netflowTopNDao;
    @Autowired
    MyLoad myLoad;
    @Value("${Language.Chose}")
    private int languageChose;
    @Value("${Open.False.Data}")
    private boolean OPENFALSEDATA;



    //运营商表
    public Map<String, UiCarrierDictInfo> listUiCarrierDictInfo() {
        return myLoad.listUiCarrierDictInfo;
    }

    /**
     * 5分钟内活跃IP流量TOP10
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<IpActiveInt> getIpActiveStatisticOf5m(String start, String end) {
        List<IpActiveInt> list = new ArrayList<>();
        Map<String, UiCarrierDictInfo> listCarrier = listUiCarrierDictInfo();
        try {
            list = netflowTopNDao.getIpActiveStatisticOf5mOnly(start,start, end);
        } catch (Exception e) {
            logger.error("5分钟内活跃IP流量TOP10\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            logger.warn("5分钟内活跃IP流量TOP10\tTRAFFIC_IP_ACTIVE_STATISTIC\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = netflowTopNDao.getIpActiveStatisticOf5mLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = netflowTopNDao.getIpActiveStatisticOf5mOnly(start,time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("5分钟内活跃IP流量TOP10\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (!list.isEmpty()) {
            for (IpActiveInt ipActiveInt : list) {
                String area = ipActiveInt.getArea();
                UiCarrierDictInfo uiCarrierDictInfo = listCarrier.get(area);
                if (uiCarrierDictInfo != null) {
                    ipActiveInt.setArea(uiCarrierDictInfo.getItemValue());
                }
            }
        }
        if (list.isEmpty() && OPENFALSEDATA) {
            int i = (int) (Math.random() * 200);
            logger.info("5分钟内活跃IP流量TOP10------->测试数据");
            list.add(new IpActiveInt(start, "Kazakhtelecom", "119.108.229.153", i + 20));
            list.add(new IpActiveInt(start, "kazTransCom", "121.236.4.78", i + 16));
            list.add(new IpActiveInt(start, "Kazakhtelecom", "192.158.229.15", i + 15));
            list.add(new IpActiveInt(start, "kazTransCom", "192.168.99.153", i + 14));
            list.add(new IpActiveInt(start, "Kazakhtelecom", "119.188.29.125", i + 12));
            list.add(new IpActiveInt(start, "Kazakhtelecom", "119.168.229.45", i + 6));
            list.add(new IpActiveInt(start, "Kazakhtelecom", "119.168.229.11", i + 5));
            list.add(new IpActiveInt(start, "kazTransCom", "119.168.229.1", i + 3));
            list.add(new IpActiveInt(start, "Kazakhtelecom", "119.168.229.6", i + 2));
            list.add(new IpActiveInt(start, "kazTransCom", "119.168.229.8", i));
        }
        return list;
    }

    /**
     * 互联网流量TOP10
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NetFlowWaterPush> getNetFlowWaterTop10(String start, String end, int range) {
        Map<String, UiCarrierDictInfo> listCarrier = listUiCarrierDictInfo();
        List<NetFlowWater> list = new ArrayList<>();
        List<NetFlowWaterPush> resule = new ArrayList<>();
        List<Entrance> entrance = netflowTopNDao.getIpEntranceRe();
        try {
            list = netflowTopNDao.getNetFlowWaterTop10(start, end);
        } catch (Exception e) {
            logger.error("互联网流量TOP10\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            logger.warn("互联网流量TOP10\tTRAFFIC_NETFLOW_PORT_INFO\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = netflowTopNDao.getNetFlowWaterTop10LastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                String lastEnd = DateUtils.getAfter5min(time);
                String lastStart = null;
                if (range == 1) {
                    lastStart = DateUtils.getBeforeNminOfTime(lastEnd, -60);
                } else if (range == 7) {
                    lastStart = DateUtils.getBeforeNminOfTime(lastEnd, -60 * 24 * 7);
                } else {
                    lastStart = DateUtils.getBeforeNminOfTime(lastEnd, -60 * 24);
                }
                list = netflowTopNDao.getNetFlowWaterTop10(lastStart, lastEnd);
            } catch (Exception e) {
                logger.error("互联网流量TOP10\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        int size = list.size();
        if (size == 1) {
            long avg = 0, subtractSum = 0;
            String area = list.get(0).getArea();
            long count = list.get(0).getCount();
            if (count >= 0) {
                avg = count;
            }
            resule.add(new NetFlowWaterPush(start, area, avg));
        } else if (size > 1) {
            for (Entrance entrance1 : entrance) {
                List<NetFlowWater> temp = new ArrayList<>();
                String entrance2 = entrance1.getEntrance();
                for (int i = 0; i < size; i++) {
                    String area = list.get(i).getArea();
                    if (entrance2.equals(area)) {
                        temp.add(list.get(i));
                    }
                }
                long endCount = 0;
                long startCount = 0;
                long count = 0;
                long avg = 0;
                Date timeEnd;
                Date timeStart;
                int tempSize = temp.size();
                for (int i = tempSize - 1; i > 0; i--) {
                    endCount = temp.get(i).getCount();
                    startCount = temp.get(i - 1).getCount();
                    timeEnd = temp.get(i).getTime();
                    timeStart = temp.get(i - 1).getTime();
                    long timeSecond = (timeEnd.getTime() - timeStart.getTime()) / 1000;
                    count = endCount - startCount;
                    if (count >= 0) {
                        avg = avg + count / timeSecond;
                    } else {
                        if (endCount > 0) {
                            avg = avg + endCount / timeSecond;
                        }
                    }
                }
                if (tempSize > 1) {
                    avg = avg / (size - 1);
                } else if (tempSize == 1) {
                    avg = temp.get(0).getCount();
                }else {
                    continue;
                }
                resule.add(new NetFlowWaterPush(start, temp.get(0).getArea(), avg));
            }
        } else {
            long avg = 0;
            avg = 0;
        }
        for (NetFlowWaterPush netFlowWaterPush : resule) {
            String area = netFlowWaterPush.getArea();
            UiCarrierDictInfo uiCarrierDictInfo = listCarrier.get(area);
            if (uiCarrierDictInfo == null) {
                continue;
            }
            netFlowWaterPush.setArea(uiCarrierDictInfo.getItemValue());
        }
        if (resule.isEmpty() && OPENFALSEDATA) {
            logger.info("互联网流量TOP10------->测试数据");
            if (languageChose == 3) {
                resule.add(new NetFlowWaterPush(start, "Астана", (long) (Math.random() * 10000)));
            } else {
                resule.add(new NetFlowWaterPush(start, "Astana", (long) (Math.random() * 10000)));
            }
        }
        return resule;
    }
}
