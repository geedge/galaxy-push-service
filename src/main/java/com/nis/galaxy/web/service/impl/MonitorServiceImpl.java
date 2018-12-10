package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.*;
import com.nis.galaxy.domain.dict.AreaDictInfo;
import com.nis.galaxy.util.ConfigUtil;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.util.ListUtils;
import com.nis.galaxy.web.dao.MonitorDao;
import com.nis.galaxy.web.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author GouGe
 * @data 2018/9/8 14:55
 */
@Service
public class MonitorServiceImpl implements MonitorService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    MonitorDao monitorDao;
    @Autowired
    MyLoad myLoad;

    //地域表
    public Map<String, AreaDictInfo> listAreaDictInfo() {
        return myLoad.listAreaDictInfo;
    }

    /**
     * 系统监控
     *
     * @return
     */
    @Override
    public List<NmsServerBean> getMonitorOf1m() {
        List<NmsServerBean> list = new ArrayList<>();
        Map<String, AreaDictInfo> listArea = listAreaDictInfo();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = monitorDao.getSystemMonitor();
        } catch (Exception e) {
            logger.error("系统监控\t查询异常\tserviceImpl", e);
        }
        if (!ListUtils.isEmptyList(list)) {
            for (NmsServerBean nmsServerBean : list) {
                String area = nmsServerBean.getArea();
                AreaDictInfo areaDictInfo = listArea.get(area);
                if (areaDictInfo != null) {
                    nmsServerBean.setArea(areaDictInfo.getItemValue());
                }
            }
        }
        logger.info("系统监控耗时：" + (System.currentTimeMillis() - timeMillisStart));
        if (ListUtils.isEmptyList(list) && ConfigUtil.testData) {
            logger.info("系统监控------->测试数据");
            list = new ArrayList<>();
            NmsServerBean nmsServerBean = new NmsServerBean();
            nmsServerBean.setAbnormal(2);
            nmsServerBean.setArea("Астана");
            nmsServerBean.setNormal(8);
            nmsServerBean.setSystem("ntc");
            nmsServerBean.setTime(DateUtils.getRangeOf1min(new Date()));
            nmsServerBean.setTotal(10);
            AbnormalBean abnormalBean1 = new AbnormalBean("serverMachine01", "10.3.48.6");
            AbnormalBean abnormalBean2 = new AbnormalBean("serverMachine02", "10.3.48.8");
            ArrayList<AbnormalBean> abnormalBeans = new ArrayList<>();
            abnormalBeans.add(abnormalBean1);
            abnormalBeans.add(abnormalBean2);
            nmsServerBean.setAbnormalmachines(abnormalBeans);
            list.add(nmsServerBean);
        }
        return list;
    }

    /**
     * 网络流量监控_5min
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NetWaterMonitor> getNetWaterMonitor(String start, String end, int areaNetWaterMonitorExpTimes) {
        List<NetWaterMonitor> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            String before = DateUtils.getBeforeNminOfTime(start, -5);
            list = monitorDao.getNetWaterMonitor(start, start, end, before);
            //上阶段异常数据的获取
            if (areaNetWaterMonitorExpTimes > 0) {
                if (areaNetWaterMonitorExpTimes >= 3) {
                    areaNetWaterMonitorExpTimes = 3;
                }
                for (int i = 1; i <= areaNetWaterMonitorExpTimes; i++) {
                    String emptStart = DateUtils.getBeforeNminOfTime(start, -5 * i);
                    list = monitorDao.getNetWaterMonitor(emptStart, emptStart, DateUtils.getAfter5min(emptStart), DateUtils.getBeforeNminOfTime(emptStart, -5));
                }
            }
        } catch (Exception e) {
            logger.error("网络流量监控\t查询异常\tserviceImpl", e);
            throw new RuntimeException();
        }
        if (ListUtils.isEmptyList(list) && ConfigUtil.tendencyData) {
            logger.warn("网络流量监控\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = monitorDao.getNetWaterMonitorLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = monitorDao.getNetWaterMonitor(start, time, DateUtils.getAfter5min(time), DateUtils.getBeforeNminOfTime(time, -5));
            } catch (Exception e) {
                logger.error("网络流量监控\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (!ListUtils.isEmptyList(list)) {
            Map<String, AreaDictInfo> listArea = listAreaDictInfo();
            for (NetWaterMonitor netWaterMonitor : list) {
                String area = netWaterMonitor.getArea();
                AreaDictInfo areaDictInfo = listArea.get(area);
                if (areaDictInfo != null) {
                    netWaterMonitor.setArea(areaDictInfo.getItemValue());
                }
            }
        }
        logger.info("网络流量监控耗时：" + (System.currentTimeMillis() - timeMillisStart));
        if (ListUtils.isEmptyList(list) && ConfigUtil.testData) {
            logger.info("网络流量监控------->测试数据");
            if (ConfigUtil.languageChose == 3) {
                list.add(new NetWaterMonitor(start, "Астана", "INOCTETS", (long) (Math.random() * 20000)));
                list.add(new NetWaterMonitor(start, "Астана", "OUTOCTETS", (long) (Math.random() * 20000)));
            } else {
                list.add(new NetWaterMonitor(start, "Astana", "OUTOCTETS", (long) (Math.random() * 20000)));
                list.add(new NetWaterMonitor(start, "Astana", "INOCTETS", (long) (Math.random() * 20000)));
            }
        }
        return list;
    }
}
