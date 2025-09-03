package com.kir.passengerservice.dto.response;

import com.kir.passengerservice.entity.IdentityDocument;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
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
