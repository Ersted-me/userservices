package com.ersted.userservices.rest;

import com.ersted.userservices.service.InvitationService;
import lombok.RequiredArgsConstructor;
import ru.ersted.common.dto.InvitationDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invitations")
public class InvitationController {
    private final InvitationService invitationService;

    @PostMapping
    public Mono<?> registration(@RequestBody InvitationDto dto) {
        return invitationService.create(dto);
    }

    @GetMapping("/{invitationId:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public Mono<?> find(@PathVariable("invitationId") UUID invitationId) {
        return invitationService.find(invitationId);
    }
}
