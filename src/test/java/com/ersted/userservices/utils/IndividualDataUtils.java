package com.ersted.userservices.utils;

import com.ersted.userservices.entity.Individual;
import com.ersted.userservices.entity.User;
import net.ersted.dto.IndividualDto;
import net.ersted.dto.UserDto;

import java.time.LocalDateTime;

public class IndividualDataUtils {
    public static Individual transientIndividual() {
        LocalDateTime currentTime = LocalDateTime.now();
        return Individual.builder()
                .id(null)
                .created(currentTime)
                .updated(currentTime)
                .passportNumber("1234123456")
                .phoneNumber("79998887766")
                .email("email@mail.ru")
                .verifiedAt(currentTime)
                .archivedAt(currentTime)
                .status(null)
                .userId(null)
                .build();
    }

    public static Individual persistIndividual() {
        LocalDateTime currentTime = LocalDateTime.now();
        return Individual.builder()
                .id("persistIndividual")
                .created(currentTime)
                .updated(currentTime)
                .passportNumber("1234123456")
                .phoneNumber("79998887766")
                .email("email@mail.ru")
                .verifiedAt(currentTime)
                .archivedAt(currentTime)
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
        User persistUser = UserDataUtils.persistUser();
        persistIndividual.setUser(persistUser);
        persistIndividual.setUserId(persistUser.getId());
        return persistIndividual;
    }

    public static IndividualDto individualDto() {
        String passportNumber = "1234123456";
        String phoneNumber = "79998887766";
        String email = "email@mail.ru";
        UserDto user = null;
        return new IndividualDto(passportNumber, phoneNumber, email, user);
    }
}
