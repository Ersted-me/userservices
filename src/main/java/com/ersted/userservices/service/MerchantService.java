package com.ersted.userservices.service;

import com.ersted.userservices.entity.Merchant;
import com.ersted.userservices.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MerchantService {
    private final MerchantRepository merchantRepository;

    public Mono<Merchant> findById(UUID merchantId){
        return merchantRepository.findById(merchantId);
    }
}
