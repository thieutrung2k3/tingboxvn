package org.kir.bookingservice.client;

import com.kir.commonservice.dto.ApiResponse;
import com.kir.commonservice.dto.request.TicketEmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://localhost:8001/notification/email")
public interface NotificationClient {

    @PostMapping("/public/ticket")
    ApiResponse<String> sendTicket(@RequestBody TicketEmailRequest request);
}


