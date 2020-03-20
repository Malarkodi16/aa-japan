package com.nexware.aajapan.repositories.custom.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.TDayBook;
import com.nexware.aajapan.repositories.custom.TDayBookRepositoryCustom;

public class TDayBookRepositoryCustomImpl implements TDayBookRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void updateById(String id) {

		Update update = new Update().set("status", Constants.DAYBOOK_ENTRY_OWNED);
		this.mongoTemplate.updateMulti(Query.query(Criteria.where("_id").in(id)), update, TDayBook.class);
	}

}
