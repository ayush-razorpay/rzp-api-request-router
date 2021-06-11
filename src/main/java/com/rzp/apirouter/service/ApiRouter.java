package com.rzp.apirouter.service;

import com.rzp.apirouter.dto.ApiRouterRequestDto;
import com.rzp.apirouter.dto.WebhookDto;
import com.rzp.apirouter.exception.ApiRouterException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public interface ApiRouter {


    default void invokeRequest(WebhookDto wDto, Map<String, String> headers, String keyId) {
        new Thread(() -> {
            try {
                processRequest(wDto, headers,keyId);
            } catch (IOException | ApiRouterException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void processRequest(WebhookDto wDto, Map<String, String> headers, String keyId) throws IOException, ApiRouterException;

    public void subscribe(String auth,ApiRouterRequestDto aDto) throws ApiRouterException;

    public void unsubscribe(String auth) throws ApiRouterException;


}
