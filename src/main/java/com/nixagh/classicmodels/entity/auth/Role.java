package com.nixagh.classicmodels.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nixagh.classicmodels.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long roleId;

    private String roleName;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "Roles_Permissions",
            joinColumns = @JoinColumn(name = "roleId"),
            inverseJoinColumns = @JoinColumn(name = "permissionId")
    )
    private Set<Permission> permissions = new HashSet<>();

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public Role(String roleName) {
        this.roleName = roleName;
    }

    @JsonIgnore
    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = new java.util.ArrayList<>(permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName()))
                .toList());
        list.add(new SimpleGrantedAuthority("ROLE_" + roleName));
        return list;
    }
}
