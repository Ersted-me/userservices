package com.ersted.userservices.repository;

import com.ersted.userservices.entity.ProfileHistory;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface ProfileHistoryRepository extends R2dbcRepository<ProfileHistory, UUID> {
}
