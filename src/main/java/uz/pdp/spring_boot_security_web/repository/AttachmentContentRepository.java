package uz.pdp.spring_boot_security_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.spring_boot_security_web.entity.AttachmentContentEntity;

import java.util.Optional;

public interface AttachmentContentRepository extends JpaRepository<AttachmentContentEntity,Integer> {

    Optional<AttachmentContentEntity> findByAttachmentId(Integer attachment_id);
}
