package uz.pdp.spring_boot_security_web.common.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class FieldErrorResponse {
    private String messageError;
    private String statusCode;
}
