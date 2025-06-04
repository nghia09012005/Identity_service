package com.example.identity_service.service;

import com.example.identity_service.dto.UserResponse.PermissionResponse;
import com.example.identity_service.dto.request.PermissionRequest;
import com.example.identity_service.entity.Permission;
import com.example.identity_service.mapper.PermissionMapper;
import com.example.identity_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // generate constructor cho tac ca field final mien la khong null
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // private final thanh default
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll(){
        var permissions = permissionRepository.findAll();
       return  permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission){
        permissionRepository.deleteById(permission);
    }
}
