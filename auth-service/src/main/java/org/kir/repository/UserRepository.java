package org.kir.repository;

import jakarta.persistence.SqlResultSetMapping;
import org.kir.dto.response.UserResponse;
import org.kir.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
    SELECT new org.kir.dto.response.UserResponse(
        u.email,
        u.firstName,
        u.lastName,
        u.isActive,
        u.isDelete,
        u.createdAt,
        u.updatedAt
    )
    FROM User u
    WHERE (:keyword IS NULL 
           OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))
      AND u.createdAt >= COALESCE(:from, u.createdAt)
      AND u.createdAt <= COALESCE(:to, u.createdAt)
    ORDER BY u.createdAt DESC
    """)
    Page<UserResponse> getUsersWithPaging(Pageable pageable,
                                          @Param("keyword") String keyword,
                                          @Param("from") LocalDateTime from,
                                          @Param("to") LocalDateTime to);


}
