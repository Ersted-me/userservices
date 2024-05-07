package com.ersted.userservices.rest;

import com.ersted.userservices.exception.BadRequestException;
import com.ersted.userservices.service.IndividualService;
import com.ersted.userservices.utils.IndividualDataUtils;
import com.ersted.userservices.utils.ResponseDataUtils;
import net.ersted.dto.IndividualDto;
import net.ersted.dto.ResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = {IndividualController.class})
class IndividualControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private IndividualService individualService;

    @Test
    @DisplayName("registration individual")
    public void givenIndividualDto_whenRegistration_thenResponseDtoIsReturned() {
        //given
        IndividualDto individualDto = IndividualDataUtils.individualDtoWithTransient();
        ResponseDto successResponse = ResponseDataUtils.success("createdId", "Individual has been successfully registered");
        BDDMockito.given(individualService.registration(any(IndividualDto.class)))
                .willReturn(Mono.just(successResponse));
        //when
        WebTestClient.ResponseSpec response = webTestClient.post()
                .uri("/api/v1/individuals")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(individualDto), IndividualDto.class)
                .exchange();
        //then
        response.expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("createdId")
                .jsonPath("$.message").isEqualTo("Individual has been successfully registered")
                .jsonPath("$.status").isEqualTo("SUCCESS");
    }
}