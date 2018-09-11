package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.IpActiveInt;
import com.nis.galaxy.domain.dict.AreaDict;
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
    private int max = 3;

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
    public List<IpActiveInt> getIpActiveStatisticOf5m(String start, String end, int times) {
        List<IpActiveInt> result = new ArrayList<>();
        HashMap<String, UiSysDictInfo> listDictUiSys = listDictUiSys();
        List<IpActiveInt> list = null;
        try {
            list = netflowTopNDao.getIpActiveStatisticOf5mOnly(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("5分钟内活跃IP流量TOP10\t查询异常\tserviceImpl");
        }
        if (list == null || list.size() == 0) {
            if (times >= max) {
                return null;
            }
            getIpActiveStatisticOf5m(DateUtils.getBeforeNminOfTime(start, -5), DateUtils.getBeforeNminOfTime(end, -5), ++times);
        }
        if (list != null && list.size() != 0) {
            for (IpActiveInt ipActiveInt : list) {
                String area = ipActiveInt.getArea();
                if (area != null && listDictUiSys != null) {
                    UiSysDictInfo dict = listDictUiSys.get(area);
                    if (dict != null && dict.getItemType() == 1) {
                        if (languageChose == 2) {
                            ipActiveInt.setArea(dict.getItemValue());
                            result.add(ipActiveInt);
                        } else if (languageChose == 1) {
                            ipActiveInt.setArea(dict.getItemDesc());
                            result.add(ipActiveInt);
                        } else {
                            ipActiveInt.setArea(dict.getItemDesc());
                            result.add(ipActiveInt);
                        }
                    }
                }
            }

        }
        return result;
    }
}
