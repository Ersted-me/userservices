package com.ersted.userservices.rest;

import com.ersted.userservices.exception.NotFoundException;
import com.ersted.userservices.service.IndividualService;
import com.ersted.userservices.utils.IndividualDataUtils;
import com.ersted.userservices.utils.ResponseDataUtils;
import net.ersted.dto.IndividualDto;
import net.ersted.dto.ResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = {IndividualController.class})
class IndividualControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private IndividualService individualService;

    @Test
    @DisplayName("registrationByInvitation individual")
    public void givenIndividualDto_whenRegistration_thenResponseDtoIsReturned() {
        //given
        IndividualDto individualDto = IndividualDataUtils.individualDtoWithTransient();
        ResponseDto successResponse = ResponseDataUtils.success("7064f21b-db21-4ef7-acf7-ac68b563b908", "Individual has been successfully registered");
        BDDMockito.given(individualService.registrationByInvitation(any(IndividualDto.class)))
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
                .jsonPath("$.id").isEqualTo("7064f21b-db21-4ef7-acf7-ac68b563b908")
                .jsonPath("$.message").isEqualTo("Individual has been successfully registered")
                .jsonPath("$.status").isEqualTo("SUCCESS");
    }

    @Test
    @DisplayName("find all functionality")
    void givenIndividualDtos_whenFindAll_thenIndividualDtosAreReturned() {
        //given
        IndividualDto individualDto = IndividualDataUtils.individualDtoWithTransient();
        BDDMockito.given(individualService.findAllWithTransient())
                .willReturn(Flux.just(individualDto));
        //when
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/v1/individuals")
                .exchange();
        //then
        response.expectStatus().isOk()
                .expectBodyList(IndividualDto.class).hasSize(1).contains(individualDto);
    }

    @Test
    @DisplayName("find all empty list functionality")
    void givenNothing_whenFindAll_thenEmptyListIsReturned() {
        //given
        BDDMockito.given(individualService.findAllWithTransient())
                .willReturn(Flux.empty());
        //when
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/v1/individuals")
                .exchange();
        //then
        response.expectStatus().isOk()
                .expectBodyList(IndividualDto.class).hasSize(0);
    }

    @Test
    @DisplayName("find by id functionality")
    void givenIndividualDto_whenFindById_thenIndividualDtoIsReturned() {
        //given
        IndividualDto individualDto = IndividualDataUtils.individualDtoWithTransient();
        individualDto.setId("57dfe828-1e15-4501-b891-dda4f19e657a");
        BDDMockito.given(individualService.findByIdWithTransient(any(UUID.class)))
                .willReturn(Mono.just(individualDto));
        //when
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/v1/individuals/%s".formatted(individualDto.getId()))
                .exchange();
        //then
        response.expectStatus().isOk()
                .expectBodyList(IndividualDto.class).hasSize(1).contains(individualDto);
    }

    @Test
    @DisplayName("find by id non exist individual functionality")
    void givenNonExistIndividualDto_whenFindById_thenExceptionDtoIsReturned() {
        //given
        IndividualDto individualDto = IndividualDataUtils.individualDtoWithTransient();
        individualDto.setId("57dfe828-1e15-4501-b891-dda4f19e657a");
        BDDMockito.given(individualService.findByIdWithTransient(any(UUID.class)))
                .willReturn(Mono.error(new NotFoundException("NOT_FOUND", "Individual not found")));
        //when
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/v1/individuals/%s".formatted(individualDto.getId()))
                .exchange();
        //then
        response.expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Individual not found");
    }
}
