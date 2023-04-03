package uz.pdp.spring_boot_security_web.model.dto.receive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.spring_boot_security_web.entity.role.PermissionEnum;
import uz.pdp.spring_boot_security_web.entity.role.RoleEnum;
import uz.pdp.spring_boot_security_web.entity.role.RolePermissionEntity;

import java.io.File;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class UserRegisterDTO {
    private String username;
    private String password;
    private String name;
    private String email;

    private MultipartFile image;
    private List<String> role;
    private List<String> permissions;

    @JsonIgnore
    public boolean isUser(){
        return role == null && permissions == null;
    }



}
