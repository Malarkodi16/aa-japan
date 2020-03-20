package com.nexware.aajapan.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.AccountTransactionConstants;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MBank;
import com.nexware.aajapan.models.MForwarder;
import com.nexware.aajapan.models.MSupplier;
import com.nexware.aajapan.models.MTransporter;
import com.nexware.aajapan.models.TAccountsTransaction;
import com.nexware.aajapan.models.TAdvancePayment;
import com.nexware.aajapan.repositories.MForwarderRepository;
import com.nexware.aajapan.repositories.MasterBankRepository;
import com.nexware.aajapan.repositories.MasterSupplierRepository;
import com.nexware.aajapan.repositories.TAdvancePaymentRepository;
import com.nexware.aajapan.repositories.TransportersRepository;
import com.nexware.aajapan.services.TAccountsTransactionService;
import com.nexware.aajapan.services.TAdvancePaymentService;

@Service
@Transactional
public class TAdvancePaymentServiceImpl implements TAdvancePaymentService {

	@Autowired
	private MasterBankRepository mBankRepository;
	@Autowired
	private TAdvancePaymentRepository advancePaymentRepository;
	@Autowired
	private TAccountsTransactionService accountsTransactionService;
	@Autowired
	private TransportersRepository transportersRepository;
	@Autowired
	private MasterSupplierRepository masterSupplierRepository;
	@Autowired
	private MForwarderRepository mForwarderRepository;

	@Override
	public void advancePaymentCompleteTransactions(TAdvancePayment payment) {
		// Save Advance Payment
		this.advancePaymentRepository.save(payment);
		// Master Bank
		MBank mBank = this.mBankRepository.findOneByBankSeq(payment.getBank());
		// create account transaction entry - Amount - credit
		this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(null, payment.getRemitTo(),
				mBank.getCoaCode(), Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT, payment.getAmount(),
				AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_ADVANCE_PREPAYMENT, payment.getApprovedDate()));

		if (payment.getRemitterType().equalsIgnoreCase(Constants.PAYMENT_BOOKING_PRE_ADVANCE_REMITTER_TYPE_SUPPLIER)) {
			MSupplier supplier = this.masterSupplierRepository.findOneBySupplierCode(payment.getRemitterId());
			// create account transaction entry - Amount - debit
			this.accountsTransactionService.accountTransactionEntry(
					new TAccountsTransaction(null, supplier.getCompany(), AccountTransactionConstants.AUCTION_PAYABLE,
							Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, payment.getAmount(),
							AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_ADVANCE_PREPAYMENT,
							payment.getApprovedDate()));
		} else if (payment.getRemitterType()
				.equalsIgnoreCase(Constants.PAYMENT_BOOKING_PRE_ADVANCE_REMITTER_TYPE_FORWARDER)) {
			MForwarder forwarder = this.mForwarderRepository.findOneByCode(payment.getRemitterId());
			// create account transaction entry - Amount - debit
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(null, forwarder.getName(),
					AccountTransactionConstants.STORAGE_PHOTOS_FREIGHT_SHIPPING_PAYABLE, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT, payment.getAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_ADVANCE_PREPAYMENT,
					payment.getApprovedDate()));
		} else if (payment.getRemitterType()
				.equalsIgnoreCase(Constants.PAYMENT_BOOKING_PRE_ADVANCE_REMITTER_TYPE_TRANSPORTER)) {
			MTransporter transporter = this.transportersRepository.findOneByCode(payment.getRemitterId());
			// create account transaction entry - Amount - debit
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(null,
					transporter.getName(), AccountTransactionConstants.TRANSPORT_RIKUSO_PAYABLE, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT, payment.getAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_ADVANCE_PREPAYMENT,
					payment.getApprovedDate()));
		}

	}

}
