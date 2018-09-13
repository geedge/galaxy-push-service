package com.nis.galaxy.config;


import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class ConfigBean {

    @Value("${RestTemplate.MaxTotal}")
    private int MaxTotal;
    @Value("${RestTemplate.DefaultMaxPerRoute}")
    private int DefaultMaxPerRoute;
    @Value("${RestTemplate.ConnectTimeout}")
    private int ConnectTimeout;
    @Value("${RestTemplate.SocketTimeout}")
    private int SocketTimeout;
    @Value("${RestTemplate.ConnectionRequestTimeout}")
    private int ConnectionRequestTimeout;



    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
        return restTemplate;
    }

    @Bean
    public CloseableHttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        //连接池最大连接数
        connectionManager.setMaxTotal(MaxTotal);
        //每个主机的并发
        connectionManager.setDefaultMaxPerRoute(DefaultMaxPerRoute);

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SocketTimeout)
                .setConnectTimeout(ConnectTimeout)
                .setConnectionRequestTimeout(ConnectionRequestTimeout)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
    }
}
