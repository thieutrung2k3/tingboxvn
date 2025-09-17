package com.kir.commonservice.constant;

public final class CacheConstant {

    public static final class CacheKeys{
        public static final String USER_ROLES = "user_roles";
        public static final String USER_PERMISSIONS = "user_permissions";
        public static final String ROLE_PERMISSIONS = "role_permissions";

        public static String userRolesKey(Long userId) {
            return USER_ROLES + ":" + userId;
        }

        public static String userPermissionsKey(Long userId) {
            return USER_PERMISSIONS + ":" + userId;
        }

        public static String rolePermissionsKey(Long roleId) {
            return ROLE_PERMISSIONS + ":" + roleId;
        }

        public static String otpKey(String obj){
            return CachePrefix.Otp + obj;
        }
    }

    public static final class CachePrefix {
        public static final String Otp = "otp:";
    }
}
