package com.ersted.userservices.utils;

import com.ersted.userservices.entity.Country;
import net.ersted.dto.CountryDto;

import java.time.LocalDateTime;

public class CountryDataUtils {
    private final static LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    public static Country transientCountry(){
        return Country.builder()
                .id(null)
                .created(LOCAL_DATE_TIME)
                .updated(LOCAL_DATE_TIME)
                .name("Russia")
                .alpha2("Ru")
                .alpha3("Rus")
                .status(null)
                .build();
    }
    public static Country persistCountry(){
        return Country.builder()
                .id(1)
                .created(LOCAL_DATE_TIME)
                .updated(LOCAL_DATE_TIME)
                .name("Russia")
                .alpha2("Ru")
                .alpha3("Rus")
                .status(null)
                .build();
    }

    public static CountryDto countryDto() {
        return new CountryDto("Russia","Ru", "Rus");
    }
}
