package com.nixagh.classicmodels.repository.permission;

import com.nixagh.classicmodels.entity.auth.Permission;
import com.nixagh.classicmodels.repository.BaseRepositoryImpl;
import jakarta.persistence.EntityManager;

public class PermissionRepositoryImpl extends BaseRepositoryImpl<Permission, Long> implements PermissionRepository {
    public PermissionRepositoryImpl(EntityManager entityManager) {
        super(Permission.class, entityManager);
    }
}
