package com.ersted.userservices.utils;

import com.ersted.userservices.entity.Individual;
import com.ersted.userservices.entity.User;
import ru.ersted.common.dto.IndividualDto;
import ru.ersted.common.dto.UserDto;

import java.time.LocalDateTime;
import java.util.UUID;

public class IndividualDataUtils {
    private final static LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    public static Individual transientIndividual() {
        return Individual.builder()
                .id(null)
                .created(LOCAL_DATE_TIME)
                .updated(LOCAL_DATE_TIME)
                .passportNumber("1234123456")
                .phoneNumber("79998887766")
                .email("email@mail.ru")
                .verifiedAt(LOCAL_DATE_TIME)
                .archivedAt(LOCAL_DATE_TIME)
                .status(null)
                .userId(null)
                .build();
    }

    public static Individual persistIndividual() {
        return Individual.builder()
                .id(UUID.fromString("7064f21b-db21-4ef7-acf7-ac68b563b908"))
                .created(LOCAL_DATE_TIME)
                .updated(LOCAL_DATE_TIME)
                .passportNumber("1234123456")
                .phoneNumber("79998887766")
                .email("email@mail.ru")
                .verifiedAt(LOCAL_DATE_TIME)
                .archivedAt(LOCAL_DATE_TIME)
                .status(null)
                .userId(null)
                .build();
    }

    public static Individual transientIndividualWithAssociation() {
        Individual transientIndividual = IndividualDataUtils.transientIndividual();
        User transientUser = UserDataUtils.transientUser();
        transientIndividual.setUser(transientUser);
        return transientIndividual;
    }

    public static Individual persistIndividualWithAssociation() {
        Individual persistIndividual = IndividualDataUtils.persistIndividual();
        User persistUser = UserDataUtils.persistUserWithAssociations();
        persistIndividual.setUser(persistUser);
        persistIndividual.setUserId(persistUser.getId());
        return persistIndividual;
    }

    public static IndividualDto individualDtoWithTransient() {
        String passportNumber = "1234123456";
        String phoneNumber = "79998887766";
        String email = "email@mail.ru";
        UserDto user = UserDataUtils.userDto();
        return new IndividualDto(null,passportNumber, phoneNumber, email, user);
    }

    public static IndividualDto individualDto() {
        String passportNumber = "1234123456";
        String phoneNumber = "79998887766";
        String email = "email@mail.ru";
        UserDto user = null;
        return new IndividualDto(null,passportNumber, phoneNumber, email, user);
    }
}
