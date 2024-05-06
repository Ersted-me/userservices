package com.ersted.userservices.utils;

import com.ersted.userservices.entity.Country;

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
                .id("persistCountry")
                .created(currentTime)
                .updated(currentTime)
                .name("Russia")
                .alpha2("Ru")
                .alpha3("Rus")
                .status(null)
                .build();
    }
}
