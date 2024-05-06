package com.ersted.userservices.service;

import com.ersted.userservices.entity.Address;
import com.ersted.userservices.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final CountryService countryService;

    public Mono<Address> save(Address transientAddress) {
        if (transientAddress == null) {
            return Mono.empty();
        }

        if (!transientAddress.isNew()) {
            return Mono.just(transientAddress);
        }

        if (transientAddress.getCountry() == null) {
            return addressRepository.save(transientAddress);
        }

        if (transientAddress.getCountry().isNew()) {
            return countryService.save(transientAddress.getCountry())
                    .flatMap(country -> {
                        transientAddress.setCountryId(country.getId());
                        return addressRepository.save(transientAddress);
                    });
        }
        transientAddress.setCountryId(transientAddress.getCountry().getId());
        return addressRepository.save(transientAddress);
    }
}
