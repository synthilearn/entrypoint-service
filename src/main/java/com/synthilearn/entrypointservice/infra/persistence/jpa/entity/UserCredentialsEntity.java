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

@Table("user_credentials")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCredentialsEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    @Column("customer_id")
    private UUID customerId;
    @Column("creation_date")
    private ZonedDateTime creationDate;
    @Column("updated_date")
    private ZonedDateTime updatedDate;
    private String password;
    private String email;
    private Boolean remember;
    @Column("is_locked")
    private Boolean isLocked;
    @Transient
    private boolean newRecord;

    @Override
    public boolean isNew() {
        return this.newRecord;
    }
}
