package com.ersted.userservices.repository;

import com.ersted.userservices.entity.Individual;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface IndividualRepository extends R2dbcRepository<Individual, UUID> {
}