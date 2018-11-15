package com.nis.galaxy;

import com.nis.galaxy.config.PushParams;
import com.nis.galaxy.web.dao.NetflowTopNDao;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

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
        String root = stringEncryptor.encrypt("root");
        System.out.println(root);
    }
}
