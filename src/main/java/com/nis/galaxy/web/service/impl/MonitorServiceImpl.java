package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.domain.*;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.dao.MonitorDao;
import com.nis.galaxy.web.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GouGe
 * @data 2018/9/8 14:55
 */
@Service
public class MonitorServiceImpl implements MonitorService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${Language.Chose}")
    private int languageChose;
    @Autowired
    MonitorDao monitorDao;
    @Value("${Open.False.Data}")
    private boolean OPENFALSEDATA;


    @Override
    public List<NmsServerBean> getMonitorOf1m() {
        List<NmsServerBean> list = new ArrayList<>();
        NmsServiceBeanTmp nmsServerBeanTmp = new NmsServiceBeanTmp();
        try {
            //查询
            nmsServerBeanTmp = monitorDao.getMonitorOf1m01();
        } catch (Exception e) {
            logger.error("系统监控\t查询异常\tserviceImpl", e);
        }
        List<AbnormalBean> abnormalBean = new ArrayList<>();
        if (nmsServerBeanTmp != null) {
            long id = nmsServerBeanTmp.getId();
            abnormalBean = monitorDao.getMonitorOf1m02(id);
            //结果的封装
            NmsServerBean nmsServerBean = new NmsServerBean();
            nmsServerBean.setTime(nmsServerBeanTmp.getTime());
            nmsServerBean.setArea(nmsServerBeanTmp.getArea());
            nmsServerBean.setTotal(Integer.parseInt(nmsServerBeanTmp.getTotal()));
            nmsServerBean.setNormal(Integer.parseInt(nmsServerBeanTmp.getNormal()));
            nmsServerBean.setAbnormal(Integer.parseInt(nmsServerBeanTmp.getAbnormal()));
            nmsServerBean.setAbnormalmachines(abnormalBean);
            list.add(nmsServerBean);
            list.remove(null);
        }
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("系统监控------->测试数据");
            nmsServerBeanTmp = new NmsServiceBeanTmp(DateUtils.getDateTime(), "Region Center6", "" + (int) (Math.random() * 5000), (int) (Math.random() * 5000) + "", "2", 22);
            abnormalBean.add(new AbnormalBean("competer1", "120.213." + (int) (Math.random() * 50)));
            abnormalBean.add(new AbnormalBean("competer2", "120.213." + (int) (Math.random() * 20)));
            //结果的封装
            NmsServerBean nmsServerBean = new NmsServerBean();
            nmsServerBean.setTime(nmsServerBeanTmp.getTime());
            nmsServerBean.setArea(nmsServerBeanTmp.getArea());
            nmsServerBean.setTotal(Integer.parseInt(nmsServerBeanTmp.getTotal()));
            nmsServerBean.setNormal(Integer.parseInt(nmsServerBeanTmp.getNormal()));
            nmsServerBean.setAbnormal(Integer.parseInt(nmsServerBeanTmp.getAbnormal()));
            nmsServerBean.setAbnormalmachines(abnormalBean);
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
    public List<NetWaterMonitor> getNetWaterMonitor(String start, String end) {
        List<NetWaterMonitor> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        try {
            String before = DateUtils.getBeforeNminOfTime(start, -5);
            map = monitorDao.getNetWaterMonitor(start, end, before);
        } catch (Exception e) {
            logger.error("网络流量监控\t查询异常\tserviceImpl", e);
        }
        if (map.size() == 2) {
            logger.warn("网络流量监控\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                String lastTime = monitorDao.getNetWaterMonitorLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                map = monitorDao.getNetWaterMonitor(time, DateUtils.getAfter5min(time), DateUtils.getBeforeNminOfTime(time, -5));
            } catch (Exception e) {
                logger.error("网络流量监控\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (map.size() != 2) {
            String time = map.get("time").toString();
            if (languageChose == 3) {
                list.add(new NetWaterMonitor(time, "Ақмолаоблысы", "INOCTETS", (long)map.get("INOCTETS")));
                list.add(new NetWaterMonitor(time, "Ақмолаоблысы", "OUTOCTETS", (long)map.get("OUTOCTETS")));
            } else {
                list.add(new NetWaterMonitor(time, "Akmola", "INOCTETS",Long.valueOf(map.get("INOCTETS").toString())));
                list.add(new NetWaterMonitor(time, "Akmola", "OUTOCTETS", Long.valueOf(map.get("OUTOCTETS").toString())));
            }
        }
        //测试数据
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("网络流量监控------->测试数据");
            if (languageChose == 3) {
                list.add(new NetWaterMonitor(start, "Ақмолаоблысы", "INOCTETS", (int) (Math.random() * 20000)));
                list.add(new NetWaterMonitor(start, "Ақмолаоблысы", "OUTOCTETS", (int) (Math.random() * 20000)));
            } else {
                list.add(new NetWaterMonitor(start, "Akmola", "OUTOCTETS", (int) (Math.random() * 20000)));
                list.add(new NetWaterMonitor(start, "Akmola", "INOCTETS", (int) (Math.random() * 20000)));
            }
        }
        return list;
    }
}
