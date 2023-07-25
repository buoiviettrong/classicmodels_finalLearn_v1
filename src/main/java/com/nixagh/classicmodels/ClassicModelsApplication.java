package com.nixagh.classicmodels;

import com.nixagh.classicmodels.dto.auth.RegisterRequest;
import com.nixagh.classicmodels.entity.auth.Permission;
import com.nixagh.classicmodels.entity.auth.Role;
import com.nixagh.classicmodels.entity.enums.LoginType;
import com.nixagh.classicmodels.service.auth_service.AuthenticationService;
import com.nixagh.classicmodels.service.auth_service.PermissionService;
import com.nixagh.classicmodels.service.auth_service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@SpringBootApplication
@RequiredArgsConstructor
public class ClassicModelsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClassicModelsApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            AuthenticationService service,
            RoleService roleService,
            PermissionService permissionService
    ) {
        return args -> {
//			createRoleAndPermissions(roleService, permissionService);
//			createAdminAndManagerAccount(service, roleService);
        };
    }

    private void createAdminAndManagerAccount(
            AuthenticationService service,
            RoleService roleService
    ) {
        var admin = RegisterRequest.builder()
                .firstName("Admin")
                .lastName("Admin")
                .email("admin@mail.com")
                .password("password")
                .role(roleService.getRoleByRoleName("ADMIN"))
                .type(LoginType.NORMAL)
                .build();
        System.out.println("Admin token: " + service.register(admin).getAccessToken());

        var manager = RegisterRequest.builder()
                .firstName("Admin")
                .lastName("Admin")
                .email("manager@mail.com")
                .password("password")
                .role(roleService.getRoleByRoleName("MANAGER"))
                .type(LoginType.NORMAL)
                .build();
        System.out.println("Manager token: " + service.register(manager).getAccessToken());
    }

    private void createRoleAndPermissions(
            RoleService roleService,
            PermissionService permissionService
    ) {
        // create roles
        var roleAdmin = new Role("ADMIN");
        var roleManager = new Role("MANAGER");
        var roleUser = new Role("USER");

        // create permissions
        // admin permissions
        var Admin_Read = new Permission("admin:read");
        var Admin_Update = new Permission("admin:update");
        var Admin_Create = new Permission("admin:create");
        var Admin_Delete = new Permission("admin:delete");
        // manager permissions
        var Manager_Read = new Permission("management:read");
        var Manager_Update = new Permission("management:update");
        var Manager_Create = new Permission("management:create");
        var Manager_Delete = new Permission("management:delete");
        // user permissions
        var User_Read = new Permission("user:read");
        var User_Update = new Permission("user:update");
        var User_Create = new Permission("user:create");
        var User_Delete = new Permission("user:delete");

        // set permissions for each role
        roleAdmin.setPermissions(Set.of(
                Admin_Read, Admin_Update, Admin_Create, Admin_Delete,
                Manager_Read, Manager_Update, Manager_Create, Manager_Delete
        ));
        roleManager.setPermissions(Set.of(
                Manager_Read, Manager_Update, Manager_Create, Manager_Delete
        ));
        roleUser.setPermissions(Set.of(
                User_Read, User_Update, User_Delete, User_Create
        ));

        // save permissions
        permissionService.createPermission(Admin_Read);
        permissionService.createPermission(Admin_Create);
        permissionService.createPermission(Admin_Update);
        permissionService.createPermission(Admin_Delete);

        permissionService.createPermission(Manager_Read);
        permissionService.createPermission(Manager_Update);
        permissionService.createPermission(Manager_Delete);
        permissionService.createPermission(Manager_Create);

        permissionService.createPermission(User_Read);
        permissionService.createPermission(User_Update);
        permissionService.createPermission(User_Delete);
        permissionService.createPermission(User_Create);

        // Save role to database
        roleService.createRole(roleAdmin);
        roleService.createRole(roleManager);
        roleService.createRole(roleUser);
    }

}
