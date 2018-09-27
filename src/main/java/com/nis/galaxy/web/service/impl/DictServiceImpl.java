package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.domain.dict.*;
import com.nis.galaxy.web.dao.DictDao;
import com.nis.galaxy.web.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<UiSysDictInfo> getUiSysDictInfo() {
        return dictDao.getUiSysDictInfo();
    }

    @Override
    public List<NtcServiceDict> getNtcServiceDict() {
        return dictDao.getNtcServiceDict();
    }

    @Override
    public List<UiTagDicInfo> getUiTagDicInfo() {
        return dictDao.getUiTagDicInfo();
    }

}
