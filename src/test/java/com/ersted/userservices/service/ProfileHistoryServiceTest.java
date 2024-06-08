package com.ersted.userservices.service;

import com.ersted.userservices.entity.ProfileHistory;
import com.ersted.userservices.repository.ProfileHistoryRepository;
import com.ersted.userservices.utils.ProfileHistoryDataUtils;
import io.r2dbc.postgresql.codec.Json;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProfileHistoryServiceTest {
    @Mock
    private ProfileHistoryRepository profileHistoryRepository;
    @InjectMocks
    private ProfileHistoryService profileHistoryService;

    @Test
    @DisplayName("save profile history")
    public void givenDataForSave_whenSave_thenProfileHistoryIsReturned() {
        //given
        ProfileHistory persist = ProfileHistoryDataUtils.persist(UUID.randomUUID());
        BDDMockito.given(profileHistoryRepository.save(any(ProfileHistory.class)))
                .willReturn(Mono.just(persist));
        //when
        StepVerifier.create(profileHistoryService.save(persist.getProfileId(),persist.getProfileType(),persist.getReason(),persist.getComment(), persist.getChangedValues().asString()))
        //then
                .expectNextMatches(history->!history.isNew() && persist.equals(history))
                .verifyComplete();
        verify(profileHistoryRepository, times(1)).save(any(ProfileHistory.class));
    }

    @Test
    @DisplayName("save profile history")
    public void givenNullableProfileId_whenSave_thenMonoEmptyIsReturned() {
        //given
        ProfileHistory persist = ProfileHistoryDataUtils.persist(null);
        //when
        StepVerifier.create(profileHistoryService.save(persist.getProfileId(),persist.getProfileType(),persist.getReason(),persist.getComment(),persist.getChangedValues().asString()))
                //then
                .expectNextCount(0)
                .verifyComplete();
        verify(profileHistoryRepository, times(0)).save(any(ProfileHistory.class));
    }
}