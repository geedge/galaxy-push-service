package com.nis.galaxy.web.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UpDao {
    void upImage(Map map);
}
