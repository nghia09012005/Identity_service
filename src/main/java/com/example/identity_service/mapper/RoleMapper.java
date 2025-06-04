package com.example.identity_service.mapper;

import com.example.identity_service.dto.UserResponse.RoleResponse;
import com.example.identity_service.dto.request.RoleRequest;
import com.example.identity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore= true) // ignore set permissions -> tu map thu cong
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);

}
