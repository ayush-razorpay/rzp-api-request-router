package com.rzp.apirouter.controller;

import com.rzp.apirouter.dto.ApiRouterRequestDto;
import com.rzp.apirouter.dto.ControllerResponseDto;
import com.rzp.apirouter.dto.WebhookDto;
import com.rzp.apirouter.exception.ApiRouterException;
import com.rzp.apirouter.service.ApiRouter;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class apiRouterController {

    @Autowired
    ApiRouter apiRouter;

    @PostMapping("/webhookProcess")
    ResponseEntity webhookProcess(@RequestBody WebhookDto aDto) throws ApiRouterException {
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

}
