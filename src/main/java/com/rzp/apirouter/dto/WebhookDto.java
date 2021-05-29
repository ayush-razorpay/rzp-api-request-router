package com.rzp.apirouter.dto;

import com.google.gson.annotations.Expose;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class WebhookDto {

    @Expose
    private String entity;
    @Expose
    private String account_id;
    @Expose
    private String event;
    @Expose
    private List<String> contains;
    @Expose
    private Object payload;

    private Map<String,String> headers;
}
