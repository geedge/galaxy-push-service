package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.IpActiveInt;
import com.nis.galaxy.domain.dict.UiSysDictInfo;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.dao.NetflowTopNDao;
import com.nis.galaxy.web.service.NetflowTopNService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class NetflowTopNServiceImpl implements NetflowTopNService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NetflowTopNDao netflowTopNDao;
    @Autowired
    MyLoad myLoad;
    @Value("${Language.Chose}")
    private int languageChose;

    //局点表
    public HashMap<String, UiSysDictInfo> listDictUiSys() {
        return myLoad.listDictUiSys;
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
        List<IpActiveInt> list = null;
        HashMap<String, UiSysDictInfo> listDictUiSys = listDictUiSys();
        try {
            list = netflowTopNDao.getIpActiveStatisticOf5mOnly(start, end);
        } catch (Exception e) {
            logger.error("5分钟内活跃IP流量TOP10\t查询异常\tserviceImpl", e);
        }
        if (list == null || list.size() == 0) {
            try {
                String lastTime = netflowTopNDao.getIpActiveStatisticOf5mLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = netflowTopNDao.getIpActiveStatisticOf5mOnly(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("5分钟内活跃IP流量TOP10\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list != null && list.size() != 0) {
            for (IpActiveInt ipActiveInt : list) {
                String area = ipActiveInt.getArea();
                if (area != null && listDictUiSys != null) {
                    UiSysDictInfo dict = listDictUiSys.get(area);
                    if (dict != null && dict.getItemType() == 1) {
                        if (languageChose == 2) {
                            ipActiveInt.setArea(dict.getItemValue());
                        } else if (languageChose == 1) {
                            ipActiveInt.setArea(dict.getItemDesc());
                        } else {
                            ipActiveInt.setArea(dict.getItemDesc());
                        }
                    }
                }
            }
        }
        return list;
    }
}
