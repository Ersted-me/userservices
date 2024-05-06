package com.ersted.userservices.service;

import com.ersted.userservices.entity.Individual;
import com.ersted.userservices.repository.IndividualRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class IndividualService {
    private final IndividualRepository individualRepository;
    private final UserService userService;

    public Mono<Individual> save(Individual transientIndividual) {
        if (Objects.isNull(transientIndividual)) {
            return Mono.empty();
        }
        if (!transientIndividual.isNew()) {
            return Mono.just(transientIndividual);
        }
        if (Objects.isNull(transientIndividual.getUser())) {
            return individualRepository.save(transientIndividual);
        }
        if (transientIndividual.getUser().isNew()) {
            return userService.save(transientIndividual.getUser())
                    .flatMap(user -> {
                        transientIndividual.setUserId(user.getId());
                        return individualRepository.save(transientIndividual);
                    });
        }
        transientIndividual.setUserId(transientIndividual.getUser().getId());
        return individualRepository.save(transientIndividual);
    }
}
