package com.ersted.userservices.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("person.profile_history")
public class ProfileHistory implements Persistable<UUID> {
    @Id
    private UUID id;
    private LocalDateTime created;
    private UUID profileId;
    private String profileType;
    private String reason;
    private String comment;
    private String changedValues;

    @Transient
    private User user;

    @Override
    public boolean isNew() {
        return Objects.isNull(id);
    }
}