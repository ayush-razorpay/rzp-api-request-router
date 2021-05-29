package com.rzp.apirouter.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class WebhookDto {

    private String entity;
    private String account_id;
    private String event;
    private List<String> contains;
    private Object payload;
}
