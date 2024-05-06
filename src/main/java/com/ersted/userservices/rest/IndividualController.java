package com.ersted.userservices.rest;

import com.ersted.userservices.service.IndividualService;
import lombok.RequiredArgsConstructor;
import net.ersted.dto.IndividualDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/individuals")
public class IndividualController {
    private final IndividualService individualService;

    @PostMapping
    public Mono<?> registration(@RequestBody IndividualDto dto) {
        return individualService.registration(dto);
    }
}