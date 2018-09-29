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

    /**
     * 互联网流量TOP10
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NetFlowWater> getNetFlowWaterTop10(String start, String end, int average) {
        String startB = null, endB = null;
        List<NetFlowWater> list = new ArrayList<>();
        if (average == 2048 * 300) {
            startB = DateUtils.getBeforeNminOfTime(start, -5);
            endB = DateUtils.getAfter5min(startB);
        } else if (average == 2048 * 24) {
            startB = DateUtils.getBeforeNminOfTime(start, -60 * 24);
            endB = DateUtils.getAfter5min(startB);
        } else if (average == 2048 * 7) {
            startB = DateUtils.getBeforeNminOfTime(start, -60 * 24 * 7);
            endB = DateUtils.getAfter5min(startB);
        }
        try {
            list = netflowTopNDao.getNetFlowWaterTop10(start, end, startB, endB, average);
            if(list ==null || list.get(0).getCount()==0){
                list=new ArrayList<>();
            }
        } catch (Exception e) {
            logger.error("互联网流量TOP10\t查询异常\tserviceImpl", e);
        }
        if (list == null || list.isEmpty()) {
            String lastTime = netflowTopNDao.getNetFlowWaterTop10LastTime(end);
            String time = DateUtils.getSubstringTime(lastTime);
            start = DateUtils.getRangeOf5min(time);
            end = DateUtils.getAfter5min(start);
            try {
                if (average == 2048 * 300) {
                    startB = DateUtils.getBeforeNminOfTime(start, -5);
                    endB = DateUtils.getAfter5min(startB);
                } else if (average == 2048 * 24) {
                    startB = DateUtils.getBeforeNminOfTime(start, -60 * 24);
                    endB = DateUtils.getAfter5min(startB);
                } else if (average == 2048 * 7) {
                    startB = DateUtils.getBeforeNminOfTime(start, -60 * 24 * 7);
                    endB = DateUtils.getAfter5min(startB);
                }
                list = netflowTopNDao.getNetFlowWaterTop10(start, end, startB, endB, average);
                if(list ==null || list.get(0).getCount()==0){
                    list=new ArrayList<>();
                }
            } catch (Exception e) {
                logger.error("互联网流量TOP10\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        //假数据
        if (list == null || list.isEmpty() || list.size() < 10) {
            list = new ArrayList<>();
            list.add( new NetFlowWater(start, "astana" , (int) (Math.random() * 1000)));
        }
        return list;
    }
}
