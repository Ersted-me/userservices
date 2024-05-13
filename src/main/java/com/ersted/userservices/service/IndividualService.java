package com.ersted.userservices.service;

import com.ersted.userservices.entity.Individual;
import com.ersted.userservices.enums.ResponseStatus;
import com.ersted.userservices.exception.BadRequestException;
import com.ersted.userservices.exception.NotFoundException;
import com.ersted.userservices.mapper.IndividualMapper;
import com.ersted.userservices.repository.IndividualRepository;
import com.ersted.userservices.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import net.ersted.dto.IndividualDto;
import net.ersted.dto.ResponseDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IndividualService {
    private final IndividualRepository individualRepository;
    private final IndividualMapper individualMapper;
    private final UserService userService;
    private final ProfileHistoryService profileHistoryService;

    public Mono<Individual> save(Individual transientIndividual) {
        if (Objects.isNull(transientIndividual)) {
            return Mono.empty();
        }
        if (!transientIndividual.isNew()) {
            return Mono.just(transientIndividual);
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        transientIndividual.setCreated(currentDateTime);
        transientIndividual.setUpdated(currentDateTime);
        transientIndividual.setArchivedAt(currentDateTime);
        transientIndividual.setVerifiedAt(currentDateTime);

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

    public Mono<ResponseDto> registration(IndividualDto dto) {
        Individual transientIndividual = individualMapper.map(dto);
        return this.save(transientIndividual)
                .switchIfEmpty(Mono.error(new BadRequestException("BAD_REQUEST", "Body can not be blank")))
                .flatMap(individual -> Mono.just(new ResponseDto(ResponseStatus.SUCCESS.name(), "Individual has been successfully registered", individual.getId().toString())));
    }

    public Flux<IndividualDto> findAllWithTransient() {
        return individualRepository.findAll()
                .flatMap(this::loadTransient)
                .map(individualMapper::map);
    }

    public Mono<IndividualDto> findByIdWithTransient(UUID id) {
        return individualRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("NOT_FOUND", "Individual not found")))
                .flatMap(this::loadTransient)
                .map(individualMapper::map);
    }

    private Mono<Individual> loadTransient(Individual individual) {
        if (Objects.isNull(individual)) {
            return Mono.empty();
        }
        if (Objects.isNull(individual.getUserId())) {
            return Mono.just(individual);
        }
        return userService.findWithTransient(individual.getUserId())
                .map(user -> {
                    if (Objects.nonNull(user)) {
                        individual.setUser(user);
                    }
                    return individual;
                });
    }

    public Mono<IndividualDto> update(IndividualDto dto, UUID oldIndividualId) {
        Individual newIndividual = individualMapper.map(dto);
        return update(newIndividual, oldIndividualId)
                .map(individualMapper::map);
    }

    public Mono<Individual> update(Individual individual, UUID oldIndividualId) {
        if (Objects.isNull(individual) || Objects.isNull(oldIndividualId)) {
            return Mono.empty();
        }
        return individualRepository.findById(oldIndividualId)
                .flatMap(this::loadTransient)
                .flatMap(oldIndividual -> profileHistoryService.save(oldIndividual.getUserId(), "", "", "", JsonUtils.diffJsonLikeString(individual, oldIndividual))
                        .map(history -> oldIndividual))
                .flatMap(oldIndividual -> userService.update(individual.getUser(), oldIndividual.getUserId())
                        .map(updatedUser -> {
                            individual.setUser(updatedUser);
                            individual.setUserId(updatedUser.getId());
                            return oldIndividual;
                        }))
                .flatMap(oldIndividual -> individualRepository.save(setRequiredFieldsForUpdate(individual, oldIndividual)));
    }

    private Individual setRequiredFieldsForUpdate(final Individual individual, final Individual oldIndividual) {
        individual.setId(oldIndividual.getId());
        individual.setUpdated(LocalDateTime.now());
        individual.setCreated(oldIndividual.getCreated());
        individual.setArchivedAt(oldIndividual.getArchivedAt());
        individual.setVerifiedAt(oldIndividual.getVerifiedAt());
        return individual;
    }
}
