package com.ersted.userservices.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("person.countries")
public class Country implements Persistable<Integer> {
    @Id
    private Integer id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String name;
    private String alpha2;
    private String alpha3;
    private String status;

    @Transient
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Address> addresses;

    @Override
    public boolean isNew() {
        return Objects.isNull(id);
    }
}