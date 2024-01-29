package com.synthilearn.entrypointservice.infra.persistence.jpa.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;
import java.util.UUID;

@Table("token")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenEntity implements Persistable<UUID> {
    private UUID id;
    @Column("customer_id")
    private UUID customerId;
    private String payload;
    @Column("creation_date")
    private ZonedDateTime creationDate;
    @Column("expired_at")
    private ZonedDateTime expiredAt;
    @Column("is_revoked")
    private Boolean isRevoked;

    @Transient
    private boolean newRecord;

    @Override
    public boolean isNew() {
        return newRecord;
    }
}
