package com.ersted.userservices.mapper;

import com.ersted.userservices.entity.Individual;
import net.ersted.dto.IndividualDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IndividualMapper {
    IndividualDto map(Individual entity);

    Individual map(IndividualDto dto);
}