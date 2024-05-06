package com.ersted.userservices.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("users")
public class User implements Persistable<String> {
    @Id
    private String id;
    private String secretKey;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String firstName;
    private String lastName;
    private LocalDateTime verifiedAt;
    private LocalDateTime archivedAt;
    private String status;
    private Boolean filled;
    private String addressId;

    @Transient
    private Address address;
    @Transient
    @ToString.Exclude
    private Set<Individual> individuals;

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(id);
    }
}