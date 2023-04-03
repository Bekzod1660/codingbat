package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.entity.LanguageEntity;
import uz.pdp.spring_boot_security_web.entity.TaskEntity;
import uz.pdp.spring_boot_security_web.entity.TestCaseEntity;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;
import uz.pdp.spring_boot_security_web.model.dto.LanguageRequestDTO;
import uz.pdp.spring_boot_security_web.repository.LanguageRepository;
import uz.pdp.spring_boot_security_web.repository.TaskRepository;
import uz.pdp.spring_boot_security_web.repository.TestCaseRepository;
import uz.pdp.spring_boot_security_web.repository.TopicRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageRepository languageRepository;
    private final TaskRepository taskRepository;
    private final TopicRepository topicRepository;
    private final TestCaseRepository testCaseRepository;

    public List<LanguageEntity> languageEntityList() {
        return languageRepository.findAll();
    }

    public LanguageEntity getLanguage(String name) {
        Optional<LanguageEntity> optionalSubjectEntity = languageRepository.findByTitle(name);
        return optionalSubjectEntity.orElse(null);
    }

    public LanguageEntity addLanguage(LanguageRequestDTO languageRequestDTO) {
        Optional<LanguageEntity> optional = languageRepository.findByTitle(languageRequestDTO.getTitle());
        if (optional.isPresent()) {
            throw new IllegalArgumentException(String.format("The %s already exist", languageRequestDTO.getTitle()));
        }
        LanguageEntity language = LanguageEntity.builder()
                .title(languageRequestDTO.getTitle())
                .build();

        return languageRepository.save(language);
    }

    public void delete(int id) {
        Optional<LanguageEntity> optional = languageRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RecordNotFountException("The language is not found");
        }

        Optional<List<TopicEntity>> byLanguageEntity_id = topicRepository.findByLanguageEntity_Id(optional.get().getId());
        for (TopicEntity topic : byLanguageEntity_id.get()) {
            Optional<List<TaskEntity>> allByTopicIdId = taskRepository.findAllByTopicIdId(topic.getId());
            if (allByTopicIdId.isPresent() && allByTopicIdId.get().size() > 0) {
                List<TaskEntity> taskEntities = allByTopicIdId.get();
                for (TaskEntity task : taskEntities) {
                    Optional<List<TestCaseEntity>> allByQuestionId = testCaseRepository.findAllByQuestionId(task.getId());
                    allByQuestionId.ifPresent(testCaseRepository::deleteAll);
                }
                taskRepository.deleteAll(taskEntities);
            }
        }
        topicRepository.deleteAll(byLanguageEntity_id.get());
        languageRepository.deleteById(id);
    }

    public LanguageEntity update(int id, LanguageRequestDTO newLanguageRequestDTO) {
        Optional<LanguageEntity> byTitle = languageRepository.findById(id);
        if (byTitle.isEmpty()) {
            throw new RecordNotFountException("The language is not found");
        }
        LanguageEntity language = byTitle.get();
        language.setTitle(newLanguageRequestDTO.getTitle());
        return languageRepository.save(language);
    }

    public LanguageEntity getById(int id) {
        Optional<LanguageEntity> byId = languageRepository.findById(id);
        return byId.orElse(null);
    }
}
