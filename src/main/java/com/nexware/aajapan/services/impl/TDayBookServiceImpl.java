package com.nexware.aajapan.services.impl;

import java.util.List;

import org.bson.types.ObjectId;
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
import com.nexware.aajapan.models.MCurrency;
import com.nexware.aajapan.models.TAccountsTransaction;
import com.nexware.aajapan.models.TAdvanceDayBookAllocation;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.models.TCustomerTransaction;
import com.nexware.aajapan.models.TDayBook;
import com.nexware.aajapan.models.TDayBookTransaction;
import com.nexware.aajapan.models.TExchangeRate;
import com.nexware.aajapan.models.TLcDetails;
import com.nexware.aajapan.models.TLcInvoice;
import com.nexware.aajapan.models.TProformaInvoice;
import com.nexware.aajapan.models.TSalesInvoice;
import com.nexware.aajapan.repositories.MCurrencyRepository;
import com.nexware.aajapan.repositories.MasterBankRepository;
import com.nexware.aajapan.repositories.TAdvanceDayBookAllocationRepository;
import com.nexware.aajapan.repositories.TDayBookRepository;
import com.nexware.aajapan.repositories.TDayBookTransactionRepository;
import com.nexware.aajapan.repositories.TExchageRateRepository;
import com.nexware.aajapan.repositories.TLcDetailsRepository;
import com.nexware.aajapan.repositories.TLcInvoiceRepository;
import com.nexware.aajapan.repositories.TProformaInvoiceRepository;
import com.nexware.aajapan.repositories.TSalesInvoiceRepository;
import com.nexware.aajapan.services.BankTransactionService;
import com.nexware.aajapan.services.SalesOrderService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.TAccountsTransactionService;
import com.nexware.aajapan.services.TCustomerTransactionService;
import com.nexware.aajapan.services.TDayBookService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class TDayBookServiceImpl implements TDayBookService {

	@Autowired
	private TAccountsTransactionService accountsTransactionService;
	@Autowired
	private TDayBookTransactionRepository dayBookTransactionRepository;
	@Autowired
	private MasterBankRepository masterBankRepository;

	@Autowired
	private BankTransactionService bankTransactionService;

	@Autowired
	private TCustomerTransactionService customerTransactionService;
	@Autowired
	private TDayBookRepository dayBookRepository;
	@Autowired
	private TLcInvoiceRepository lcInvoiceRepository;

	@Autowired
	private TLcDetailsRepository lcDetailsRepository;
	@Autowired
	private MCurrencyRepository currencyRepository;
	@Autowired
	private TProformaInvoiceRepository proformaInvoiceRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private SalesOrderService salesOrderService;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private TAdvanceDayBookAllocationRepository advanceDayBookAllocationRepository;
	@Autowired
	private TSalesInvoiceRepository salesInvoiceRepository;
	@Autowired
	private TExchageRateRepository tExchageRateRepository;

	@Override
	public void dayBookPaymentApproveTransactions(List<TDayBook> tDayBook) {
		tDayBook.stream().forEach(tDBook ->
		// create account transaction entry - DayBook - Credit
		this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(null, null, tDBook.getCoaNo(),
				Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT, tDBook.getAmount(),
				AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK, tDBook.getRemitDate())));

	}

	@Override
	public void dayBookPaymentBankTransactions(Long coaCode, List<TDayBook> tDayBook) {
		tDayBook.stream().forEach(tDBook -> {
			Double amount = 0.0;
			Double bankAmt = 0.0;
			bankAmt = tDBook.getAmount() - tDBook.getBankCharges();
			if (tDBook.getCurrency() == 1) {
				amount = tDBook.getAmount() - tDBook.getBankCharges();

			} else {
				amount = (tDBook.getAmount() * tDBook.getExchangeRate())
						- (tDBook.getBankCharges() * tDBook.getExchangeRate());

			}

			if (tDBook.getClearingAccount() == 1) {
				this.bankTransactionService.modifyAndGetClearingBalanceOfBank(tDBook.getBank(), bankAmt,
						tDBook.getCurrency(), Constants.TRANSACTION_DEBIT);
				// create account transaction entry - Day Book Bank - Debit
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(null, null,
						this.getClearingAccountCoaCode(tDBook), Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT,
						amount, AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK, tDBook.getRemitDate()));
				//
				// // create account transaction entry - Day Book Bank - Credit
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(null, null,
						AccountTransactionConstants.TRADE_RECEIVABLE_OTHER_CURRENCY_CLEARING, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_CREDIT, amount, AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK,
						tDBook.getRemitDate()));
			} else if (tDBook.getClearingAccount() == 0) {
				// create account transaction entry - Day Book Bank - Debit
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(null, null, coaCode,
						Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, amount,
						AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK, tDBook.getRemitDate()));
				// create account transaction entry - Day Book Bank - Credit
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(null, null,
						AccountTransactionConstants.TRADE_RECEIVABLE_CIF_JPY_CLEARING, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_CREDIT, amount, AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK,
						tDBook.getRemitDate()));
			}

			if (tDBook.getBankCharges() > 0) {
				this.accountsTransactionService.accountTransactionEntry(
						new TAccountsTransaction(null, null, AccountTransactionConstants.BANK_CHARGES_COA,
								Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, tDBook.getBankCharges(),
								AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK, tDBook.getRemitDate()));
			}

		});
	}

	@Override
	public void dayBookPaymentBankTransactions(Long coaCode, TDayBook tDBook) {
		Double amount;
		Double bankAmt;
		bankAmt = tDBook.getAmount() - tDBook.getBankCharges();
		if (tDBook.getCurrency() == 1) {
			amount = tDBook.getAmount() - tDBook.getBankCharges();

		} else {
			amount = (tDBook.getAmount() * tDBook.getExchangeRate())
					- (tDBook.getBankCharges() * tDBook.getExchangeRate());

		}
		if (tDBook.getClearingAccount() == 1) {
			this.bankTransactionService.modifyAndGetClearingBalanceOfBank(tDBook.getBank(), bankAmt,
					tDBook.getCurrency(), Constants.TRANSACTION_DEBIT);
			// create account transaction entry - Day Book Bank - Debit

			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(null, null,
					this.getClearingAccountCoaCode(tDBook), Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, amount,
					AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK, tDBook.getRemarks(),
					tDBook.getRemitDate()));

			// create account transaction entry - Day Book Bank - Credit
			if (tDBook.getCurrency() == 1) {
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(null, null,
						AccountTransactionConstants.TRADE_RECEIVABLE_CIF_JPY_CLEARING, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_CREDIT, amount, AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK,
						tDBook.getRemarks(), tDBook.getRemitDate()));
			} else {
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(null, null,
						AccountTransactionConstants.TRADE_RECEIVABLE_OTHER_CURRENCY_CLEARING, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_CREDIT, amount, AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK,
						tDBook.getRemarks(), tDBook.getRemitDate()));

			}
		} else if (tDBook.getClearingAccount() == 0) {
			// create account transaction entry - Day Book Bank - Debit
			this.accountsTransactionService.accountTransactionEntry(
					new TAccountsTransaction(null, null, coaCode, Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT,
							amount, AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK, tDBook.getRemarks(),
							tDBook.getRemitDate()));
			// create account transaction entry - Day Book Bank - Credit
			if (tDBook.getCurrency() == 1) {
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(null, null,
						AccountTransactionConstants.TRADE_RECEIVABLE_CIF_JPY_CLEARING, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_CREDIT, amount, AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK,
						tDBook.getRemarks(), tDBook.getRemitDate()));
			} else {
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(null, null,
						AccountTransactionConstants.TRADE_RECEIVABLE_OTHER_CURRENCY_DEBTORS, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_CREDIT, amount, AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK,
						tDBook.getRemarks(), tDBook.getRemitDate()));

			}
		}

		if (tDBook.getBankCharges() > 0) {
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(null, null,
					AccountTransactionConstants.BANK_CHARGES_COA, Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT,
					tDBook.getBankCharges(), AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK,
					tDBook.getRemarks(), tDBook.getRemitDate()));
		}

	}

	@Override
	public void dayBookApproveTTOwned(String id) {
		TDayBookTransaction daybook = this.dayBookTransactionRepository.findOneById(id);
		daybook.setPaymentApprove(Constants.DAYBOOK_TRANSACTION_APPROVED);
		this.dayBookTransactionRepository.save(daybook);

		if ((daybook.getAllocationType() == Constants.DAYBOOK_ALLOCATION_FIFO)
				|| (daybook.getAllocationType() == Constants.DAYBOOK_ALLOCATION_UNIT)
				|| (daybook.getAllocationType() == Constants.DAYBOOK_ALLOCATION_LC)) {
			TCustomerTransaction transaction = new TCustomerTransaction(daybook.getCustomerId(), daybook.getStockId(),
					daybook.getCurrency(), Constants.TRANSACTION_CREDIT, daybook.getAllocatedAmount());
			this.customerTransactionService.customerTransactionEntry(transaction);
			if (!AppUtil.isObjectEmpty(daybook.getSalesInvoiceId())) {
				this.salesOrderService.updateReceivedAmount(daybook.getSalesInvoiceId(), daybook.getStockId(),
						daybook.getAllocatedAmount(), daybook.getTransactionType());
			}

		} else if (daybook.getAllocationType() == Constants.DAYBOOK_ALLOCATION_ADVANCE) {
			this.customerTransactionService.updateCustomerBalance(daybook.getCustomerId(), Constants.TRANSACTION_CREDIT,
					daybook.getAllocatedAmount());
		} else if (daybook.getAllocationType() == Constants.DAYBOOK_ALLOCATION_DEPOSITE) {
			this.customerTransactionService.updateCustomerDepositAmount(daybook.getCustomerId(),
					Constants.TRANSACTION_CREDIT, daybook.getAllocatedAmount());

		}
		this.accountTransactionEntry(daybook);
	}

//	@Override
//	public void dayBookCancelTTOwned(String id) {
//		TDayBookTransaction daybook = this.dayBookTransactionRepository.findOneById(id);
//		daybook.setPaymentApprove(Constants.DAYBOOK_TRANSACTION_APPROVED)
//
//		if ((daybook.getAllocationType() == Constants.DAYBOOK_ALLOCATION_FIFO)
//				|| (daybook.getAllocationType() == Constants.DAYBOOK_ALLOCATION_UNIT)
//				|| (daybook.getAllocationType() == Constants.DAYBOOK_ALLOCATION_LC)) {
//			TCustomerTransaction transaction = new TCustomerTransaction(daybook.getCustomerId(), daybook.getStockId(),
//					daybook.getCurrency(), Constants.TRANSACTION_DEBIT, daybook.getAllocatedAmount());
//			this.customerTransactionService.customerTransactionEntry(transaction);
//			if (!AppUtil.isObjectEmpty(daybook.getSalesInvoiceId())) {
//				this.salesOrderService.allocattedAmountForInvoice(daybook.getSalesInvoiceId(), daybook.getStockId(),
//						daybook.getAllocatedAmount(), Constants.TRANSACTION_DEBIT);
//			}
//
//		} else if (daybook.getAllocationType() == Constants.DAYBOOK_ALLOCATION_ADVANCE) {
//			this.customerTransactionService.updateCustomerBalance(daybook.getCustomerId(), Constants.TRANSACTION_CREDIT,
//					daybook.getAllocatedAmount());
//		} else if (daybook.getAllocationType() == Constants.DAYBOOK_ALLOCATION_DEPOSITE) {
//			this.customerTransactionService.updateCustomerDepositAmount(daybook.getCustomerId(),
//					Constants.TRANSACTION_CREDIT, daybook.getAllocatedAmount());
//
//		}
//		this.accountTransactionEntry(daybook);
//	}

	@Override
	public long getClearingAccountCoaCode(TDayBook tDayBook) {
		if ((tDayBook.getCurrency() == Constants.CURRENCY_USD)
				&& (tDayBook.getClearingAccount() == Constants.DAY_BOOK_CLEARING_ACCOUNT)) {
			return AccountTransactionConstants.CLEARING_ACCOUNT;

		} else if ((tDayBook.getCurrency() == Constants.CURRENCY_AUD)
				&& (tDayBook.getClearingAccount() == Constants.DAY_BOOK_CLEARING_ACCOUNT)) {
			return AccountTransactionConstants.AUD_CLEARING_ACCOUNT;

		} else if ((tDayBook.getCurrency() == Constants.CURRENCY_POUND)
				&& (tDayBook.getClearingAccount() == Constants.DAY_BOOK_CLEARING_ACCOUNT)) {
			return AccountTransactionConstants.PND_CLEARING_ACCOUNT;

		}
		return tDayBook.getCoaNo();
	}

	@Override
	public void createDayBookEntry(List<TDayBook> dayBookentry) {
		// create entry
		this.dayBookRepository.insert(dayBookentry);

	}

	@Override
	public void approveDaybookEntry(String id) {
		TDayBook dayBookentry = this.dayBookRepository.findOneByid(id);
		List<TExchangeRate> exchange = tExchageRateRepository.findTop3ByOrderByCreatedDate(dayBookentry.getRemitDate());
		if(AppUtil.isObjectEmpty(exchange)) {
			throw new AAJRuntimeException("Exchange Rate Not Yet Created.Try Again After Sometime");
		}
		dayBookentry.setStatus(Constants.DAYBOOK_ENTRY_APPROVED);
		dayBookentry.setPoundRate(exchange.get(0).getId());
		dayBookentry.setAusDollarRate(exchange.get(1).getId());
		dayBookentry.setDollarRate(exchange.get(2).getId());
		this.dayBookRepository.save(dayBookentry);
		this.masterBankRepository.findOneByBankSeq(dayBookentry.getBank());

		this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(dayBookentry.getDaybookId(),
				dayBookentry.getRemitter(), AccountTransactionConstants.UNRECOGNIZED_TRADE_RECEIVABLE,
				dayBookentry.getCurrency(), Constants.TRANSACTION_CREDIT, dayBookentry.getAmount(),
				AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK, dayBookentry.getRemitDate()));
		final MBank mBank = masterBankRepository.findOneByBankSeq(dayBookentry.getBank());
		accountsTransactionService.accountTransactionEntry(
				new TAccountsTransaction(dayBookentry.getDaybookId(), dayBookentry.getRemitter(), mBank.getCoaCode(),
						dayBookentry.getCurrency(), Constants.TRANSACTION_DEBIT, dayBookentry.getAmount(),
						AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK, dayBookentry.getRemitDate()));

	}

	@Override
	public void createDayBookTransaction(TDayBookTransaction transaction) {
		Query query = new Query(Criteria.where("daybookId").is(transaction.getDaybookId()));
		// update balance
		Update update = new Update();
		TDayBook dayBook;
		if (transaction.getTransactionType() == Constants.TRANSACTION_CREDIT) {
			update.inc("ownedAmount", transaction.getAmount());
		} else if (transaction.getTransactionType() == Constants.TRANSACTION_DEBIT) {
			update.inc("ownedAmount", (transaction.getAmount() * -1));
		}
		// return new balance
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		dayBook = this.mongoTemplate.findAndModify(query, update, options, TDayBook.class);

		if (AppUtil.isObjectEmpty(dayBook)) {
			throw new AAJRuntimeException("Exception while create daybook transaction : " + transaction.getDaybookId());
		}
		if ((dayBook.getOwnedAmount()) > dayBook.getAmount()) {
			throw new AAJRuntimeException(
					"Owned amount is greater than available amount : " + transaction.getDaybookId());
		}
		if (transaction.getAllocationType() == 3) {
			Query customerQuery = new Query(Criteria.where("code").is(transaction.getCustomerId()));
			// update balance
			Update updateCustomer = new Update();
			TCustomer tCustomer;
			if (transaction.getTransactionType() == Constants.TRANSACTION_CREDIT) {
				updateCustomer.inc("advanceAmount", transaction.getAllocatedAmount());
			} else if (transaction.getTransactionType() == Constants.TRANSACTION_DEBIT) {
				updateCustomer.inc("advanceAmount", (transaction.getAllocatedAmount() * -1));
			}
			// return new balance
			FindAndModifyOptions modifyCustomer = new FindAndModifyOptions();
			modifyCustomer.returnNew(true);
			tCustomer = this.mongoTemplate.findAndModify(customerQuery, updateCustomer, modifyCustomer,
					TCustomer.class);
		}
		if (transaction.getAllocationType() == 4) {
			Query customerQuery = new Query(Criteria.where("code").is(transaction.getCustomerId()));
			// update balance
			Update updateCustomer = new Update();
			TCustomer tCustomer;
			if (transaction.getTransactionType() == Constants.TRANSACTION_CREDIT) {
				updateCustomer.inc("depositAmount", transaction.getAllocatedAmount());
			} else if (transaction.getTransactionType() == Constants.TRANSACTION_DEBIT) {
				updateCustomer.inc("depositAmount", (transaction.getAllocatedAmount() * -1));
			}
			// return new balance
			FindAndModifyOptions modifyCustomer = new FindAndModifyOptions();
			modifyCustomer.returnNew(true);
			tCustomer = this.mongoTemplate.findAndModify(customerQuery, updateCustomer, modifyCustomer,
					TCustomer.class);
		}
		this.dayBookTransactionRepository.insert(transaction);

	}

	@Override
	public void deleteDayBookTransaction(TDayBookTransaction transaction) {
		Query query = new Query(Criteria.where("daybookId").is(transaction.getDaybookId()));
		// update balance
		Update update = new Update();
		TDayBook dayBook;
		if (transaction.getTransactionType() == Constants.TRANSACTION_CREDIT) {
			update.inc("ownedAmount", transaction.getAmount());
		} else if (transaction.getTransactionType() == Constants.TRANSACTION_DEBIT) {
			update.inc("ownedAmount", (transaction.getAmount() * -1));
		}
		// return new balance
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		dayBook = this.mongoTemplate.findAndModify(query, update, options, TDayBook.class);

		if (AppUtil.isObjectEmpty(dayBook)) {
			throw new AAJRuntimeException("Exception while update daybook transaction : " + transaction.getDaybookId());
		}
		if ((transaction.getAllocationType() == Constants.DAYBOOK_ALLOCATION_FIFO)
				|| (transaction.getAllocationType() == Constants.DAYBOOK_ALLOCATION_UNIT)) {
			TSalesInvoice tSalesInvoice = salesInvoiceRepository
					.findOneByInvoiceNoAndStockNo(transaction.getSalesInvoiceId(), transaction.getStockId());

			this.salesOrderService.allocattedAmountForInvoice(tSalesInvoice.getInvoiceNo(), tSalesInvoice.getStockNo(),
					transaction.getAllocatedAmount(), Constants.TRANSACTION_DEBIT);
		}
		if ((transaction.getAllocationType() == Constants.DAYBOOK_ALLOCATION_LC)) {
			TLcInvoice tLcInvoice = lcInvoiceRepository.findOneByStockNo(transaction.getStockId());

			this.salesOrderService.allocattedAmountForLcInvoice(tLcInvoice.getId(), transaction.getAllocatedAmount(),
					Constants.TRANSACTION_DEBIT);
		}

		this.dayBookTransactionRepository.deleteById(transaction.getId());

	}

	@Override
	public void accountTransactionEntry(TDayBookTransaction daybook) {

		Double amount;
		Double bankAmt = daybook.getAmount();
		Double soldAmt = 0.0;
		TDayBook tDBook = this.dayBookRepository.findOneByDaybookId(daybook.getDaybookId());

		if (daybook.getCurrency() == 1) {
			amount = daybook.getAmount();

		} else {
			amount = (tDBook.getAmount() * tDBook.getExchangeRate())
					- (tDBook.getBankCharges() * tDBook.getExchangeRate());
			soldAmt = tDBook.getAmount() * daybook.getExchangeRate();

		}
		if (tDBook.getClearingAccount() == 1) {
			this.bankTransactionService.modifyAndGetClearingBalanceOfBank(tDBook.getBank(), bankAmt,
					tDBook.getCurrency(), Constants.TRANSACTION_DEBIT);
			// create account transaction entry - Day Book Bank - Debit

			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(tDBook.getDaybookId(),
					null,
					// this.getClearingAccountCoaCode(tDBook)
					AccountTransactionConstants.UNRECOGNIZED_TRADE_RECEIVABLE, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT, amount, AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK,
					tDBook.getRemarks(), tDBook.getRemitDate()));

			// create account transaction entry - Day Book Bank - Credit
			if (tDBook.getCurrency() == 1) {
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(tDBook.getDaybookId(),
						null, AccountTransactionConstants.TRADE_RECEIVABLE_CIF_JPY_CLEARING, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_CREDIT, amount, AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK,
						tDBook.getRemarks(), tDBook.getRemitDate()));
			} else {
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(tDBook.getDaybookId(),
						null, AccountTransactionConstants.TRADE_RECEIVABLE_OTHER_CURRENCY_CLEARING,
						Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT, soldAmt,
						AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK, tDBook.getRemarks(),
						tDBook.getRemitDate()));

			}
		} else if (tDBook.getClearingAccount() == 0) {
			// create account transaction entry - Day Book Bank - Debit
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(tDBook.getDaybookId(),
					null, tDBook.getCoaNo(), Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, amount,
					AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK, tDBook.getRemarks(),
					tDBook.getRemitDate()));
			// create account transaction entry - Day Book Bank - Credit
			if (tDBook.getCurrency() == 1) {
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(tDBook.getDaybookId(),
						null, AccountTransactionConstants.TRADE_RECEIVABLE_CIF_JPY_DEBTORS, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_CREDIT, amount, AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK,
						tDBook.getRemarks(), tDBook.getRemitDate()));
			} else {
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(tDBook.getDaybookId(),
						null, AccountTransactionConstants.TRADE_RECEIVABLE_OTHER_CURRENCY_DEBTORS,
						Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT, soldAmt,
						AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK, tDBook.getRemarks(),
						tDBook.getRemitDate()));

			}
		}

		if (tDBook.getBankCharges() > 0) {
			if (tDBook.getIsCustomerBankCharge().equals(Constants.IS_AAJ_PAYABLE_YES)) {
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(tDBook.getDaybookId(),
						null, AccountTransactionConstants.BANK_CHARGES_COA, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_DEBIT, tDBook.getBankCharges(),
						AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK, tDBook.getRemarks(),
						tDBook.getRemitDate()));
			}
		}
		Double checkExcRate = daybook.getCurrency().equals(Constants.CURRENCY_YEN) ? 1 : daybook.getExchangeRate();

		if ((daybook.getAmount() * checkExcRate) < (daybook.getAmount() * daybook.getExchangeRate())) {
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(daybook.getTransactionId(),
					null, AccountTransactionConstants.EXCHANGE_GAIN_LOSS, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_CREDIT,
					(daybook.getAmount() * daybook.getExchangeRate()) - (daybook.getAmount() * daybook.getSoExchRate()),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK, tDBook.getRemitDate()));
		} else if ((daybook.getAmount() * checkExcRate) > (daybook.getAmount() * daybook.getExchangeRate())) {
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(daybook.getTransactionId(),
					null, AccountTransactionConstants.EXCHANGE_GAIN_LOSS, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT,
					(daybook.getAmount() * daybook.getSoExchRate()) - (daybook.getAmount() * daybook.getExchangeRate()),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK, tDBook.getRemitDate()));
		}
	}

	@Override
	public void dayBookApproveReAllocate(String id, Integer type, String invoiceNo, String stockNo, Double soExchRate,
			Double ownedAmt) {
		TDayBookTransaction daybook = this.dayBookTransactionRepository.findOneById(id);
		daybook.setSalesInvoiceId(invoiceNo);
		daybook.setStockId(stockNo);
		daybook.setExchangeRate(soExchRate);
		daybook.setAdvanceOwned((daybook.getAdvanceOwned() - ownedAmt));
		this.dayBookTransactionRepository.save(daybook);

		if ((type == Constants.DAYBOOK_ALLOCATION_FIFO) || (type == Constants.DAYBOOK_ALLOCATION_UNIT)) {
			TCustomerTransaction transaction = new TCustomerTransaction(daybook.getCustomerId(), daybook.getStockId(),
					daybook.getCustomerCurrency(), Constants.TRANSACTION_CREDIT, ownedAmt);
			transaction.setTransactionId(id);
			this.customerTransactionService.customerTransactionEntry(transaction);
			if (!AppUtil.isObjectEmpty(daybook.getSalesInvoiceId())) {
				this.salesOrderService.updateReceivedAmount(daybook.getSalesInvoiceId(), daybook.getStockId(), ownedAmt,
						daybook.getTransactionType());
			}

		}

		TAdvanceDayBookAllocation reAllocate = new TAdvanceDayBookAllocation(
				this.sequenceService.getNextSequence(Constants.SEQUENCE_KEY_ADVANCE_DAY_BOOK), daybook.getDaybookId(),
				new ObjectId(daybook.getId()), daybook.getAllocationType(), type);
		advanceDayBookAllocationRepository.insert(reAllocate);
	}

}
