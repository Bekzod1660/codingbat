package uz.pdp.spring_boot_security_web.model.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String username;
    private String password;
}
