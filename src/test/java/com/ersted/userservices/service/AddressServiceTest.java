package com.ersted.userservices.service;

import com.ersted.userservices.entity.Address;
import com.ersted.userservices.entity.Country;
import com.ersted.userservices.repository.AddressRepository;
import com.ersted.userservices.utils.AddressDataUtils;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
        UUID addressId = persistAddressWithAssociations.getId();
        Country transientCountry = persistAddressWithAssociations.getCountry();
        Integer countryId = transientCountry.getId();

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
        UUID randomUUID = UUID.fromString("7064f21b-db21-4ef7-acf7-ac68b563b908");
        //given
        BDDMockito.given(addressRepository.findById(randomUUID))
                .willReturn(Mono.empty());
        //when
        StepVerifier.create(addressService.findWithTransient(randomUUID))
                //then
                .expectNextCount(0)
                .verifyComplete();
        verify(addressRepository, times(1)).findById(any(UUID.class));
        verify(countryService, times(0)).find(anyInt());
    }

    @Test
    @DisplayName("find by id address without transients")
    public void givenAddressId_whenFind_thenAddressIsReturned() {
        //given
        Address persistAddress = AddressDataUtils.persistAddress();
        UUID addressId = persistAddress.getId();
        BDDMockito.given(addressRepository.findById(addressId))
                .willReturn(Mono.just(persistAddress));
        //when
        StepVerifier.create(addressService.findWithTransient(addressId))
                //then
                .expectNextMatches(address -> Objects.nonNull(address) && !address.isNew() && Objects.isNull(address.getCountry()) && Objects.isNull(address.getCountryId()))
                .verifyComplete();
        verify(addressRepository, times(1)).findById(any(UUID.class));
        verify(countryService, times(0)).find(anyInt());
    }

    @Test
    @DisplayName("update null address or empty oldAddressId")
    void givenNothing_whenUpdate_thenMonoIsReturned() {
        //given
        //when
        StepVerifier.create(addressService.update(null,null))
        //then
                .expectNextCount(0)
                .verifyComplete();
        verify(addressRepository, times(0)).findById(any(UUID.class));
        verify(addressRepository, times(0)).save(any(Address.class));
    }

    @Test
    @DisplayName("update address")
    void givenAddress_whenUpdate_thenUpdatedAddressIsReturned() {
        //given
        Address oldAddress = AddressDataUtils.persistAddress();
        Address toUpdateAddress = AddressDataUtils.persistAddress();
        toUpdateAddress.setAddress("new address");

        assert oldAddress.getId() != null;
        BDDMockito.given(addressRepository.findById(oldAddress.getId()))
                        .willReturn(Mono.just(oldAddress));
        BDDMockito.given(addressRepository.save(toUpdateAddress))
                        .willReturn(Mono.just(toUpdateAddress));
        //when
        StepVerifier.create(addressService.update(toUpdateAddress,oldAddress.getId()))
                //then
                .expectNextMatches(address -> !address.isNew() && "new address".equals(address.getAddress()))
                .verifyComplete();
        verify(addressRepository, times(1)).findById(any(UUID.class));
        verify(addressRepository, times(1)).save(any(Address.class));
        verify(countryService, times(0)).update(any(Country.class), anyInt());
    }

    @Test
    @DisplayName("update address setters calling check")
    void givenAddress_whenUpdate_thenSettersAreCalled() {
        //given
        Address oldAddress = AddressDataUtils.persistAddress();
        Address toUpdateAddress = mock(Address.class);

        assert oldAddress.getId() != null;
        BDDMockito.given(addressRepository.findById(oldAddress.getId()))
                .willReturn(Mono.just(oldAddress));
        BDDMockito.given(addressRepository.save(any(Address.class)))
                .willReturn(Mono.just(toUpdateAddress));
        //when
        StepVerifier.create(addressService.update(toUpdateAddress, oldAddress.getId()))
              //then
                .expectNextCount(1)
                .verifyComplete();

        verify(toUpdateAddress, times(1)).setId(oldAddress.getId());
        verify(toUpdateAddress, times(1)).setUpdated(any(LocalDateTime.class));
        verify(toUpdateAddress, times(1)).setCreated(oldAddress.getUpdated());
        verify(toUpdateAddress, times(1)).setArchived(oldAddress.getArchived());
    }

    @Test
    @DisplayName("update address with transients")
    void givenAddressWithTransients_whenUpdate_thenAddressAndTransientsAreUpdated() {
        //given
        Address oldAddress = AddressDataUtils.persistAddressWithAssociations();
        UUID oldAddressId = oldAddress.getId();
        Address toUpdateAddress = AddressDataUtils.persistAddressWithAssociations();
        toUpdateAddress.setAddress("new address");

        Country countryToUpdate = toUpdateAddress.getCountry();
        Integer countryId = toUpdateAddress.getCountryId();

        BDDMockito.given(addressRepository.findById(oldAddressId))
                .willReturn(Mono.just(oldAddress));
        BDDMockito.given(addressRepository.save(toUpdateAddress))
                .willReturn(Mono.just(toUpdateAddress));

        BDDMockito.given(countryService.update(countryToUpdate, countryId))
                        .willReturn(Mono.just(countryToUpdate));
        //when
        StepVerifier.create(addressService.update(toUpdateAddress, oldAddress.getId()))
                //then
                .expectNextCount(1)
                .verifyComplete();

        verify(addressRepository, times(1)).findById(any(UUID.class));
        verify(addressRepository, times(1)).save(toUpdateAddress);
        verify(countryService, times(1)).update(countryToUpdate, countryId);
    }

    @Test
    @DisplayName("update address with transients")
    void givenAddressWithNewTransients_whenUpdate_thenAddressUpdatedTransientSaved() {
        //given
        Address oldAddress = AddressDataUtils.persistAddress();
        UUID oldAddressId = oldAddress.getId();
        Address toUpdateAddress = AddressDataUtils.persistAddressWithAssociations();
        toUpdateAddress.setAddress("new address");

        Country countryToUpdate = toUpdateAddress.getCountry();

        assert oldAddressId != null;
        BDDMockito.given(addressRepository.findById(oldAddressId))
                .willReturn(Mono.just(oldAddress));
        BDDMockito.given(addressRepository.save(toUpdateAddress))
                .willReturn(Mono.just(toUpdateAddress));

        BDDMockito.given(countryService.save(countryToUpdate))
                .willReturn(Mono.just(countryToUpdate));
        //when
        StepVerifier.create(addressService.update(toUpdateAddress, oldAddress.getId()))
                //then
                .expectNextCount(1)
                .verifyComplete();

        verify(addressRepository, times(1)).findById(any(UUID.class));
        verify(addressRepository, times(1)).save(toUpdateAddress);
        verify(countryService, times(1)).save(countryToUpdate);
    }

    @Test
    @DisplayName("update address with transient setters calling check")
    void givenAddressWithTransient_whenUpdate_thenSettersAreCalled() {
        //given
        Address oldAddress = AddressDataUtils.persistAddress();
        Address toUpdateAddress = mock(Address.class);
        Country transientCountry = CountryDataUtils.transientCountry();
        Country persistCountry = CountryDataUtils.persistCountry();

        assert oldAddress.getId() != null;
        BDDMockito.given(addressRepository.findById(oldAddress.getId()))
                .willReturn(Mono.just(oldAddress));
        BDDMockito.given(addressRepository.save(any(Address.class)))
                .willReturn(Mono.just(toUpdateAddress));
        BDDMockito.given(toUpdateAddress.getCountry())
                        .willReturn(transientCountry);
        BDDMockito.given(countryService.save(transientCountry))
                .willReturn(Mono.just(persistCountry));
        //when
        StepVerifier.create(addressService.update(toUpdateAddress, oldAddress.getId()))
                //then
                .expectNextCount(1)
                .verifyComplete();

        verify(toUpdateAddress, times(1)).setId(oldAddress.getId());
        verify(toUpdateAddress, times(1)).setUpdated(any(LocalDateTime.class));
        verify(toUpdateAddress, times(1)).setCreated(oldAddress.getUpdated());
        verify(toUpdateAddress, times(1)).setArchived(oldAddress.getArchived());
        verify(toUpdateAddress, times(1)).setCountry(persistCountry);
        verify(toUpdateAddress, times(1)).setCountryId(persistCountry.getId());
        verify(countryService, times(1)).save(transientCountry);
    }

}