package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TDayBook;
import com.nexware.aajapan.repositories.custom.TDayBookRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TDayBookRepository extends MongoRepository<TDayBook, String>, TDayBookRepositoryCustom {
	TDayBook findOneByid(String id);

	TDayBook findOneByDaybookId(String daybookId);
}
