package com.kir.passengerservice.entity;

import com.kir.commonservice.constant.AppConstants;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "identity_documents",
uniqueConstraints = {@UniqueConstraint(columnNames = {"document_type", "document_number"})})
public class IdentityDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_type")
    private AppConstants.DocumentType documentType;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "issue_place")
    private String issuePlace;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "identityDocument", cascade = CascadeType.ALL)
    private Passenger passenger;
}
