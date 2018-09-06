package com.nis.galaxy;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GalaxyApplicationTests {

    @Autowired
    private StringEncryptor stringEncryptor;
    @Test
    public void contextLoads() {
        String root = stringEncryptor.encrypt("root");
        String password = stringEncryptor.encrypt("111111");
        System.out.println(root);
        System.out.println(password);


    }

}
