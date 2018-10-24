package com.nis.galaxy.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
@Component
@Order(2)
public class PushParams {

    @Value("${Content-Type}")
    private String ContentType;

    private HttpHeaders headers = new HttpHeaders();

    public HttpHeaders getHeaders() {
        headers.set("Content-Type",ContentType);
         return headers;
    }
}
