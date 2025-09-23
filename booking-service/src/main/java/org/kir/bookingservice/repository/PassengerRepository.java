package org.kir.bookingservice.repository;

import org.kir.bookingservice.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}


