package uz.pdp.spring_boot_security_web.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.pdp.spring_boot_security_web.entity.UserEntity;

import java.util.Optional;

public class AuditListener implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            UserEntity userEntity=(UserEntity) authentication.getPrincipal();
            return Optional.of(userEntity.getUsername());
        }
        return Optional.empty();
    }
}
