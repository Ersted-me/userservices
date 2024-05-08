package com.ersted.userservices.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("person.individuals")
public class Individual implements Persistable<UUID> {
    @Id
    private UUID id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String passportNumber;
    private String phoneNumber;
    private String email;
    private LocalDateTime verifiedAt;
    private LocalDateTime archivedAt;
    private String status;
    private UUID userId;

    @Transient
    private User user;

    @Override
    public boolean isNew() {
        return Objects.isNull(id);
    }
}
