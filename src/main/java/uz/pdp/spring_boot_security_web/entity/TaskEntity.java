package uz.pdp.spring_boot_security_web.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class TaskEntity extends BaseEntity {
    private String name;
    private String title;
    private String tickIcon;
    private String example;
    private String methodAndParams;
    @ManyToOne
    TopicEntity topicId;
}
