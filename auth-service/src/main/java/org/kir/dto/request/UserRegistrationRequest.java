package org.kir.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private LocalDate dob;
}
