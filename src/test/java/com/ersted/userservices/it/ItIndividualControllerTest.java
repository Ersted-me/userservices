package com.ersted.userservices.it;

import com.ersted.userservices.config.PostgreTestContainerConfig;
import com.ersted.userservices.utils.IndividualDataUtils;
import net.ersted.dto.IndividualDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(PostgreTestContainerConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ItIndividualControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    
    @Test
    @DisplayName("registration individual")
    public void givenIndividualDto_whenRegistration_thenResponseDtoIsReturned() {
        //given
        IndividualDto individualDto = IndividualDataUtils.individualDto();

        //when
        WebTestClient.ResponseSpec response = webTestClient.post()
                .uri("/api/v1/individuals")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(individualDto), IndividualDto.class)
                .exchange();
        //then
        response.expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.message").isEqualTo("Individual has been successfully registered")
                .jsonPath("$.status").isEqualTo("SUCCESS");
    }
}