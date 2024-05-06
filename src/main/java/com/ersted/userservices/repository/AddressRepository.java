package com.ersted.userservices.repository;

import com.ersted.userservices.entity.Address;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface AddressRepository extends R2dbcRepository<Address, String> {
}