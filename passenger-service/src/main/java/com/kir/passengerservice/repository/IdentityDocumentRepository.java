package com.kir.passengerservice.repository;

import com.kir.passengerservice.entity.IdentityDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityDocumentRepository extends JpaRepository<IdentityDocument, Long> {
}
