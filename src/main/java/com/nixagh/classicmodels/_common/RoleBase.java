package com.nixagh.classicmodels._common;

import com.nixagh.classicmodels.entity.auth.Role;
import com.nixagh.classicmodels.exception.NotFoundRole;
import com.nixagh.classicmodels.service.RoleService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleBase {
    private static List<Role> ROLES = new ArrayList<>();

    public RoleBase(RoleService roleService) {
        ROLES = roleService.getRoles();
    }

    public static Role getRole(String roleName) {
        return ROLES.stream()
                .filter(role -> role.getRoleName().equals(roleName))
                .findAny()
                .orElseThrow(() -> new NotFoundRole("Role " + roleName + " does not exist"));
    }
}
