package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TForwardBooking;
import com.nexware.aajapan.repositories.custom.TForwardBookingRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TForwardBookingRepository
		extends MongoRepository<TForwardBooking, String>, TForwardBookingRepositoryCustom {

}
