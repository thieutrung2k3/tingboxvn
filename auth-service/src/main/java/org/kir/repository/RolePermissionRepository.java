package org.kir.repository;

import org.kir.entity.Permission;
import org.kir.entity.Role;
import org.kir.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    @Query("SELECT rp FROM RolePermission rp WHERE rp.role IN :roles")
    Set<RolePermission> findAllByRoles(@Param("roles") Set<Role> roles);

    @Query("SELECT rp.permission FROM RolePermission rp WHERE rp.role.id IN :rolesIds")
    Set<Permission> findAllPermissionsByRoles(@Param("rolesIds") Set<Long> roleIds);
}
