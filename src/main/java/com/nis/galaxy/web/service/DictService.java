package com.nis.galaxy.web.service;

import com.nis.galaxy.domain.TimeTypeCount;
import com.nis.galaxy.domain.dict.*;

import java.util.List;

/**
 * @author GouGe
 * @data 2018/8/30 16:20
 */
public interface DictService {
    List<ProtocolDict> getProtocolDict();

    List<UiSysDictInfo> getUiSysDictInfo();

    List<NtcServiceDict> getNtcServiceDict();

    List<UiTagDicInfo> getUiTagDicInfo();
}
