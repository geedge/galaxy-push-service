package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.domain.AbnormalBean;
import com.nis.galaxy.domain.NmsServerBean;
import com.nis.galaxy.domain.NmsServiceBeanTmp;
import com.nis.galaxy.web.dao.MonitorDao;
import com.nis.galaxy.web.service.MonitorService;
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

    @Autowired
    MonitorDao monitorDao;

    @Override
    public List<NmsServerBean> getMonitorOf1m() {
        //查询
        NmsServiceBeanTmp nmsServerBeanTmp = monitorDao.getMonitorOf1m01();
        List<AbnormalBean> abnormalBean = null;
        if (nmsServerBeanTmp != null) {
            int id = nmsServerBeanTmp.getId();
            abnormalBean = monitorDao.getMonitorOf1m02(id);
        }
        //结果的封装
        NmsServerBean nmsServerBean = new NmsServerBean();
        nmsServerBean.setTime(nmsServerBeanTmp.getTime());
        nmsServerBean.setArea(nmsServerBeanTmp.getArea());
        nmsServerBean.setTotal(nmsServerBeanTmp.getTotal());
        nmsServerBean.setNormal(nmsServerBeanTmp.getNormal());
        nmsServerBean.setAbnormal(nmsServerBeanTmp.getNormal());
        nmsServerBean.setAbnormalmachines(abnormalBean);
        List<NmsServerBean> list = new ArrayList<>();
        list.add(nmsServerBean);
        return list;
    }
}
