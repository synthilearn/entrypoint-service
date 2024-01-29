package com.synthilearn.entrypointservice.infra.persistence.jpa.entity;

import com.synthilearn.entrypointservice.domain.VerificationOperation;
import com.synthilearn.entrypointservice.domain.VerificationStatus;
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

@Table("email_verification_info")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailVerificationInfoEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    @Column("credentials_id")
    private UUID credentialsId;
    @Column("creation_date")
    private ZonedDateTime creationDate;
    @Column("last_updated_date")
    private ZonedDateTime updatedDate;
    @Column("valid_to")
    private ZonedDateTime validTo;
    @Column("email_otp")
    private String emailOtp;
    private String email;
    private VerificationStatus status;
    @Column("left_attempts")
    private Integer leftAttempts;
    private VerificationOperation operation;
    @Transient
    private boolean newRecord;

    @Override
    public boolean isNew() {
        return this.newRecord;
    }
}
