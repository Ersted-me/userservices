package com.ersted.userservices.rest;

import com.ersted.userservices.service.IndividualService;
import lombok.RequiredArgsConstructor;
import net.ersted.dto.IndividualDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/individuals")
public class IndividualController {
    private final IndividualService individualService;

    @PostMapping
    public Mono<?> registration(@RequestBody IndividualDto dto) {
        return individualService.registration(dto);
    }

    @GetMapping
    public Flux<?> findAll() {
        return individualService.findAllWithTransient();
    }

    @GetMapping("/{individualId:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public Mono<?> find(@PathVariable("individualId") String individualId) {
        return individualService.findByIdWithTransient(UUID.fromString(individualId));
    }
}