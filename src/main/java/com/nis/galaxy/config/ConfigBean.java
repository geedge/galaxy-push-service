package com.nis.galaxy.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class ConfigBean {

    @Value("${RestTemplate.ConnectTimeout}")
    private int ConnectTimeout;
    @Value("${RestTemplate.SocketTimeout}")
    private int SocketTimeout;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(ConnectTimeout);
        requestFactory.setReadTimeout(SocketTimeout);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }
}
