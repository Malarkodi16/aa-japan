package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TShipmentSchedule;
import com.nexware.aajapan.repositories.custom.TShipmentScheduleRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TShipmentScheduleRepository
		extends MongoRepository<TShipmentSchedule, String>, TShipmentScheduleRepositoryCustom {
	TShipmentSchedule findOneByid(String id);

	TShipmentSchedule findOneByScheduleId(String scheduleId);

	TShipmentSchedule findByVoyageNo(String voyageNo);

}
