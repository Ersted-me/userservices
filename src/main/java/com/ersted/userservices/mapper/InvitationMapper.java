package com.ersted.userservices.mapper;

import com.ersted.userservices.entity.Invitation;
import ru.ersted.common.dto.InvitationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvitationMapper {

    InvitationDto map(Invitation entity);

    Invitation map(InvitationDto dto);
}