package com.ersted.userservices.service;

import com.ersted.userservices.entity.Address;
import com.ersted.userservices.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final CountryService countryService;

    public Mono<Address> save(Address transientAddress) {
        if (Objects.isNull(transientAddress)) {
            return Mono.empty();
        }

        if (!transientAddress.isNew()) {
            return Mono.just(transientAddress);
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        transientAddress.setCreated(currentDateTime);
        transientAddress.setUpdated(currentDateTime);
        transientAddress.setArchived(currentDateTime);

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

    public Mono<Address> findWithTransient(String addressId) {
        return addressRepository.findById(addressId)
                .flatMap(address -> {
                    if (Objects.isNull(address.getCountryId())) {
                        return Mono.just(address);
                    }
                    return countryService.find(address.getCountryId())
                            .map(country -> {
                                if (Objects.nonNull(country)) {
                                    address.setCountry(country);
                                }
                                return address;
                            });
                });
    }
}
