package org.kir.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String email;
    String firstName;
    String lastName;
    Boolean isDelete;
    Boolean isActive;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public UserResponse(String email,
                        String firstName,
                        String lastName,
                        Boolean isActive,
                        Boolean isDelete,
                        LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.isDelete = isDelete;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
