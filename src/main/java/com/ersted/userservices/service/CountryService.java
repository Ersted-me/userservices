package com.ersted.userservices.service;

import com.ersted.userservices.entity.Country;
import com.ersted.userservices.mapper.CountryMapper;
import com.ersted.userservices.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import net.ersted.dto.CountryDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public Mono<Country> save(CountryDto dto) {
        Country entityForSave = countryMapper.map(dto);
        return countryRepository.save(entityForSave);
    }

    public Mono<Country> save(Country newEntity) {
        return countryRepository.save(newEntity);
    }
}