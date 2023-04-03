package uz.pdp.spring_boot_security_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.spring_boot_security_web.entity.TestCaseEntity;

import java.util.List;
import java.util.Optional;

public interface TestCaseRepository extends JpaRepository<TestCaseEntity, Integer> {
    Optional<List<TestCaseEntity>>findAllByQuestionId(Integer questionId);
}
