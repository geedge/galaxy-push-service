package com.nis.galaxy.web.service;

import com.nis.galaxy.domain.IpActiveInt;

import java.util.List;

public interface NetflowTopNService {
    List<IpActiveInt> getIpActiveStatisticOf5m(String start, String end);
}
