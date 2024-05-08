package com.ersted.userservices.service;

import com.ersted.userservices.entity.Country;
import com.ersted.userservices.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public Mono<Country> save(Country transientCountry) {
        if (Objects.isNull(transientCountry)) {
            return Mono.empty();
        }
        if (!transientCountry.isNew()) {
            return Mono.just(transientCountry);
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        transientCountry.setCreated(currentDateTime);
        transientCountry.setUpdated(currentDateTime);
        return countryRepository.save(transientCountry);
    }

    public Mono<Country> find(Integer countryId) {
        return countryRepository.findById(countryId);
    }

    public Mono<Country> update(Country newCountry, Integer countryId) {
        if (Objects.isNull(newCountry) || Objects.isNull(countryId)) {
            return Mono.empty();
        }
        return countryRepository.findById(countryId)
                .switchIfEmpty(Mono.empty())
                .flatMap(old -> {
                    newCountry.setId(old.getId());
                    newCountry.setCreated(old.getCreated());
                    newCountry.setUpdated(LocalDateTime.now());
                    newCountry.setStatus(old.getStatus());
                    return countryRepository.save(newCountry);
                });
    }
}