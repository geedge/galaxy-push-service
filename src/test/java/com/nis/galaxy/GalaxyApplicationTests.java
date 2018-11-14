package com.nis.galaxy;

import com.nis.galaxy.common.InterfaceBase;
import com.nis.galaxy.config.PushParams;
import com.nis.galaxy.domain.NetFlowWater;
import com.nis.galaxy.web.dao.NetflowTopNDao;
import com.nis.galaxy.web.service.NetflowTopNService;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GalaxyApplicationTests {

    @Autowired
    DataSource dataSource;
    @Autowired
    NetflowTopNDao netflowTopNDao;
    @Autowired
    private StringEncryptor stringEncryptor;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    PushParams pushParams;

    @Test
    public void contextLoads() {
        HashMap<String, String> map = new HashMap<>();
        map.put("11","44");
        map.put("tag1","44");

        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),map,"blocking.global", new ArrayList());
        System.out.println(1);

        //String root = stringEncryptor.encrypt("roo//String password = stringEncryptor.encrypt("111111");
    }
}
