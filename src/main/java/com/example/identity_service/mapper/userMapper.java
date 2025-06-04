package com.example.identity_service.mapper;

import com.example.identity_service.dto.UserResponse.ApiUserResponse;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface userMapper {
    User userMap(UserCreationRequest request);
    @Mapping(target = "id", ignore = true)// khong map id voi nhau
    ApiUserResponse apiUsRes(User us);

    @Mapping(target = "roles",ignore = true)
    void upDateU(@MappingTarget User us, UserUpdateRequest udr);
}
