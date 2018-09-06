package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.web.dao.AcceptDao;
import com.nis.galaxy.web.service.AcceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author GouGe
 * @data 2018/8/31 11:05
 */

@Service
public class AcceptServiceImpl implements AcceptService {
    @Autowired
    AcceptDao acceptDao;

    @Override
    public void acceptMonitoringSystem(Map request) {
        acceptDao.toTrafficNmsserverStatistic(request);
        acceptDao.toTrafficAbnormalMachine(request);
    }
}
