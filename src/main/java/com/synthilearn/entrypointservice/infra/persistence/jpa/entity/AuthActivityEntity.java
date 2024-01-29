package com.synthilearn.entrypointservice.infra.persistence.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;
import java.util.UUID;

@Table("auth_activity")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthActivityEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    @Column("credentials_id")
    private UUID credentialId;
    @Column("creation_date")
    private ZonedDateTime creationDate;
    private String device;
    private String ip;
    @Transient
    private boolean newRecord;

    @Override
    public boolean isNew() {
        return this.newRecord;
    }
}
