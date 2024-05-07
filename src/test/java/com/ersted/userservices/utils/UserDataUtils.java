package com.ersted.userservices.utils;

import com.ersted.userservices.entity.Address;
import com.ersted.userservices.entity.User;

import java.time.LocalDateTime;

public class UserDataUtils {
    public static User transientUser() {
        LocalDateTime currentTime = LocalDateTime.now();
        return User.builder()
                .id(null)
                .secretKey("secretKey")
                .created(currentTime)
                .updated(currentTime)
                .firstName("FirstName")
                .lastName("LastName")
                .verifiedAt(currentTime)
                .archivedAt(currentTime)
                .status(null)
                .filled(false)
                .addressId(null)
                .build();
    }

    public static User persistUser() {
        LocalDateTime currentTime = LocalDateTime.now();
        return User.builder()
                .id("1ba34cca-2ef6-4a69-9fef-7cc726ccc076")
                .secretKey("secretKey")
                .created(currentTime)
                .updated(currentTime)
                .firstName("FirstName")
                .lastName("LastName")
                .verifiedAt(currentTime)
                .archivedAt(currentTime)
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
        Address persistAddress = AddressDataUtils.persistAddress();
        persistUser.setAddressId(persistAddress.getId());
        persistUser.setAddress(persistAddress);
        return persistUser;
    }
}
