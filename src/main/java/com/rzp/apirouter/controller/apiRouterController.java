package com.rzp.apirouter.controller;

import com.rzp.apirouter.dto.ApiRouterRequestDto;
import com.rzp.apirouter.dto.ControllerResponseDto;
import com.rzp.apirouter.dto.WebhookDto;
import com.rzp.apirouter.exception.ApiRouterException;
import com.rzp.apirouter.service.ApiRouter;
import lombok.var;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@RestController
public class apiRouterController {

    @Autowired
    ApiRouter apiRouter;

    @PostMapping("/webhookProcess")
    ResponseEntity webhookProcess(@RequestBody WebhookDto aDto, @RequestHeader HttpHeaders headers) throws ApiRouterException {

        Map<String,String> headerMap=headers.toSingleValueMap();
        aDto.setHeaders(headerMap);
        apiRouter.invokeRequest(aDto);
        var response = ControllerResponseDto.of("Processed", "");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/Subscription")
    ResponseEntity subscribe(@RequestBody ApiRouterRequestDto aDto) throws ApiRouterException {
        apiRouter.subscribe(aDto);

        var response = ControllerResponseDto.of("Subscription Added\"", "");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/Subscription")
    ResponseEntity unsubscribe(@RequestBody ApiRouterRequestDto aDto) throws ApiRouterException {
        apiRouter.unsubscribe(aDto);
        var response = ControllerResponseDto.of("Subscription Removed", "");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/paymentMethods/{keyId}")
    ResponseEntity getPaymentMethods(@PathVariable(name ="keyId" ) String keyId ) throws ApiRouterException, IOException {


      //  String encoded = Base64.getEncoder().encodeToString(keyId.getBytes());
       // String credential = Credentials.basic(keyId.trim(), "");
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
       // String value = "Basic "+"cnpwX3Rlc3Rfb0pQYmo5ckMxckRHQVE6";
        System.out.printf("credential"+keyId);
        StringBuilder value = new StringBuilder("Basic ").append(keyId);
        Request request = new Request.Builder()
                .url("https://api.razorpay.com/v1/methods")
                .method("GET", null)
                .addHeader("Host", "api.razorpay.com")
                .addHeader("Authorization", value.toString().trim())
                .build();
        Response response = client.newCall(request).execute();
      //  Response response = client.newCall(request).execute();


       // var res = ControllerResponseDto.of("Processed", "");
        return ResponseEntity.ok(response.body().string());
    }



}
