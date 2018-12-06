package com.nis.galaxy.web.dao;

import com.nis.galaxy.domain.dict.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author GouGe
 * @data 2018/8/30 16:31
 */

@Mapper
public interface DictDao {
    List<NtcServiceDict> getNtcServiceDict();

    List<ProtocolDict> getProtocolDict();

    List<UiTagDictInfo> getUiTagDicInfo();

    List<UiAreaDictInfo> getUiAreaDictInfo();

    List<UiCarrierDictInfo> getUiCarrierDictInfo();

    Map<String, String> getNtcServiceCurrent(@Param("languageChose") int languageChose);

    int getNtcServiceDictCount();
}
