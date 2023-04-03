package uz.pdp.spring_boot_security_web.model.dto;

import lombok.Data;

@Data
public class TaskRequestDTO {

    private String name;
    private String title;
    private Integer topicId;
    private String example;
    private String methodName;
    private String firstParam;
    private String secondParam;
    private String returnParam;


}
