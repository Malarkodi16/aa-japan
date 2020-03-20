package com.nexware.aajapan.core;

public class AccountTransactionConstants {

	private AccountTransactionConstants() {

	}

	// Purchase Key
	public static final String PURCHASE_KEY_C = "PUR001_C";
	public static final String PURCHASE_KEY_D = "PUR001_D";
	public static final String COMMISSION_KEY_C = "COMM001_C";
	public static final String COMMISSION_KEY_D = "COMM001_D";
	public static final String RECYCLE_KEY_C = "REC001_C";
	public static final String RECYCLE_KEY_D = "REC001_D";
	public static final String ROADTAX_KEY_C = "TAX001_C";
	public static final String ROADTAX_KEY_D = "TAX001_D";

	// Sales Order Debit Code Trade Receivable - CIF JPY Debtors
	public static final long TRADE_RECEIVABLE_CIF_JPY_DEBTORS = 123310;
	public static final long TRADE_RECEIVABLE_OTHER_CURRENCY_DEBTORS = 123320;
	public static final long TRADE_RECEIVABLE_CIF_JPY_CLEARING = 123311;
	public static final long TRADE_RECEIVABLE_OTHER_CURRENCY_CLEARING = 123321;

	// Account Transaction Purchase Code

	public static final long AUCTION_PAYABLE = 231110;
	public static final long RECYCLE_RECOVERABLE = 124120;
	public static final long CAR_TAX_RECOVERABLE = 124140;
	public static final long REVENUE_LOCAL_OTHER = 321400;

	public static final long CONSUMPTION_TAX = 124110;
	// Account Transaction Transport Code
	public static final long TRANSPORT_COST_OF_GOOD_SOLD_RIKUSO = 322150;
	public static final long TRANSPORT_RIKUSO_PAYABLE = 231140;
	// Account Transaction Others Debit Code
	public static final long OTHER_PAYABLE = 231240;
	// Account Transaction Type
	public static final String TRANSACTION_CATEGORY_STOCK = "STOCK";
	public static final String TRANSACTION_CATEGORY_OTHERS = "OTHERS";
	// Account transaction source
	public static final String ACCOUNT_TRANSACTION_SOURCE_PURCHASE = "PURCHASE";
	public static final String ACCOUNT_TRANSACTION_SOURCE_TRANSPORT = "TRANSPORT";
	public static final String ACCOUNT_TRANSACTION_SOURCE_OTHERS_PAYMENT = "OTHERS";
	public static final String ACCOUNT_TRANSACTION_SOURCE_STORAGE = "STORAGE";
	public static final String ACCOUNT_TRANSACTION_SOURCE_PHOTOS = "PHOTOS";
	public static final String ACCOUNT_TRANSACTION_SOURCE_STORAGE_PHOTOS = "STORAGE & PHOTOS";
	public static final String ACCOUNT_TRANSACTION_DAYBOOK = "DAYBOOK";
	public static final String ACCOUNT_TRANSACTION_SOURCE_FREIGHT = "FREIGHT";
	public static final String ACCOUNT_TRANSACTION_SOURCE_SHIPPING = "SHIPPING";
	public static final String ACCOUNT_TRANSACTION_SOURCE_INSPECTION = "INSPECTION";
	public static final String ACCOUNT_TRANSACTION_SOURCE_RADIATION = "RADIATION";
	public static final String ACCOUNT_TRANSACTION_SOURCE_FREIGHT_SHIPPING = "FREIGHT & SHIPPING";
	public static final String ACCOUNT_TRANSACTION_SOURCE_ADVANCE_PREPAYMENT = "ADVANCE & PREPAYMENT";
	public static final String ACCOUNT_TRANSACTION_LOAN = "LOAN";
	public static final String ACCOUNT_TRANSACTION_SOURCE_LOAN_REPAYMENT = "LOAN REPAYMENT";
	// Storage & Photos Types
	public static final String ACCOUNT_STORAGE_AND_PHOTOS_TYPES_STORAGE = "Storage";
	public static final String ACCOUNT_STORAGE_AND_PHOTOS_TYPES_PHOTOS = "Photos";
	public static final String ACCOUNT_STORAGE_AND_PHOTOS_TYPES_BL_AMEND = "BL Amend";
	public static final String ACCOUNT_STORAGE_AND_PHOTOS_TYPES_REPAIR = "Repair";
	// Recycle Claim
	public static final String ACCOUNT_TRANSACTION_RECYCLE_CLAIM = "RECYCLE CLAIM";

	// Sales Order Invoice
	public static final String SALES_ORDER_TYPE = "Sales Order Invoice";
	// Account Storage Code
	public static final long STORAGE_COST_OF_GOODS_SOLD = 322160;
	public static final long REPAIR_COST_OF_GOODS_SOLD = 322200;
	public static final long BL_AMEND_COST_OF_GOODS_SOLD = 322230;
	public static final long PHOTOS_COST_OF_GOODS_SOLD = 322220;
	public static final long COST_OF_GOODS_SOLD = 322240;
	public static final long STORAGE_PHOTOS_FREIGHT_SHIPPING_PAYABLE = 231130;

	// Account Photo Code
	public static final long PHOTOS_COST_OF_GOODS_SOLD_OTHERS_UNSPECIFIED = 322190;

	// DayBook Code Clearing balance
	public static final long CLEARING_ACCOUNT = 121306;
	public static final long AUD_CLEARING_ACCOUNT = 121307;
	public static final long PND_CLEARING_ACCOUNT = 121308;
	public static final long BANK_CHARGES_COA = 325225;
	public static final long UNRECOGNIZED_TRADE_RECEIVABLE = 124313;

	// Accounts Freight And Shipping Code
	public static final long FREIGHT_COST_OF_GOODS_SOLD = 322130;
	public static final long SHIPPING_COST_OF_GOODS_SOLD = 322140;
	public static final long INSPECTION_COST_OF_GOODS_SOLD = 322180;
	public static final long RADIATION_COST_OF_GOODS_SOLD = 124130;
	public static final long FREIGHT_SHIPPING_PAYABLE = 231130;
	// purchase invoice coa code
	public static final long COST_OF_GOOD_SOLD_RE_AUCTION = 322210;
	public static final long AUCTION_CANCELLATION_CHARGES = 325227;
	public static final long SELLING_EXPENSE_NAGARE_CHARGE = 327109;

	public static final long INVENTORY_GENERAL = 122110;
	public static final long INVENTORY_STAGING = 122120;
	public static final long INVENTORY_IN_TRANSIT = 122130;
	public static final long INVENTORY_CONSIGNMENT = 122140;

	// Exchange Gain/Loss
	public static final long EXCHANGE_GAIN_LOSS = 323140;
	// Sales Order code
	public static final long REVENUE_EXPORT_CIF = 321200;
	// Create Loan
	public static final long LOAN_PRINCIPLE = 221110;
	public static final long LOAN_INTEREST = 326100;

	// Car Tax Recoverable
	public static final long OTHERS_CAR_TAX_DEPRECIATION = 324123;

	// Car Tax Recoverable source
	public static final String ACCOUNT_TRANSACTION_SOURCE_CAR_TAX_RECOVERABLE = "CAR TAX RECOVERABLE";
	public static final String ACCOUNT_TRANSACTION_SOURCE_OTHERS_CAR_TAX_DEPRECIATION = "OTHERS CAR TAX DEPRECIATION";
	public static final String ACCOUNT_TRANSACTION_SOURCE_INVENTORY = "INVENTORY";
	public static final String ACCOUNT_TRANSACTION_SOURCE_LOAN_CREATE = "LOAN CREATE";
	// purchase payment booking
	public static final long CAR_TAX_CLEARING_ACCOUNT = 124213;
	public static final long RECYCLE_TAX_CLEARING_ACCOUNT = 124214;
	public static final long PENALITY_CLAIMED = 327102;
	public static final long PENALITY_PAID = 327103;
	public static final long AUCTION_PENALTY = 325235;
	public static final long AUCTION_MEMBERSHIP = 325236;
	public static final long MISCELLANEOUS_INCOME = 323150;

}
