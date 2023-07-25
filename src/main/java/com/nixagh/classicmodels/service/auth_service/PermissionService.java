package com.nixagh.classicmodels.service.auth_service;

import com.nixagh.classicmodels.entity.auth.Permission;
import com.nixagh.classicmodels.repository.permission.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public List<Permission> getPermissions() {
        return permissionRepository.findAll();
    }
}
