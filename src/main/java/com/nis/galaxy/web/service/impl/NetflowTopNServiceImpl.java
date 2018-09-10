package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.IpActiveInt;
import com.nis.galaxy.domain.dict.AreaDict;
import com.nis.galaxy.domain.dict.UiSysDictInfo;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.dao.NetflowTopNDao;
import com.nis.galaxy.web.service.NetflowTopNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${Language.Chose}")
    private int languageChose;



    //局点表
    public HashMap<String, UiSysDictInfo> listDictUiSys() {
        return myLoad.listDictUiSys;
    }

    @Override
    public List<IpActiveInt> getIpActiveStatisticOf5m(String start,String end) {
        List<IpActiveInt> result = new ArrayList<>();
        List<IpActiveInt> list = netflowTopNDao.getIpActiveStatisticOf5mOnly(start, end);
        HashMap<String, UiSysDictInfo> listDictUiSys = listDictUiSys();
        for (IpActiveInt ipActiveInt : list) {
            String area = ipActiveInt.getArea();
            if(area!=null && listDictUiSys!=null){
                UiSysDictInfo dict = listDictUiSys.get(area);
                if(dict!=null && dict.getItemType()==1){
                    if(languageChose==2){
                        ipActiveInt.setArea(dict.getItemValue());
                        result.add(ipActiveInt);
                    }else if(languageChose==1){
                        ipActiveInt.setArea(dict.getItemDesc());
                        result.add(ipActiveInt);
                    }else{
                        ipActiveInt.setArea(dict.getItemDesc());
                        result.add(ipActiveInt);
                    }
                }
            }
        }
        if(result.size()==0){
            return getIpActiveStatisticOf5m(DateUtils.getBeforeNminOfTime(start,-5),DateUtils.getBeforeNminOfTime(end,-5));
        }
        return result;

     }
}
