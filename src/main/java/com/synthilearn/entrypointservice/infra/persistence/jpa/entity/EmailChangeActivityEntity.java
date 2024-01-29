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

import java.util.UUID;

@Table("email_change_activity")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailChangeActivityEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    @Column("old_email")
    private String oldEmail;
    @Column("new_email")
    private String newEmail;
    @Transient
    private boolean newRecord;

    @Override
    public boolean isNew() {
        return this.newRecord;
    }
}
