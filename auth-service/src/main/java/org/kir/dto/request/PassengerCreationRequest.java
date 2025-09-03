package org.kir.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerCreationRequest {
    private Long accountId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private LocalDate dob;
}
