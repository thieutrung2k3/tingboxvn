package org.kir.repository;

import org.kir.entity.UserDeniedPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserDeniedPermissionRepository extends JpaRepository<UserDeniedPermission, Long> {
}
