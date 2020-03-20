package com.nexware.aajapan.repositories.custom.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.client.result.UpdateResult;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.repositories.custom.TCustomerRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TCustomerRepositoryCustomImpl implements TCustomerRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<TCustomer> findBySearchTermsAndSalesPerson(String searchterm, Integer flag, List<String> salesPerson) {
		final List<Criteria> orCriterias = Arrays.asList(Criteria.where("companyName").regex(".*" + searchterm + ".*", "i"),
				Criteria.where("firstName").regex(".*" + searchterm + ".*", "i"),
				Criteria.where("nickName").regex(".*" + searchterm + ".*", "i"),
				Criteria.where("email").regex(".*" + searchterm + ".*", "i"));
		final ArrayList<Criteria> andCriteria = new ArrayList<>();
		andCriteria.add(Criteria.where("flag").is(flag));
		andCriteria.add(Criteria.where("approveCustomerflag").is(Constants.CUSTOMER_APPROVED_FLAG));
		if (!AppUtil.isObjectEmpty(salesPerson)) {
			andCriteria.add(Criteria.where("salesPerson").in(salesPerson));
		}
		andCriteria.add(new Criteria().orOperator(orCriterias.toArray(new Criteria[0])));
		final Criteria criteria = new Criteria().andOperator(andCriteria.toArray(new Criteria[0]));
		return mongoTemplate.find(Query.query(criteria), TCustomer.class);

	}

	@Override
	public List<TCustomer> findByAdminSearchTermsAndSalesPerson(String searchterm, List<String> salesPerson) {
		final List<Criteria> orCriterias = Arrays.asList(Criteria.where("companyName").regex(".*" + searchterm + ".*", "i"),
				Criteria.where("firstName").regex(".*" + searchterm + ".*", "i"),
				Criteria.where("nickName").regex(".*" + searchterm + ".*", "i"),
				Criteria.where("email").regex(".*" + searchterm + ".*", "i"));
		final ArrayList<Criteria> andCriteria = new ArrayList<>();
		andCriteria.add(Criteria.where("approveCustomerflag").is(Constants.CUSTOMER_APPROVED_FLAG));
		if (!AppUtil.isObjectEmpty(salesPerson)) {
			andCriteria.add(Criteria.where("salesPerson").in(salesPerson));
		}
		andCriteria.add(new Criteria().orOperator(orCriterias.toArray(new Criteria[0])));
		final Criteria criteria = new Criteria().andOperator(andCriteria.toArray(new Criteria[0]));
		return mongoTemplate.find(Query.query(criteria), TCustomer.class);

	}

	@Override
	public UpdateResult updateById(String id, Update update) {
		return mongoTemplate.updateMulti(Query.query(Criteria.where("id").is(id)), update, TCustomer.class);

	}

	@Override
	public TCustomer updateBalance(String customerId, Integer transactionType, Double amount) {
		final Query query = new Query(Criteria.where("code").is(customerId));
		final Update update = new Update();
		if (transactionType.equals(Constants.TRANSACTION_CREDIT)) {
			update.inc("balance", amount);
		} else if (transactionType.equals(Constants.TRANSACTION_DEBIT)) {
			update.inc("balance", (amount * -1));
		}
		// return new balance
		final FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		final TCustomer updated = mongoTemplate.findAndModify(query, update, options, TCustomer.class);

		if (AppUtil.isObjectEmpty(updated)) {
			throw new AAJRuntimeException("Exception while update customer balance : " + customerId);
		}
		return updated;
	}

	@Override
	public List<TCustomer> findBySearchTermsAndSalesPersonName(String search, String salesName) {
		final Criteria criteria = new Criteria().andOperator(Criteria.where("salesPerson").is(salesName),
				new Criteria().orOperator(Criteria.where("companyName").regex(".*" + search + ".*", "i"),
						Criteria.where("firstName").regex(".*" + search + ".*", "i"),
						Criteria.where("lastName").regex(".*" + search + ".*", "i"),
						Criteria.where("nickName").regex(".*" + search + ".*", "i"),
						Criteria.where("email").regex(".*" + search + ".*", "i")));
		return mongoTemplate.find(Query.query(criteria), TCustomer.class);
	}

	@Override
	public List<TCustomer> findBySearchTerms(String search) {
		final List<Criteria> orCriterias = Arrays.asList((Criteria.where("companyName").regex(".*" + search + ".*", "i")),
				Criteria.where("firstName").regex(".*" + search + ".*", "i"),
				Criteria.where("lastName").regex(".*" + search + ".*", "i"),
				Criteria.where("nickName").regex(".*" + search + ".*", "i"),
				Criteria.where("email").regex(".*" + search + ".*", "i"));
		final ArrayList<Criteria> andCriteria = new ArrayList<>();
		andCriteria.add(Criteria.where("approveCustomerflag").is(Constants.CUSTOMER_APPROVED_FLAG));
		andCriteria.add(new Criteria().orOperator(orCriterias.toArray(new Criteria[0])));
		final Criteria criteria = new Criteria().andOperator(andCriteria.toArray(new Criteria[0]));
		return mongoTemplate.find(Query.query(criteria), TCustomer.class);
	}

	@Override
	public TCustomer updateDepositeAmount(String customerId, Integer transactionType, Double amount) {
		final Query query = new Query(Criteria.where("code").is(customerId));
		final Update update = new Update();
		if (transactionType.equals(Constants.TRANSACTION_CREDIT)) {
			update.inc("depositAmount", amount);
		} else if (transactionType.equals(Constants.TRANSACTION_DEBIT)) {
			update.inc("depositAmount", (amount * -1));
		}
		// return new balance
		final FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		final TCustomer updated = mongoTemplate.findAndModify(query, update, options, TCustomer.class);

		if (AppUtil.isObjectEmpty(updated)) {
			throw new AAJRuntimeException("Exception while update deposite amount: " + customerId);
		}
		return updated;
	}
	
	@Override
	public TCustomer updateAdvanceAmount(String customerId, Integer transactionType, Double amount) {
		final Query query = new Query(Criteria.where("code").is(customerId));
		final Update update = new Update();
		if (transactionType.equals(Constants.TRANSACTION_DEBIT)) {
			update.inc("advanceAmount", amount);
		} else if (transactionType.equals(Constants.TRANSACTION_CREDIT)) {
			update.inc("advanceAmount", (amount * -1));
		}
		// return new balance
		final FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		final TCustomer updated = mongoTemplate.findAndModify(query, update, options, TCustomer.class);

		if (AppUtil.isObjectEmpty(updated)) {
			throw new AAJRuntimeException("Exception while update deposite amount: " + customerId);
		}
		return updated;
	}

	@Override
	public List<TCustomer> findLcCustomerBySearchTerms(String search) {
		final List<Criteria> orCriterias = Arrays.asList((Criteria.where("companyName").regex(".*" + search + ".*", "i")),
				Criteria.where("firstName").regex(".*" + search + ".*", "i"),
				Criteria.where("lastName").regex(".*" + search + ".*", "i"),
				Criteria.where("nickName").regex(".*" + search + ".*", "i"),
				Criteria.where("email").regex(".*" + search + ".*", "i"));
		final ArrayList<Criteria> andCriteria = new ArrayList<>();
		andCriteria.add(Criteria.where("isLcCustomer").is(true));
		andCriteria.add(Criteria.where("approveCustomerflag").is(Constants.CUSTOMER_APPROVED_FLAG));
		andCriteria.add(new Criteria().orOperator(orCriterias.toArray(new Criteria[0])));
		final Criteria criteria = new Criteria().andOperator(andCriteria.toArray(new Criteria[0]));
		return mongoTemplate.find(Query.query(criteria), TCustomer.class);
	}

	@Override
	public List<TCustomer> findBySearchTermsAndFlag(String search, Integer flag) {
		final List<Criteria> orCriterias = Arrays.asList((Criteria.where("code").regex(".*" + search + ".*", "i")),
				Criteria.where("firstName").regex(".*" + search + ".*", "i"),
				Criteria.where("lastName").regex(".*" + search + ".*", "i"),
				Criteria.where("nickName").regex(".*" + search + ".*", "i"),
				Criteria.where("email").regex(".*" + search + ".*", "i"));
		final ArrayList<Criteria> andCriteria = new ArrayList<>();
		andCriteria.add(Criteria.where("flag").is(flag));
		andCriteria.add(Criteria.where("approveCustomerflag").is(Constants.CUSTOMER_APPROVED_FLAG));
		andCriteria.add(new Criteria().orOperator(orCriterias.toArray(new Criteria[0])));
		final Criteria criteria = new Criteria().andOperator(andCriteria.toArray(new Criteria[0]));
		return mongoTemplate.find(Query.query(criteria), TCustomer.class);

	}

}
