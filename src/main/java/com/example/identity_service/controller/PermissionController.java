package com.example.identity_service.controller;

import com.example.identity_service.dto.UserResponse.PermissionResponse;
import com.example.identity_service.dto.request.ApiResponse;
import com.example.identity_service.dto.request.PermissionRequest;
import com.example.identity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.http11.filters.VoidOutputFilter;
import org.springframework.security.web.jaasapi.JaasApiIntegrationFilter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/permissions")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request){
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll(){
        return  ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable String permission){
        permissionService.delete(permission);
        return ApiResponse.<Void>builder()
                .build();
    }



}
