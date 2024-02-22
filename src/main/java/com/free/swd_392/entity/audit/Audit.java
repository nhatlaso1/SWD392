package com.free.swd_392.entity.audit;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

import static org.springframework.data.jpa.domain.AbstractAuditable_.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Audit<I extends Serializable> implements Serializable {

    @CreatedBy
    @Column(name = CREATED_BY, updatable = false)
    private I createdBy;
    @CreationTimestamp(source = SourceType.DB)
    @Column(name = CREATED_DATE, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();
    @LastModifiedBy
    @Column(name = LAST_MODIFIED_BY)
    private I lastModifiedBy;
    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = LAST_MODIFIED_DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
}
