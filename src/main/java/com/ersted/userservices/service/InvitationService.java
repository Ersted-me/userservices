package com.ersted.userservices.service;

import com.ersted.userservices.entity.Invitation;
import com.ersted.userservices.exception.NotFoundException;
import com.ersted.userservices.mapper.InvitationMapper;
import com.ersted.userservices.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import net.ersted.dto.InvitationDto;
import net.ersted.dto.ResponseDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final static Integer LIFETIME_INVITATION = 7;
    private final InvitationRepository invitationRepository;
    private final InvitationMapper invitationMapper;
    private final MerchantService merchantService;

    public Mono<ResponseDto> create(InvitationDto dto) {
        return merchantService.findById(dto.getMerchantId())
                .switchIfEmpty(Mono.error(new NotFoundException("NOT_FOUND", "Merchant not found")))
                .flatMap(merchant -> {
                    Invitation invitation = invitationMapper.map(dto);
                    invitation.setCreated(LocalDateTime.now());
                    invitation.setExpires(LocalDateTime.now().plusHours(LIFETIME_INVITATION));
                    invitation.setStatus("some status");
                    return invitationRepository.save(invitation);
                })
                .flatMap(invitation -> Mono.just(new ResponseDto("SUCCESS", "Invitation success created", invitation.getId().toString())));
    }

    public Mono<Invitation> find(UUID invitationId) {
        return invitationRepository.findById(invitationId);
    }

    public Mono<InvitationDto> findById(UUID invitationId) {
        return find(invitationId)
                .switchIfEmpty(Mono.error(new NotFoundException("NOT_FOUND", "Invitation not found")))
                .map(invitationMapper::map);
    }


}
