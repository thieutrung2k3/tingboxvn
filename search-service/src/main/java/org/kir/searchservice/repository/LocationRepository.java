package org.kir.searchservice.repository;

import org.kir.searchservice.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = "SELECT * FROM locations l WHERE l.location LIKE %:location%",
            countQuery = "SELECT count(*) FROM locations l WHERE l.location LIKE %:location%",
            nativeQuery = true)
    Page<Location> findByLocationKey(@Param("location") String location, Pageable pageable);

}
