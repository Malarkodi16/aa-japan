package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TNotification;
import com.nexware.aajapan.repositories.custom.TNotificationRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TNotificationRepository extends MongoRepository<TNotification, String>, TNotificationRepositoryCustom {

}
