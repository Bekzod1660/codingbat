package uz.pdp.spring_boot_security_web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class TopicEntity extends BaseEntity {

    private String name;
    private String content;
    @ManyToOne()
    private LanguageEntity languageEntity;
    @OneToMany(mappedBy = "topicId")
    private List<TaskEntity> taskEntityList;
}
