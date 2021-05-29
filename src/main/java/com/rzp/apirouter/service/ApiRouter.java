package com.rzp.apirouter.service;

import com.rzp.apirouter.dto.ApiRouterRequestDto;
import com.rzp.apirouter.dto.WebhookDto;
import com.rzp.apirouter.exception.ApiRouterException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public interface ApiRouter {


    default void invokeRequest(WebhookDto wDto, Map<String,String> headers) {
        new Thread(() -> {
            try {
                processRequest(wDto,headers);
            } catch (IOException | ApiRouterException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void processRequest(WebhookDto wDto,Map<String,String> headers) throws IOException, ApiRouterException;

    public void subscribe(ApiRouterRequestDto aDto) throws ApiRouterException;

    public void unsubscribe(ApiRouterRequestDto aDto) throws ApiRouterException;


}
