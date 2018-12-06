package com.nis.galaxy.web.service;

import com.nis.galaxy.domain.dict.*;

import java.util.List;
import java.util.Map;

/**
 * @author GouGe
 * @data 2018/8/30 16:20
 */
public interface DictService {
    List<ProtocolDict> getProtocolDict();

    List<NtcServiceDict> getNtcServiceDict();

    List<UiTagDictInfo> getUiTagDicInfo();

    List<UiAreaDictInfo> getUiAreaDictInfo();

    List<UiCarrierDictInfo> getUiCarrierDictInfo();

    int getNtcServiceDictCount();
}
