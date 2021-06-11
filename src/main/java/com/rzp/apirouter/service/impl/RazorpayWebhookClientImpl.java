package com.rzp.apirouter.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rzp.apirouter.exception.ApiRouterException;
import com.rzp.apirouter.service.RazorpayWebhookClient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;


@Slf4j
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RazorpayWebhookClientImpl implements RazorpayWebhookClient {

    static String routerURL = "https://floating-woodland-30144.herokuapp.com/webhookProcess";
    private final ObjectMapper mapper = new ObjectMapper();
    private final String basicAuthHeader;

//    private static String getContent() {
//
//        return "{\n\n\"url\":\"" + routerURL + "\"" +
//                "" +
//                ",\n\"secret\": \"12345\",\n \"events\": {\n      " +
//                "  \"payment.authorized\": true,\n   " +
//                "     \"payment.failed\": true,\n     " +
//                "  \"payment.captured\": true,\n    " +
//                "    \"payment.dispute.created\": true,\n  " +
//                "      \"order.paid\": true,\n      " +
//                "  \"invoice.paid\": true,\n     " +
//                "   \"invoice.partially_paid\": true,\n " +
//                "       \"invoice.expired\": true,\n     " +
//                "   \"subscription.authenticated\": true,\n  " +
//                "      \"subscription.paused\": true,\n     " +
//                "   \"subscription.resumed\": true,\n    " +
//                "    \"subscription.activated\": true,\n   " +
//                "     \"subscription.pending\": true,\n   " +
//                "     \"subscription.halted\": true,\n     " +
//                "   \"subscription.charged\": true,\n     " +
//                "   \"subscription.cancelled\": true,\n  " +
//                "      \"subscription.completed\": true,\n  " +
//                "      \"subscription.updated\": true,\n   " +
//                "     \"settlement.processed\": true,\n   " +
//                "     \"virtual_account.credited\": true,\n    " +
//                "    \"virtual_account.created\": true,\n     " +
//                "   \"virtual_account.closed\": true,\n     " +
//                "   \"payment.dispute.won\": true,\n     " +
//                "   \"payment.dispute.lost\": true,\n   " +
//                "     \"payment.dispute.closed\": true,\n  " +
//                "      \"fund_account.validation.completed\": true,\n   " +
//                "     \"fund_account.validation.failed\": true,\n    " +
//                "    \"payment.downtime.started\": true,\n   " +
//                "     \"payment.downtime.resolved\": true,\n    " +
//                "    \"refund.speed_changed\": true,\n    " +
//                "    \"refund.processed\": true,\n   " +
//                "     \"refund.failed\": true,\n    " +
//                "    \"refund.created\": true,\n     " +
//                "   \"transfer.processed\": true,\n  " +
//                "      \"payment_link.paid\": true,\n     " +
//                "   \"payment_link.partially_paid\": true,\n   " +
//                "     \"payment_link.expired\": true,\n    " +
//                "    \"payment_link.cancelled\": true,\n  " +
//                "      \"account.product_status\": true\n    },\n " +
//                "   \"active\": true\n}";
//    }

    @Override
    public String getPayload( Map<String,Boolean> events) throws JsonProcessingException {

        if(events == null) {
            return "{\n" +
                    "    \"url\": \"" + "https://floating-woodland-30144.herokuapp.com/webhookProcess/" + this.getKeyId() + "\",\n" +
                    "    \"secret\": \"secret123321\",\n" +
                    "    \"events\": {},\n" +
                    "    \"active\": true\n" +
                    "}";
        }else {

            for (Map.Entry<String, Boolean> event : events.entrySet()) {
                event.setValue(true);
            }
            val str =  mapper.writeValueAsString(events);

            return "{\n" +
                    "    \"url\": \"" + "https://floating-woodland-30144.herokuapp.com/webhookProcess/" + this.getKeyId() + "\",\n" +
                    "    \"secret\": \"secret123321\",\n" +
                    "    \"events\": "+str+",\n" +
                    "    \"active\": true\n" +
                    "}";
        }

    }

    @Override
    public String getBasicAuth() {
        // return "Basic cnpwX3Rlc3RfSlZsMG5hNWlRUjE1M2c6c2lDbldLS0VYQ0h4UmNJdmpuaW0wWUdU";
        return basicAuthHeader;
    }

    @Override
    public String getKeyId() {
        // return "Basic cnpwX3Rlc3RfSlZsMG5hNWlRUjE1M2c6c2lDbldLS0VYQ0h4UmNJdmpuaW0wWUdU";
        Base64.Decoder decoder = Base64.getDecoder();
        val x = basicAuthHeader.substring(basicAuthHeader.indexOf(" ")+1);
        String dStr = new String(decoder.decode(x));
        return dStr.substring(dStr.indexOf(":")+1);
    }

    @Override
    public List fetchAll() throws IOException, ApiRouterException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(HTTPS_API_RAZORPAY_COM_V_1_WEBHOOKS)
                .method("GET", null)
                .addHeader("Authorization", getBasicAuth())
                .build();
        Response response = client.newCall(request).execute();

        Map map = mapper.readValue(response.body().string(), Map.class);

        List<Map> list = new ArrayList<>();


        final Object items = map.get("items");

        if (items == null)
            throw new ApiRouterException("Failed to fetch webhooks");

        list = mapper.readValue(mapper.writeValueAsString(items), list.getClass());

        return list;
    }


    @Override
    public Map update(String webhookId,String payload) throws IOException, ApiRouterException {


        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, payload);
        Request request = new Request.Builder()
                .url(HTTPS_API_RAZORPAY_COM_V_1_WEBHOOKS + "/" + webhookId)
                .method("PUT", body)
                .addHeader("Authorization", getBasicAuth())
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();


        if (response.code() != 200) {
            val res =response.body().string();
            log.error("Failed to update webhook" + res);
            throw new ApiRouterException("Failed to update webhook" + res);

        }
        val res =response.body().string();
        log.info("updated webhook for id {}, api response {} ",webhookId,res);
        Map toReturn = mapper.readValue(res, Map.class);
        return toReturn;

    }

    @Override
    public Map registerNew() throws IOException, ApiRouterException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, this.getPayload(null));

        Request request = new Request.Builder()
                .url(HTTPS_API_RAZORPAY_COM_V_1_WEBHOOKS)
                .method("POST", body)
                .addHeader("Authorization", getBasicAuth())
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

        if (response.code() != 200) {
            val res = response.body().string();
            log.error("Failed to register webhook: {}" ,res );
            throw new ApiRouterException("Failed to register webhook" + res);
        }
        val res = response.body().string();
        log.info("registered new webhook  api response {} ",res);
        Map toReturn = mapper.readValue(res, Map.class);

        //not enabling all available events
        String id = (String) toReturn.get("id");
        Map<String,Boolean> events = (Map<String,Boolean>) toReturn.get("events");
     val payload2 =   this.getPayload(events);

     this.update(id,payload2);


        return toReturn;
    }


}
