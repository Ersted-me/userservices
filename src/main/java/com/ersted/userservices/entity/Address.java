package com.ersted.userservices.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("person.addresses")
public class Address implements Persistable<UUID> {
    @Id
    private UUID id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Integer countryId;
    private String address;
    private String zipCode;
    private LocalDateTime archived;
    private String city;
    private String state;

    @Transient
    private Country country;
    @Transient
    @ToString.Exclude
    private Set<User> users;

    @Override
    public boolean isNew() {
        return Objects.isNull(id);
    }
}