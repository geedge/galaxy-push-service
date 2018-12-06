package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.domain.dict.*;
import com.nis.galaxy.web.dao.DictDao;
import com.nis.galaxy.web.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author GouGe
 * @data 2018/8/30 16:20
 */

@Service
public class DictServiceImpl implements DictService {

    @Autowired
    DictDao dictDao;

    @Override
    public List<ProtocolDict> getProtocolDict() {
        return dictDao.getProtocolDict();
    }


    @Override
    public List<NtcServiceDict> getNtcServiceDict() {
        return dictDao.getNtcServiceDict();
    }


    @Override
    public List<UiTagDictInfo> getUiTagDicInfo() {
        return dictDao.getUiTagDicInfo();
    }

    @Override
    public List<UiAreaDictInfo> getUiAreaDictInfo() {
        return dictDao.getUiAreaDictInfo();
    }

    @Override
    public List<UiCarrierDictInfo> getUiCarrierDictInfo() {
        return dictDao.getUiCarrierDictInfo();
    }

    @Override
    public int getNtcServiceDictCount() {
        return dictDao.getNtcServiceDictCount();
    }
}
