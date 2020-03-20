package com.nexware.aajapan.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.AccountTransactionConstants;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.AuctionPaymentType;
import com.nexware.aajapan.models.MBank;
import com.nexware.aajapan.models.MSupplier;
import com.nexware.aajapan.models.TAccountsTransaction;
import com.nexware.aajapan.models.TCancelledInvoices;
import com.nexware.aajapan.models.TInventoryCost;
import com.nexware.aajapan.models.TInvoicePaymentTransaction;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.models.TReAuction;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.models.TSupplierTransaction;
import com.nexware.aajapan.repositories.AuctionPaymentTypeRepository;
import com.nexware.aajapan.repositories.InvoicePaymentTransactionRepository;
import com.nexware.aajapan.repositories.MasterBankRepository;
import com.nexware.aajapan.repositories.MasterSupplierRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TCancelledInvoiceRepo;
import com.nexware.aajapan.repositories.TPurchaseInvoiceRepository;
import com.nexware.aajapan.repositories.TReAuctionRepository;
import com.nexware.aajapan.services.BankTransactionService;
import com.nexware.aajapan.services.COATransactionService;
import com.nexware.aajapan.services.InvoicePaymentTransactionService;
import com.nexware.aajapan.services.MSupplierService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.TAccountsTransactionService;
import com.nexware.aajapan.services.TInventoryCostService;
import com.nexware.aajapan.services.TPurchaseInvoiceService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class TPurchaseInvoiceServiceImpl implements TPurchaseInvoiceService {

	@Autowired
	private TAccountsTransactionService accountsTransactionService;
	@Autowired
	private MasterSupplierRepository masterSupplierRepository;
	@Autowired
	private TPurchaseInvoiceRepository purchaseInvoiceRepository;
	@Autowired
	private MasterBankRepository mBankRepository;
	@Autowired
	private InvoicePaymentTransactionService invoicePaymentTransactionService;
	@Autowired
	private InvoicePaymentTransactionRepository invoicePaymentTransactionRepository;
	@Autowired
	private TInventoryCostService inventoryCostService;
	@Autowired
	private MSupplierService supplierService;
	@Autowired
	private COATransactionService coaTransactionService;
	@Autowired
	private AuctionPaymentTypeRepository auctionPaymentTypeRepository;

	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private TReAuctionRepository reauctionRepository;
	@Autowired
	private BankTransactionService bankTransactionService;
	@Autowired
	private StockRepository stkRepo;
	@Autowired
	private TCancelledInvoiceRepo cancelledinvoiceRepo;

	@Override
	public void paymentApproveTransactions(List<TPurchaseInvoice> auctionInvoices) {

		purchaseInvoiceRepository.saveAll(auctionInvoices);

		final List<TPurchaseInvoice> purchasedList = auctionInvoices.stream()
				.filter(invoice -> invoice.getStatus() == Constants.INV_STATUS_VERIFIED).collect(Collectors.toList());
		final List<TPurchaseInvoice> penalityChargeList = auctionInvoices.stream().filter(
				invoice -> invoice.getType().equalsIgnoreCase(Constants.PURCHASE_INVOICE_ITEM_TYPE_PENALTY_CHARGE)
						&& (invoice.getStatus() == Constants.INV_STATUS_VERIFIED))
				.collect(Collectors.toList());
		/*
		 * final List<TPurchaseInvoice> penalityClaimedList =
		 * auctionInvoices.stream().filter( invoice ->
		 * invoice.getType().equalsIgnoreCase(Constants.
		 * PURCHASE_INVOICE_ITEM_TYPE_PENALTY_CLAIMED) && (invoice.getStatus() ==
		 * Constants.INV_STATUS_VERIFIED)) .collect(Collectors.toList());
		 */

		final List<TPurchaseInvoice> penalityDocumentList = auctionInvoices.stream().filter(
				invoice -> invoice.getType().equalsIgnoreCase(Constants.PURCHASE_INVOICE_ITEM_TYPE_DOCUMENT_PENALTY)
						&& (invoice.getStatus() == Constants.INV_STATUS_VERIFIED))
				.collect(Collectors.toList());
		final List<TPurchaseInvoice> penalityLateList = auctionInvoices.stream()
				.filter(invoice -> invoice.getType().equalsIgnoreCase(Constants.PURCHASE_INVOICE_ITEM_TYPE_LATE_PENALTY)
						&& (invoice.getStatus() == Constants.INV_STATUS_VERIFIED))
				.collect(Collectors.toList());
		final List<TPurchaseInvoice> roadtaxClaimedList = auctionInvoices.stream().filter(
				invoice -> invoice.getType().equalsIgnoreCase(Constants.PURCHASE_INVOICE_ITEM_TYPE_ROADTAX_CLAIMED)
						&& (invoice.getStatus() == Constants.INV_STATUS_VERIFIED))
				.collect(Collectors.toList());
		final List<TPurchaseInvoice> cashBackList = auctionInvoices.stream()
				.filter(invoice -> invoice.getType().equalsIgnoreCase(Constants.PURCHASE_INVOICE_ITEM_TYPE_CASH_BACK)
						&& (invoice.getStatus() == Constants.INV_STATUS_VERIFIED))
				.collect(Collectors.toList());
		final List<TPurchaseInvoice> rikusoPaymentList = auctionInvoices.stream().filter(
				invoice -> invoice.getType().equalsIgnoreCase(Constants.PURCHASE_INVOICE_ITEM_TYPE_RIKUSO_PAYMENT)
						&& (invoice.getStatus() == Constants.INV_STATUS_VERIFIED))
				.collect(Collectors.toList());
		final List<TPurchaseInvoice> membershipList = auctionInvoices.stream()
				.filter(invoice -> invoice.getType().equalsIgnoreCase(Constants.PURCHASE_INVOICE_ITEM_TYPE_MEMBERSHIP)
						&& (invoice.getStatus() == Constants.INV_STATUS_VERIFIED))
				.collect(Collectors.toList());

		final List<TPurchaseInvoice> cancelledList = auctionInvoices.stream().filter(
				invoice -> invoice.getType().equalsIgnoreCase(Constants.PURCHASE_INVOICE_ITEM_TYPE_CANCELLATION_CHARGES)
						&& (invoice.getStatus() == Constants.INV_STATUS_VERIFIED))
				.collect(Collectors.toList());
		final List<TPurchaseInvoice> reauctionList = auctionInvoices.stream()
				.filter(invoice -> invoice.getType().equalsIgnoreCase(Constants.PURCHASE_INVOICE_ITEM_TYPE_REAUCTION))
				.collect(Collectors.toList());
		final List<TPurchaseInvoice> nagareChargeList = auctionInvoices.stream()
				.filter(invoice -> invoice.getType()
						.equalsIgnoreCase(Constants.PURCHASE_INVOICE_ITEM_TYPE_UNSOLD_AUCTION_CHARGE))
				.collect(Collectors.toList());
		final List<TPurchaseInvoice> recycleClaimedList = auctionInvoices.stream().filter(
				invoice -> invoice.getType().equalsIgnoreCase(Constants.PURCHASE_INVOICE_ITEM_TYPE_RECYCLE_CLAIMED))
				.collect(Collectors.toList());
		final List<TPurchaseInvoice> recyclePaidList = auctionInvoices.stream().filter(
				invoice -> invoice.getType().equalsIgnoreCase(Constants.PURCHASE_INVOICE_ITEM_TYPE_RECYCLE_PAID))
				.collect(Collectors.toList());
		final List<TPurchaseInvoice> carTaxPaidList = auctionInvoices.stream().filter(
				invoice -> invoice.getType().equalsIgnoreCase(Constants.PURCHASE_INVOICE_ITEM_TYPE_ROADTAX_PAID))
				.collect(Collectors.toList());
		final List<TPurchaseInvoice> othersList = auctionInvoices.stream()
				.filter(invoice -> invoice.getType().contains(Constants.INVOICE_TYPE_OTHERS))
				.collect(Collectors.toList());

		if (!purchasedList.isEmpty()) {
			this.typePurchasePaymentApproveTransactions(purchasedList);
		}
		if (!cancelledList.isEmpty()) {
			this.typeCancelPaymentApproveTransactions(cancelledList);
		}
		if (!reauctionList.isEmpty()) {
			this.typeReauctionPaymentApproveTransactions(reauctionList);
		}
		if (!nagareChargeList.isEmpty()) {
			this.typeNagareChargePaymentApproveTransactions(nagareChargeList);
		}
		if (!penalityChargeList.isEmpty()) {
			this.typePenalityPaidPaymentApproveTransactions(penalityChargeList);
		}
		/*
		 * if (!penalityClaimedList.isEmpty()) {
		 * this.typePenalityClaimedPaymentApproveTransactions(penalityClaimedList); }
		 */
		if (!penalityDocumentList.isEmpty()) {
			this.typePenalityClaimedPaymentApproveTransactions(penalityDocumentList);
		}
		if (!penalityLateList.isEmpty()) {
			this.typePenalityClaimedPaymentApproveTransactions(penalityLateList);
		}
		if (!roadtaxClaimedList.isEmpty()) {
			this.typeRoadTaxClaimedPaymentApproveTransactions(roadtaxClaimedList);
		}
		if (!cashBackList.isEmpty()) {
			this.typeCashbackApproveTransactions(cashBackList);
		}
		if (!rikusoPaymentList.isEmpty()) {
			this.typeRikusoPaymentPaymentApproveTransactions(rikusoPaymentList);
		}
		if (!membershipList.isEmpty()) {
			this.typeMemershipPaymentApproveTransactions(membershipList);
		}
		if (!recycleClaimedList.isEmpty()) {
			this.typeRecycleClaimedPaymentApproveTransactions(recycleClaimedList);
		}
		if (!recyclePaidList.isEmpty()) {
			this.typeRecyclePaidPaymentApproveTransactions(recyclePaidList);
		}
		if (!carTaxPaidList.isEmpty()) {
			this.typeCarTaxPaidPaymentApproveTransactions(carTaxPaidList);
		}
		if (!othersList.isEmpty()) {
			this.typeOtherPaymentApproveTransactions(othersList);
		}

		// Inventory Cost List Save
		final List<TInventoryCost> inventoryCosts = new ArrayList<>();
		for (final TPurchaseInvoice tPurchaseInvoice : auctionInvoices) {
			if (tPurchaseInvoice.getPurchaseCost() > 0) {

				final TInventoryCost cost = new TInventoryCost(Constants.COST_OF_GOODS_TYPE_PURCHASE,
						tPurchaseInvoice.getStockNo(), tPurchaseInvoice.getPurchaseCost(),
						tPurchaseInvoice.getInvoiceNo());
				inventoryCosts.add(cost);
			}
			if (tPurchaseInvoice.getPurchaseCostTax() > 0) {

				final TInventoryCost cost = new TInventoryCost(Constants.COST_OF_GOODS_TYPE_PURCHASE_TAX,
						tPurchaseInvoice.getStockNo(), tPurchaseInvoice.getPurchaseCostTaxAmount(),
						tPurchaseInvoice.getInvoiceNo());
				inventoryCosts.add(cost);
			}
			if (tPurchaseInvoice.getCommision() > 0) {

				final TInventoryCost cost = new TInventoryCost(Constants.COST_OF_GOODS_TYPE_PURCHASE_COMM,
						tPurchaseInvoice.getStockNo(), tPurchaseInvoice.getCommision(),
						tPurchaseInvoice.getInvoiceNo());
				inventoryCosts.add(cost);
			}
			if (tPurchaseInvoice.getCommisionTax() > 0) {

				final TInventoryCost cost = new TInventoryCost(Constants.COST_OF_GOODS_TYPE_PURCHASE_COMM_TAX,
						tPurchaseInvoice.getStockNo(), tPurchaseInvoice.getCommisionTaxAmount(),
						tPurchaseInvoice.getInvoiceNo());
				inventoryCosts.add(cost);
			}

			if (tPurchaseInvoice.getRecycle() > 0) {

				final TInventoryCost cost = new TInventoryCost(Constants.COST_OF_GOODS_TYPE_PURCHASE_RECYCLE,
						tPurchaseInvoice.getStockNo(), tPurchaseInvoice.getRecycle(), tPurchaseInvoice.getInvoiceNo());
				inventoryCosts.add(cost);
			}

			if (tPurchaseInvoice.getRoadTax() > 0) {

				final TInventoryCost cost = new TInventoryCost(Constants.COST_OF_GOODS_TYPE_PURCHASE_ROADTAX,
						tPurchaseInvoice.getStockNo(), tPurchaseInvoice.getRoadTax(), tPurchaseInvoice.getInvoiceNo());
				inventoryCosts.add(cost);
			}
			if (tPurchaseInvoice.getOtherCharges() > 0 && !AppUtil.isObjectEmpty(tPurchaseInvoice.getChassisNo())) {

				final TInventoryCost cost = new TInventoryCost(Constants.COST_OF_GOODS_TYPE_PURCHASE_OTHERS,
						tPurchaseInvoice.getStockNo(), tPurchaseInvoice.getOtherCharges(),
						tPurchaseInvoice.getInvoiceNo());
				inventoryCosts.add(cost);
			}
			if (tPurchaseInvoice.getOthersCostTaxAmount() > 0
					&& !AppUtil.isObjectEmpty(tPurchaseInvoice.getChassisNo())) {

				final TInventoryCost cost = new TInventoryCost(Constants.COST_OF_GOODS_TYPE_PURCHASE_OTHERS_TAX,
						tPurchaseInvoice.getStockNo(), tPurchaseInvoice.getOthersCostTaxAmount(),
						tPurchaseInvoice.getInvoiceNo());
				inventoryCosts.add(cost);
			}

		}
		inventoryCostService.saveInventoryCost(inventoryCosts);
	}

	private void typePurchasePaymentApproveTransactions(List<TPurchaseInvoice> auctionInvoices) {
		Double totalAmount = 0.0;
		for (final TPurchaseInvoice invoice : auctionInvoices) {
			if (invoice.getOtherCharges() >= 0) {
				totalAmount += invoice.getTotalTaxIncluded();
			}
		}

		final TPurchaseInvoice firstTPurchaseInvoice = auctionInvoices.stream().findFirst()
				.orElseThrow(() -> new AAJRuntimeException("Exception while account transaction entry."));
		final String invoiceNo = firstTPurchaseInvoice.getInvoiceNo();
		final Date invoiceDate = firstTPurchaseInvoice.getInvoiceDate();
		final MSupplier supplier = masterSupplierRepository
				.findOneBySupplierCode(firstTPurchaseInvoice.getSupplierId());

		// create account transaction entry - account payable credit entry
		accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo, supplier.getCompany(),
				AccountTransactionConstants.AUCTION_PAYABLE, Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT,
				totalAmount, AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

		auctionInvoices.stream().forEach(a -> {
			if (a.getType().equalsIgnoreCase(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE)) {
				// create account transaction entry - purchase cost - debit
				accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
						supplier.getCompany(), AccountTransactionConstants.INVENTORY_GENERAL, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_DEBIT, a.getPurchaseCost(),
						AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

				// create account transaction entry - commision - debit
				accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
						supplier.getCompany(), AccountTransactionConstants.INVENTORY_GENERAL, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_DEBIT, a.getCommision(),
						AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

				// create account transaction entry - recycle - debit
				accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
						supplier.getCompany(), AccountTransactionConstants.RECYCLE_RECOVERABLE, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_DEBIT, a.getRecycle(),
						AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

				// create account transaction entry - road tax - debit
				if (a.getRoadTax() > 0) {
					accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
							supplier.getCompany(), AccountTransactionConstants.CAR_TAX_CLEARING_ACCOUNT,
							Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, a.getRoadTax(),
							AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
				}

				// create account transaction entry - purchase cost tax - debit
				accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
						supplier.getCompany(), AccountTransactionConstants.CONSUMPTION_TAX, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_DEBIT, a.getPurchaseCostTaxAmount(),
						AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

				// create account transaction entry - commission tax - debit
				accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
						supplier.getCompany(), AccountTransactionConstants.CONSUMPTION_TAX, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_DEBIT, a.getCommisionTaxAmount(),
						AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
				// // other charges entry
				//
				accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
						supplier.getCompany(), AccountTransactionConstants.INVENTORY_GENERAL, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_DEBIT, a.getOtherCharges(),
						AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
			}
		});

	}

	private void typeReauctionPaymentApproveTransactions(List<TPurchaseInvoice> auctionInvoices) {

		final TPurchaseInvoice firstTPurchaseInvoice = auctionInvoices.stream().findFirst()
				.orElseThrow(() -> new AAJRuntimeException("Exception while account transaction entry."));
		final String invoiceNo = firstTPurchaseInvoice.getInvoiceNo();
		final Date invoiceDate = firstTPurchaseInvoice.getInvoiceDate();
		final MSupplier supplier = masterSupplierRepository
				.findOneBySupplierCode(firstTPurchaseInvoice.getSupplierId());

		// create account transaction entry - Cost of Good Sold - Re Auction
		auctionInvoices.stream().forEach(reauctionInvoice -> {
			final TPurchaseInvoice purchaseInvoice = purchaseInvoiceRepository.findOneByStockNoAndType(
					reauctionInvoice.getStockNo(), Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE);
			// old invoice
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.COST_OF_GOOD_SOLD_RE_AUCTION,
					Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT,
					purchaseInvoice.getPurchaseCost() + purchaseInvoice.getCommision()
							+ reauctionInvoice.getCommision(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.INVENTORY_GENERAL, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_CREDIT, purchaseInvoice.getPurchaseCost() + purchaseInvoice.getCommision(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.CONSUMPTION_TAX, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_CREDIT, purchaseInvoice.getPurchaseCostTaxAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.CONSUMPTION_TAX, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_CREDIT, purchaseInvoice.getCommisionTaxAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

			// new invoice
			// Purchase cost
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.AUCTION_PAYABLE, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT, Math.abs(reauctionInvoice.getPurchaseCost()),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.REVENUE_LOCAL_OTHER, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_CREDIT, Math.abs(reauctionInvoice.getPurchaseCost()),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.AUCTION_PAYABLE, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_CREDIT, reauctionInvoice.getCommision(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

			// create account transaction entry - commission tax - debit
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.AUCTION_PAYABLE, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_CREDIT, reauctionInvoice.getCommisionTaxAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.CONSUMPTION_TAX, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT, reauctionInvoice.getCommisionTaxAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.AUCTION_PAYABLE, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT, purchaseInvoice.getPurchaseCostTaxAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.AUCTION_PAYABLE, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT, purchaseInvoice.getCommisionTaxAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

			// purchase recycle recoverable credit
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.RECYCLE_RECOVERABLE, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_CREDIT, purchaseInvoice.getRecycle(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

			// purchase car tax recoverable credit
			if (purchaseInvoice.getRoadTax() > 0) {
				accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
						supplier.getCompany(), AccountTransactionConstants.CAR_TAX_RECOVERABLE, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_CREDIT, purchaseInvoice.getRoadTax(),
						AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
			}
		});

	}

	private void typeNagareChargePaymentApproveTransactions(List<TPurchaseInvoice> auctionInvoices) {

		final TPurchaseInvoice firstTPurchaseInvoice = auctionInvoices.stream().findFirst()
				.orElseThrow(() -> new AAJRuntimeException("Exception while account transaction entry."));
		final String invoiceNo = firstTPurchaseInvoice.getInvoiceNo();
		final MSupplier supplier = masterSupplierRepository
				.findOneBySupplierCode(firstTPurchaseInvoice.getSupplierId());

		// create account transaction entry - Cost of Good Sold - Re Auction
		auctionInvoices.stream().forEach(reauctionInvoice -> {
			final TPurchaseInvoice purchaseInvoice = purchaseInvoiceRepository.findOneByStockNoAndType(
					reauctionInvoice.getStockNo(), Constants.PURCHASE_INVOICE_ITEM_TYPE_UNSOLD_AUCTION_CHARGE);
			// old invoice
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.SELLING_EXPENSE_NAGARE_CHARGE,
					Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, purchaseInvoice.getOtherCharges(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE,
					firstTPurchaseInvoice.getInvoiceDate()));
		});

	}

	private void typeCancelPaymentApproveTransactions(List<TPurchaseInvoice> auctionInvoices) {
		final TPurchaseInvoice firstTPurchaseInvoice = auctionInvoices.stream().findFirst()
				.orElseThrow(() -> new AAJRuntimeException("Exception while account transaction entry."));
		final String invoiceNo = firstTPurchaseInvoice.getInvoiceNo();
		final Date invoiceDate = firstTPurchaseInvoice.getInvoiceDate();
		final MSupplier supplier = masterSupplierRepository
				.findOneBySupplierCode(firstTPurchaseInvoice.getSupplierId());
		// create account transaction entry - Cost of Good Sold - Re Auction
		auctionInvoices.stream().forEach(invoice -> {
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.AUCTION_CANCELLATION_CHARGES,
					Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, invoice.getOtherCharges(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

		});
	}

	private void typeOtherPaymentApproveTransactions(List<TPurchaseInvoice> auctionInvoices) {
		final TPurchaseInvoice firstTPurchaseInvoice = auctionInvoices.stream().findFirst()
				.orElseThrow(() -> new AAJRuntimeException("Exception while account transaction entry."));
		final String invoiceNo = firstTPurchaseInvoice.getInvoiceNo();
		final Date invoiceDate = firstTPurchaseInvoice.getInvoiceDate();
		final MSupplier supplier = masterSupplierRepository
				.findOneBySupplierCode(firstTPurchaseInvoice.getSupplierId());
		// create account transaction entry - Cost of Good Sold - Re Auction
		auctionInvoices.stream().forEach(invoice -> {
			String[] arrOfStr = invoice.getType().split("- ", 2);
			System.out.println(arrOfStr[1]);
			AuctionPaymentType auctionPaymentType = auctionPaymentTypeRepository.findOneByType(arrOfStr[1]);
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.CONSUMPTION_TAX, Constants.CURRENCY_YEN,
					(invoice.getOthersCostTaxAmount() > 0 ? Constants.TRANSACTION_DEBIT : Constants.TRANSACTION_CREDIT),
					(invoice.getOthersCostTaxAmount() < 0 ? invoice.getOthersCostTaxAmount() * -1
							: invoice.getOthersCostTaxAmount()),
					arrOfStr[1], invoiceDate));
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), auctionPaymentType.getCoaCode(), Constants.CURRENCY_YEN,
					(invoice.getOtherCharges() > 0 ? Constants.TRANSACTION_DEBIT : Constants.TRANSACTION_CREDIT),
					(invoice.getOtherCharges() < 0 ? invoice.getOtherCharges() * -1 : invoice.getOtherCharges()),
					arrOfStr[1], invoiceDate));

		});
	}

	private void typeRoadTaxClaimedPaymentApproveTransactions(List<TPurchaseInvoice> auctionInvoices) {
		final TPurchaseInvoice firstTPurchaseInvoice = auctionInvoices.stream().findFirst()
				.orElseThrow(() -> new AAJRuntimeException("Exception while account transaction entry."));
		final String invoiceNo = firstTPurchaseInvoice.getInvoiceNo();
		final Date invoiceDate = firstTPurchaseInvoice.getInvoiceDate();
		final MSupplier supplier = masterSupplierRepository
				.findOneBySupplierCode(firstTPurchaseInvoice.getSupplierId());

		auctionInvoices.stream().forEach(invoice -> {
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.AUCTION_PAYABLE, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT,
					(invoice.getTotalTaxIncluded() < 0) ? (invoice.getTotalTaxIncluded() * -1) : 0.0,
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
		});

		auctionInvoices.stream().forEach(invoice -> {
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.CAR_TAX_CLEARING_ACCOUNT, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_CREDIT,
					(invoice.getOtherCharges() < 0) ? (invoice.getOtherCharges() * -1) : 0.0,
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
			if (!AppUtil.isObjectEmpty(invoice.getOthersCostTaxAmount()) && invoice.getOthersCostTaxAmount() != 0) {
				accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
						supplier.getCompany(), AccountTransactionConstants.CONSUMPTION_TAX, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_CREDIT,
						(invoice.getOthersCostTaxAmount() < 0) ? (invoice.getOthersCostTaxAmount() * -1) : 0.0,
						AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
			}
		});
	}

	private void typePenalityClaimedPaymentApproveTransactions(List<TPurchaseInvoice> auctionInvoices) {
		final TPurchaseInvoice firstTPurchaseInvoice = auctionInvoices.stream().findFirst()
				.orElseThrow(() -> new AAJRuntimeException("Exception while account transaction entry."));
		final String invoiceNo = firstTPurchaseInvoice.getInvoiceNo();
		final Date invoiceDate = firstTPurchaseInvoice.getInvoiceDate();
		final MSupplier supplier = masterSupplierRepository
				.findOneBySupplierCode(firstTPurchaseInvoice.getSupplierId());

		auctionInvoices.stream().forEach(invoice -> {
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.AUCTION_PAYABLE, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT,
					(invoice.getTotalTaxIncluded() < 0) ? (invoice.getTotalTaxIncluded() * -1) : 0.0,
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
		});

		auctionInvoices.stream().forEach(invoice -> {
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.PENALITY_CLAIMED, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_CREDIT,
					(invoice.getOtherCharges() < 0) ? (invoice.getOtherCharges() * -1) : 0.0,
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
			if (invoice.getOthersCostTaxAmount() != 0) {
				accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
						supplier.getCompany(), AccountTransactionConstants.CONSUMPTION_TAX, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_CREDIT,
						(invoice.getOthersCostTaxAmount() < 0) ? (invoice.getOthersCostTaxAmount() * -1) : 0.0,
						AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
			}
		});
	}

	private void typePenalityPaidPaymentApproveTransactions(List<TPurchaseInvoice> auctionInvoices) {
		final TPurchaseInvoice firstTPurchaseInvoice = auctionInvoices.stream().findFirst()
				.orElseThrow(() -> new AAJRuntimeException("Exception while account transaction entry."));
		final String invoiceNo = firstTPurchaseInvoice.getInvoiceNo();
		final Date invoiceDate = firstTPurchaseInvoice.getInvoiceDate();
		final MSupplier supplier = masterSupplierRepository
				.findOneBySupplierCode(firstTPurchaseInvoice.getSupplierId());

		auctionInvoices.stream().forEach(invoice -> {
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.AUCTION_PENALTY, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT, invoice.getTotalTaxIncluded(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

		});
	}

	private void typeRecycleClaimedPaymentApproveTransactions(List<TPurchaseInvoice> auctionInvoices) {
		final TPurchaseInvoice firstTPurchaseInvoice = auctionInvoices.stream().findFirst()
				.orElseThrow(() -> new AAJRuntimeException("Exception while account transaction entry."));
		final String invoiceNo = firstTPurchaseInvoice.getInvoiceNo();
		final Date invoiceDate = firstTPurchaseInvoice.getInvoiceDate();
		final MSupplier supplier = masterSupplierRepository
				.findOneBySupplierCode(firstTPurchaseInvoice.getSupplierId());

		auctionInvoices.stream().forEach(invoice -> {
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.AUCTION_PAYABLE, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT,
					(invoice.getTotalTaxIncluded() < 0) ? (invoice.getTotalTaxIncluded() * -1) : 0.0,
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
		});

		auctionInvoices.stream().forEach(invoice -> {
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.RECYCLE_TAX_CLEARING_ACCOUNT,
					Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT,
					(invoice.getOtherCharges() < 0) ? (invoice.getOtherCharges() * -1) : 0.0,
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
			if (invoice.getOthersCostTaxAmount() != 0) {
				accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
						supplier.getCompany(), AccountTransactionConstants.CONSUMPTION_TAX, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_CREDIT,
						(invoice.getOthersCostTaxAmount() < 0) ? (invoice.getOthersCostTaxAmount() * -1) : 0.0,
						AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
			}
		});
	}

	@Override
	public void purchaseInvoicePaymentCompleteTransactions(List<TPurchaseInvoice> invoices) {
		final TPurchaseInvoice firstTPurchaseInvoice = invoices.stream().findFirst()
				.orElseThrow(() -> new AAJRuntimeException("Exception while account transaction entry."));
		final String invoiceNo = firstTPurchaseInvoice.getInvoiceNo();
		final Date invoiceDate = firstTPurchaseInvoice.getInvoiceDate();
		final MSupplier supplier = masterSupplierRepository
				.findOneBySupplierCode(firstTPurchaseInvoice.getSupplierId());
		// create account transaction entry - auction payable debit
		final List<TInvoicePaymentTransaction> transactions = invoicePaymentTransactionRepository
				.findAllByInvoiceNoAndStatus(invoiceNo, Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED);
		for (final TInvoicePaymentTransaction transaction : transactions) {
			final MBank bank = mBankRepository.findOneByBankSeq(transaction.getBankId());
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), bank.getCoaCode(), bank.getCurrencyType(), Constants.TRANSACTION_CREDIT,
					transaction.getAmount(), AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE,
					transaction.getCode(), invoiceDate));
			// create account transaction entry - auction payable debit
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.AUCTION_PAYABLE, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT, transaction.getAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, transaction.getCode(),
					invoiceDate));

			final TSupplierTransaction supplierTransaction = new TSupplierTransaction();
			supplierTransaction.setInvoiceNo(invoiceNo);
			supplierTransaction.setSupplierCode(firstTPurchaseInvoice.getSupplierId());
			supplierTransaction.setTransactionType(Constants.TRANSACTION_DEBIT);
			supplierTransaction.setAmount(transaction.getAmount());
			supplierService.supplierTransactionEntry(supplierTransaction);
		}
	}

	private void typeRikusoPaymentPaymentApproveTransactions(List<TPurchaseInvoice> auctionInvoices) {
		final TPurchaseInvoice firstTPurchaseInvoice = auctionInvoices.stream().findFirst()
				.orElseThrow(() -> new AAJRuntimeException("Exception while account transaction entry."));
		final String invoiceNo = firstTPurchaseInvoice.getInvoiceNo();
		final Date invoiceDate = firstTPurchaseInvoice.getInvoiceDate();
		final MSupplier supplier = masterSupplierRepository
				.findOneBySupplierCode(firstTPurchaseInvoice.getSupplierId());

		auctionInvoices.stream().forEach(invoice -> {
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.INVENTORY_GENERAL, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT,
					invoice.getPurchaseCostTaxAmount() + invoice.getCommisionTaxAmount()
							+ invoice.getOthersCostTaxAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
		});

		auctionInvoices.stream().forEach(invoice -> {
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.CONSUMPTION_TAX, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT,
					invoice.getPurchaseCost() + invoice.getCommision() + invoice.getRoadTax() + invoice.getRecycle()
							+ invoice.getOtherCharges(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
		});
	}

	private void typeMemershipPaymentApproveTransactions(List<TPurchaseInvoice> auctionInvoices) {
		final TPurchaseInvoice firstTPurchaseInvoice = auctionInvoices.stream().findFirst()
				.orElseThrow(() -> new AAJRuntimeException("Exception while account transaction entry."));
		final String invoiceNo = firstTPurchaseInvoice.getInvoiceNo();
		final Date invoiceDate = firstTPurchaseInvoice.getInvoiceDate();
		final MSupplier supplier = masterSupplierRepository
				.findOneBySupplierCode(firstTPurchaseInvoice.getSupplierId());

		auctionInvoices.stream().forEach(invoice -> {
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.AUCTION_MEMBERSHIP, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT,
					invoice.getPurchaseCostTaxAmount() + invoice.getCommisionTaxAmount()
							+ invoice.getOthersCostTaxAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
		});

		auctionInvoices.stream().forEach(invoice -> {
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.CONSUMPTION_TAX, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT,
					invoice.getPurchaseCostTaxAmount() + invoice.getCommisionTaxAmount()
							+ invoice.getOthersCostTaxAmount(),
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
		});
	}

	private void typeCashbackApproveTransactions(List<TPurchaseInvoice> auctionInvoices) {
		final TPurchaseInvoice firstTPurchaseInvoice = auctionInvoices.stream().findFirst()
				.orElseThrow(() -> new AAJRuntimeException("Exception while account transaction entry."));
		final String invoiceNo = firstTPurchaseInvoice.getInvoiceNo();
		final Date invoiceDate = firstTPurchaseInvoice.getInvoiceDate();
		final MSupplier supplier = masterSupplierRepository
				.findOneBySupplierCode(firstTPurchaseInvoice.getSupplierId());

		auctionInvoices.stream().forEach(invoice -> {
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.AUCTION_PAYABLE, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_DEBIT,
					(invoice.getTotalTaxIncluded() < 0) ? (invoice.getTotalTaxIncluded() * -1) : 0.0,
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
		});

		auctionInvoices.stream().forEach(invoice -> {
			accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
					supplier.getCompany(), AccountTransactionConstants.MISCELLANEOUS_INCOME, Constants.CURRENCY_YEN,
					Constants.TRANSACTION_CREDIT,
					(invoice.getOtherCharges() < 0) ? (invoice.getOtherCharges() * -1) : 0.0,
					AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
			if (!AppUtil.isObjectEmpty(invoice.getOthersCostTaxAmount())) {
				if (invoice.getOthersCostTaxAmount() > 0) {
					accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(invoiceNo,
							supplier.getCompany(), AccountTransactionConstants.CONSUMPTION_TAX, Constants.CURRENCY_YEN,
							Constants.TRANSACTION_CREDIT,
							(invoice.getOthersCostTaxAmount() < 0) ? (invoice.getOthersCostTaxAmount() * -1) : 0.0,
							AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));
				}
			}
		});
	}

	private void typeRecyclePaidPaymentApproveTransactions(List<TPurchaseInvoice> recyclePaidList) {
		final TPurchaseInvoice firstTPurchaseInvoice = recyclePaidList.stream().findFirst()
				.orElseThrow(() -> new AAJRuntimeException("Exception while account transaction entry."));
		final String invoiceNo = firstTPurchaseInvoice.getInvoiceNo();
		final Date invoiceDate = firstTPurchaseInvoice.getInvoiceDate();
		final MSupplier supplier = masterSupplierRepository
				.findOneBySupplierCode(firstTPurchaseInvoice.getSupplierId());

		recyclePaidList.stream().forEach(invoice -> {

			long transConstValue = !AppUtil.isObjectEmpty(invoice.getStockNo())
					? AccountTransactionConstants.RECYCLE_RECOVERABLE
					: AccountTransactionConstants.RECYCLE_TAX_CLEARING_ACCOUNT;
			accountsTransactionService
					.accountTransactionEntry(new TAccountsTransaction(invoiceNo, supplier.getCompany(), transConstValue,
							Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, invoice.getTotalTaxIncluded(),
							AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

		});
	}

	private void typeCarTaxPaidPaymentApproveTransactions(List<TPurchaseInvoice> carTaxPaidList) {
		final TPurchaseInvoice firstTPurchaseInvoice = carTaxPaidList.stream().findFirst()
				.orElseThrow(() -> new AAJRuntimeException("Exception while account transaction entry."));
		final String invoiceNo = firstTPurchaseInvoice.getInvoiceNo();
		final Date invoiceDate = firstTPurchaseInvoice.getInvoiceDate();
		final MSupplier supplier = masterSupplierRepository
				.findOneBySupplierCode(firstTPurchaseInvoice.getSupplierId());

		carTaxPaidList.stream().forEach(invoice -> {

			long transConstValue = !AppUtil.isObjectEmpty(invoice.getStockNo())
					? AccountTransactionConstants.CAR_TAX_RECOVERABLE
					: AccountTransactionConstants.CAR_TAX_CLEARING_ACCOUNT;

			accountsTransactionService
					.accountTransactionEntry(new TAccountsTransaction(invoiceNo, supplier.getCompany(), transConstValue,
							Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, invoice.getTotalTaxIncluded(),
							AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_PURCHASE, invoiceDate));

		});
	}

	@Override
	public void completePayment(List<TPurchaseInvoice> invoices, String bank, Double amount, String remarks,
			Date approvedDate) {
		if (AppUtil.isObjectEmpty(invoices) || invoices.isEmpty()) {
			throw new AAJRuntimeException("Exception while completing payment");
		}
		final TPurchaseInvoice invoice = invoices.get(0);
		final String invoiceNo = invoice.getInvoiceNo();
		// invoice payment transaction
		final TInvoicePaymentTransaction invoicePaymentTransaction = new TInvoicePaymentTransaction(
				Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE, invoiceNo, bank, amount, approvedDate, remarks,
				Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED);
		invoicePaymentTransactionService.saveInvoicePaymentTransaction(invoicePaymentTransaction);

	}

	@Override
	public void addAuctionCancellationCharge(Map<String, Object> data) {
		final String invoiceId = data.get("invoiceId").toString();
		final String purchaseInvoiceCode = data.get("purchaseInvoiceId").toString();
		final Double amount = Double.parseDouble(data.get("amount").toString());
		final TPurchaseInvoice invoice = purchaseInvoiceRepository.findOneByCode(purchaseInvoiceCode);
		invoice.setInvoiceNo(invoiceId);
		invoice.setPurchaseCost(0.0);
		invoice.setPurchaseCostTax(0.0);
		invoice.setCommision(0.0);
		invoice.setCommisionTax(0.0);
		invoice.setRecycle(0.0);
		invoice.setRoadTax(0.0);
		invoice.setOtherCharges(amount);
		invoice.setStatus(Constants.INV_CANCEL_CHARGE_UPDATED);
		purchaseInvoiceRepository.save(invoice);

		final TSupplierTransaction transaction = new TSupplierTransaction();
		transaction.setStockNo(invoice.getStockNo());
		transaction.setInvoiceNo(invoice.getInvoiceNo());
		transaction.setSupplierCode(invoice.getSupplierId());
		transaction.setTransactionType(Constants.TRANSACTION_CREDIT);
		transaction.setAmount(invoice.getTotalTaxIncluded());

		supplierService.supplierTransactionEntry(transaction);

	}

	@Override
	public void carTaxReceived(String id, Double amount, Date receivedDate, String chassisNo) {
		final TPurchaseInvoice invoice = purchaseInvoiceRepository.findOneById(id);
		invoice.setCarTaxClaimReceivedAmount(amount);
		invoice.setCarTaxClaimStatus(Constants.TPURCHASEINVOICE_CARTAX_RECEIVED);
		invoice.setCarTaxReceivedDate(receivedDate);
		purchaseInvoiceRepository.save(invoice);

		final MSupplier supplier = masterSupplierRepository.findOneBySupplierCode(invoice.getSupplierId());
		final Double depreciationAmount = invoice.getRoadTax() - amount;
		final Long coaCode = coaTransactionService.checkStockInventoryStatusAndGetCoaCode(invoice.getStockNo(),
				AccountTransactionConstants.INVENTORY_GENERAL);

		// create account transaction entry - Car Tax recoverable credit
		accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(chassisNo, supplier.getCompany(),
				AccountTransactionConstants.CAR_TAX_RECOVERABLE, Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT,
				amount, AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_CAR_TAX_RECOVERABLE, null));

//		// create account transaction entry - Others - Car tax Depreciation credit
//		accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(chassisNo,
//				supplier.getCompany(), AccountTransactionConstants.OTHERS_CAR_TAX_DEPRECIATION, Constants.CURRENCY_YEN,
//				Constants.TRANSACTION_CREDIT, depreciationAmount,
//				AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_OTHERS_CAR_TAX_DEPRECIATION, null));
//
//		// create account transaction entry - Others - Car tax Depreciation debit
//		accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(chassisNo,
//				supplier.getCompany(), coaCode, Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, depreciationAmount,
//				AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_INVENTORY, null));
	}

	@Override
	public String generateInvoiceNo(String invoiceId) {
		final TPurchaseInvoice invoice = purchaseInvoiceRepository.findOneByCode(invoiceId);
		final MSupplier supplier = masterSupplierRepository.findOneBySupplierCode(invoice.getSupplierId());
		if (!AppUtil.isObjectEmpty(invoice)) {
			return sequenceService.getNextPurchaseInvoiceNo(Constants.SEQUENCE_KEY_INVOICE, invoice.getInvoiceDate(),
					supplier.getCompany());
		}
		throw new AAJRuntimeException("couldn't create invoice no : " + invoiceId);

	}

	@Override
	public void revertPurchaseConfirm(String invoiceNo, String cancelledRemarks) {
		final List<TPurchaseInvoice> invoices = purchaseInvoiceRepository.findAllByInvoiceNo(invoiceNo);

		// Cancelled Invoice Entry History
		TCancelledInvoices cancelinvoice = new TCancelledInvoices();
		cancelinvoice.setSupplierCode(invoices.stream().findFirst().get().getSupplierId());
		cancelinvoice.setAuctionHouse(invoices.stream().findFirst().get().getAuctionHouseId());
		cancelinvoice.setInvoiceNo(invoices.stream().findFirst().get().getInvoiceNo());
		cancelinvoice.setRefNo(invoices.stream().findFirst().get().getAuctionRefNo());
		cancelinvoice.setInvoiceDate(invoices.stream().findFirst().get().getInvoiceDate());
		cancelinvoice.setInvoiceType(Constants.INVOICE_TYPE_PURCHASE);
		double invCost = invoices.stream().mapToDouble(TPurchaseInvoice::getTotalInventoryAmount).sum();
		cancelinvoice.setInvoiceAmount(invCost);
		cancelinvoice.setCancelledDate(new Date());
		cancelinvoice.setCancellationRemarks(cancelledRemarks);
		cancelinvoice.setRemarks(invoices.stream().findFirst().get().getRemarks());
		cancelledinvoiceRepo.save(cancelinvoice);
		// Cancelled Invoice History End

		for (final TPurchaseInvoice purchaseInvoice : invoices) {
			purchaseInvoice.setStatus(Constants.INV_STATUS_NEW);
			if (purchaseInvoice.getType().equals(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE)) {
				purchaseInvoice.setPurchaseCostFlag(Constants.FLAG_NO);
				purchaseInvoice.setCommissionFlag(Constants.FLAG_NO);
				purchaseInvoice.setRoadTaxFlag(Constants.FLAG_NO);
				purchaseInvoice.setRecycleFlag(Constants.FLAG_NO);

				TStock stk = stkRepo.findOneByStockNo(purchaseInvoice.getStockNo());
				if (!AppUtil.isObjectEmpty(stk)) {
					stk.setStatus(Constants.STOCK_STATUS_NEW);
					stkRepo.save(stk);
				}
			}
			purchaseInvoiceRepository.save(purchaseInvoice);
			// Updated By Brindhan
			// Supplier Transaction Credit entry is created while approving the invoice so
			// the Debit entry not required
//			if (purchaseInvoice.getType().equals(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE)) {
//				// supplier transaction entry
//				final TSupplierTransaction transaction = new TSupplierTransaction();
//				transaction.setStockNo(purchaseInvoice.getStockNo());
//				transaction.setInvoiceNo(purchaseInvoice.getInvoiceNo());
//				transaction.setSupplierCode(purchaseInvoice.getInvoiceName());
//				transaction.setTransactionType(Constants.TRANSACTION_DEBIT);
//				transaction.setAmount(purchaseInvoice.getTotalTaxIncluded());
//				transaction.setRemark("Invoice Cancel entry");
//				supplierService.supplierTransactionEntry(transaction);
//
//			} else 
			if (purchaseInvoice.getType().equals(Constants.PURCHASE_INVOICE_ITEM_TYPE_REAUCTION)) {
				final TReAuction reAuction = reauctionRepository.findOneByStockNo(purchaseInvoice.getStockNo());
				reAuction.setStatus(Constants.REAUCTION_STATUS_INVOICE_GENARATED);
				reauctionRepository.save(reAuction);
			}
		}

	}

	@Override
	public Double getBalanceToPayAmount(String invoiceNo, List<Integer> paymentStatus, List<Integer> invoiceStatus) {
		final List<TPurchaseInvoice> invoices = purchaseInvoiceRepository
				.findAllByInvoiceNoAndPaymentApproveInAndStatusIn(invoiceNo, paymentStatus, invoiceStatus);
		// calculate invoice total
		final Double invoiceTotal = invoices.stream().mapToDouble(TPurchaseInvoice::getTotalTaxIncluded).sum();
		// get total paid amount
		final Double totalPaidAmount = invoicePaymentTransactionService.getTotalTransactionAmountForAuction(invoiceNo);
		return invoiceTotal - totalPaidAmount;

	}

	@Override
	public void revertPaymentTransaction(String invoiceNo, Double amount) {
		final List<TPurchaseInvoice> invoices = purchaseInvoiceRepository.findAllByInvoiceNo(invoiceNo);
		// calculate invoice total
		final Double invoiceTotal = invoices.stream().mapToDouble(TPurchaseInvoice::getTotalTaxIncluded).sum();

		// get total paid amount
		final Double totalPaidAmount = invoicePaymentTransactionService.getTotalTransactionAmountForAuction(invoiceNo);

		final Double amountAfterTransaction = totalPaidAmount - amount;
		Integer paymentStatus;
		if (amountAfterTransaction >= invoiceTotal) {
			paymentStatus = Constants.PAYMENT_COMPLETED;
		} else if (amountAfterTransaction > 0) {
			paymentStatus = Constants.PAYMENT_PARTIAL;
		} else {
			paymentStatus = Constants.PAYMENT_APPROVED;
		}
		for (final TPurchaseInvoice tPurchaseInvoice : invoices) {
			tPurchaseInvoice.setPaymentApprove(paymentStatus);
			tPurchaseInvoice.setInvoiceAmountReceived(amountAfterTransaction);
		}
		purchaseInvoiceRepository.saveAll(invoices);
	}

}
