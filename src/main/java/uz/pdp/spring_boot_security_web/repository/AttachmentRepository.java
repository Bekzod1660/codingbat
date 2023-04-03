package uz.pdp.spring_boot_security_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.spring_boot_security_web.entity.AttachmentEntity;

import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<AttachmentEntity,Integer> {
    Optional<AttachmentEntity> findByName(String name);
}
