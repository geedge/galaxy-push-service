package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.IpActiveInfo;
import com.nis.galaxy.domain.NetflowProtocol;
import com.nis.galaxy.domain.TimeTypeCount;
import com.nis.galaxy.domain.dict.UiSysDictInfo;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.dao.NetflowDao;
import com.nis.galaxy.web.service.NetflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class NetflowServiceImpl implements NetflowService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NetflowDao netflowDao;
    @Autowired
    MyLoad myLoad;
    @Value("${Language.Chose}")
    private int languageChose;

    //协议表
    public HashMap<String, String> listProtocolDict() {
        return myLoad.listProtocolDict;
    }

    //局点表
    public HashMap<String, UiSysDictInfo> listDictUiSys() {
        return myLoad.listDictUiSys;
    }

    /**
     * 5分钟内网络流量统计
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<TimeTypeCount> getNetflowStatistic5m(String start, String end) {
        List<TimeTypeCount> list = null;
        HashMap<String, String> listProtocolDict = listProtocolDict();
        try {
            list = netflowDao.getNetflowStatistic5mOnly(start, end);
        } catch (Exception e) {
            logger.error("5分钟内网络流量统计\t查询异常\tserviceImpl", e);
        }
        if (list == null || list.size() == 0) {
            try {
                String lastTime = netflowDao.getNetflowStatistic5mLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = netflowDao.getNetflowStatistic5mOnly(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("5分钟内网络流量统计\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list != null && list.size() != 0) {

            for (TimeTypeCount timeTypeCount : list) {
                String type = timeTypeCount.getType();
                if (type != null && listProtocolDict != null) {
                    String s = listProtocolDict.get(type);
                    if (s != null) {
                        //协议类型字典表
                        timeTypeCount.setType(s);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 1小时内网络协议统计
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NetflowProtocol> getProtocolStatisticOf1h(String start, String end) {
        List<NetflowProtocol> list = null;
        HashMap<String, String> listProtocolDict = listProtocolDict();
        try {
            list = netflowDao.getProtocolStatisticOf1hOnly(start, end);
        } catch (Exception e) {
            logger.error("1小时内网络协议统计\t查询异常\tserviceImpl", e);
        }
        if (list == null || list.size() == 0) {
            try {
                String lastTime = netflowDao.getProtocolStatisticOf1hLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = netflowDao.getProtocolStatisticOf1hOnly(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("1小时内网络协议统计\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list != null && list.size() != 0) {
            for (NetflowProtocol netflowProtocol : list) {
                String type = netflowProtocol.getType();
                if (type != null && listProtocolDict != null) {
                    //与协议类型的字典对应
                    netflowProtocol.setType(listProtocolDict.get(type));
                }
            }
        }
        return list;
    }

    /**
     * 5分钟内活跃IP信息
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<IpActiveInfo> getIpActiveInfoOf5min(String start, String end) {
        List<IpActiveInfo> list = null;
        HashMap<String, UiSysDictInfo> listDictUiSys = listDictUiSys();
        try {
            list = netflowDao.getIpActiveInfoOf5minOnly(start, end);
        } catch (Exception e) {
            logger.error("5分钟内活跃IP信息\t查询异常\tserviceImpl", e);
        }
        if (list == null || list.size() == 0) {
            try {
                String lastTime = netflowDao.getIpActiveInfoOf5minLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = netflowDao.getIpActiveInfoOf5minOnly(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("5分钟内活跃IP信息\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list != null && list.size() != 0) {
            for (IpActiveInfo ipActiveInfo : list) {
                String area = ipActiveInfo.getArea();
                if (area != null && listDictUiSys != null) {
                    UiSysDictInfo dict = listDictUiSys.get(area);
                    //item_type=1表示地域
                    if (dict != null && dict.getItemType() == 1) {
                        if (languageChose == 2) {
                            //英文
                            ipActiveInfo.setArea(dict.getItemValue());
                        } else if (languageChose == 1) {
                            ipActiveInfo.setArea(dict.getItemDesc());
                        } else {
                            ipActiveInfo.setArea(dict.getItemValue());
                        }
                    }
                }
            }
        }
        return list;
    }
}
