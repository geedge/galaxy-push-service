package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.Entrance;
import com.nis.galaxy.domain.IpActiveInt;
import com.nis.galaxy.domain.NetFlowWater;
import com.nis.galaxy.domain.NetFlowWaterPush;
import com.nis.galaxy.domain.dict.AreaDictInfo;
import com.nis.galaxy.util.ConfigUtil;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.util.ListUtils;
import com.nis.galaxy.web.dao.NetflowTopNDao;
import com.nis.galaxy.web.service.NetflowTopNService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NetflowTopNServiceImpl implements NetflowTopNService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NetflowTopNDao netflowTopNDao;


    //地域表
    public Map<String, AreaDictInfo> listAreaDictInfo() {
        Map<String, AreaDictInfo> listAreaDictInfo = MyLoad.listAreaDictInfo;
        if (listAreaDictInfo == null || listAreaDictInfo.size() == 0) {
            logger.warn("地域表无数据");
            listAreaDictInfo = new HashMap<>();
        }
        return listAreaDictInfo;
    }

    /**
     * 5分钟内活跃IP流量
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<IpActiveInt> getNetflow5minIpActiveWaterflow(String start, String end) {
        long timeMillisStart = System.currentTimeMillis();
        List<IpActiveInt> list = new ArrayList<>();
        Map<String, AreaDictInfo> listArea = listAreaDictInfo();
        try {
            list = netflowTopNDao.getNetflow5minIpActiveWaterflow(start, DateUtils.getDateOfString(start), DateUtils.getDateOfString(end));
        } catch (Exception e) {
            logger.error("5分钟内活跃IP流量\t查询异常\tserviceImpl", e);
        }
        if (ListUtils.isEmptyList(list) && ConfigUtil.recentData) {
            logger.warn("5分钟内活跃IP流量\tTRAFFIC_IP_ACTIVE_STATISTIC\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = netflowTopNDao.getNetflow5minIpActiveWaterflowLastTime(DateUtils.getDateOfString(start));
                if (lastTime != null) {
                    String startRecent = DateUtils.formatDateTime(lastTime);
                    startRecent = DateUtils.getRangeOf5min(startRecent);
                    String endRecent = DateUtils.getAfter5min(startRecent);
                    list = netflowTopNDao.getNetflow5minIpActiveWaterflow(start, DateUtils.getDateOfString(startRecent), DateUtils.getDateOfString(endRecent));
                } else {
                    logger.warn("5分钟内活跃IP流量\tTRAFFIC_IP_ACTIVE_STATISTIC\t无数据");
                }
            } catch (Exception e) {
                logger.error("5分钟内活跃IP流量\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        //迭代映射地域表，过滤无地域映射数据
        if (!ListUtils.isEmptyList(list)) {
            Iterator<IpActiveInt> ipActiveInt = list.iterator();
            while (ipActiveInt.hasNext()) {
                IpActiveInt next = ipActiveInt.next();
                String area = next.getArea();
                AreaDictInfo areaDictInfo = listArea.get(area);
                if (areaDictInfo == null) {
                    ipActiveInt.remove();
                    continue;
                } else {
                    next.setArea(areaDictInfo.getItemValue());
                }
            }
        }
        logger.info("5分钟内活跃IP流量耗时：" + (System.currentTimeMillis() - timeMillisStart));
        if (ListUtils.isEmptyList(list) && ConfigUtil.testData) {
            int i = (int) (Math.random() * 200);
            logger.info("5分钟内活跃IP流量------->测试数据");
            list.add(new IpActiveInt(start, "up", "Астана", "119.108.229.153", i + 20));
            list.add(new IpActiveInt(start, "up", "Астана", "121.236.4.78", i + 16));
            list.add(new IpActiveInt(start, "up", "Алматы", "192.158.229.15", i + 15));
            list.add(new IpActiveInt(start, "up", "Астана", "192.168.99.153", i + 14));
            list.add(new IpActiveInt(start, "up", "Алматы", "119.188.29.125", i + 12));
            list.add(new IpActiveInt(start, "down", "Астана", "119.108.229.153", i + 6));
            list.add(new IpActiveInt(start, "down", "Алматы", "121.236.4.78", i + 5));
            list.add(new IpActiveInt(start, "down", "Алматы", "192.158.229.15", i + 3));
            list.add(new IpActiveInt(start, "down", "Алматы", "192.168.99.153", i + 2));
            list.add(new IpActiveInt(start, "down", "Астана", "119.188.29.125", i));
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
    public List<NetFlowWaterPush> getNetFlowWaterTop10(String start, String end, int range) {
        Map<String, AreaDictInfo> listArea = listAreaDictInfo();
//        List<NetFlowWater> list = new ArrayList<>();
        List<NetFlowWaterPush> resule = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        /*List<Entrance> entrance = new ArrayList<>();
        try {
            entrance = netflowTopNDao.getIpEntranceRe();
        } catch (Exception ex) {
            logger.error("互联网流量TOP10\t获取局点异常\tserviceImpl", ex);
        }*/
        try {
            resule = netflowTopNDao.getNetFlowWaterTop10(start, start, end);
        } catch (Exception e) {
            logger.error("互联网流量TOP10\t查询异常\tserviceImpl", e);
        }
        if (ListUtils.isEmptyList(resule) && ConfigUtil.recentData) {
            logger.warn("互联网流量TOP10\tTRAFFIC_NETFLOW_PORT_INFO\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = netflowTopNDao.getNetFlowWaterTop10LastTime(end);
                if (lastTime != null) {
                    String time = DateUtils.formatDateTime(lastTime);
                    time = DateUtils.getRangeOf5min(time);
                    String lastEnd = DateUtils.getAfter5min(time);
                    String lastStart = null;
                    if (range == 1) {
                        lastStart = DateUtils.getBeforeNminOfTime(lastEnd, -60);
                    } else if (range == 7) {
                        lastStart = DateUtils.getBeforeNminOfTime(lastEnd, -60 * 24 * 7);
                    } else {
                        lastStart = DateUtils.getBeforeNminOfTime(lastEnd, -60 * 24);
                    }
                    resule = netflowTopNDao.getNetFlowWaterTop10(start, lastStart, lastEnd);
                } else {
                    logger.warn("互联网流量TOP10\tTRAFFIC_NETFLOW_PORT_INFO\t无数据");
                }
            } catch (Exception e) {
                logger.error("互联网流量TOP10\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        /*if (!ListUtils.isEmptyList(list)) {
            int size = list.size();
            if (size == 1) {
                long avg = 0;
                String area = list.get(0).getArea();
                long count = list.get(0).getCount();
                if (count >= 0) {
                    avg = count;
                }
                resule.add(new NetFlowWaterPush(start, area, avg));
            } else if (size > 1) {
                //根据局点遍历
                if (!ListUtils.isEmptyList(entrance)) {
                    for (Entrance entranceOne : entrance) {
                        List<NetFlowWater> temp = new ArrayList<>();
                        String entranceNanme = entranceOne.getEntrance();
                        //遍历list结果集
                        for (int i = 0; i < size; i++) {
                            String area = list.get(i).getArea();
                            if (entranceNanme.equals(area)) {
                                temp.add(list.get(i));
                            }
                        }
                        long endCount = 0;
                        long startCount = 0;
                        long count = 0;
                        long avg = 0;
                        Date timeEnd;
                        Date timeStart;
                        int tempSize = temp.size();
                        for (int i = tempSize - 1; i > 0; i--) {
                            endCount = temp.get(i).getCount();
                            startCount = temp.get(i - 1).getCount();
                            timeEnd = temp.get(i).getTime();
                            timeStart = temp.get(i - 1).getTime();
                            long timeSecond = (timeEnd.getTime() - timeStart.getTime()) / 1000;
                            count = endCount - startCount;
                            if (count >= 0) {
                                avg = avg + count / timeSecond;
                            } else {
                                if (endCount > 0) {
                                    avg = avg + endCount / timeSecond;
                                }
                            }
                        }
                        if (tempSize > 1) {
                            avg = avg / (size - 1);
                        } else if (tempSize == 1) {
                            avg = temp.get(0).getCount() / 300;
                        } else {
                            continue;
                        }
                        resule.add(new NetFlowWaterPush(start, entranceNanme, avg));
                    }
                }
            } else {
                long avg = 0;
            }
            //映射地域
            Iterator<NetFlowWaterPush> netFlowWaterPush = resule.iterator();
            while (netFlowWaterPush.hasNext()) {
                NetFlowWaterPush next = netFlowWaterPush.next();
                String area = next.getArea();
                AreaDictInfo areaDictInfo = listArea.get(area);
                if (areaDictInfo == null) {
                    netFlowWaterPush.remove();
                    continue;
                } else {
                    next.setArea(areaDictInfo.getItemValue());
                }
            }
        }*/
        //映射地域
        if (!ListUtils.isEmptyList(resule)) {
            Iterator<NetFlowWaterPush> netFlowWaterPush = resule.iterator();
            while (netFlowWaterPush.hasNext()) {
                NetFlowWaterPush next = netFlowWaterPush.next();
                String area = next.getArea();
                AreaDictInfo areaDictInfo = listArea.get(area);
                if (areaDictInfo == null) {
                    netFlowWaterPush.remove();
                    continue;
                } else {
                    next.setArea(areaDictInfo.getItemValue());
                }
            }
        }
        logger.info("互联网流量TOP10耗时：" + (System.currentTimeMillis() - timeMillisStart));
        if (ListUtils.isEmptyList(resule) && ConfigUtil.testData) {
            logger.info("互联网流量TOP10------->测试数据");
            if (ConfigUtil.languageChose == 3) {
                resule.add(new NetFlowWaterPush(start, "Астана", (long) (Math.random() * 10000)));
            } else {
                resule.add(new NetFlowWaterPush(start, "Astana", (long) (Math.random() * 10000)));
            }
        }
        return resule;
    }

    /**
     * 1小时互联网流量TOP10_test
     *
     * @param end
     * @return
     */
    @Override
    public List getNetflow1HourWaterflowTop10(String end) {
        long startTimeMillis = System.currentTimeMillis();
        List<NetFlowWaterPush> result = new ArrayList<>();
        List<NetFlowWater> resultTemp = new ArrayList<>();
        for (int j = 0; j < 2; j++) {
            List<Map<String, Date>> listMapParam = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                String start = DateUtils.getBeforeNminOfTime(end, -5);
                HashMap<String, Date> map = new HashMap<>();
                map.put("start", DateUtils.getDateOfString(start));
                map.put("end", DateUtils.getDateOfString(end));
                listMapParam.add(map);
                end = start;
            }
            List<NetFlowWater> temp = netflowTopNDao.getNetflow1HourWaterflowTop10(listMapParam);
            resultTemp.addAll(temp);
        }
        if (ListUtils.isEmptyList(resultTemp)) {
            Date lastTime = netflowTopNDao.getNetFlowWaterTop10LastTime(end);
            if (lastTime != null) {
                end = DateUtils.formatDateTime(lastTime);
                end = DateUtils.getRangeOf5min(end);
                end = DateUtils.getAfter5min(end);
                for (int j = 0; j < 2; j++) {
                    List<Map<String, Date>> listMapParam = new ArrayList<>();
                    for (int i = 0; i < 6; i++) {
                        String start = DateUtils.getBeforeNminOfTime(end, -5);
                        HashMap<String, Date> map = new HashMap<>();
                        map.put("start", DateUtils.getDateOfString(start));
                        map.put("end", DateUtils.getDateOfString(end));
                        listMapParam.add(map);
                        end = start;
                    }
                    List<NetFlowWater> temp = netflowTopNDao.getNetflow1HourWaterflowTop10(listMapParam);
                    resultTemp.addAll(temp);
                }
            }
        }
        Map<String, AreaDictInfo> listAreaDictInfo = listAreaDictInfo();
        if (!ListUtils.isEmptyList(resultTemp)) {
            int size = resultTemp.size();
            if (size == 1) {
                long avg = 0;
                String area = resultTemp.get(0).getArea();
                long count = resultTemp.get(0).getCount();
                if (count >= 0) {
                    avg = count;
                }
                result.add(new NetFlowWaterPush(end, area, avg));
            } else if (size > 1) {
                //依据地域临时封装
                for (String areaId : listAreaDictInfo.keySet()) {
                    List<NetFlowWater> tempAreaResult = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        String area = resultTemp.get(i).getArea();
                        if (areaId.equals(area)) {
                            tempAreaResult.add(resultTemp.get(i));
                        }
                    }
                    if (ListUtils.isEmptyList(tempAreaResult)) {
                        continue;
                    }
                    long endCount = 0;
                    long startCount = 0;
                    long count = 0;
                    long avg = 0;
                    Date timeEnd;
                    Date timeStart;
                    int tempResultSize = tempAreaResult.size();
                    for (int i = 0; i < tempResultSize - 1; i++) {
                        endCount = tempAreaResult.get(i).getCount();
                        startCount = tempAreaResult.get(i + 1).getCount();
                        timeEnd = tempAreaResult.get(i).getTime();
                        timeStart = tempAreaResult.get(i + 1).getTime();
                        long timeSecond = (timeEnd.getTime() - timeStart.getTime()) / 1000;
                        count = endCount - startCount;
                        if (count >= 0) {
                            avg = avg + count / timeSecond;
                        } else {
                            if (endCount > 0) {
                                avg = avg + endCount / timeSecond;
                            }
                        }
                    }
                    if (tempResultSize > 1) {
                        avg = avg / (size - 1);
                    } else if (tempResultSize == 1) {
                        avg = tempAreaResult.get(0).getCount() / 300;
                    } else {
                        continue;
                    }
                    result.add(new NetFlowWaterPush(DateUtils.getBeforeNminOfTime(end, -60), areaId, avg));
                }
            } else {
                long avg = 0;
            }
            //映射地域
            for (NetFlowWaterPush netFlowWaterPush : result) {
                String area = netFlowWaterPush.getArea();
                AreaDictInfo areaDictInfo = listAreaDictInfo.get(area);
                if (areaDictInfo == null) {
                    continue;
                }
                netFlowWaterPush.setArea(areaDictInfo.getItemValue());
            }
        }
        logger.info("1小时互联网流量TOP10耗时(新方法)：" + (System.currentTimeMillis() - startTimeMillis));
        return result;
    }
}