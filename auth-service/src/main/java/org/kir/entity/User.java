package org.kir.entity;

import jakarta.persistence.*;
import lombok.*;
import org.kir.dto.response.UserResponse;

import java.time.LocalDateTime;
import java.util.Set;


@SqlResultSetMapping(
        name = "UserResponseMapping",
        classes = @ConstructorResult(
                targetClass = UserResponse.class,
                columns = {
                        @ColumnResult(name = "email", type = String.class),
                        @ColumnResult(name = "first_name", type = String.class),
                        @ColumnResult(name = "last_name", type = String.class),
                        @ColumnResult(name = "is_active", type = Boolean.class),
                        @ColumnResult(name = "is_delete", type = Boolean.class),
                        @ColumnResult(name = "created_at", type = LocalDateTime.class),
                        @ColumnResult(name = "updated_at", type = LocalDateTime.class)
                }
        )
)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", unique = true, nullable = false)
    private String passwordHash;

    @Column(name = "is_delete", columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDelete = false;

    @Column(name = "is_active", columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isActive = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserRole> userRoles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserDeniedPermission> userDeniedPermissions;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

//CREATE TABLE users
//(
//id            BIGSERIAL PRIMARY KEY,
//email         VARCHAR(255) UNIQUE NOT NULL,
//password_hash VARCHAR(255) UNIQUE NOT NULL,
//is_active     BOOLEAN DEFAULT FALSE,
//is_delete     BOOLEAN DEFAULT FALSE,
//created_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
//updated_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
//)
