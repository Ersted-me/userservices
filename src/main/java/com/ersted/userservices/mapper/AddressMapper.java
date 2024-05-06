package com.ersted.userservices.mapper;

import com.ersted.userservices.entity.Address;
import net.ersted.dto.AddressDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address map(AddressDto dto);

    AddressDto map(Address entity);
}
