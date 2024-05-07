package com.ersted.userservices.utils;

import com.ersted.userservices.entity.Country;
import net.ersted.dto.CountryDto;

import java.time.LocalDateTime;

public class CountryDataUtils {
    public static Country transientCountry(){
        LocalDateTime currentTime = LocalDateTime.now();
        return Country.builder()
                .id(null)
                .created(currentTime)
                .updated(currentTime)
                .name("Russia")
                .alpha2("Ru")
                .alpha3("Rus")
                .status(null)
                .build();
    }
    public static Country persistCountry(){
        LocalDateTime currentTime = LocalDateTime.now();
        return Country.builder()
                .id("123e4567-e89b-42d3-a456-556642440000")
                .created(currentTime)
                .updated(currentTime)
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
