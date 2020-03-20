package com.nexware.aajapan.repositories.custom;

import java.util.List;

import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.client.result.UpdateResult;
import com.nexware.aajapan.models.MLocation;

public interface MLocationRepositoryCustom {

	List<MLocation> getListWithoutDelete();

	UpdateResult updateById(String id, Update update);

}
