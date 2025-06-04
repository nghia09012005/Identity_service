package com.example.identity_service.service;

import com.example.identity_service.dto.UserResponse.RoleResponse;
import com.example.identity_service.dto.request.RoleRequest;
import com.example.identity_service.entity.Role;
import com.example.identity_service.mapper.RoleMapper;
import com.example.identity_service.repository.PermissionRepository;
import com.example.identity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.sql.results.spi.ListResultsConsumer;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor // generate constructor cho tac ca field final mien la khong null
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // private final thanh default
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;


    public RoleResponse create(RoleRequest request){
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);

    }

    public List<RoleResponse> getAll(){
        var roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String role){
        roleRepository.deleteById(role);
    }


}
