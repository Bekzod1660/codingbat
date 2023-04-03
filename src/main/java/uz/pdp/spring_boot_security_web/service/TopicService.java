package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.entity.LanguageEntity;
import uz.pdp.spring_boot_security_web.entity.TaskEntity;
import uz.pdp.spring_boot_security_web.entity.TestCaseEntity;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;
import uz.pdp.spring_boot_security_web.model.dto.TopicRequestDTO;
import uz.pdp.spring_boot_security_web.repository.LanguageRepository;
import uz.pdp.spring_boot_security_web.repository.TaskRepository;
import uz.pdp.spring_boot_security_web.repository.TestCaseRepository;
import uz.pdp.spring_boot_security_web.repository.TopicRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final LanguageRepository languageRepository;
    private final TaskRepository taskRepository;
    private final TestCaseRepository testCaseRepository;

    public List<TopicEntity> getList(String languageName) {
        Optional<LanguageEntity> byTitle = languageRepository.findByTitle(languageName);
        if (byTitle.isEmpty()) return null;
        return languageRepository.findByTitle(languageName).get().getTopicEntities();
    }

    public TopicEntity add(TopicRequestDTO topicRequestDTO) {
        Optional<LanguageEntity> languageOptional = languageRepository.findByTitle(topicRequestDTO.getLanguage());
        if (languageOptional.isEmpty()) {
            return null;
        }
        TopicEntity topic = TopicEntity.builder()
                .name(topicRequestDTO.getName())
                .content(topicRequestDTO.getContent())
                .languageEntity(languageOptional.get())
                .build();

        return topicRepository.save(topic);
    }

    public void delete(int id) {
        Optional<TopicEntity> byId = topicRepository.findById(id);
        if (byId.isEmpty()){
            throw new RecordNotFountException("The topic is not found");
        }
        Optional<List<TaskEntity>> allByTopicIdId = taskRepository.findAllByTopicIdId(byId.get().getId());
        if (allByTopicIdId.isPresent() && allByTopicIdId.get().size()>0){
            List<TaskEntity> taskEntities = allByTopicIdId.get();
            for (TaskEntity task:taskEntities){
                Optional<List<TestCaseEntity>> allByQuestionId = testCaseRepository.findAllByQuestionId(task.getId());
                allByQuestionId.ifPresent(testCaseRepository::deleteAll);
            }
            taskRepository.deleteAll(taskEntities);
        }
        topicRepository.deleteById(id);
    }

    public TopicEntity update(int id, TopicRequestDTO topicRequestDTO) {
        Optional<TopicEntity> byId = topicRepository.findById(id);
        if (byId.isEmpty())
            return null;
        TopicEntity topic = byId.get();
//        if (topicRequestDTO.getLanguageId() != null) {
//            Optional<LanguageEntity> byId1 = languageRepository.findById(topicRequestDTO.getLanguageId());
//            byId1.ifPresent(topic::setLanguageEntity);
//        }
        if (topicRequestDTO.getContent() != null)
            topic.setContent(topicRequestDTO.getContent());
        if (topicRequestDTO.getName() != null)
            topic.setName(topicRequestDTO.getName());
        return topicRepository.save(topic);
    }

    public TopicEntity getById(int id) {
        Optional<TopicEntity> byId = topicRepository.findById(id);
        return byId.orElse(null);
    }
}
