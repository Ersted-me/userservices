package com.ersted.userservices.utils;

import com.ersted.userservices.entity.Address;
import com.ersted.userservices.entity.Country;
import net.ersted.dto.AddressDto;

import java.time.LocalDateTime;
import java.util.UUID;

public class AddressDataUtils {
    private final static LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    public static Address transientAddress() {
        return Address.builder()
                .id(null)
                .created(LOCAL_DATE_TIME)
                .updated(LOCAL_DATE_TIME)
                .countryId(null)
                .address("Aleksandrovskoe 1")
                .zipCode("636761")
                .archived(LOCAL_DATE_TIME)
                .city("Aleksandrovskoe 1")
                .state("Tomskaya Oblast")
                .build();
    }

    public static Address persistAddress() {
        return Address.builder()
                .id(UUID.fromString("a8098c1a-f86e-11da-bd1a-00112444be1e"))
                .created(LOCAL_DATE_TIME)
                .updated(LOCAL_DATE_TIME)
                .countryId(null)
                .address("Aleksandrovskoe 1")
                .zipCode("636761")
                .archived(LOCAL_DATE_TIME)
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

    public static AddressDto addressDto() {
        return new AddressDto(null,"Lenina street", "123456", "Moscow", "Moscow", CountryDataUtils.countryDto());
    }
}
