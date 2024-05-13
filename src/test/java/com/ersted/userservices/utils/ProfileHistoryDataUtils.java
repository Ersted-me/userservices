package com.ersted.userservices.utils;

import com.ersted.userservices.entity.ProfileHistory;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProfileHistoryDataUtils {
    private final static LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    public static ProfileHistory persist(UUID profileId){
        return ProfileHistory.builder()
                .id(UUID.fromString("7064f21b-db21-4ef7-acf7-ac68b563b908"))
                .created(LOCAL_DATE_TIME)
                .profileId(profileId)
                .profileType("some type")
                .reason("reason")
                .comment("comment")
                .changedValues("""
                        {
                          "user": {
                            "firstName": "update name"
                          },
                          "email": "update@mail.ru"
                        }
                        """)
                .build();
    }
}
