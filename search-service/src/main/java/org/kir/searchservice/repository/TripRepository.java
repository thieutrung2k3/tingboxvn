package org.kir.searchservice.repository;

import org.kir.searchservice.entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
  @Query(value = """
      SELECT *
      FROM trips t
      WHERE t.departure_time >= COALESCE(:startTime, t.departure_time)
        AND t.departure_time < COALESCE(:endTime, t.departure_time)
        AND ( :applyOrigin = false OR t.origin_code IN (:originCodes) )
        AND ( :applyDestination = false OR t.destination_code IN (:destinationCodes) )
      """, countQuery = """
      SELECT count(*)
      FROM trips t
      WHERE t.departure_time >= COALESCE(:startTime, t.departure_time)
        AND t.departure_time < COALESCE(:endTime, t.departure_time)
        AND ( :applyOrigin = false OR t.origin_code IN (:originCodes) )
        AND ( :applyDestination = false OR t.destination_code IN (:destinationCodes) )
      """, nativeQuery = true)
  Page<Trip> searchWithPageable(@Param("startTime") LocalDateTime startTime,
      @Param("endTime") LocalDateTime endTime,
      @Param("applyOrigin") boolean applyOrigin,
      @Param("originCodes") List<String> originCodes,
      @Param("applyDestination") boolean applyDestination,
      @Param("destinationCodes") List<String> destinationCodes,
      Pageable pageable);

}
