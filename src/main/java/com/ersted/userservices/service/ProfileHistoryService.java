package com.ersted.userservices.service;

import com.ersted.userservices.entity.ProfileHistory;
import com.ersted.userservices.repository.ProfileHistoryRepository;
import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileHistoryService {
    private final ProfileHistoryRepository profileHistoryRepository;

    public Mono<ProfileHistory> save(UUID profileId, String profileType, String reason, String comment, String changedValues) {
        if (Objects.isNull(profileId)) {
            return Mono.empty();
        }
        return profileHistoryRepository.save(ProfileHistory.builder()
                .profileId(profileId)
                .profileType(profileType)
                .reason(reason)
                .comment(comment)
                .changedValues(Json.of(changedValues))
                .created(LocalDateTime.now())
                .build());
    }
}