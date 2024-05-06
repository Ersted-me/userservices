package com.ersted.userservices.utils;

import com.ersted.userservices.entity.Address;
import com.ersted.userservices.entity.Country;

import java.time.LocalDateTime;

public class AddressDataUtils {
    public static Address transientAddress() {
        LocalDateTime currentTime = LocalDateTime.now();
        return Address.builder()
                .id(null)
                .created(currentTime)
                .updated(currentTime)
                .countryId(null)
                .address("Aleksandrovskoe 1")
                .zipCode("636761")
                .archived(currentTime)
                .city("Aleksandrovskoe 1")
                .state("Tomskaya Oblast")
                .build();
    }

    public static Address persistAddress() {
        LocalDateTime currentTime = LocalDateTime.now();
        return Address.builder()
                .id("persistAddress")
                .created(currentTime)
                .updated(currentTime)
                .countryId(null)
                .address("Aleksandrovskoe 1")
                .zipCode("636761")
                .archived(currentTime)
                .city("Aleksandrovskoe 1")
                .state("Tomskaya Oblast")
                .build();
    }

    public static Address transientAddressWithAssociations() {
        Address transientAddress = AddressDataUtils.transientAddress();
        Country transientCountry = CountryDataUtils.transientCountry();

        transientAddress.setCountry(transientCountry);

        return transientAddress;
    }

    public static Address transientAddressWithPersistAssociations() {
        Address transientAddress = AddressDataUtils.transientAddress();
        Country persistCountry = CountryDataUtils.persistCountry();

        transientAddress.setCountry(persistCountry);

        return transientAddress;
    }

    public static Address persistAddressWithAssociations() {
        Address persistAddress = AddressDataUtils.persistAddress();
        Country persistCountry = CountryDataUtils.persistCountry();

        persistAddress.setCountry(persistCountry);
        persistAddress.setCountryId(persistCountry.getId());

        return persistAddress;
    }
}