package com.nis.galaxy;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GalaxyApplicationTests {

    @Autowired
    DataSource dataSource;
    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void contextLoads() {
        System.out.println("333");
        //String root = stringEncryptor.encrypt("root");
        //String password = stringEncryptor.encrypt("111111");
    }


}
