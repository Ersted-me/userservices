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

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    @DisplayName("find by id user with transients")
    void givenUserWithTransient_whenFind_thenUserWithTransientIsReturned() {
        //given
        User persistUserWithAssociations = UserDataUtils.persistUserWithAssociations();
        UUID userId = persistUserWithAssociations.getId();
        Address address = persistUserWithAssociations.getAddress();
        UUID addressId = address.getId();
        BDDMockito.given(userRepository.findById(userId))
                .willReturn(Mono.just(persistUserWithAssociations));
        BDDMockito.given(addressService.findWithTransient(addressId))
                .willReturn(Mono.just(address));
        //when
        StepVerifier.create(userService.findWithTransient(userId))
                //then
                .expectNextMatches(user -> Objects.nonNull(user) && !user.isNew()
                        && Objects.nonNull(user.getAddress()) && !user.getAddress().isNew())
                .verifyComplete();
        verify(userRepository, times(1)).findById(userId);
        verify(addressService, times(1)).findWithTransient(addressId);
    }

    @Test
    @DisplayName("find by id user")
    void givenUser_whenFind_thenUserIsReturned() {
        //given
        User persistUser = UserDataUtils.persistUser();
        UUID userId = persistUser.getId();
        BDDMockito.given(userRepository.findById(userId))
                .willReturn(Mono.just(persistUser));
        //when
        StepVerifier.create(userService.findWithTransient(userId))
                //then
                .expectNextMatches(user -> Objects.nonNull(user) && !user.isNew() && Objects.isNull(user.getAddress()))
                .verifyComplete();
        verify(userRepository, times(1)).findById(userId);
        verify(addressService, times(0)).findWithTransient(any(UUID.class));
    }

    @Test
    @DisplayName("find by id non exist user")
    void givenNonExistUser_whenFind_thenMonoEmptyIsReturned() {
        UUID randomUUID = UUID.fromString("7064f21b-db21-4ef7-acf7-ac68b563b908");
        //given
        BDDMockito.given(userRepository.findById(randomUUID))
                .willReturn(Mono.empty());
        //when
        StepVerifier.create(userService.findWithTransient(randomUUID))
                //then
                .expectNextCount(0)
                .verifyComplete();
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(addressService, times(0)).findWithTransient(any(UUID.class));
    }

    @Test
    @DisplayName("update user")
    public void givenUser_whenUpdate_thenUserIsReturned() {
        //given
        User oldUser = UserDataUtils.persistUser();
        User userToUpdate = UserDataUtils.persistUser();
        userToUpdate.setFirstName("new name");

        BDDMockito.given(userRepository.findById(oldUser.getId()))
                .willReturn(Mono.just(oldUser));
        BDDMockito.given(userRepository.save(userToUpdate))
                .willReturn(Mono.just(userToUpdate));
        //when
        StepVerifier.create(userService.update(userToUpdate, oldUser.getId()))
                //then
                .expectNextMatches(user -> !user.isNew() && "new name".equals(user.getFirstName()))
                .verifyComplete();
        verify(userRepository, times(1)).findById(oldUser.getId());
        verify(userRepository, times(1)).save(userToUpdate);
    }

    @Test
    @DisplayName("update null user or null id")
    public void givenNothing_whenUpdate_thenMonoEmptyIsReturned() {
        //given
        //when
        StepVerifier.create(userService.update(null, null))
                //then
                .expectNextCount(0)
                .verifyComplete();
        verify(userRepository, times(0)).findById(any(UUID.class));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    @DisplayName("update user setters calling check")
    public void givenUser_whenUpdate_thenSettersIsCalled() {
        //given
        User oldUser = UserDataUtils.persistUser();
        User userToUpdate = mock(User.class);

        BDDMockito.given(userRepository.findById(oldUser.getId()))
                .willReturn(Mono.just(oldUser));
        BDDMockito.given(userRepository.save(userToUpdate))
                .willReturn(Mono.just(userToUpdate));
        //when
        StepVerifier.create(userService.update(userToUpdate, oldUser.getId()))
                //then
                .expectNextCount(1)
                .verifyComplete();
        verify(userToUpdate, times(1)).setId(oldUser.getId());
        verify(userToUpdate, times(1)).setUpdated(any(LocalDateTime.class));
        verify(userToUpdate, times(1)).setCreated(oldUser.getCreated());
        verify(userToUpdate, times(1)).setArchivedAt(oldUser.getArchivedAt());
        verify(userToUpdate, times(1)).setVerifiedAt(oldUser.getVerifiedAt());
    }

    @Test
    @DisplayName("update user with associations")
    public void givenUserWithAssociation_whenUpdate_thenUserAndAssociationAreUpdated() {
        //given
        User oldUser = UserDataUtils.persistUserWithAssociations();

        User userToUpdate = UserDataUtils.transientUserWithAssociations();
        userToUpdate.setFirstName("new name");

        User updatedUser = UserDataUtils.persistUserWithAssociations();
        updatedUser.setFirstName("new name");

        BDDMockito.given(userRepository.findById(oldUser.getId()))
                .willReturn(Mono.just(oldUser));

        BDDMockito.given(userRepository.save(userToUpdate))
                .willReturn(Mono.just(updatedUser));

        BDDMockito.given(addressService.update(userToUpdate.getAddress(), oldUser.getAddressId()))
                .willReturn(Mono.just(userToUpdate.getAddress()));
        //when
        StepVerifier.create(userService.update(userToUpdate, oldUser.getId()))
                //then
                .expectNextMatches(user -> !user.isNew() && "new name".equals(user.getFirstName()))
                .verifyComplete();
        verify(userRepository, times(1)).findById(oldUser.getId());
        verify(userRepository, times(1)).save(userToUpdate);
        verify(addressService, times(1)).update(userToUpdate.getAddress(), oldUser.getAddressId());
    }

    @Test
    @DisplayName("update user and save associations")
    public void givenUser_whenUpdate_thenUserUpdatedAndSaveAssociation() {
        //given
        User oldUser = UserDataUtils.persistUser();
        User userToUpdate = mock(User.class);

        User updatedUser = UserDataUtils.persistUserWithAssociations();

        BDDMockito.given(userRepository.findById(oldUser.getId()))
                .willReturn(Mono.just(oldUser));

        BDDMockito.given(userRepository.save(userToUpdate))
                .willReturn(Mono.just(updatedUser));

        BDDMockito.given(userToUpdate.getAddress())
                .willReturn(UserDataUtils.transientUserWithAssociations().getAddress());

        BDDMockito.given(addressService.save(userToUpdate.getAddress()))
                .willReturn(Mono.just(updatedUser.getAddress()));

        //when
        StepVerifier.create(userService.update(userToUpdate, oldUser.getId()))
                //then
                .expectNextCount(1)
                .verifyComplete();
        verify(userToUpdate, times(1)).setId(oldUser.getId());
        verify(userToUpdate, times(1)).setUpdated(any(LocalDateTime.class));
        verify(userToUpdate, times(1)).setCreated(oldUser.getCreated());
        verify(userToUpdate, times(1)).setArchivedAt(oldUser.getArchivedAt());
        verify(userToUpdate, times(1)).setVerifiedAt(oldUser.getVerifiedAt());
        verify(userToUpdate, times(1)).setAddress(updatedUser.getAddress());
        verify(userToUpdate, times(1)).setAddressId(updatedUser.getAddress().getId());
    }
}
