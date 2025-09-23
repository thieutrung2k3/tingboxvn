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
      WHERE t.departure_time = COALESCE(CAST(:departureTime AS timestamp), t.departure_time)
        AND (:originCodes IS NULL OR t.origin_code IN (:originCodes))
        AND (:destinationCodes IS NULL OR t.destination_code IN (:destinationCodes))
        AND t.provider_id = COALESCE(:providerId, t.provider_id)
      """, countQuery = """
      SELECT count(*)
      FROM trips t
      WHERE t.departure_time = COALESCE(CAST(:departureTime AS timestamp), t.departure_time)
        AND (:originCodes IS NULL OR t.origin_code IN (:originCodes))
        AND (:destinationCodes IS NULL OR t.destination_code IN (:destinationCodes))
        AND t.provider_id = COALESCE(:providerId, t.provider_id)
      """, nativeQuery = true)
  Page<Trip> searchWithPageable(@Param("departureTime") LocalDateTime departureTime,
      @Param("originCodes") List<String> originCodes,
      @Param("destinationCodes") List<String> destinationCodes,
      @Param("providerId") Long providerId,
      Pageable pageable);

}
