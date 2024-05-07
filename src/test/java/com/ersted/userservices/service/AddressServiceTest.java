package com.ersted.userservices.service;

import com.ersted.userservices.entity.Address;
import com.ersted.userservices.entity.Country;
import com.ersted.userservices.repository.AddressRepository;
import com.ersted.userservices.utils.AddressDataUtils;
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
class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CountryService countryService;

    @InjectMocks
    private AddressService addressService;

    @Test
    @DisplayName("save transient address entity to database")
    public void givenTransientAddress_whenSave_thenPersistAddressIsReturned() {
        //given
        Address transientAddress = AddressDataUtils.transientAddress();
        Address persistAddress = AddressDataUtils.persistAddress();
        BDDMockito.given(addressRepository.save(any(Address.class)))
                .willReturn(Mono.just(persistAddress));
        //when
        StepVerifier.create(addressService.save(transientAddress))
                //then
                .expectNextMatches(address -> !address.isNew())
                .verifyComplete();
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    @DisplayName("save nullable address entity to database")
    public void givenNullableAddress_whenSave_thenMonoEmptyIsReturned() {
        //given
        Address address = null;
        //when
        StepVerifier.create(addressService.save(address))
                //then
                .expectNextCount(0)
                .verifyComplete();
        verify(addressRepository, times(0)).save(any(Address.class));
    }

    @Test
    @DisplayName("save persist address entity to database")
    public void givenPersistAddress_whenSave_thenPersistAddressIsReturnedWithoutCallingRepository() {
        //given
        Address persistAddress = AddressDataUtils.persistAddress();
        //when
        StepVerifier.create(addressService.save(persistAddress))
                //then
                .expectNextMatches(address -> !address.isNew())
                .verifyComplete();

        verify(addressRepository, times(0)).save(any(Address.class));
    }

    @Test
    @DisplayName("save transient address entity with transient country association to database")
    public void givenTransientAddressAndTransientCountry_whenSave_thenPersistAddressWithPersistCountryAreReturned() {
        //given
        Address transientAddressWithAssociations = AddressDataUtils.transientAddressWithAssociations();
        Country associationTransientCountry = transientAddressWithAssociations.getCountry();

        BDDMockito.given(addressRepository.save(any(Address.class)))
                .willReturn(Mono.just(AddressDataUtils.persistAddressWithAssociations()));
        BDDMockito.given(countryService.save(any(Country.class)))
                .willReturn(Mono.just(associationTransientCountry));
        //when
        StepVerifier.create(addressService.save(transientAddressWithAssociations))
                //then
                .expectNextMatches(address -> !address.isNew() && Objects.nonNull(address.getCountryId()))
                .verifyComplete();

        verify(addressRepository, times(1)).save(any(Address.class));
        verify(countryService, times(1)).save(any(Country.class));
    }

    @Test
    @DisplayName("save transient address entity with persist country association to database")
    public void givenTransientAddressAndPersistCountry_whenSave_thenPersistAddressWithPersistCountryAreReturned() {
        //given
        Address transientAddressWithAssociations = AddressDataUtils.transientAddressWithPersistAssociations();

        BDDMockito.given(addressRepository.save(any(Address.class)))
                .willReturn(Mono.just(AddressDataUtils.persistAddressWithAssociations()));

        //when
        StepVerifier.create(addressService.save(transientAddressWithAssociations))
                //then
                .expectNextMatches(address -> !address.isNew() && Objects.nonNull(address.getCountryId()))
                .verifyComplete();

        verify(addressRepository, times(1)).save(any(Address.class));
        verify(countryService, times(0)).save(any(Country.class));
    }

    @Test
    @DisplayName("find by id address with transients")
    public void givenAddressIdWithTransients_whenFind_thenAddressWithTransientAreReturned() {
        //given
        Address persistAddressWithAssociations = AddressDataUtils.persistAddressWithAssociations();
        String addressId = persistAddressWithAssociations.getId();
        Country transientCountry = persistAddressWithAssociations.getCountry();
        String countryId = transientCountry.getId();

        BDDMockito.given(addressRepository.findById(addressId))
                .willReturn(Mono.just(persistAddressWithAssociations));
        BDDMockito.given(countryService.find(countryId))
                .willReturn(Mono.just(transientCountry));
        //when
        StepVerifier.create(addressService.findWithTransient(addressId))
                //then
                .expectNextMatches(address -> Objects.nonNull(address) && !address.isNew()
                        && Objects.nonNull(address.getCountry()) && !address.getCountry().isNew())
                .verifyComplete();
        verify(addressRepository, times(1)).findById(addressId);
        verify(countryService, times(1)).find(countryId);
    }

    @Test
    @DisplayName("find by id non exist address")
    public void givenNonExistAddressId_whenFind_thenMonoEmptyIsReturned() {
        //given
        BDDMockito.given(addressRepository.findById(anyString()))
                .willReturn(Mono.empty());
        //when
        StepVerifier.create(addressService.findWithTransient(anyString()))
                //then
                .expectNextCount(0)
                .verifyComplete();
        verify(addressRepository, times(1)).findById(anyString());
        verify(countryService, times(0)).find(anyString());
    }

    @Test
    @DisplayName("find by id address without transients")
    public void givenAddressId_whenFind_thenAddressIsReturned() {
        //given
        Address persistAddress = AddressDataUtils.persistAddress();
        String addressId = persistAddress.getId();
        BDDMockito.given(addressRepository.findById(addressId))
                .willReturn(Mono.just(persistAddress));
        //when
        StepVerifier.create(addressService.findWithTransient(addressId))
                //then
                .expectNextMatches(address -> Objects.nonNull(address) && !address.isNew() && Objects.isNull(address.getCountry()) && Objects.isNull(address.getCountryId()))
                .verifyComplete();
    }
}