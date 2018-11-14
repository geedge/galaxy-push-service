package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.IpActiveInt;
import com.nis.galaxy.domain.NetFlowWater;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    //地域表
    public Map<String, UiAreaDictInfo> listArea() {
        return myLoad.listUiAreaDicInfo;
    }

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
            list = netflowTopNDao.getIpActiveStatisticOf5mOnly(start, end);
        } catch (Exception e) {
            logger.error("5分钟内活跃IP流量TOP10\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            logger.warn("5分钟内活跃IP流量TOP10\tTRAFFIC_IP_ACTIVE_STATISTIC\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                String lastTime = netflowTopNDao.getIpActiveStatisticOf5mLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = netflowTopNDao.getIpActiveStatisticOf5mOnly(time, DateUtils.getAfter5min(time));
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
    public List<NetFlowWater> getNetFlowWaterTop10(String start, String end, int range) {
        List<NetFlowWater> list = new ArrayList<>();
        List<NetFlowWater> resule = new ArrayList<>();
        try {
            list = netflowTopNDao.getNetFlowWaterTop10(start, end);
        } catch (Exception e) {
            logger.error("互联网流量TOP10\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            logger.warn("互联网流量TOP10\tTRAFFIC_NETFLOW_PORT_INFO\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                String lastTime = netflowTopNDao.getNetFlowWaterTop10LastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
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
        int avg = 0;
        int size = list.size();
        for (int i = size; i > 1; i--) {
            int count = list.get(i - 1).getCount() - list.get(i - 2).getCount();
            if (count >= 0) {
                avg = avg + count;
            } else {
                avg = avg + list.get(i).getCount();
            }
        }
        if (size == 1) {
            avg = list.get(0).getCount();
        } else if (size > 1) {
            avg = avg / (size - 1);
        }
        if (!list.isEmpty()) {
            if (languageChose == 2) {
                resule.add(new NetFlowWater(start, "Akmola", avg));
            } else {
                resule.add(new NetFlowWater(start, "Ақмолаоблысы", avg));
            }
            return resule;
        }
        //测试数据
        if (list.isEmpty() && OPENFALSEDATA) {
            list = new ArrayList<>();
            logger.info("互联网流量TOP10------->测试数据");
            if (languageChose == 3) {
                list.add(new NetFlowWater(start, "Ақмолаоблысы", (int) (Math.random() * 1000)));
            } else {
                list.add(new NetFlowWater(start, "Akmola", (int) (Math.random() * 1000)));
            }
        }
        return list;
    }
}
