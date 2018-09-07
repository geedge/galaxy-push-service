package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.IpActiveInt;
import com.nis.galaxy.domain.dict.AreaDict;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.dao.NetflowTopNDao;
import com.nis.galaxy.web.service.NetflowTopNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class NetflowTopNServiceImpl implements NetflowTopNService {
    @Autowired
    NetflowTopNDao netflowTopNDao;
    @Autowired
    MyLoad myLoad;

    //地域表
    public HashMap<String, AreaDict> listAreaDict() {
        return myLoad.listAreaDict;
    }
    @Override
    public List<IpActiveInt> getIpActiveStatisticOf5m(String start,String end) {
        List<IpActiveInt> result = new ArrayList<>();
        List<IpActiveInt> list = netflowTopNDao.getIpActiveStatisticOf5mOnly(start, end);
        HashMap<String, AreaDict> listAreaDict = listAreaDict();
        for (IpActiveInt ipActiveInt : list) {
            String area = ipActiveInt.getArea();
            if(area!=null && listAreaDict!=null){
                AreaDict dict = listAreaDict.get(area);
                if(dict!=null){
                    ipActiveInt.setArea(dict.getAreaName());
                    result.add(ipActiveInt);
                }
            }
        }
        return result;

     }
}
