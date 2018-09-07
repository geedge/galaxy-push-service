package com.nis.galaxy.web.dao;

import com.nis.galaxy.domain.dict.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author GouGe
 * @data 2018/8/30 16:31
 */

@Mapper
public interface DictDao {
    List<UiSysDictInfo> getUiSysDictInfo();
    List<NtcServiceDict> getNtcServiceDict();
    List<ProtocolDict> getProtocolDict();
    List<UiTagDicInfo> getUiTagDicInfo();

    List<AreaDict> getAreaDict();
}
