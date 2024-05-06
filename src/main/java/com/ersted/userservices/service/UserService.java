package com.ersted.userservices.service;

import com.ersted.userservices.entity.User;
import com.ersted.userservices.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressService addressService;

    public Mono<User> save(User transientUser) {
        if (Objects.isNull(transientUser)) {
            return Mono.empty();
        }
        if (!transientUser.isNew()) {
            return Mono.just(transientUser);
        }
        if (Objects.isNull(transientUser.getAddress())) {
            return userRepository.save(transientUser);
        }
        if (transientUser.getAddress().isNew()) {
            return addressService.save(transientUser.getAddress())
                    .flatMap(address -> {
                        transientUser.setAddressId(address.getId());
                        return userRepository.save(transientUser);
                    });
        }
        transientUser.setAddressId(transientUser.getAddress().getId());
        return userRepository.save(transientUser);
    }
}