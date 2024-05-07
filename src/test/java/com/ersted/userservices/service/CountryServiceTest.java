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

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    void givenExistCountryId_whenFind_thenCountryIsReturned() {
        //given
        String existCountryId = CountryDataUtils.persistCountry().getId();
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
    void givenNonExistCountryId_whenFind_thenMonoEmptyIsReturned() {
        //given
        BDDMockito.given(countryRepository.findById(anyString()))
                .willReturn(Mono.empty());
        //when
        StepVerifier.create(countryService.find(anyString()))
                //then
                .expectNextCount(0)
                .verifyComplete();
        verify(countryRepository, times(1)).findById(anyString());
    }
}