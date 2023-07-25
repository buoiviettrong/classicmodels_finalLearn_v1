package com.nixagh.classicmodels.repository.role;

import com.nixagh.classicmodels.entity.auth.Role;
import com.nixagh.classicmodels.repository.BaseRepository;

public interface RoleRepository extends BaseRepository<Role, Long> {
    Role getRoleByRoleName(String role);
}
