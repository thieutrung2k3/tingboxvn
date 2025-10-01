package org.kir.searchservice.repository;

import org.kir.searchservice.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    
    List<Seat> findByTripIdAndIsDeleteFalseOrderByRowNumberAscColumnLetterAsc(Long tripId);
    
    List<Seat> findByTripIdAndSeatClassAndIsDeleteFalseOrderByRowNumberAscColumnLetterAsc(Long tripId, String seatClass);
    
    List<Seat> findByTripIdAndStatusAndIsDeleteFalseOrderByRowNumberAscColumnLetterAsc(Long tripId, Seat.SeatStatus status);
    
    Optional<Seat> findByTripIdAndSeatNumberAndIsDeleteFalse(Long tripId, String seatNumber);
    
    @Query("SELECT s FROM Seat s WHERE s.trip.id = :tripId AND s.isDelete = false AND s.status = 'AVAILABLE'")
    List<Seat> findAvailableSeatsByTripId(@Param("tripId") Long tripId);
    
    @Query("SELECT s FROM Seat s WHERE s.trip.id = :tripId AND s.isDelete = false AND s.seatClass = :seatClass AND s.status = 'AVAILABLE'")
    List<Seat> findAvailableSeatsByTripIdAndClass(@Param("tripId") Long tripId, @Param("seatClass") String seatClass);
    
    @Query("SELECT COUNT(s) FROM Seat s WHERE s.trip.id = :tripId AND s.isDelete = false AND s.status = 'AVAILABLE'")
    Long countAvailableSeatsByTripId(@Param("tripId") Long tripId);
    
    @Query("SELECT COUNT(s) FROM Seat s WHERE s.trip.id = :tripId AND s.isDelete = false AND s.seatClass = :seatClass AND s.status = 'AVAILABLE'")
    Long countAvailableSeatsByTripIdAndClass(@Param("tripId") Long tripId, @Param("seatClass") String seatClass);
}

