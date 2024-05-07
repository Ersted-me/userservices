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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("person.individuals")
public class Individual implements Persistable<String> {
    @Id
    private String id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String passportNumber;
    private String phoneNumber;
    private String email;
    private LocalDateTime verifiedAt;
    private LocalDateTime archivedAt;
    private String status;
    private String userId;

    @Transient
    private User user;

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(id);
    }
}
