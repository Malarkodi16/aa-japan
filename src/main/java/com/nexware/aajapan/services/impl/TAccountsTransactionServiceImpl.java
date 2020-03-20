package com.nexware.aajapan.services.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.AccountTransactionConstants;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MCOA;
import com.nexware.aajapan.models.MCurrency;
import com.nexware.aajapan.models.TAccountsTransaction;
import com.nexware.aajapan.models.TDayBook;
import com.nexware.aajapan.repositories.AccountsTransactionRepository;
import com.nexware.aajapan.repositories.MCurrencyRepository;
import com.nexware.aajapan.repositories.MasterBankRepository;
import com.nexware.aajapan.repositories.TBankTransactionRepository;
import com.nexware.aajapan.services.BankTransactionService;
import com.nexware.aajapan.services.COATransactionService;
import com.nexware.aajapan.services.MGeneralLedgerService;
import com.nexware.aajapan.services.TAccountsTransactionService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class TAccountsTransactionServiceImpl implements TAccountsTransactionService {

	@Autowired
	private COATransactionService transactionService;
	@Autowired
	private AccountsTransactionRepository accountsTransactionRepository;

	@Autowired
	private MCurrencyRepository currencyRepository;
	@Autowired
	private MasterBankRepository masterBankRepository;
	@Autowired
	private BankTransactionService bankTransactionService;
	@Autowired
	private TBankTransactionRepository bankTransactionRepository;

	@Autowired
	private MGeneralLedgerService generalLedgerService;

	@Override
	public void accountTransactionEntry(TAccountsTransaction accountsTransaction) {

		if (AppUtil.isObjectEmpty(accountsTransaction.getAmount()) || (accountsTransaction.getAmount() == 0)) {
			return;
		}
		// update and get current balance
		Double balance = null;
		MCOA mcoa = this.transactionService.modifyAndGetCurrentCOA(accountsTransaction.getCode(),
				accountsTransaction.getAmount(), accountsTransaction.getType());
		Double closingBalance = mcoa.getBalance();
		if (accountsTransaction.getType() == Constants.TRANSACTION_CREDIT) {
			balance = closingBalance + (accountsTransaction.getAmount() * -1);
		} else if (accountsTransaction.getType() == Constants.TRANSACTION_DEBIT) {
			balance = closingBalance + accountsTransaction.getAmount();
		}
		String transactionId = this.generalLedgerService.getSequenceCodeByLedgerId(mcoa.getGeneralLedger());
		accountsTransaction.setTransactionId(transactionId);
		accountsTransaction.setClosingBalance(closingBalance);
		accountsTransaction.setBalance(balance);
		// set transaction type
		if (!AppUtil.isObjectEmpty(accountsTransaction.getStockNo())) {
			accountsTransaction.setCategory(AccountTransactionConstants.TRANSACTION_CATEGORY_STOCK);
		} else {
			accountsTransaction.setCategory(AccountTransactionConstants.TRANSACTION_CATEGORY_OTHERS);
		}

		// set is clearing account
		if (this.isClearingBalance(accountsTransaction)) {
			accountsTransaction.setClearingAccount(Constants.DAY_BOOK_CLEARING_ACCOUNT);
		} else {
			accountsTransaction.setClearingAccount(Constants.DAY_BOOK_NOT_CLEARED_ACCOUNT);
		}
		// set exchange rate
		MCurrency currency = this.currencyRepository.findOneByCurrencySeq(Constants.CURRENCY_USD);
		if (AppUtil.isObjectEmpty(currency)) {
			throw new AAJRuntimeException("Currency not found!");
		}
		accountsTransaction.setExchangeRate(currency.getExchangeRate());
		// insert account transaction record
		if (AppUtil.isObjectEmpty(accountsTransaction.getTransactionDate())) {
			accountsTransaction.setTransactionDate(new Date());
		}
		this.accountsTransactionRepository.insert(accountsTransaction);

	}

	@Override
	public boolean isClearingBalance(TAccountsTransaction accountsTransaction) {
		boolean booleanCheck = false;
		if (accountsTransaction.getSource().equalsIgnoreCase(AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK)
				&& ((accountsTransaction.getCode() == AccountTransactionConstants.CLEARING_ACCOUNT)
						|| (accountsTransaction.getCode() == AccountTransactionConstants.AUD_CLEARING_ACCOUNT)
						|| (accountsTransaction.getCode() == AccountTransactionConstants.PND_CLEARING_ACCOUNT))) {
			booleanCheck = true;
		}
		return booleanCheck;

	}
}
