package org.example.simpledms.model.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * fileName : BaseTimeEntity2
 * author : GGG
 * date : 2024-04-04
 * description : 공통 클래스 , 생성일시/수정일시/삭제일시 자동 생성, 삭제유무(소프트삭제용)
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity2 {

    private String insertTime;

    private String updateTime;
    private String deleteYn;
    private String deleteTime;

    @PrePersist
    void OnPrePersist() {
        this.insertTime = LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.deleteYn = "N";
    }

    @PreUpdate
    void OnPreUpdate() {
        this.updateTime = LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.insertTime = this.updateTime;
        this.deleteYn = "N";
    }
}

