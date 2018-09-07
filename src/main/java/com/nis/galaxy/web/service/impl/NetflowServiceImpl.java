package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.IpActiveInfo;
import com.nis.galaxy.domain.NetflowProtocol;
import com.nis.galaxy.domain.PointMap;
import com.nis.galaxy.domain.TimeTypeCount;
import com.nis.galaxy.domain.dict.AreaDict;
import com.nis.galaxy.domain.dict.UiSysDictInfo;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.dao.NetflowDao;
import com.nis.galaxy.web.service.NetflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class NetflowServiceImpl implements NetflowService {
    @Autowired
    NetflowDao netflowDao;
    @Autowired
    MyLoad myLoad;

    //协议表
    public HashMap<String, String> listProtocolDict() {
        return myLoad.listProtocolDict;
    }

    //地域表
    public HashMap<String, AreaDict> listAreaDict() {
        return myLoad.listAreaDict;
    }

    /**
     * 5分钟内网络流量统计
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<TimeTypeCount> getNetflowStatistic5m(String start, String end) {
        List<TimeTypeCount> result = new ArrayList<>();
        List<TimeTypeCount> list = netflowDao.getNetflowStatistic5mOnly(start, end);
        HashMap<String, String> listProtocolDict = listProtocolDict();
        for (TimeTypeCount timeTypeCount : list) {
            String type = timeTypeCount.getType();
            if (type != null && listProtocolDict != null) {
                String s = listProtocolDict.get(type);
                if (s != null) {
                    //协议类型字典表
                    timeTypeCount.setType(s);
                    result.add(timeTypeCount);
                }
            }
        }
        return result;
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
        List<NetflowProtocol> list = netflowDao.getProtocolStatisticOf1hOnly(start, end);
        HashMap<String, String> listProtocolDict = listProtocolDict();
        List<NetflowProtocol> result = new ArrayList<>();
        for (NetflowProtocol netflowProtocol : list) {
            String type = netflowProtocol.getType();
            if (type != null && listProtocolDict != null) {
                //与协议类型的字典对应
                netflowProtocol.setType(listProtocolDict.get(type));
                result.add(netflowProtocol);
            }
        }
        return result;
    }

    /**
     * 5分钟内活跃ip信息
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<IpActiveInfo> getIpActiveInfoOf5min(String start, String end) {
        List<IpActiveInfo> list = netflowDao.getIpActiveInfoOf5minOnly(start, end);
        List<IpActiveInfo> result = new ArrayList<>();
        HashMap<String, AreaDict> listAreaDict = myLoad.listAreaDict;
        for (IpActiveInfo ipActiveInfo : list) {
            String area = ipActiveInfo.getArea();
            if (area != null && listAreaDict != null) {
                AreaDict dict = listAreaDict.get(area);
                if (dict != null) {
                    //ip对应地域表字段
                    ipActiveInfo.setArea(dict.getAreaName());
                    result.add(ipActiveInfo);
                }
            }
        }
        return result;
    }
}
