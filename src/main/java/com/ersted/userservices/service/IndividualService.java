package com.ersted.userservices.service;

import com.ersted.userservices.entity.Individual;
import com.ersted.userservices.enums.ResponseStatus;
import com.ersted.userservices.exception.BadRequestException;
import com.ersted.userservices.mapper.IndividualMapper;
import com.ersted.userservices.repository.IndividualRepository;
import lombok.RequiredArgsConstructor;
import net.ersted.dto.IndividualDto;
import net.ersted.dto.ResponseDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class IndividualService {
    private final IndividualRepository individualRepository;
    private final IndividualMapper individualMapper;
    private final UserService userService;

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
}
