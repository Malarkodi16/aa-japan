package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TransportPaymentCategory;

@Repository
@JaversSpringDataAuditable
public interface TransportPaymentRepository extends MongoRepository<TransportPaymentCategory, String> {

}
