package com.mojave.model.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Base class for entities which have to be versioned.
 * By versioned we mean that those entities will have created and updated timestamp.
 * Actual tracking of this values will be delegated to Spring Data Auditing module, so you don't
 * need to update those fields manually.
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractVersional extends AbstractIdentifiable {

    @CreatedDate
    @Column(name = "created_at")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    protected LocalDateTime updatedAt;
}
