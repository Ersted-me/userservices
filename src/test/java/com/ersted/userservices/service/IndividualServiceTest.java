package com.ersted.userservices.service;

import com.ersted.userservices.entity.Individual;
import com.ersted.userservices.entity.User;
import com.ersted.userservices.repository.IndividualRepository;
import com.ersted.userservices.utils.IndividualDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IndividualServiceTest {
    @Mock
    private IndividualRepository individualRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private IndividualService individualService;

    @Test
    @DisplayName("save transient individual to database")
    public void givenTransientIndividual_whenSave_thenPersistIndividualIsReturned() {
        //given
        Individual transientIndividual = IndividualDataUtils.transientIndividual();
        BDDMockito.given(individualRepository.save(any(Individual.class)))
                .willReturn(Mono.just(IndividualDataUtils.persistIndividual()));
        //when
        StepVerifier.create(individualService.save(transientIndividual))
                //then
                .expectNextMatches(individual -> !individual.isNew())
                .verifyComplete();
        verify(individualRepository, times(1)).save(any(Individual.class));
    }

    @Test
    @DisplayName("save persist individual to database")
    public void givenPersistIndividual_whenSave_thenPersistIndividualIsReturnedWithoutCallingRepository() {
        //given
        Individual persistIndividual = IndividualDataUtils.persistIndividual();
        //when
        StepVerifier.create(individualService.save(persistIndividual))
                //then
                .expectNextMatches(individual -> !individual.isNew())
                .verifyComplete();
        verify(individualRepository, times(0)).save(any(Individual.class));
    }

    @Test
    @DisplayName("save nullable individual to database")
    public void givenNullableIndividual_whenSave_thenMonoEmptyIsReturned() {
        //given
        Individual nullableIndividual = null;
        //when
        StepVerifier.create(individualService.save(nullableIndividual))
                //then
                .expectNextCount(0)
                .verifyComplete();
        verify(individualRepository, times(0)).save(any(Individual.class));
    }

    @Test
    @DisplayName("save transient individual with transient association to database")
    public void givenTransientIndividualAndTransientAssociation_whenSave_thenPersistsAreReturned() {
        //given
        Individual transientIndividual = IndividualDataUtils.transientIndividualWithAssociation();

        BDDMockito.given(individualRepository.save(any(Individual.class)))
                .willReturn(Mono.just(IndividualDataUtils.persistIndividualWithAssociation()));

        BDDMockito.given(userService.save(transientIndividual.getUser()))
                .willReturn(Mono.just(IndividualDataUtils.persistIndividualWithAssociation().getUser()));
        //when
        StepVerifier.create(individualService.save(transientIndividual))
                //then
                .expectNextMatches(individual -> !individual.isNew() && Objects.nonNull(individual.getUserId()))
                .verifyComplete();
        verify(individualRepository, times(1)).save(any(Individual.class));
        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("save transient individual with persist association to database")
    public void givenTransientIndividualAndPersistAssociation_whenSave_thenPersistsAreReturned() {
        //given
        Individual transientIndividual = IndividualDataUtils.transientIndividualWithAssociation();
        transientIndividual.setUser(IndividualDataUtils.persistIndividualWithAssociation().getUser());

        BDDMockito.given(individualRepository.save(any(Individual.class)))
                .willReturn(Mono.just(IndividualDataUtils.persistIndividualWithAssociation()));

        //when
        StepVerifier.create(individualService.save(transientIndividual))
                //then
                .expectNextMatches(individual -> !individual.isNew() && Objects.nonNull(individual.getUserId()))
                .verifyComplete();
        verify(individualRepository, times(1)).save(any(Individual.class));
        verify(userService, times(0)).save(any(User.class));
    }
}