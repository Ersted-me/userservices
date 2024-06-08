package com.ersted.userservices.rest;

import com.ersted.userservices.service.IndividualService;
import lombok.RequiredArgsConstructor;
import net.ersted.dto.IndividualDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/individuals")
public class IndividualController {
    private final IndividualService individualService;

    @PostMapping
    public Mono<?> registration(@RequestBody IndividualDto dto) {
        return individualService.registrationByInvitation(dto);
    }

    @PostMapping("/{invitationId:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public Mono<?> registration(@RequestBody IndividualDto dto, @PathVariable("invitationId") UUID invitationId) {
        return individualService.registrationByInvitation(dto, invitationId);
    }

    @GetMapping
    public Flux<?> findAll() {
        return individualService.findAllWithTransient();
    }

    @GetMapping("/{individualId:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public Mono<?> find(@PathVariable("individualId") UUID individualId) {
        return individualService.findByIdWithTransient(individualId);
    }

    @PutMapping("/{individualId:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public Mono<?> update(@PathVariable("individualId") UUID individualId, @RequestBody IndividualDto dto) {
        return individualService.update(dto, individualId);
    }
}