package com.nis.galaxy.web.service;

import com.nis.galaxy.domain.*;
import java.util.List;

public interface BlockingTopNService {
    List<NtcRejectUrlStatistic> getBlockTop10URLOf1h(String start, String end);

    List<NtcRejectWebsiteStatistic> getBlockTop10WebsiteOf1h(String start, String end);

    List<AreaBlockProtocol> getAreaBlockTop10ProtocolOf24(String start, String end);

    List<NtcUrlIpCount> getBlockTop10URLOf5m(String start, String end);

    List<NtcUrlIpCount> getBlockTop10WebsiteOf5m(String start, String end);

    List<NtcTagReport> getBlockTop100Tag1h(String start, String end);
}
