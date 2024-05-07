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
@Table("person.addresses")
public class Address implements Persistable<String> {
    @Id
    private String id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String countryId;
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
        return !StringUtils.hasText(id);
    }
}