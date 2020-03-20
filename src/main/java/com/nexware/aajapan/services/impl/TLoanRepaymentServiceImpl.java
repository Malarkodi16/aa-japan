package com.nexware.aajapan.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.AccountTransactionConstants;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MBank;
import com.nexware.aajapan.models.TAccountsTransaction;
import com.nexware.aajapan.models.TLoan;
import com.nexware.aajapan.models.TLoanDetails;
import com.nexware.aajapan.models.TLoanRepayment;
import com.nexware.aajapan.repositories.MasterBankRepository;
import com.nexware.aajapan.repositories.TLoanRepaymentRepository;
import com.nexware.aajapan.services.BankTransactionService;
import com.nexware.aajapan.services.TAccountsTransactionService;
import com.nexware.aajapan.services.TLoanRepaymentService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class TLoanRepaymentServiceImpl implements TLoanRepaymentService {
	@Autowired
	private TLoanRepaymentRepository loanRepaymentRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private BankTransactionService bankTransactionService;
	@Autowired
	private TAccountsTransactionService accountsTransactionService;
	@Autowired
	private MasterBankRepository masterBankRepository;

	@Override
	public void rePayLoan(TLoanRepayment loanDto) {
		// re payment insert
		TLoanRepayment loan = new TLoanRepayment(loanDto.getLoanDtlId(), loanDto.getLoanId(), loanDto.getBank(),
				loanDto.getInstallmentAmount(), loanDto.getLoanType(), loanDto.getSavingAccount(),
				loanDto.getSavingsAccountAmount(), loanDto.getSavingsBankAccount(), loanDto.getPaymentDate());
		this.loanRepaymentRepository.insert(loan);
		// update loan details status
		updateLoanDetailsStatus(loanDto.getLoanId(), loanDto.getLoanDtlId());
		// update loan loan closing amount
		updateLoanClosingBalance(loanDto.getLoanId(), loanDto.getInstallmentAmount());
		// savings bank transaction entry
		bankTransactionService.modifyAndGetCurrentBalanceOfBank(loanDto.getBank(), loanDto.getInstallmentAmount(),
				Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT);
		// Account transaction credit entry
		MBank mBank = this.masterBankRepository.findOneByBankSeq(loanDto.getBank());
		// create account transaction entry - Amount - credit
		this.accountsTransactionService
				.accountTransactionEntry(new TAccountsTransaction(loanDto.getLoanId(), null, mBank.getCoaCode(),
						Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT, loanDto.getInstallmentAmount(),
						AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_LOAN_REPAYMENT, null));
		// create account transaction entry - Amount - debit
		this.accountsTransactionService.accountTransactionEntry(
				new TAccountsTransaction(loanDto.getLoanId(), null, AccountTransactionConstants.LOAN_PRINCIPLE,
						Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, loanDto.getInstallmentAmount(),
						AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_LOAN_REPAYMENT, null));
	}

	private void updateLoanClosingBalance(String loanId, Double amount) {
		Query query = new Query(Criteria.where("loanId").is(loanId));
		// update closing balance
		Update update = new Update();
		update.inc("closingBalance", (amount * -1));
		// return new closing balance
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(false);
		TLoan updated = this.mongoTemplate.findAndModify(query, update, options, TLoan.class);
		if (AppUtil.isObjectEmpty(updated)) {
			throw new AAJRuntimeException("Exception while update balance : " + loanId);
		}

	}

	private void updateLoanDetailsStatus(String loanId, String loanDtlId) {
		Query query;
		if (AppUtil.isObjectEmpty(loanDtlId)) {
			query = new Query(Criteria.where("loanId").is(loanId));
		} else {
			query = new Query(
					Criteria.where("loanId").is(loanId).andOperator(Criteria.where("loanDtlId").is(loanDtlId)));
		}

		// update status
		Update update = new Update();
		update.inc("status", Constants.LOAN_REPAYMENT_PAID);
		// return new status
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(false);
		TLoanDetails updated = this.mongoTemplate.findAndModify(query, update, options, TLoanDetails.class);
		if (AppUtil.isObjectEmpty(updated)) {
			throw new AAJRuntimeException("Exception while update balance : " + loanDtlId);
		}
	}

}
