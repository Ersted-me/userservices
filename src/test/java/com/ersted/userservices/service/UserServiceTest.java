package com.ersted.userservices.service;

import com.ersted.userservices.entity.Address;
import com.ersted.userservices.entity.User;
import com.ersted.userservices.repository.UserRepository;
import com.ersted.userservices.utils.UserDataUtils;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AddressService addressService;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("save transient user to database")
    public void givenTransientUser_whenSave_thenPersistUserIsReturned() {
        //given
        User transientUser = UserDataUtils.transientUser();
        BDDMockito.given(userRepository.save(any(User.class)))
                .willReturn(Mono.just(UserDataUtils.persistUser()));
        //when
        StepVerifier.create(userService.save(transientUser))
                //then
                .expectNextMatches(user -> !user.isNew())
                .verifyComplete();
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("save nullable user to database")
    public void givenNullableUser_whenSave_thenPersistUserIsReturned() {
        //given
        User nullableUser = null;
        //when
        StepVerifier.create(userService.save(nullableUser))
                //then
                .expectNextCount(0)
                .verifyComplete();
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    @DisplayName("save persist user to database")
    public void givenPersistUser_whenSave_thenPersistUserIsReturnedWithoutCallingRepository() {
        //given
        User persistUser = UserDataUtils.persistUser();
        //when
        StepVerifier.create(userService.save(persistUser))
                //then
                .expectNextMatches(user -> !user.isNew())
                .verifyComplete();
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    @DisplayName("save transient user with transient associations to database")
    public void givenTransientUserAndTransientAssociations_whenSave_thenPersistUserAndPersistAssociationsAreReturned() {
        //given
        User transientUserWithAssociations = UserDataUtils.transientUserWithAssociations();
        Address transientAddress = transientUserWithAssociations.getAddress();

        BDDMockito.given(userRepository.save(any(User.class)))
                .willReturn(Mono.just(UserDataUtils.persistUserWithAssociations()));

        BDDMockito.given(addressService.save(transientAddress))
                .willReturn(Mono.just(UserDataUtils.persistUserWithAssociations().getAddress()));
        //when
        StepVerifier.create(userService.save(transientUserWithAssociations))
                //then
                .expectNextMatches(user -> !user.isNew() && Objects.nonNull(user.getAddressId()))
                .verifyComplete();
        verify(userRepository, times(1)).save(any(User.class));
        verify(addressService, times(1)).save(any(Address.class));
    }

    @Test
    @DisplayName("save transient user with persist associations to database")
    public void givenTransientUserAndPersistAssociations_whenSave_thenPersistUserAndPersistAssociationsAreReturned() {
        //given
        User transientUserWithAssociations = UserDataUtils.transientUserWithAssociations();
        User persistUserWithAssociations = UserDataUtils.persistUserWithAssociations();

        transientUserWithAssociations.setAddress(persistUserWithAssociations.getAddress());

        BDDMockito.given(userRepository.save(any(User.class)))
                .willReturn(Mono.just(persistUserWithAssociations));

        //when
        StepVerifier.create(userService.save(transientUserWithAssociations))
                //then
                .expectNextMatches(user -> !user.isNew() && Objects.nonNull(user.getAddressId()))
                .verifyComplete();
        verify(userRepository, times(1)).save(any(User.class));
        verify(addressService, times(0)).save(any(Address.class));
    }
}