package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.IpActiveInt;
import com.nis.galaxy.domain.NetFlowWater;
import com.nis.galaxy.domain.dict.UiSysDictInfo;
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
        List<IpActiveInt> list = new ArrayList<>();
        List<IpActiveInt> result = new ArrayList<>();
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
                        result.add(ipActiveInt);
                        if (result.size() >= 10) {
                            return result;
                        }
                    }
                }
            }
            if (!result.isEmpty()) {
                return result;
            }
        }
        if (list.size() >= 10) {
            return list.subList(0, 10);
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
    public List<NetFlowWater> getNetFlowWaterTop10(String start, String end, int a) {
        List<NetFlowWater> list = new ArrayList<>();
        List<NetFlowWater> resule = new ArrayList<>();
        try {
            list = netflowTopNDao.getNetFlowWaterTop10(start, end);
        } catch (Exception e) {
            logger.error("互联网流量TOP10\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            try {
                String lastTime = netflowTopNDao.getNetFlowWaterTop10LastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                String lastEnd = DateUtils.getAfter5min(time);
                String lastStart = null;
                if (a == 1) {
                    lastStart = DateUtils.getBeforeNminOfTime(lastEnd, -60);
                } else if (a == 7) {
                    lastStart = DateUtils.getBeforeNminOfTime(lastEnd, -60 * 24 * 7);
                } else {
                    lastStart = DateUtils.getBeforeNminOfTime(lastEnd, -60 * 24);
                }
                list = netflowTopNDao.getNetFlowWaterTop10(lastStart, lastEnd);
            } catch (Exception e) {
                logger.error("互联网流量TOP10\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        double avg = 0;
        int size = list.size();
        for (int i = size; i > 1; i--) {
            double count = list.get(i - 1).getCount() - list.get(i - 2).getCount();
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
            resule.add(new NetFlowWater(start, "astana", avg));
            return resule;
        }
        //假数据
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
            list.add(new NetFlowWater(start, "astana", (int) (Math.random() * 1000)));
        }
        return list;
    }
}
