package uz.pdp.spring_boot_security_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.spring_boot_security_web.entity.TaskEntity;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity,Integer> {
    TaskEntity findByName(String name);
    TaskEntity findByIdIn(Collection<Integer> id);
    Optional<List<TaskEntity>> findAllByTopicIdId(int id);
}
