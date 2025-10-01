package org.kir.searchservice.repository;

import org.kir.searchservice.entity.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProviderRepository extends JpaRepository<Provider, Long> {

    @Query(
            value = """
                    SELECT * 
                    FROM providers p
                    WHERE (:keyword IS NULL 
                           OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
                      AND (:paymentSchedule IS NULL 
                           OR LOWER(p.payment_schedule) = LOWER(:paymentSchedule))
                    """,
            countQuery = """
                    SELECT COUNT(*) 
                    FROM providers p
                    WHERE (:keyword IS NULL 
                           OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
                      AND (:paymentSchedule IS NULL 
                           OR LOWER(p.payment_schedule) = LOWER(:paymentSchedule))
                    """,
            nativeQuery = true
    )
    Page<Provider> findProviders(Pageable pageable,
                                 @Param("keyword") String keyword,
                                 @Param("paymentSchedule") String paymentSchedule);


}
