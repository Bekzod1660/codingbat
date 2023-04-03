package uz.pdp.spring_boot_security_web.model.dto;

import lombok.Data;

@Data
public class TopicRequestDTO {
    private String name;
    private String content;
    private String language;
}
