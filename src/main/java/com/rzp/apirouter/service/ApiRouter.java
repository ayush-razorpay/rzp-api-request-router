package com.rzp.apirouter.service;

import com.rzp.apirouter.dto.ApiRouterRequestDto;
import com.rzp.apirouter.dto.WebhookDto;
import com.rzp.apirouter.exception.ApiRouterException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface ApiRouter {


    default void invokeRequest(WebhookDto wDto) {
        new Thread(() -> {
            try {
                processRequest(wDto);
            } catch (IOException | ApiRouterException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void processRequest(WebhookDto wDto) throws IOException, ApiRouterException;

    public void subscribe(ApiRouterRequestDto aDto) throws ApiRouterException;

    public void unsubscribe(ApiRouterRequestDto aDto) throws ApiRouterException;


}
