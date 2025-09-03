package org.kir.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PassengerResponse {
    private Long id;

    private Long accountId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private LocalDate dob;

    private Boolean isDelete;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
