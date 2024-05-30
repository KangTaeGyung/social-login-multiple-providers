package org.example.simpledms.model.common;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author : GGG
 * @fileName : BaseTimeEntity
 * @since : 2024-04-02
 * description : 공통 클래스 , 생성일시/수정일시/삭제일시 자동 생성, 삭제유무(소프트삭제용)
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity2 {

    @Column(updatable = false)
    private String insertTime;

    private String updateTime;
    private String deleteYn;
    private String deleteTime;

    @PrePersist
    void OnPrePersist() {
        this.insertTime = getCurrentTime();
        this.deleteYn = "N";
    }

    @PreUpdate
    void OnPreUpdate() {
        this.updateTime = getCurrentTime();
        this.deleteYn = "N";
    }

    private String getCurrentTime() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}

