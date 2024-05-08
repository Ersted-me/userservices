package com.ersted.userservices.repository;

import com.ersted.userservices.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface UserRepository extends R2dbcRepository<User, UUID> {
}