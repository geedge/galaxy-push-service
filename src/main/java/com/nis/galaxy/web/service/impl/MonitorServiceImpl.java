package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.domain.AbnormalBean;
import com.nis.galaxy.domain.NmsServerBean;
import com.nis.galaxy.domain.NmsServiceBeanTmp;
import com.nis.galaxy.web.dao.MonitorDao;
import com.nis.galaxy.web.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GouGe
 * @data 2018/9/8 14:55
 */
@Service
public class MonitorServiceImpl implements MonitorService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    MonitorDao monitorDao;

    @Override
    public List<NmsServerBean> getMonitorOf1m() {
        List<NmsServerBean> list = new ArrayList<>();
        NmsServiceBeanTmp nmsServerBeanTmp = null;
        try {
            //查询
            nmsServerBeanTmp = monitorDao.getMonitorOf1m01();
        } catch (Exception e) {
            logger.error("系统监控\t查询异常\tserviceImpl", e);
        }
        List<AbnormalBean> abnormalBean = null;
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
            return list;
        }
        return list;
    }



}
