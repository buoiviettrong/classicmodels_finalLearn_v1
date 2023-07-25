package com.nixagh.classicmodels.repository.role;

import com.nixagh.classicmodels.entity.auth.Role;
import com.nixagh.classicmodels.repository.BaseRepositoryImpl;
import jakarta.persistence.EntityManager;

public class RoleRepositoryImpl extends BaseRepositoryImpl<Role, Long> implements RoleRepository {
    public RoleRepositoryImpl(EntityManager entityManager) {
        super(Role.class, entityManager);
    }

    @Override
    public Role getRoleByRoleName(String roleName) {
        return jpaQueryFactory
                .selectFrom(role)
                .join(role.permissions, permission).fetchJoin()
                .where(role.roleName.eq(roleName))
                .fetchFirst();
    }
}
