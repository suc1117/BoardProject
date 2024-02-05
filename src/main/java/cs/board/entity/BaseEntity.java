package cs.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    @CreationTimestamp          // 생성 시간
    @Column(updatable = false)  // 수정 시 관여 X
    private LocalDateTime createdTime;

    @UpdateTimestamp            // 수정 시간
    @Column(insertable = false) // 입력 시 관여 X
    private LocalDateTime updatedTime;
}
