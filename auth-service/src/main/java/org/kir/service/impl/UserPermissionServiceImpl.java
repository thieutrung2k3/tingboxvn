package org.kir.service.impl;

import lombok.RequiredArgsConstructor;
import org.kir.entity.Permission;
import org.kir.entity.User;
import org.kir.repository.RolePermissionRepository;
import org.kir.service.UserPermissionService;
import org.kir.service.cache.PermissionCacheService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserPermissionServiceImpl implements UserPermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    private final PermissionCacheService cacheService;

    @Override
    public Set<String> getUserRoles(User user) {
        Set<String> cachedRoles = cacheService.getUserRoles(user.getId());
        if(cachedRoles != null) {
            return cachedRoles;
        }

        Set<String> roles = user.getUserRoles()
                .stream()
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.toSet());

        cacheService.cacheUserRoles(user.getId(), roles);

        return roles;
    }

    @Override
    public Set<String> getEffectivePermissions(User user) {

        Set<String> cachedPermissions = cacheService.getUserPermissions(user.getId());
        if(cachedPermissions != null) {
            return cachedPermissions;
        }

        Set<Long> userRoleIds = user.getUserRoles()
                .stream()
                .map(r -> r.getRole().getId())
                .collect(Collectors.toSet());

        Set<String> rolePermissions = rolePermissionRepository
                .findAllPermissionsByRoles(userRoleIds)
                .stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());

        Set<String> deniedPermissions = user.getUserDeniedPermissions()
                .stream()
                .map(p -> p.getPermission().getName())
                .collect(Collectors.toSet());

        rolePermissions.removeAll(deniedPermissions);

        cacheService.cacheUserPermissions(user.getId(), rolePermissions);

        return rolePermissions;
    }
}
