package com.ersted.userservices.service;

import com.ersted.userservices.entity.Country;
import com.ersted.userservices.repository.CountryRepository;
import com.ersted.userservices.utils.CountryDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    @Test
    @DisplayName("Save transient country entity to Database")
    public void givenTransientCountry_whenSave_thenCountryIsReturned() {
        //given
        Country transientCountry = CountryDataUtils.transientCountry();
        Country persistCountry = CountryDataUtils.persistCountry();
        BDDMockito.given(countryRepository.save(any(Country.class)))
                .willReturn(Mono.just(persistCountry));
        //when
        StepVerifier.create(countryService.save(transientCountry))
                //then
                .expectNextMatches(country -> !country.isNew())
                .verifyComplete();
        verify(countryRepository, times(1)).save(any(Country.class));
    }

    @Test
    @DisplayName("Save nullable country entity to database")
    public void givenNullableCountry_whenSave_thenMonoEmptyIsReturned() {
        //given
        Country country = null;
        //when
        StepVerifier.create(countryService.save(country))
                //then
                .expectNextCount(0)
                .verifyComplete();
        verify(countryRepository, times(0)).save(any(Country.class));
    }

    @Test
    @DisplayName("Save persist country entity to database")
    public void givenPersistCountry_whenSave_thenPersistIsReturnedWithoutCallingRepository() {
        //given
        Country persistCountry = CountryDataUtils.persistCountry();
        //when
        StepVerifier.create(countryService.save(persistCountry))
                //then
                .expectNextMatches(country -> !country.isNew())
                .verifyComplete();
        verify(countryRepository, times(0)).save(any(Country.class));
    }

    @Test
    @DisplayName("find by id exist country functionality")
    void givenExistCountryId_whenFind_thenCountryIsReturned() {
        //given
        Integer existCountryId = CountryDataUtils.persistCountry().getId();
        BDDMockito.given(countryRepository.findById(existCountryId))
                .willReturn(Mono.just(CountryDataUtils.persistCountry()));
        //when
        StepVerifier.create(countryService.find(existCountryId))
                //then
                .expectNextMatches(country -> Objects.nonNull(country) && !country.isNew())
                .verifyComplete();
        verify(countryRepository, times(1)).findById(existCountryId);
    }

    @Test
    @DisplayName("find by id non exist country functionality")
    void givenNonExistCountryId_whenFind_thenMonoEmptyIsReturned() {
        //given
        BDDMockito.given(countryRepository.findById(anyInt()))
                .willReturn(Mono.empty());
        //when
        StepVerifier.create(countryService.find(anyInt()))
                //then
                .expectNextCount(0)
                .verifyComplete();
        verify(countryRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("update country by id")
    void givenCountryAndId_whenUpdate_thenUpdatedCountryIsReturned() {
        //given
        Country newCountry = new Country(0, LocalDateTime.now(), LocalDateTime.now(), "The United States of America", "US", "USA", "UPDATED", null);
        Country old = CountryDataUtils.persistCountry();
        Country updatedCountry = new Country(1, old.getCreated(), LocalDateTime.now(), newCountry.getName(), newCountry.getAlpha2(), newCountry.getAlpha3(), newCountry.getStatus(), null);
        Integer existCountryId = old.getId();
        BDDMockito.given(countryRepository.findById(existCountryId))
                .willReturn(Mono.just(old));

        BDDMockito.given(countryRepository.save(newCountry))
                .willReturn(Mono.just(updatedCountry));
        //when
        StepVerifier.create(countryService.update(newCountry, existCountryId))
                //then
                .expectNextMatches(country -> Objects.nonNull(country) && !country.isNew() && updatedCountry.equals(country))
                .verifyComplete();
        verify(countryRepository, times(1)).findById(existCountryId);
        verify(countryRepository, times(1)).save(any(Country.class));
    }

    @Test
    @DisplayName("when update country setters will be called")
    void givenCountryAndId_whenUpdate_thenSettersWillBeCalled() {
        //given
        Country newCountry = mock(Country.class);
        Country old = CountryDataUtils.persistCountry();
        Country updatedCountry = new Country(1, old.getCreated(), LocalDateTime.now(), newCountry.getName(), newCountry.getAlpha2(), newCountry.getAlpha3(), newCountry.getStatus(), null);
        Integer existCountryId = old.getId();
        BDDMockito.given(countryRepository.findById(existCountryId))
                .willReturn(Mono.just(old));

        BDDMockito.given(countryRepository.save(any(Country.class)))
                .willReturn(Mono.just(updatedCountry));
        //when
        StepVerifier.create(countryService.update(newCountry, existCountryId))
                //then
                .expectNextCount(1)
                .verifyComplete();
        verify(newCountry, times(1)).setId(anyInt());
        verify(newCountry, times(1)).setCreated(old.getCreated());
        verify(newCountry, times(1)).setUpdated(any(LocalDateTime.class));
        verify(newCountry, times(1)).setStatus(old.getStatus());
    }

    @Test
    @DisplayName("update called with null id and country")
    void givenNullableCountryAndId_whenUpdate_thenMonoEmptyIsReturned() {
        //given

        //when
        StepVerifier.create(countryService.update(null, null))
                //then
                .expectNextCount(0)
                .verifyComplete();
        verify(countryRepository, times(0)).findById(anyInt());
        verify(countryRepository, times(0)).save(any(Country.class));
    }
}
