package com.ersted.userservices.it;

import com.ersted.userservices.config.PostgreTestContainerConfig;
import com.ersted.userservices.entity.Address;
import com.ersted.userservices.entity.Country;
import com.ersted.userservices.entity.Individual;
import com.ersted.userservices.entity.User;
import com.ersted.userservices.mapper.IndividualMapper;
import com.ersted.userservices.repository.AddressRepository;
import com.ersted.userservices.repository.CountryRepository;
import com.ersted.userservices.repository.IndividualRepository;
import com.ersted.userservices.repository.UserRepository;
import com.ersted.userservices.utils.AddressDataUtils;
import com.ersted.userservices.utils.CountryDataUtils;
import com.ersted.userservices.utils.IndividualDataUtils;
import com.ersted.userservices.utils.UserDataUtils;
import net.ersted.dto.IndividualDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;
import reactor.core.publisher.Mono;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(PostgreTestContainerConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ItIndividualControllerTest {
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IndividualRepository individualRepository;
    @Autowired
    private IndividualMapper individualMapper;
    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        individualRepository.deleteAll().block();
        userRepository.deleteAll().block();
        addressRepository.deleteAll().block();
        countryRepository.deleteAll().block();
    }

    @Test
    @DisplayName("registration individual")
    public void givenIndividualDto_whenRegistration_thenResponseDtoIsReturned() {
        //given
        IndividualDto individualDto = IndividualDataUtils.individualDtoWithTransient();

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

    @Test
    @DisplayName("find all functionality")
    void givenIndividualDtos_whenFindAll_thenIndividualDtosAreReturned() {
        //given
        Country country = countryRepository.save(CountryDataUtils.transientCountry()).block();
        Address transientAddress = AddressDataUtils.transientAddress();
        transientAddress.setCountryId(country.getId());
        transientAddress.setCountry(country);
        Address address = addressRepository.save(transientAddress).block();
        User transientUser = UserDataUtils.transientUser();
        transientUser.setAddressId(address.getId());
        transientUser.setAddress(address);
        User user = userRepository.save(transientUser).block();
        Individual transientIndividual = IndividualDataUtils.transientIndividual();
        transientIndividual.setUserId(user.getId());
        transientIndividual.setUser(user);
        Individual individual = individualRepository.save(transientIndividual).block();
        IndividualDto dto = individualMapper.map(individual);

        //when
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/v1/individuals")
                .exchange();
        //then
        response.expectStatus().isOk()
                .expectBodyList(IndividualDto.class).consumeWith(System.out::println).hasSize(1).contains(dto);
    }

    @Test
    @DisplayName("find all empty list functionality")
    void givenNothing_whenFindAll_thenEmptyListIsReturned() {
        //given
        //when
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/v1/individuals")
                .exchange();
        //then
        response.expectStatus().isOk()
                .expectBodyList(IndividualDto.class).hasSize(0);
    }
}
