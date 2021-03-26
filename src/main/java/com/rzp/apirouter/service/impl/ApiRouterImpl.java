package com.rzp.apirouter.service.impl;

import com.google.gson.Gson;
import com.rzp.apirouter.dto.ApiRouterRequestDto;
import com.rzp.apirouter.dto.WebhookDto;
import com.rzp.apirouter.exception.ApiRouterException;
import com.rzp.apirouter.service.ApiRouter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ApiRouterImpl implements ApiRouter {

    ConcurrentHashMap<String, String> listOfSubscribers = new ConcurrentHashMap<>();

    public static boolean isNotURL(String url) {
        try {
            new URL(url);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public void processRequest(WebhookDto wDto) throws IOException {

        final String mid = wDto.getAccount_id().substring(4);
        String url = this.listOfSubscribers.get(mid);
        if (url != null) {

            log.info("routing request for mid : {} . request payload : {}", mid, wDto);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, new Gson().toJson(wDto));
            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();

            log.info("Route Response : {}", response);

        } else {
            log.info("not registered request . {}", wDto);
        }
    }

    @Override
    public void subscribe(ApiRouterRequestDto aDto) throws ApiRouterException {
        validateADto(aDto);
        this.listOfSubscribers.put(aDto.getMid(), aDto.getUrl());

    }

    @Override
    public void unsubscribe(ApiRouterRequestDto aDto) throws ApiRouterException {

        if (aDto.getMid() == null || aDto.getMid().trim().isEmpty())
            throw new ApiRouterException("not a valid MID. Unsubscribe failed");

        this.listOfSubscribers.remove(aDto.getMid());
    }

    void validateADto(ApiRouterRequestDto aDto) throws ApiRouterException {
        if (aDto.getMid() == null || aDto.getMid().trim().isEmpty())
            throw new ApiRouterException("not a valid MID. subscribe request in not valid");

        if (aDto.getUrl() == null || aDto.getUrl().trim().isEmpty() || isNotURL(aDto.getUrl().trim()))
            throw new ApiRouterException("not a valid url. subscribe request in not valid");
    }
}