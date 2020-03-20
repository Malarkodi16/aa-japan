package com.nexware.aajapan.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MBank;
import com.nexware.aajapan.models.TBankTransaction;
import com.nexware.aajapan.repositories.TBankTransactionRepository;
import com.nexware.aajapan.services.BankTransactionService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class BankTransactionServiceImpl implements BankTransactionService {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private TBankTransactionRepository bankTransactionRepository;
	@Autowired
	private SequenceService sequenceService;

	@Override
	public MBank modifyAndGetCurrentBalanceOfBank(String bankId, Double amount, int currencyType, int transactionType) {
		Query query = new Query(Criteria.where("bankSeq").is(bankId));
		// update balance
		Update update = new Update();

		if (transactionType == Constants.TRANSACTION_CREDIT) {
			update.inc("yenBalance", (amount * -1));
		} else if (transactionType == Constants.TRANSACTION_DEBIT) {
			update.inc("yenBalance", amount);
		}

		// return new balance
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(false);
		MBank updated = this.mongoTemplate.findAndModify(query, update, options, MBank.class);

		if (AppUtil.isObjectEmpty(updated)) {
			throw new AAJRuntimeException("Bank not found : " + bankId);
		}

		return updated;
	}

	@Override
	public Double modifyAndGetClearingBalanceOfBank(String bankId, Double amount, int currencyType,
			int transactionType) {
		Query query = new Query(Criteria.where("bankSeq").is(bankId));
		// update balance
		Update update = new Update();

		if (transactionType == Constants.TRANSACTION_CREDIT) {
			update.inc("clearingBalance", (amount * -1));
		} else if (transactionType == Constants.TRANSACTION_DEBIT) {
			update.inc("clearingBalance", amount);
		}

		// return new balance
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		MBank updated = this.mongoTemplate.findAndModify(query, update, options, MBank.class);

		if (AppUtil.isObjectEmpty(updated)) {
			throw new AAJRuntimeException("Bank not found : " + bankId);
		}
		Double newBalance;

		newBalance = updated.getClearingBalance();

		return newBalance;
	}

	@Override
	public String bankTransactionEntry(String bankId, Double amount, Integer currency, Integer transactionType,
			String description, String refNo) {
		MBank mbank = modifyAndGetCurrentBalanceOfBank(bankId, amount, currency, transactionType);
		Double bankClosingBalance = mbank.getYenBalance();
		Double currentBalance = bankClosingBalance;
		if (transactionType != null && transactionType.equals(Constants.TRANSACTION_CREDIT)) {
			currentBalance -= amount;
		} else if (transactionType == Constants.TRANSACTION_DEBIT) {
			currentBalance += amount;
		} else {
			throw new AAJRuntimeException("Transaction type not found.");
		}
		String paymentVoucherNo = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_PAYMENT_VOUCHER_NO);
		// insert bank transaction entry
		this.bankTransactionRepository.insert(new TBankTransaction(paymentVoucherNo, bankId, currency, refNo,
				description, transactionType, amount, currentBalance, bankClosingBalance));
		return paymentVoucherNo;
	}

}
