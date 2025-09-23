package org.kir.bookingservice.repository;

import org.kir.bookingservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}


