package uz.pdp.spring_boot_security_web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class AuditConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditListener();
    }
}
