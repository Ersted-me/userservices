package com.ersted.userservices.utils;

import com.ersted.userservices.entity.Address;
import com.ersted.userservices.entity.User;
import net.ersted.dto.UserDto;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserDataUtils {
    private final static LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    public static User transientUser() {
        return User.builder()
                .id(null)
                .secretKey("secretKey")
                .created(LOCAL_DATE_TIME)
                .updated(LOCAL_DATE_TIME)
                .firstName("FirstName")
                .lastName("LastName")
                .verifiedAt(LOCAL_DATE_TIME)
                .archivedAt(LOCAL_DATE_TIME)
                .status(null)
                .filled(false)
                .addressId(null)
                .build();
    }

    public static User persistUser() {
        return User.builder()
                .id(UUID.fromString("1ba34cca-2ef6-4a69-9fef-7cc726ccc076"))
                .secretKey("secretKey")
                .created(LOCAL_DATE_TIME)
                .updated(LOCAL_DATE_TIME)
                .firstName("FirstName")
                .lastName("LastName")
                .verifiedAt(LOCAL_DATE_TIME)
                .archivedAt(LOCAL_DATE_TIME)
                .status(null)
                .filled(false)
                .addressId(null)
                .build();
    }

    public static User transientUserWithAssociations() {
        User transientUser = UserDataUtils.transientUser();
        Address transientAddress = AddressDataUtils.transientAddress();
        transientUser.setAddress(transientAddress);
        return transientUser;
    }

    public static User persistUserWithAssociations() {
        User persistUser = UserDataUtils.persistUser();
        Address persistAddress = AddressDataUtils.persistAddressWithAssociations();
        persistUser.setAddressId(persistAddress.getId());
        persistUser.setAddress(persistAddress);
        return persistUser;
    }

    public static UserDto userDto() {
        return new UserDto(null,"Bob", "Murey", AddressDataUtils.addressDto());
    }
}
