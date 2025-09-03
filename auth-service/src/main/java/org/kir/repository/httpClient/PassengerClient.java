package org.kir.repository.httpClient;

import com.kir.commonservice.dto.ApiResponse;
import org.kir.configuration.CustomRequestInterceptor;
import org.kir.dto.request.PassengerCreationRequest;
import org.kir.dto.response.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "passenger-service",
        url = "http://localhost:8002/passenger",
        configuration = CustomRequestInterceptor.class)
public interface PassengerClient {
    @PostMapping(value = "/internal/create", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<PassengerResponse> createPassenger(PassengerCreationRequest request);
}
