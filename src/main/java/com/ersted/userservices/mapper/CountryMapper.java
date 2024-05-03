package com.ersted.userservices.mapper;

import com.ersted.userservices.entity.Country;
import net.ersted.dto.CountryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    Country map(CountryDto dto);

    CountryDto map(Country entity);
}