package com.nexware.aajapan.services.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.AccountTransactionConstants;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MBank;
import com.nexware.aajapan.models.MGeneralSupplier;
import com.nexware.aajapan.models.MPaymentCategory;
import com.nexware.aajapan.models.TAccountsTransaction;
import com.nexware.aajapan.models.TInvoice;
import com.nexware.aajapan.models.TInvoicePaymentTransaction;
import com.nexware.aajapan.repositories.InvoicePaymentTransactionRepository;
import com.nexware.aajapan.repositories.MGeneralSupplierRepository;
import com.nexware.aajapan.repositories.MasterBankRepository;
import com.nexware.aajapan.repositories.MasterPaymentRepository;
import com.nexware.aajapan.repositories.TInvoiceRepository;
import com.nexware.aajapan.services.InvoicePaymentTransactionService;
import com.nexware.aajapan.services.TAccountsTransactionService;
import com.nexware.aajapan.services.TInvoiceService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class TInvoiceServiceImpl implements TInvoiceService {
	@Autowired
	private TAccountsTransactionService accountsTransactionService;
	@Autowired
	private MasterPaymentRepository masterPaymentRepository;
	@Autowired
	private MGeneralSupplierRepository generalSupplierRepository;
	@Autowired
	private InvoicePaymentTransactionService invoicePaymentTransactionService;

	@Autowired
	private TInvoiceRepository invoiceRepository;
	@Autowired
	private InvoicePaymentTransactionRepository invoicePaymentTransactionRepository;
	@Autowired
	private MasterBankRepository bankRepository;

	@Override
	public void othersInvoicePaymentApproveTransactions(List<TInvoice> otherInvoices) {
		TInvoice invoice = otherInvoices.get(0);
		String refInvoiceNo = invoice.getInvoiceNo();
		String checkRemitter = invoice.getRemitter();
		Double totalPosAmount = otherInvoices.stream().filter(posInvoice -> posInvoice.getTaxIncludedAmount() >= 0)
				.mapToDouble(TInvoice::getTaxIncludedAmount).sum();
		Double totalNegAmount = otherInvoices.stream().filter(negInvoice -> negInvoice.getTaxIncludedAmount() < 0)
				.mapToDouble(TInvoice::getTaxIncludedAmount).sum();

		if (checkRemitter == "others") {
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(refInvoiceNo,
					otherInvoices.get(0).getRemitterOthers(), AccountTransactionConstants.OTHER_PAYABLE,
					Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT, totalPosAmount,
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_OTHERS_PAYMENT, invoice.getInvoiceDate()));
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(refInvoiceNo,
					otherInvoices.get(0).getRemitterOthers(), AccountTransactionConstants.OTHER_PAYABLE,
					Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, totalNegAmount * (-1),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_OTHERS_PAYMENT, invoice.getInvoiceDate()));
		} else {
			MGeneralSupplier mGenSupplier = this.generalSupplierRepository.findOneByCode(checkRemitter);
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(refInvoiceNo,
					mGenSupplier.getName(), AccountTransactionConstants.OTHER_PAYABLE, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_CREDIT, totalPosAmount,
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_OTHERS_PAYMENT, invoice.getInvoiceDate()));
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(refInvoiceNo,
					mGenSupplier.getName(), AccountTransactionConstants.OTHER_PAYABLE, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT, totalNegAmount * (-1),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_OTHERS_PAYMENT, invoice.getInvoiceDate()));
		}

		otherInvoices.stream().forEach(a -> {
			MGeneralSupplier mGenSupplier = this.generalSupplierRepository.findOneByCode(checkRemitter);
			MPaymentCategory category = this.masterPaymentRepository.findOneByCategoryCode(a.getCategory());
			if (AppUtil.isObjectEmpty(category)) {
				throw new AAJRuntimeException("Category code not found!");
			}
			// create account transaction entry - tinvoice - debit
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(refInvoiceNo,
					mGenSupplier.getName(), category.getCoaCode(), Constants.CURRENCY_YEN,
					a.getTaxIncludedAmount() > 0 ? Constants.TRANSACTION_DEBIT : Constants.TRANSACTION_CREDIT,
					a.getTaxIncludedAmount() < 0 ? a.getTaxIncludedAmount() * (-1) : a.getTaxIncludedAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_OTHERS_PAYMENT, a.getRemarks(),
					invoice.getInvoiceDate()));
		});
	}

	@Override
	public void completePayment(TInvoice invoice) {
		String invoiceNo = invoice.getInvoiceNo();
		String supplierName = invoice.getRemitter();
		Date invoiceDate = invoice.getInvoiceDate();
		final List<TInvoicePaymentTransaction> transactions = invoicePaymentTransactionRepository
				.findAllByInvoiceNoAndStatus(invoiceNo, Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED);
		for (TInvoicePaymentTransaction transaction : transactions) {
			final MBank bank = bankRepository.findOneByBankSeq(transaction.getBankId());
			// create account transaction entry - tinvoice cost
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo, supplierName,
					bank.getCoaCode(), bank.getCurrencyType(), Constants.TRANSACTION_CREDIT, transaction.getAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_OTHERS_PAYMENT, transaction.getCode(),
					invoiceDate));

			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo, supplierName,
					AccountTransactionConstants.OTHER_PAYABLE, Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT,
					transaction.getAmount(), AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_OTHERS_PAYMENT,
					transaction.getCode(), invoiceDate));
		}

	}

	@Override
	public void editGeneralExpenses(List<TInvoice> tInvoice) {
		final String invoiceNo = tInvoice.get(0).getInvoiceNo();
		List<TInvoice> data = invoiceRepository.findAllByInvoiceNo(invoiceNo);
		final Integer invoiceUpload = data.get(0).getInvoiceUpload();
		final Integer attachmentViewed = data.get(0).getAttachementViewed();
		final String invoiceAttachmentFilename = data.get(0).getInvoiceAttachmentFilename();
		final String invoiceAttachmentDiskFilename = data.get(0).getInvoiceAttachmentDiskFilename();
		tInvoice.stream().forEach(invoice -> {
			TInvoice oldInv = invoiceRepository.findOneById(invoice.getId());
			if (!AppUtil.isObjectEmpty(oldInv)) {
				invoice.setInvoiceNo(invoiceNo);
				invoice.setInvoiceUpload(oldInv.getInvoiceUpload());
				invoice.setAttachementViewed(oldInv.getAttachementViewed());
				invoice.setInvoiceAttachmentFilename(oldInv.getInvoiceAttachmentFilename());
				invoice.setInvoiceAttachmentDiskFilename(oldInv.getInvoiceAttachmentDiskFilename());
				invoice.setCreatedDate(oldInv.getCreatedDate());
				invoice.setCreatedBy(oldInv.getCreatedBy());
				invoice.setDeleteFlag(oldInv.getDeleteFlag());
				invoiceRepository.save(invoice);
			} else {
				TInvoice newInv = new TInvoice();
				newInv.setInvoiceNo(invoiceNo);
				newInv.setRemitter(invoice.getRemitter());
				newInv.setRefNo(invoice.getRefNo());
				newInv.setCategory(invoice.getCategory());
				newInv.setInvoiceDate(invoice.getInvoiceDate());
				newInv.setDueDate(invoice.getDueDate());
				newInv.setAmountInYen(invoice.getAmountInYen());
				newInv.setSourceCurrency(invoice.getSourceCurrency());
				newInv.setDescription(invoice.getDescription());
				newInv.setPaymentApprove(Constants.PAYMENT_NOT_APPROVED);
				newInv.setDeleteFlag(Constants.DELETE_FLAG_0);
				newInv.setTaxInclusive(invoice.isTaxInclusive());
				newInv.setTaxPercentage(invoice.getTaxPercentage());
				newInv.setTaxAmount(invoice.getTaxAmount());
				newInv.setTaxIncludedAmount(invoice.getTaxIncludedAmount());
				newInv.setAmount(invoice.getAmount());
				newInv.setExchangeRate(invoice.getExchangeRate());
				newInv.setInvoiceUpload(invoiceUpload);
				newInv.setAttachementViewed(attachmentViewed);
				newInv.setInvoiceAttachmentFilename(invoiceAttachmentFilename);
				newInv.setInvoiceAttachmentDiskFilename(invoiceAttachmentDiskFilename);
				invoiceRepository.insert(newInv);

			}

		});

	}

	@Override
	public void deleteGeneralExpenses(List<TInvoice> tInvoice) {
		tInvoice.stream().forEach(invoice -> {
			invoice.setDeleteFlag(Constants.DELETE_FLAG_1);
		});

		invoiceRepository.saveAll(tInvoice);
	}

	@Override
	public void revertPaymentTransaction(String invoiceNo, Double amount) {
		final List<TInvoice> invoices = invoiceRepository.findAllByInvoiceNo(invoiceNo);
		// calculate invoice total
		final Double invoiceTotal = invoices.stream().mapToDouble(TInvoice::getTaxIncludedAmount).sum();

		// get total paid amount
		final Double totalPaidAmount = invoicePaymentTransactionService
				.getTotalTransactionAmount(Constants.INVOICE_TYPE_OTHERS, invoiceNo);

		final Double amountAfterTransaction = totalPaidAmount - amount;
		Integer paymentStatus;
		if (amountAfterTransaction >= invoiceTotal) {
			paymentStatus = Constants.PAYMENT_COMPLETED;
		} else if (amountAfterTransaction > 0) {
			paymentStatus = Constants.PAYMENT_PARTIAL;
		} else {
			paymentStatus = Constants.PAYMENT_APPROVED;
		}
		for (final TInvoice invoice : invoices) {
			invoice.setPaymentApprove(paymentStatus);
			invoice.setInvoiceAmountReceived(amountAfterTransaction);
		}
		invoiceRepository.saveAll(invoices);
	}

}
