package com.ersted.userservices.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("person.merchant_members_invitations")
public class Invitation implements Persistable<UUID> {
    @Id
    private UUID id;
    private LocalDateTime created;
    private LocalDateTime expires;
    private UUID merchantId;
    private String firstName;
    private String lastName;
    private String email;
    private String status;

    @Override
    public boolean isNew() {
        return Objects.isNull(id);
    }
}