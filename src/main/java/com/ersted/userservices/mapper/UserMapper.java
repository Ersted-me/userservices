package com.ersted.userservices.mapper;

import com.ersted.userservices.entity.User;
import net.ersted.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto map(User entity);

    User map(UserDto dto);
}