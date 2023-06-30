package com.nixagh.classicmodels.repository.authRepo.impl;

import com.nixagh.classicmodels.entity.auth.Permission;
import com.nixagh.classicmodels.repository.authRepo.PermissionRepository;
import com.nixagh.classicmodels.repository.impl.BaseRepositoryImpl;
import jakarta.persistence.EntityManager;

public class PermissionRepositoryImpl extends BaseRepositoryImpl<Permission, Long> implements PermissionRepository {
    public PermissionRepositoryImpl(EntityManager entityManager) {
        super(Permission.class, entityManager);
    }
}
