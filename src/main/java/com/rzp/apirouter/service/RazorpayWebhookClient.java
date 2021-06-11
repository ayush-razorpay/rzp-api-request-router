package com.rzp.apirouter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rzp.apirouter.exception.ApiRouterException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface RazorpayWebhookClient {



     static final String HTTPS_API_RAZORPAY_COM_V_1_WEBHOOKS = "https://api.razorpay.com/v1/webhooks";


    String getBasicAuth();

    List fetchAll() throws IOException, ApiRouterException;
    Map update(String webhookId,String payload) throws IOException, ApiRouterException;
    Map registerNew() throws IOException, ApiRouterException;
    public String getPayload( Map<String,Boolean> events) throws JsonProcessingException;
    public String getKeyId();
}
