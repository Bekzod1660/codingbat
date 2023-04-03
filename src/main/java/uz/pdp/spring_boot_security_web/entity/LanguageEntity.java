package uz.pdp.spring_boot_security_web.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.spring_boot_security_web.model.dto.LanguageRequestDTO;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class LanguageEntity extends BaseEntity {
    @Column(unique = true)
    private String title;
    @OneToMany( mappedBy = "languageEntity",cascade = CascadeType.REMOVE)
    private List<TopicEntity> topicEntities;

    public LanguageEntity of(LanguageRequestDTO languageRequestDTO) {
        return LanguageEntity.builder()
                .title(languageRequestDTO.getTitle())
                .build();
    }
}
