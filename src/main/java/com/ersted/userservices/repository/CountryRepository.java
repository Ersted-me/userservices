package com.ersted.userservices.repository;

import com.ersted.userservices.entity.Country;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CountryRepository extends R2dbcRepository<Country, String> {
}