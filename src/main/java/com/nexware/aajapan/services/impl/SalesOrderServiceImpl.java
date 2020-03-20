package com.nexware.aajapan.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
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
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MCurrency;
import com.nexware.aajapan.models.TAccountsTransaction;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.models.TCustomerTransaction;
import com.nexware.aajapan.models.TDayBook;
import com.nexware.aajapan.models.TDayBookTransaction;
import com.nexware.aajapan.models.TInvoice;
import com.nexware.aajapan.models.TLcDetails;
import com.nexware.aajapan.models.TLcInvoice;
import com.nexware.aajapan.models.TProformaInvoice;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.models.TSalesInvoice;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.models.TTransportInvoice;
import com.nexware.aajapan.repositories.MCurrencyRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TCustomerRepository;
import com.nexware.aajapan.repositories.TDayBookRepository;
import com.nexware.aajapan.repositories.TDayBookTransactionRepository;
import com.nexware.aajapan.repositories.TLcDetailsRepository;
import com.nexware.aajapan.repositories.TLcInvoiceRepository;
import com.nexware.aajapan.repositories.TProformaInvoiceRepository;
import com.nexware.aajapan.repositories.TPurchaseInvoiceRepository;
import com.nexware.aajapan.repositories.TSalesInvoiceRepository;
import com.nexware.aajapan.repositories.TTransportInvoiceRepository;
import com.nexware.aajapan.services.SalesOrderService;
import com.nexware.aajapan.services.SecurityService;
import com.nexware.aajapan.services.TAccountsTransactionService;
import com.nexware.aajapan.services.TCustomerTransactionService;
import com.nexware.aajapan.services.TDayBookService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class SalesOrderServiceImpl implements SalesOrderService {
	@Autowired
	private TAccountsTransactionService accountsTransactionService;
	@Autowired
	private TPurchaseInvoiceRepository purchaseInvoiceRepository;
	@Autowired
	private TTransportInvoiceRepository transportInvoiceRepository;
	@Autowired
	private MCurrencyRepository mCurrencyRepository;
	@Autowired
	private TSalesInvoiceRepository salesInvoiceRepository;
	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private TDayBookRepository dayBookRepository;
	@Autowired
	private TLcInvoiceRepository tLcInvoiceRepository;
	@Autowired
	private TDayBookService dayBookService;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private SecurityService securityService;
	@Autowired
	private TCustomerTransactionService customerTransactionService;
	@Autowired
	private TCustomerRepository customerRepository;
	@Autowired
	private TDayBookTransactionRepository dayBookTransactionRepository;
	@Autowired
	private TLcDetailsRepository lcDetailsRepository;
	@Autowired
	private TProformaInvoiceRepository proformaInvoiceRepository;

	@Override
	public void salesOrderTransaction(List<TSalesInvoice> salesInvoiceList) {

		salesInvoiceList.stream().forEach(a -> {
			String refInvoiceNo = a.getInvoiceNo();
			Integer currency = a.getCurrencyType();
			Double totalAmount = a.getTotal();
			Double stockAndTransportAmount = this.getActualPrice(a.getStockNo());
			// create account transaction entry - salesOrder - debit
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(refInvoiceNo, null,
					AccountTransactionConstants.COST_OF_GOODS_SOLD, Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT,
					stockAndTransportAmount, AccountTransactionConstants.SALES_ORDER_TYPE, null));

			// create account transaction entry - salesOrder - Credit
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(refInvoiceNo, null,
					AccountTransactionConstants.INVENTORY_GENERAL, Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT,
					stockAndTransportAmount, AccountTransactionConstants.SALES_ORDER_TYPE, null));

			if (currency.equals(Constants.CURRENCY_YEN)) {

				// create account transaction entry - salesOrder - Credit
				this.accountsTransactionService.accountTransactionEntry(
						new TAccountsTransaction(refInvoiceNo, null, AccountTransactionConstants.REVENUE_EXPORT_CIF,
								Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT, a.getTotal(),
								AccountTransactionConstants.SALES_ORDER_TYPE, null));

				// create account transaction entry - salesOrder - debit
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(refInvoiceNo, null,
						AccountTransactionConstants.TRADE_RECEIVABLE_CIF_JPY_DEBTORS, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_DEBIT, a.getTotal(), AccountTransactionConstants.SALES_ORDER_TYPE, null));

			} else {

				MCurrency mCurrency = this.mCurrencyRepository.findOneByCurrencySeq(currency);
				Double exchangeRateValue = mCurrency.getExchangeRate()!=null ? mCurrency.getExchangeRate() : 1;	
				System.out.println("total amount = "+totalAmount);
//				Double exchangeRateValue = mCurrency.getExchangeRate();
				Double totalValueAmount = totalAmount * exchangeRateValue;
				// create account transaction entry - salesOrder - debit
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(refInvoiceNo, null,
						AccountTransactionConstants.TRADE_RECEIVABLE_OTHER_CURRENCY_DEBTORS, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_DEBIT, totalValueAmount, AccountTransactionConstants.SALES_ORDER_TYPE,
						null));

				// create account transaction entry - salesOrder - Credit
				this.accountsTransactionService.accountTransactionEntry(
						new TAccountsTransaction(refInvoiceNo, null, AccountTransactionConstants.REVENUE_EXPORT_CIF,
								Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT, totalValueAmount,
								AccountTransactionConstants.SALES_ORDER_TYPE, null));

			}
		});

	}

	@Override
	public Double getActualPrice(String stockNo) {
		List<TPurchaseInvoice> purchasedStock = this.purchaseInvoiceRepository.findAllByStockNo(stockNo);
		double invCost = purchasedStock.stream().mapToDouble(TPurchaseInvoice::getTotalInventoryAmount).sum();
		List<TTransportInvoice> invoiceList = this.transportInvoiceRepository.findAllByStockNo(stockNo);
		Double totalInvoiceAmount = invoiceList.stream().mapToDouble(TTransportInvoice::getInvoiceTotal).sum();

		return AppUtil.ifNull(invCost, 0.0) + AppUtil.ifNull(totalInvoiceAmount, 0.0);
	}

	@Override
	public void createSalesOrder(List<TSalesInvoice> salesInvoice) {
		// save sales order
		this.salesInvoiceRepository.saveAll(salesInvoice);
		// create account transactions
		this.salesOrderTransaction(salesInvoice);
		this.checkAndUpdateReceivedAmount(salesInvoice);
		// update stock info
		salesInvoice.forEach(invoice -> {
			TStock stock = stockRepository.findOneByStockNo(invoice.getStockNo());
			stock.setStatus(Constants.STOCK_STATUS_SOLD);
			stock.setInventoryStatus(Constants.STOCK_INVENTORY_STATUS_STAGING);
			stock.setSalesInvoiceId(new ObjectId(invoice.getId()));
			this.stockRepository.save(stock);
			TCustomerTransaction transaction = new TCustomerTransaction(invoice.getCustomerId(), invoice.getStockNo(),
					invoice.getCurrencyType(), Constants.TRANSACTION_DEBIT, invoice.getTotal());
			this.customerTransactionService.customerTransactionEntry(transaction);
		});

	}

	@Override
	public void cancelSalesOrder(String invoiceNo) {
		List<TSalesInvoice> salesInvoices = this.salesInvoiceRepository.findAllByInvoiceNo(invoiceNo);
		boolean valid = salesInvoices.stream()
				.allMatch(invoice -> invoice.getStatus() == Constants.SALES_INV_PAYMENT_NOT_RECEIVED);
		if (!valid) {
			throw new AAJRuntimeException("Payment Already Given.So Cant Delete This Order");
		}
		salesInvoices.forEach(invoice -> {
			invoice.setStatus(Constants.SALES_INV_CANCEL);
			final String stockNo = invoice.getStockNo();
			final Update update = new Update().set("status", Constants.STOCK_STATUS_PURCHASED_CONFIRMED);
			mongoTemplate.updateFirst(Query.query(Criteria.where("stockNo").is(stockNo)), update, TStock.class);
		});
		this.salesInvoiceRepository.saveAll(salesInvoices);

		// create account transactions
		this.cancelSalesOrderTransaction(salesInvoices);
	}

	@Override
	public void cancelSalesOrderTransaction(List<TSalesInvoice> salesInvoiceList) {

		salesInvoiceList.stream().forEach(a -> {
			String refInvoiceNo = a.getInvoiceNo();
			Integer currency = a.getCurrencyType();
			Double totalAmount = a.getTotal();
			Double stockAndTransportAmount = this.getActualPrice(a.getStockNo());
			// create account transaction entry - salesOrder - debit
			this.accountsTransactionService.accountTransactionEntry(
					new TAccountsTransaction(refInvoiceNo, null, AccountTransactionConstants.COST_OF_GOODS_SOLD,
							Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT, stockAndTransportAmount,
							AccountTransactionConstants.SALES_ORDER_TYPE, null));

			// create account transaction entry - salesOrder - Credit
			this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(refInvoiceNo, null,
					AccountTransactionConstants.INVENTORY_GENERAL, Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT,
					stockAndTransportAmount, AccountTransactionConstants.SALES_ORDER_TYPE, null));

			if (currency.equals(Constants.CURRENCY_YEN)) {

				// create account transaction entry - salesOrder - Credit
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(refInvoiceNo, null,
						AccountTransactionConstants.REVENUE_EXPORT_CIF, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_DEBIT, a.getTotal(), AccountTransactionConstants.SALES_ORDER_TYPE, null));

				// create account transaction entry - salesOrder - debit
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(refInvoiceNo, null,
						AccountTransactionConstants.TRADE_RECEIVABLE_CIF_JPY_DEBTORS, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_CREDIT, a.getTotal(), AccountTransactionConstants.SALES_ORDER_TYPE,
						null));

			} else {

				MCurrency mCurrency = this.mCurrencyRepository.findOneByCurrencySeq(currency);
				Double exchangeRateValue = mCurrency.getExchangeRate();
				Double totalValueAmount = totalAmount * exchangeRateValue;
				// create account transaction entry - salesOrder - debit
				this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(refInvoiceNo, null,
						AccountTransactionConstants.TRADE_RECEIVABLE_OTHER_CURRENCY_DEBTORS, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_CREDIT, totalValueAmount, AccountTransactionConstants.SALES_ORDER_TYPE,
						null));

				// create account transaction entry - salesOrder - Credit
				this.accountsTransactionService.accountTransactionEntry(
						new TAccountsTransaction(refInvoiceNo, null, AccountTransactionConstants.REVENUE_EXPORT_CIF,
								Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, totalValueAmount,
								AccountTransactionConstants.SALES_ORDER_TYPE, null));

			}
		});

	}

	@Override
	public void editSalesOrderTransaction(List<TSalesInvoice> editedSalesList) {

		String invoiceNo = editedSalesList.stream().findFirst().get().getInvoiceNo();
		List<TSalesInvoice> actualList = this.salesInvoiceRepository.findAllByInvoiceNo(invoiceNo);

		if (editedSalesList.size() == actualList.size()) {
			boolean checkValue = false;
			for (int i = 0; i < editedSalesList.size(); i++) {
				if (editedSalesList.get(i).getTotal().equals(actualList.get(i).getTotal())) {
					checkValue = false;
				} else {
					checkValue = true;
					break;
				}
			}
			if (!checkValue) {
				// save edited transaction
				this.salesInvoiceRepository.saveAll(editedSalesList);
				return;
			}
		}
		// create account transactions
		this.cancelSalesOrderTransaction(actualList);

		// save edited transaction
		this.salesInvoiceRepository.saveAll(editedSalesList);

		List<String> ids = actualList.stream().map(TSalesInvoice::getId).collect(Collectors.toList());
		List<TSalesInvoice> deletedInvoice = new ArrayList<TSalesInvoice>();
		if (actualList.size() != editedSalesList.size()) {
			deletedInvoice = actualList.stream().filter(list -> !ids.contains(list.getId()))
					.collect(Collectors.toList());
		}

		deletedInvoice.forEach(invoice -> {
			invoice.setStatus(Constants.SALES_INV_CANCEL);
			final String stockNo = invoice.getStockNo();
			final Update update = new Update().set("status", Constants.STOCK_STATUS_PURCHASED_CONFIRMED);
			mongoTemplate.updateFirst(Query.query(Criteria.where("stockNo").is(stockNo)), update, TStock.class);
		});
		this.salesInvoiceRepository.saveAll(deletedInvoice);

		// create account transactions
		this.salesOrderTransaction(editedSalesList);

	}

	@Override
	public void fifoAllocationByCustomerAndDaybook(String advanceAmount, String exchangeRate1, String exchangeRate2,
			String customerId, String daybookId) {
		MLoginDto loginDto = this.securityService.findLoggedInUser();
		TCustomer customer = this.customerRepository.findOneByCode(customerId);
		Double exchangRate1 = AppUtil.isObjectEmpty(exchangeRate1) ? 0.0 : Double.parseDouble(exchangeRate1);
		Double exchangRate2 = AppUtil.isObjectEmpty(exchangeRate1) ? 0.0 : Double.parseDouble(exchangeRate2);
		Double advance = Double.parseDouble(advanceAmount);
		if (advance > 0.0) {
			TDayBook dayBookEntry = this.dayBookRepository.findOneByDaybookId(daybookId);
			Double balanceDaybookAmount = 0.0;
			if (customer.getCurrencyType() == 1 && dayBookEntry.getCurrency() != 1) {
				balanceDaybookAmount = dayBookEntry.getBalanceAmount() * Double.parseDouble(exchangeRate2);
			} else if (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() == 1) {
				balanceDaybookAmount = dayBookEntry.getBalanceAmount() / Double.parseDouble(exchangeRate2);
			} else if (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() != 1) {
				balanceDaybookAmount = (dayBookEntry.getBalanceAmount() * Double.parseDouble(exchangeRate1))
						/ Double.parseDouble(exchangeRate2);
			} else {
				balanceDaybookAmount = dayBookEntry.getBalanceAmount();
			}
			if (balanceDaybookAmount < advance) {
				throw new AAJRuntimeException("In sufficient balance in daybook " + daybookId);
			}
			TDayBookTransaction daybookTransaction = new TDayBookTransaction(dayBookEntry.getDaybookId(), null, null,
					(customer.getCurrencyType() == 1 && dayBookEntry.getCurrency() != 1) ? (advance / exchangRate2)
							: (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() == 1)
									? (advance * exchangRate2)
									: (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() != 1)
											? (advance * exchangRate2) / exchangRate1
											: advance,
					Constants.TRANSACTION_CREDIT, Constants.DAYBOOK_ALLOCATION_ADVANCE,
					Constants.DAYBOOK_TRANSACTION_NOT_APPROVED, dayBookEntry.getExchangeRate(), null);
			daybookTransaction.setCustomerId(customerId);
			daybookTransaction.setSalesPersonId(loginDto.getUserId());
			daybookTransaction.setCurrency(dayBookEntry.getCurrency());
			daybookTransaction.setCustomerCurrency(customer.getCurrencyType());
			daybookTransaction.setExchangeRate1(exchangRate1);
			daybookTransaction.setExchangeRate2(exchangRate2);
			daybookTransaction.setAllocatedAmount(advance);
			daybookTransaction.setAdvanceOwned(advance);
			daybookTransaction.setAmountRefund(Constants.ADVANCE_AMOUNT_NOT_REFUNDED);
			this.dayBookService.createDayBookTransaction(daybookTransaction);
		}
		List<TSalesInvoice> itemList = this.salesInvoiceRepository.fifoSalesInvoice(customerId);
		if (itemList.isEmpty()) {
			throw new AAJRuntimeException("No invoice found.");
		}
		for (TSalesInvoice tSalesInvoice : itemList) {
			TDayBook dayBookEntry = this.dayBookRepository.findOneByDaybookId(daybookId);
			Double balanceDaybookAmount = 0.0;
			if (customer.getCurrencyType() == 1 && dayBookEntry.getCurrency() != 1) {
				balanceDaybookAmount = dayBookEntry.getBalanceAmount() * Double.parseDouble(exchangeRate2);
			} else if (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() == 1) {
				balanceDaybookAmount = dayBookEntry.getBalanceAmount() / Double.parseDouble(exchangeRate2);
			} else if (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() != 1) {
				balanceDaybookAmount = (dayBookEntry.getBalanceAmount() * Double.parseDouble(exchangeRate1))
						/ Double.parseDouble(exchangeRate2);
			} else {
				balanceDaybookAmount = dayBookEntry.getBalanceAmount();
			}
			Double exchRate = dayBookEntry.getExchangeRate();
			// get balance

			if (balanceDaybookAmount <= 0) {
				continue;
			}

			Double salesInvoiceBalance = tSalesInvoice.getTotal() - tSalesInvoice.getAmountAllocatted();
			if (salesInvoiceBalance <= 0) {
				continue;
			}
			Double amount;
			if (salesInvoiceBalance <= balanceDaybookAmount) {
				amount = salesInvoiceBalance;
			} else {
				amount = balanceDaybookAmount;
			}
			TDayBookTransaction transaction = new TDayBookTransaction(dayBookEntry.getDaybookId(),
					tSalesInvoice.getInvoiceNo(), tSalesInvoice.getStockNo(),
					(customer.getCurrencyType() == 1 && dayBookEntry.getCurrency() != 1) ? (amount / exchangRate2)
							: (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() == 1)
									? (amount * exchangRate2)
									: (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() != 1)
											? (amount * exchangRate2) / exchangRate1
											: amount,
					Constants.TRANSACTION_CREDIT, Constants.DAYBOOK_ALLOCATION_FIFO,
					Constants.DAYBOOK_TRANSACTION_NOT_APPROVED, exchRate, tSalesInvoice.getExchangeRate());
			transaction.setCustomerId(tSalesInvoice.getCustomerId());
			transaction.setSalesPersonId(tSalesInvoice.getSalesPerson());
			transaction.setCurrency(tSalesInvoice.getCurrencyType());
			transaction.setExchangeRate1(exchangRate1);
			transaction.setExchangeRate2(exchangRate2);
			transaction.setAllocatedAmount(amount);
			this.dayBookService.createDayBookTransaction(transaction);
			this.allocattedAmountForInvoice(tSalesInvoice.getInvoiceNo(), tSalesInvoice.getStockNo(), amount,
					Constants.TRANSACTION_CREDIT);
		}

	}

	@Override
	public void unitAllocationByCustomerAndDaybook(String advanceAmount, String exchangeRate1, String exchangeRate2,
			String customerId, String daybookId, List<Document> allocations) {
		MLoginDto loginDto = this.securityService.findLoggedInUser();
		TCustomer customer = this.customerRepository.findOneByCode(customerId);
		Double exchangRate1 = AppUtil.isObjectEmpty(exchangeRate1) ? 0.0 : Double.parseDouble(exchangeRate1);
		Double exchangRate2 = AppUtil.isObjectEmpty(exchangeRate1) ? 0.0 : Double.parseDouble(exchangeRate2);

		Double advance = Double.parseDouble(advanceAmount);
		if (advance > 0.0) {
			TDayBook dayBookEntry = this.dayBookRepository.findOneByDaybookId(daybookId);
			Double balanceDaybookAmount = 0.0;
			if (customer.getCurrencyType() == 1 && dayBookEntry.getCurrency() != 1) {
				balanceDaybookAmount = dayBookEntry.getBalanceAmount() * Double.parseDouble(exchangeRate2);
			} else if (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() == 1) {
				balanceDaybookAmount = dayBookEntry.getBalanceAmount() / Double.parseDouble(exchangeRate2);
			} else if (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() != 1) {
				balanceDaybookAmount = (dayBookEntry.getBalanceAmount() * Double.parseDouble(exchangeRate1))
						/ Double.parseDouble(exchangeRate2);
			} else {
				balanceDaybookAmount = dayBookEntry.getBalanceAmount();
			}

			if (balanceDaybookAmount <= 0) {
				throw new AAJRuntimeException("No balance in daybook - " + daybookId);
			}
			if (balanceDaybookAmount < advance) {
				throw new AAJRuntimeException("In sufficient balance in daybook " + daybookId);
			}
			TDayBookTransaction daybookTransaction = new TDayBookTransaction(dayBookEntry.getDaybookId(), null, null,
					(customer.getCurrencyType() == 1 && dayBookEntry.getCurrency() != 1) ? (advance / exchangRate2)
							: (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() == 1)
									? (advance * exchangRate2)
									: (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() != 1)
											? (advance * exchangRate2) / exchangRate1
											: advance,
					Constants.TRANSACTION_CREDIT, Constants.DAYBOOK_ALLOCATION_ADVANCE,
					Constants.DAYBOOK_TRANSACTION_NOT_APPROVED, dayBookEntry.getExchangeRate(), null);
			daybookTransaction.setCustomerId(customerId);
			daybookTransaction.setSalesPersonId(loginDto.getUserId());
			daybookTransaction.setCurrency(dayBookEntry.getCurrency());
			daybookTransaction.setCustomerCurrency(customer.getCurrencyType());
			daybookTransaction.setExchangeRate1(exchangRate1);
			daybookTransaction.setExchangeRate2(exchangRate2);
			daybookTransaction.setAllocatedAmount(advance);
			daybookTransaction.setAdvanceOwned(advance);
			daybookTransaction.setAmountRefund(Constants.ADVANCE_AMOUNT_NOT_REFUNDED);
			this.dayBookService.createDayBookTransaction(daybookTransaction);
		}

		for (Document allocation : allocations) {
			TDayBook dayBookEntry = this.dayBookRepository.findOneByDaybookId(daybookId);
			String id = allocation.getString("id");
			if (AppUtil.isObjectEmpty(allocation.getString("priceAllocation"))) {
				continue;
			}
			Double amount = Double.valueOf(allocation.getString("priceAllocation"));

			TSalesInvoice tSalesInvoice = this.salesInvoiceRepository.findOneById(id);

			Double exchRate = dayBookEntry.getExchangeRate();
			// get balance
			Double salesInvoiceBalance = tSalesInvoice.getTotal() - tSalesInvoice.getAmountAllocatted();
			Double balanceDaybookAmount = 0.0;
			if (customer.getCurrencyType() == 1 && dayBookEntry.getCurrency() != 1) {
				balanceDaybookAmount = dayBookEntry.getBalanceAmount() * Double.parseDouble(exchangeRate2);
			} else if (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() == 1) {
				balanceDaybookAmount = dayBookEntry.getBalanceAmount() / Double.parseDouble(exchangeRate2);
			} else if (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() != 1) {
				balanceDaybookAmount = (dayBookEntry.getBalanceAmount() * Double.parseDouble(exchangeRate1))
						/ Double.parseDouble(exchangeRate2);
			} else {
				balanceDaybookAmount = dayBookEntry.getBalanceAmount();
			}

			if (balanceDaybookAmount <= 0) {
				throw new AAJRuntimeException("No balance in daybook - " + daybookId);
			}
			if (salesInvoiceBalance < amount) {
				throw new AAJRuntimeException(
						"Allocation amount is grater than balance for stockNo " + tSalesInvoice.getStockNo());
			}
			if (balanceDaybookAmount < amount) {
				throw new AAJRuntimeException("In sufficient balance in daybook " + daybookId);
			}

			if (salesInvoiceBalance <= 0) {
				continue;
			}

			TDayBookTransaction transaction = new TDayBookTransaction(dayBookEntry.getDaybookId(),
					tSalesInvoice.getInvoiceNo(), tSalesInvoice.getStockNo(),
					(customer.getCurrencyType() == 1 && dayBookEntry.getCurrency() != 1) ? (amount / exchangRate2)
							: (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() == 1)
									? (amount * exchangRate2)
									: (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() != 1)
											? (amount * exchangRate2) / exchangRate1
											: amount,
					Constants.TRANSACTION_CREDIT, Constants.DAYBOOK_ALLOCATION_UNIT,
					Constants.DAYBOOK_TRANSACTION_NOT_APPROVED, exchRate, tSalesInvoice.getExchangeRate());
			transaction.setCustomerId(tSalesInvoice.getCustomerId());
			transaction.setSalesPersonId(tSalesInvoice.getSalesPerson());
			transaction.setCurrency(tSalesInvoice.getCurrencyType());
			transaction.setExchangeRate1(exchangRate1);
			transaction.setExchangeRate2(exchangRate2);
			transaction.setAllocatedAmount(amount);
			this.dayBookService.createDayBookTransaction(transaction);
			this.allocattedAmountForInvoice(tSalesInvoice.getInvoiceNo(), tSalesInvoice.getStockNo(), amount,
					Constants.TRANSACTION_CREDIT);
		}

	}

	@Override
	public void lcAllocationByCustomerAndDaybook(String advanceAmount, String customerId, String daybookId,
			List<Document> allocations) {
		MLoginDto loginDto = this.securityService.findLoggedInUser();
		TCustomer customer = this.customerRepository.findOneByCode(customerId);

		Double advance = Double.parseDouble(advanceAmount);
		if (advance > 0.0) {
			TDayBook dayBookEntry = this.dayBookRepository.findOneByDaybookId(daybookId);
			Double balanceDaybookAmount = dayBookEntry.getBalanceAmount();

			if (balanceDaybookAmount <= 0) {
				throw new AAJRuntimeException("No balance in daybook - " + daybookId);
			}
			if (balanceDaybookAmount < advance) {
				throw new AAJRuntimeException("In sufficient balance in daybook " + daybookId);
			}
			TLcDetails lcDetails = this.lcDetailsRepository.findOneByBillOfExchangeNo(dayBookEntry.getBillOfExchange());
			TProformaInvoice proformaInvoice = this.proformaInvoiceRepository
					.findOneByInvoiceNo(lcDetails.getProformaInvoiceId());
			TDayBookTransaction daybookTransaction = new TDayBookTransaction(dayBookEntry.getDaybookId(), null, null,
					advance, Constants.TRANSACTION_CREDIT, Constants.DAYBOOK_ALLOCATION_ADVANCE,
					Constants.DAYBOOK_TRANSACTION_NOT_APPROVED, dayBookEntry.getExchangeRate(), null);
			daybookTransaction.setCustomerId(customerId);
			daybookTransaction.setSalesPersonId(proformaInvoice.getSalesPerson());
			daybookTransaction.setCurrency(dayBookEntry.getCurrency());
			daybookTransaction.setAllocatedAmount(advance);
			daybookTransaction.setAdvanceOwned(daybookTransaction.getAmount());
			daybookTransaction.setAmountRefund(Constants.ADVANCE_AMOUNT_NOT_REFUNDED);
			this.dayBookService.createDayBookTransaction(daybookTransaction);
		}

		for (Document allocation : allocations) {
			TDayBook dayBookEntry = this.dayBookRepository.findOneByDaybookId(daybookId);
			Double balanceDaybookAmount = dayBookEntry.getBalanceAmount();
			String id = allocation.getString("id");
			if (AppUtil.isObjectEmpty(allocation.getString("priceAllocation"))) {
				continue;
			}
			Double amount = Double.valueOf(allocation.getString("priceAllocation"));

			TLcInvoice tLcInvoice = this.tLcInvoiceRepository.findOneById(id);

			Double exchRate = dayBookEntry.getExchangeRate();
			// get balance
			Double lcInvoiceBalance = tLcInvoice.getAmount() - tLcInvoice.getAmountAllocatted();

			if (balanceDaybookAmount <= 0) {
				throw new AAJRuntimeException("No balance in daybook - " + daybookId);
			}
			if (balanceDaybookAmount < amount) {
				throw new AAJRuntimeException("In sufficient balance in daybook " + daybookId);
			}
			if (lcInvoiceBalance < amount) {
				throw new AAJRuntimeException(
						"Allocation amount is grater than balance for stockNo " + tLcInvoice.getStockNo());
			}
			if (lcInvoiceBalance <= 0) {
				continue;
			}
			TLcDetails lcDetails = this.lcDetailsRepository.findOneByBillOfExchangeNo(dayBookEntry.getBillOfExchange());
			TProformaInvoice proformaInvoice = this.proformaInvoiceRepository
					.findOneByInvoiceNo(lcDetails.getProformaInvoiceId());

			MCurrency currency = this.mCurrencyRepository.findOneByCurrencySeq(Constants.CURRENCY_YEN);
			TDayBookTransaction dayBookTransaction = new TDayBookTransaction(dayBookEntry.getDaybookId(), null,
					tLcInvoice.getStockNo(), amount, Constants.TRANSACTION_CREDIT, Constants.DAYBOOK_ALLOCATION_LC,
					Constants.DAYBOOK_TRANSACTION_NOT_APPROVED, currency.getExchangeRate(), null);
			dayBookTransaction.setCustomerId(tLcInvoice.getCustomerId());
			dayBookTransaction.setSalesPersonId(proformaInvoice.getSalesPerson());
			dayBookTransaction.setCurrency(dayBookEntry.getCurrency());
			dayBookTransaction.setAllocatedAmount(amount);
			this.dayBookService.createDayBookTransaction(dayBookTransaction);

			this.allocattedAmountForLcInvoice(id, amount, Constants.TRANSACTION_CREDIT);
		}

	}

	@Override
	public void depositOrAdvanceByCustomerAndDaybook(String advanceAmount, String exchangeRate1, String exchangeRate2,
			String customerId, String daybookId, Integer allocationType) {
		MLoginDto loginDto = this.securityService.findLoggedInUser();
		TDayBook dayBookEntry = this.dayBookRepository.findOneByDaybookId(daybookId);
		TCustomer customer = this.customerRepository.findOneByCode(customerId);
		Double exchangRate1 = AppUtil.isObjectEmpty(exchangeRate1) ? 0.0 : Double.parseDouble(exchangeRate1);
		Double exchangRate2 = AppUtil.isObjectEmpty(exchangeRate1) ? 0.0 : Double.parseDouble(exchangeRate2);
		Double balanceDaybookAmount = 0.0;
		if (customer.getCurrencyType() == 1 && dayBookEntry.getCurrency() != 1) {
			balanceDaybookAmount = dayBookEntry.getBalanceAmount() * Double.parseDouble(exchangeRate2);
		} else if (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() == 1) {
			balanceDaybookAmount = dayBookEntry.getBalanceAmount() / Double.parseDouble(exchangeRate2);
		} else if (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() != 1) {
			balanceDaybookAmount = (dayBookEntry.getBalanceAmount() * Double.parseDouble(exchangeRate1))
					/ Double.parseDouble(exchangeRate2);
		} else {
			balanceDaybookAmount = dayBookEntry.getBalanceAmount();
		}

		if (balanceDaybookAmount <= 0) {
			throw new AAJRuntimeException("No balance in daybook - " + daybookId);
		}
		Double amount = Double.parseDouble(advanceAmount);
		if (balanceDaybookAmount < amount) {
			throw new AAJRuntimeException("In sufficient balance in daybook " + daybookId);
		}
		TDayBookTransaction transaction = new TDayBookTransaction(dayBookEntry.getDaybookId(), null, null,
				(customer.getCurrencyType() == 1 && dayBookEntry.getCurrency() != 1) ? (amount / exchangRate2)
						: (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() == 1) ? (amount * exchangRate2)
								: (customer.getCurrencyType() != 1 && dayBookEntry.getCurrency() != 1)
										? (amount * exchangRate2) / exchangRate1
										: amount,
				Constants.TRANSACTION_CREDIT, allocationType, Constants.DAYBOOK_TRANSACTION_NOT_APPROVED,
				dayBookEntry.getExchangeRate(), null);
		transaction.setCustomerId(customerId);
		transaction.setSalesPersonId(loginDto.getUserId());
		transaction.setCurrency(dayBookEntry.getCurrency());
		transaction.setCustomerCurrency(customer.getCurrencyType());
		transaction.setExchangeRate1(exchangRate1);
		transaction.setExchangeRate2(exchangRate2);
		transaction.setAllocatedAmount(amount);
		transaction.setAdvanceOwned(amount);
		transaction.setAmountRefund(Constants.ADVANCE_AMOUNT_NOT_REFUNDED);
		this.dayBookService.createDayBookTransaction(transaction);

	}

	@Override
	public void updateReceivedAmount(String invoiceNo, String stockNo, Double amount, Integer transactionType) {
		Query query = new Query(Criteria.where("invoiceNo").is(invoiceNo).and("stockNo").is(stockNo));
		// update balance
		Update update = new Update();
		TSalesInvoice salesInvoice;
		if (transactionType == Constants.TRANSACTION_CREDIT) {
			update.inc("amountReceived", amount);
			// check amount will not grater than required
			salesInvoice = this.salesInvoiceRepository.findOneByInvoiceNoAndStockNo(invoiceNo, stockNo);
			if ((salesInvoice.getAmountReceived() + amount) > salesInvoice.getTotal()) {
				throw new AAJRuntimeException("Received amount is greater than required amount : " + invoiceNo);
			}
		} else if (transactionType == Constants.TRANSACTION_DEBIT) {
			update.inc("amountReceived", (amount * -1));
		}
		// return new balance
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);

		salesInvoice = this.mongoTemplate.findAndModify(query, update, options, TSalesInvoice.class);
		if (AppUtil.isObjectEmpty(salesInvoice)) {
			throw new AAJRuntimeException("Exception while update received amount : " + invoiceNo);
		}
		salesInvoice.setStatus(this.findSalesInvoiceStatus(salesInvoice));
		this.salesInvoiceRepository.save(salesInvoice);
	}

	@Override
	public Integer findSalesInvoiceStatus(TSalesInvoice invoice) {
		if (invoice.getBalance() == 0) {
			return Constants.SALES_INV_PAYMENT_RECEIVED;
		} else if (invoice.getBalance() == invoice.getTotal()) {
			return Constants.SALES_INV_PAYMENT_NOT_RECEIVED;
		} else if ((invoice.getBalance() > 0) && (invoice.getBalance() < invoice.getTotal())) {
			return Constants.SALES_INV_PAYMENT_RECEIVED_PARTIAL;
		} else {
			throw new AAJRuntimeException("Unable to find sales invoice status");
		}

	}

	@Override
	public void checkAndUpdateReceivedAmount(List<TSalesInvoice> invoices) {
		for (TSalesInvoice invoice : invoices) {
			Double amount = this.customerTransactionService.getAmountReceivedForStockByCustomer(invoice.getCustomerId(),
					invoice.getStockNo());
			if (!AppUtil.isObjectEmpty(amount) && (amount > 0)) {
				this.updateReceivedAmount(invoice.getInvoiceNo(), invoice.getStockNo(), amount,
						Constants.TRANSACTION_CREDIT);
			}
		}

	}

	@Override
	public Double getBalanceAmountReceivedForStockByCustomer(String invoiceNo, String stockNo, String customerId) {
		TSalesInvoice salesInvoice = this.salesInvoiceRepository.findOneByInvoiceNoAndStockNo(invoiceNo, stockNo);
		Double amountReceived = this.customerTransactionService.getAmountReceivedForStockByCustomer(customerId,
				stockNo);
		return salesInvoice.getTotal() - amountReceived;
	}

	@Override
	public void allocattedAmountForInvoice(String invoiceNo, String stockNo, Double amount, Integer transactionType) {
		Query query = new Query(Criteria.where("invoiceNo").is(invoiceNo).and("stockNo").is(stockNo));
		// update balance
		Update update = new Update();
		TSalesInvoice salesInvoice;
		if (transactionType == Constants.TRANSACTION_CREDIT) {
			update.inc("amountAllocatted", amount);
			// check amount will not grater than required
			salesInvoice = this.salesInvoiceRepository.findOneByInvoiceNoAndStockNo(invoiceNo, stockNo);

			if ((salesInvoice.getAmountAllocatted() + amount) > salesInvoice.getTotal()) {
				throw new AAJRuntimeException("Allocatted amount is greater than required amount : " + invoiceNo);
			}
		} else if (transactionType == Constants.TRANSACTION_DEBIT) {
			update.inc("amountAllocatted", (amount * -1));
		}
		// return new balance
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);

		salesInvoice = this.mongoTemplate.findAndModify(query, update, options, TSalesInvoice.class);
		if (AppUtil.isObjectEmpty(salesInvoice)) {
			throw new AAJRuntimeException("Exception while update received amount : " + invoiceNo);
		}
	}

	@Override
	public void allocattedAmountForLcInvoice(String id, Double amount, Integer transactionType) {
		Query query = new Query(Criteria.where("id").is(id));
		// update balance
		Update update = new Update();
		TLcInvoice lcInvoice;
		if (transactionType == Constants.TRANSACTION_CREDIT) {
			update.inc("amountAllocatted", amount);
			// check amount will not grater than required
			lcInvoice = this.tLcInvoiceRepository.findOneById(id);

			if ((lcInvoice.getAmountAllocatted() + amount) > lcInvoice.getAmount()) {
				throw new AAJRuntimeException(
						"Allocatted amount is greater than required amount : " + lcInvoice.getLcInvoiceNo());
			}
		} else if (transactionType == Constants.TRANSACTION_DEBIT) {
			update.inc("amountAllocatted", (amount * -1));
		}
		// return new balance
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);

		lcInvoice = this.mongoTemplate.findAndModify(query, update, options, TLcInvoice.class);
		if (AppUtil.isObjectEmpty(lcInvoice)) {
			throw new AAJRuntimeException("Exception while update received amount : " + lcInvoice.getLcInvoiceNo());
		}
	}

	@Override
	public void fifoAllocationByCustomerAndDaybookReAllocate(String customerId, String daybookId, String transId) {

		List<TSalesInvoice> itemList = this.salesInvoiceRepository.fifoSalesInvoice(customerId);
		if (itemList.isEmpty()) {
			throw new AAJRuntimeException("No invoice found.");
		}
		for (TSalesInvoice tSalesInvoice : itemList) {
			TDayBookTransaction dayBookEntry = this.dayBookTransactionRepository.findOneById(transId);
			TCustomer customer = this.customerRepository.findOneByCode(customerId);
			// get balance
			final Double customerAdvanceBalance = dayBookEntry.getAdvanceOwned();
			if (customerAdvanceBalance == 0) {
				throw new AAJRuntimeException("No balance in customer account - " + daybookId);
			}
			if (customer.getCurrencyType() != tSalesInvoice.getCurrencyType()) {
				throw new AAJRuntimeException("Daybook currency and sales invoice currency mismatch - " + daybookId);
			}

			Double salesInvoiceBalance = tSalesInvoice.getTotal() - tSalesInvoice.getAmountAllocatted();
			if (salesInvoiceBalance <= 0) {
				continue;
			}
			Double amount;
			if (salesInvoiceBalance <= customerAdvanceBalance) {
				amount = salesInvoiceBalance;
			} else {
				amount = customerAdvanceBalance;
			}

			// TDayBook dayBookEntry = this.dayBookRepository.findOneByDaybookId(daybookId);
			this.allocattedAmountForInvoice(tSalesInvoice.getInvoiceNo(), tSalesInvoice.getStockNo(), amount,
					Constants.TRANSACTION_CREDIT);
			dayBookService.dayBookApproveReAllocate(transId, Constants.DAYBOOK_ALLOCATION_FIFO,
					tSalesInvoice.getInvoiceNo(), tSalesInvoice.getStockNo(), tSalesInvoice.getExchangeRate(), amount);
		}

	}

	@Override
	public void unitAllocationByCustomerAndDaybookReAllocate(String customerId, String daybookId,
			List<Document> allocations, String transId) {
		for (Document allocation : allocations) {
			String id = allocation.getString("id");
			if (AppUtil.isObjectEmpty(allocation.getString("priceAllocation"))) {
				continue;
			}
			Double amount = Double.valueOf(allocation.getString("priceAllocation"));
			TDayBookTransaction dayBookEntry = this.dayBookTransactionRepository.findOneById(transId);
			TSalesInvoice tSalesInvoice = this.salesInvoiceRepository.findOneById(id);
			TCustomer customer = this.customerRepository.findOneByCode(customerId);
			// get balance
			final Double customerAdvanceBalance = dayBookEntry.getAdvanceOwned();
			Double salesInvoiceBalance = tSalesInvoice.getTotal() - tSalesInvoice.getAmountAllocatted();
			if (customerAdvanceBalance == 0) {
				throw new AAJRuntimeException("No balance in daybook - " + daybookId);
			}
			if (salesInvoiceBalance < amount) {
				throw new AAJRuntimeException(
						"Allocation amount is grater than balance for stockNo " + tSalesInvoice.getStockNo());
			}
			if (customerAdvanceBalance < amount) {
				throw new AAJRuntimeException("In sufficient balance in daybook " + daybookId);
			}
			if (customer.getCurrencyType() != tSalesInvoice.getCurrencyType()) {
				throw new AAJRuntimeException("Daybook currency and sales invoice currency mismatch - " + daybookId);
			}

			if (salesInvoiceBalance <= 0) {
				continue;
			}
			// TDayBook dayBookEntry = this.dayBookRepository.findOneByDaybookId(daybookId);

			this.allocattedAmountForInvoice(tSalesInvoice.getInvoiceNo(), tSalesInvoice.getStockNo(), amount,
					Constants.TRANSACTION_CREDIT);
			dayBookService.dayBookApproveReAllocate(transId, Constants.DAYBOOK_ALLOCATION_UNIT,
					tSalesInvoice.getInvoiceNo(), tSalesInvoice.getStockNo(), tSalesInvoice.getExchangeRate(), amount);
		}

	}

}
