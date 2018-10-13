package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.web.dao.UpDao;
import com.nis.galaxy.web.service.UpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class UpServiceImpl implements UpService {
    @Autowired
    UpDao upDao;

    @Override
    public void upImage(Map params) {
        upDao.upImage(params);
    }
}
