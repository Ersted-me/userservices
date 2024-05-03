package com.ersted.userservices.repository;

import com.ersted.userservices.entity.Country;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CountryRepository extends ReactiveCrudRepository<Country, String> {
}