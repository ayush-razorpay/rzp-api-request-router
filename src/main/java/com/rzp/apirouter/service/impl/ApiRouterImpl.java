package com.rzp.apirouter.service.impl;

import com.google.gson.Gson;
import com.rzp.apirouter.dto.ApiRouterRequestDto;
import com.rzp.apirouter.dto.WebhookDto;
import com.rzp.apirouter.exception.ApiRouterException;
import com.rzp.apirouter.service.ApiRouter;
import com.rzp.apirouter.service.RazorpayWebhookClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.rzp.apirouter.service.impl.RazorpayWebhookClientImpl.routerURL;

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
    public void processRequest(WebhookDto wDto, Map<String, String> headers, String keyId) throws IOException, ApiRouterException {

        System.out.printf(wDto.toString());
       // String mid;
        String url;
        try {
            url = this.listOfSubscribers.get(keyId);

            if(url == null || url.isEmpty())
            throw new ApiRouterException("keyid not registered");
           // mid = wDto.getAccount_id().substring(4);
        } catch (Exception e) {
            log.error("Failed to identify the mid for requested mid {} and payload : {}"
                    , wDto.getAccount_id(), wDto);
            throw new ApiRouterException("Failed to identify the mid for requested mid:" + wDto.getAccount_id());

        }



        if (url != null) {

            log.info("routing request for keyId : {} . request payload : {}", keyId, wDto);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");


            val toSend = wDto;

            RequestBody body = RequestBody.create(mediaType, new Gson().toJson(wDto));
            Request.Builder requestBuilder = new Request.Builder()
                    .url(url)
                    .method("POST", body);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("X-Razorpay-Signature"))
                    requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }

            Request request = requestBuilder.build();
            Response response = client.newCall(request).execute();

            log.info("Route Response : {}", response);

        } else {
            log.info("not registered request . {}", wDto);
        }
    }

    @Override
    public void subscribe(String auth,ApiRouterRequestDto aDto) throws ApiRouterException {


        if(auth.isEmpty())
            throw new ApiRouterException("Auth header cannot ve null");
        this.validateADto(aDto);


        RazorpayWebhookClient client = new RazorpayWebhookClientImpl(auth);

        try {
           List<Map> webhooks =  client.fetchAll();
            log.info("webhooks fetched : {}",webhooks);

            Optional<Map> w =  webhooks.stream().filter(x->x.get("url").equals(routerURL+"/"+client.getKeyId())).findFirst();

            if(w.isPresent()){
                //update
                Map<String,Boolean> events = (Map<String,Boolean>) w.get().get("events");
               client.update(String.valueOf(w.get().get("id")), client.getPayload(events));

            }else {
                //add new
                client.registerNew();
            }
            this.listOfSubscribers.put(client.getKeyId(),aDto.getUrl());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void unsubscribe(String auth) throws ApiRouterException {

    }


    void validateADto(ApiRouterRequestDto aDto) throws ApiRouterException {

        if (aDto.getUrl() == null || aDto.getUrl().trim().isEmpty() || isNotURL(aDto.getUrl().trim()))
            throw new ApiRouterException("not a valid url. subscribe request in not valid");
    }
}
