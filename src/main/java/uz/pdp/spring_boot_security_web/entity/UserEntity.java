package uz.pdp.spring_boot_security_web.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.spring_boot_security_web.entity.role.RoleEnum;
import uz.pdp.spring_boot_security_web.entity.role.RolePermissionEntity;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends BaseEntity implements UserDetails {

    private String name;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;

    @Column(name = "logo_url")
    private String logoUrl;
    @OneToOne(cascade = CascadeType.ALL)
    private RolePermissionEntity rolePermissionEntities;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TaskEntity> taskEntityList;

    public UserEntity(String name, String username, String password, String logoUrl, RolePermissionEntity rolePermissionEntities) {
        this.name = name;
        this.username = username;
        this.logoUrl = logoUrl;
        this.password = password;
        this.rolePermissionEntities = rolePermissionEntities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rolePermissionEntities.getAuthority();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserEntity of(UserRegisterDTO userRegisterDTO) {

        if (userRegisterDTO.isUser()) {
            RolePermissionEntity rolePermission = new RolePermissionEntity();
            rolePermission.setRoleEnum(List.of(RoleEnum.USER.name()));
            return UserEntity.builder()
                    .username(userRegisterDTO.getUsername())
                    .name(userRegisterDTO.getName())
                    .email(userRegisterDTO.getEmail())
                    .rolePermissionEntities(rolePermission)
                    .build();
        }

        return UserEntity.builder()
                .username(userRegisterDTO.getUsername())
                .name(userRegisterDTO.getName())
                .email(userRegisterDTO.getEmail())
                .rolePermissionEntities(new RolePermissionEntity(userRegisterDTO.getRole(), userRegisterDTO.getPermissions()))
                .build();
    }

    public static UserEntity from(String name, String email) {
        return UserEntity.builder()
                .name(name)
                .email(email)
                .build();
    }

}

