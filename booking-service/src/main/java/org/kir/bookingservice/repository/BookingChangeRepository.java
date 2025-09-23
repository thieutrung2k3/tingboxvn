package org.kir.bookingservice.repository;

import org.kir.bookingservice.entity.BookingChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingChangeRepository extends JpaRepository<BookingChange, Long> {
}


