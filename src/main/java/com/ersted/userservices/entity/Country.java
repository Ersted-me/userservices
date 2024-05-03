package com.ersted.userservices.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Table("countries")
public class Country {
    @Id
    private String id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String name;
    private String alpha2;
    private String alpha3;
    private String status;

    @Transient
    @ToString.Exclude
    private Set<Address> addresses;
}