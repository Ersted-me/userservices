package com.ersted.userservices.repository;

import com.ersted.userservices.entity.Invitation;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface InvitationRepository extends R2dbcRepository<Invitation, UUID> {
}
