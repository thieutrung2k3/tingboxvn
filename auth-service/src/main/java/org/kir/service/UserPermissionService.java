package org.kir.service;

import org.kir.entity.User;

import java.util.Set;

public interface UserPermissionService {
    Set<String> getUserRoles(User user);

    Set<String> getEffectivePermissions(User user);
}
