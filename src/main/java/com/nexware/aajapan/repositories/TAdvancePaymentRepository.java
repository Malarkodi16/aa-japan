package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TAdvancePayment;
import com.nexware.aajapan.repositories.custom.TAdvancePaymentRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TAdvancePaymentRepository
		extends MongoRepository<TAdvancePayment, String>, TAdvancePaymentRepositoryCustom {

	List<TAdvancePayment> findAllByPaymentApprove(Integer paymentApprove);

	TAdvancePayment findOneById(String id);

}
