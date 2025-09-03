package org.kir.mapper;

import org.kir.dto.request.UserRegistrationRequest;
import org.kir.dto.response.UserResponse;
import org.kir.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegistrationRequest request);

    UserResponse toUserResponse(User user);
}

