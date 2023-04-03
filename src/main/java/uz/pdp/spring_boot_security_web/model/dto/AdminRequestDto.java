package uz.pdp.spring_boot_security_web.model.dto;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@ToString
public class AdminRequestDto {

    private String name;
    private String username;
    private String password;
    @Column(name = "logo_url")
    private String logoUrl;
    private String permission;
    private String roles;


}

