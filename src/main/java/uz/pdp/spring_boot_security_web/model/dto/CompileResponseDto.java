package uz.pdp.spring_boot_security_web.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.spring_boot_security_web.compiler.CompileResponseStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompileResponseDto {
    private CompileResponseStatus status;
    private String message;
}
