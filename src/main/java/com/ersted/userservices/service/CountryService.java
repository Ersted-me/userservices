package com.ersted.userservices.service;

import com.ersted.userservices.entity.Country;
import com.ersted.userservices.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public Mono<Country> save(Country transientCountry) {
        if (transientCountry == null) {
            return Mono.empty();
        }
        if (!transientCountry.isNew()) {
            return Mono.just(transientCountry);
        }
        return countryRepository.save(transientCountry);
    }
}