package com.kir.passengerservice.repository;

import com.kir.passengerservice.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
      List<Passenger> findAllByAccountIdAndIsDeleteFalse(Long accountId);
}
