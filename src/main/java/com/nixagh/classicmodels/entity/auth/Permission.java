package com.nixagh.classicmodels.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long permissionId;

    private String permissionName;

    @ManyToMany
    @JoinTable(
            name = "Roles_Permissions",
            joinColumns = @JoinColumn(name = "permissionId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    public Permission(String permissionName) {
        this.permissionName = permissionName;
    }
}
