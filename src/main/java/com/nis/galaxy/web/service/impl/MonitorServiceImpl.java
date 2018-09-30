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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
            int id = nmsServerBeanTmp.getId();
            abnormalBean = monitorDao.getMonitorOf1m02(id);
            //结果的封装
            NmsServerBean nmsServerBean = new NmsServerBean();
            nmsServerBean.setTime(nmsServerBeanTmp.getTime());
            nmsServerBean.setArea(nmsServerBeanTmp.getArea());
            nmsServerBean.setTotal(Integer.parseInt(nmsServerBeanTmp.getTotal()));
            nmsServerBean.setNormal(Integer.parseInt(nmsServerBeanTmp.getNormal()));
            nmsServerBean.setAbnormal(Integer.parseInt(nmsServerBeanTmp.getNormal()));
            nmsServerBean.setAbnormalmachines(abnormalBean);
            list.add(nmsServerBean);
        } else {
            if (list == null || list.isEmpty()) {
                nmsServerBeanTmp = new NmsServiceBeanTmp("2018-09-26 00:00:00","Region Center6","4200","4198","2",22);
                abnormalBean.add(new AbnormalBean("competer1", "120.213.20"));
                abnormalBean.add(new AbnormalBean("competer2", "120.213.24"));

                //结果的封装
                NmsServerBean nmsServerBean = new NmsServerBean();
                nmsServerBean.setTime(nmsServerBeanTmp.getTime());
                nmsServerBean.setArea(nmsServerBeanTmp.getArea());
                nmsServerBean.setTotal(Integer.parseInt(nmsServerBeanTmp.getTotal()));
                nmsServerBean.setNormal(Integer.parseInt(nmsServerBeanTmp.getNormal()));
                nmsServerBean.setAbnormal(Integer.parseInt(nmsServerBeanTmp.getNormal()));
                nmsServerBean.setAbnormalmachines(abnormalBean);
                list.add(nmsServerBean);
            }
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
            list = monitorDao.getNetWaterMonitor(start, end);
            list.remove(null);
            if (list.get(0).getCount() == 0) {
                list = null;
            }
        } catch (Exception e) {
            logger.error("网络流量监控\t查询异常\tserviceImpl", e);
        }
        if (list == null || list.isEmpty()) {
            try {
                String lastTime = monitorDao.getNetWaterMonitorLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = monitorDao.getNetWaterMonitor(time, DateUtils.getAfter5min(time));
                list.remove(null);
                if (list.get(0).getCount() == 0) {
                    list = null;
                }
            } catch (Exception e) {
                logger.error("网络流量监控\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        //假数据
        if (list == null || list.isEmpty()) {
            list =new ArrayList<>();
            list.add(new NetWaterMonitor(start, "astana", "OCTETS", (int) (Math.random() * 150)));
        }
        return list;
    }
}
