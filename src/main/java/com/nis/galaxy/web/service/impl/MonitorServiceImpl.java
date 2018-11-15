package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.*;
import com.nis.galaxy.domain.dict.UiCarrierDictInfo;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.dao.MonitorDao;
import com.nis.galaxy.web.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Autowired
    MyLoad myLoad;
    @Value("${Open.Recent.Data.Tendency}")
    private boolean OpenRecentDataTendency;

    //运营商表
    public Map<String, UiCarrierDictInfo> listUiCarrierDictInfo() {
        return myLoad.listUiCarrierDictInfo;
    }


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
            nmsServerBeanTmp = new NmsServiceBeanTmp(DateUtils.getDateTime(), "Астана", "" + (int) (Math.random() * 5000), (int) (Math.random() * 5000) + "", "2", 22);
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
        try {
            String before = DateUtils.getBeforeNminOfTime(start, -5);
            list = monitorDao.getNetWaterMonitor(start,start, end, before);
        } catch (Exception e) {
            logger.error("网络流量监控\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty() && OpenRecentDataTendency) {
            logger.warn("网络流量监控\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = monitorDao.getNetWaterMonitorLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = monitorDao.getNetWaterMonitor(start,time, DateUtils.getAfter5min(time), DateUtils.getBeforeNminOfTime(time, -5));
            } catch (Exception e) {
                logger.error("网络流量监控\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (!list.isEmpty()) {
            Map<String, UiCarrierDictInfo> listCarrier = listUiCarrierDictInfo();
            for (NetWaterMonitor netWaterMonitor : list) {
                String area = netWaterMonitor.getArea();
                UiCarrierDictInfo uiCarrierDictInfo = listCarrier.get(area);
                if (uiCarrierDictInfo != null) {
                    netWaterMonitor.setArea(uiCarrierDictInfo.getItemValue());
                }
            }
        }
        //测试数据
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("网络流量监控------->测试数据");
            if (languageChose == 3) {
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
