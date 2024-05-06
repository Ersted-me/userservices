package com.ersted.userservices.repository;

import com.ersted.userservices.entity.Individual;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface IndividualRepository extends R2dbcRepository<Individual, String> {
}