package com.nexware.aajapan.repositories;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TExchangeRate;
import com.nexware.aajapan.repositories.custom.TExchageRateRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TExchageRateRepository extends MongoRepository<TExchangeRate, String>, TExchageRateRepositoryCustom {

	@Query("{'CreatedDate' : { $gte: ?0, $lte: ?1 } }]}")
	TExchangeRate findByDate(Date startDate, Date endDate);

	@Query("{ 'date' : ?0 }")
	List<TExchangeRate> findAllByDate(LocalDate date);

	@Query("{ 'date' : ?0 }")
	TExchangeRate deleteByDate(LocalDate date);

	@Query("{ 'date' : ?0 }")
	TExchangeRate findTopByDateDesc();
	// 2018-10-11

	@Query("{ 'createdDate' :{ $gte: ?0, $lte: ?1 } },$sort : { 'createdDate' : -1 }]}")
	List<TExchangeRate> find3ByOrderByCreatedDateDesc(Date startDate, Date endDate);
	
}
