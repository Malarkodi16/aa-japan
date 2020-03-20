package com.nexware.aajapan.core;

public class Constants {
	// sequence
	public static final String SEQUENCE_KEY_STOCK = "stock";
	public static final String SEQUENCE_KEY_OLD_STOCK = "oldstock";
	public static final String SEQUENCE_KEY_SUPPLIER = "SPPLR";
	public static final String SEQUENCE_KEY_PRCHSINVC = "PRCHSINVC";
	public static final String SEQUENCE_KEY_OLD_PRCHSINVC = "CARSOFTINVC";
	public static final String SEQUENCE_KEY_CATEGORYTYPE = "TPE-";
	public static final String SEQUENCE_KEY_EXPTYPE = "EXPTYPE";
	public static final String SEQUENCE_KEY_TRANSPORTCATEGORYTYPE = "TNPE-";
	public static final String SEQUENCE_KEY_AUCTIONCATEGORYTYPE = "AUCTION_PAYMENT";
	public static final String SEQUENCE_KEY_CSTMR = "CSTMR";
	public static final String SEQUENCE_KEY_FRWDR = "FWD-";
	public static final String SEQUENCE_KEY_TO = "TO";
	public static final String SEQUENCE_KEY_TI = "TI";
	public static final String SEQUENCE_KEY_PINV = "PINV";
	public static final String SEQUENCE_KEY_SINV = "SINV";
	public static final String SEQUENCE_KEY_USR = "USR";
	public static final String SEQUENCE_KEY_LC = "LC";
	public static final String SEQUENCE_KEY_SR = "SR";
	public static final String SEQUENCE_KEY_SCHDL = "SCHDL";
	public static final String SEQUENCE_KEY_TFINV = "TFINV";
	public static final String SEQUENCE_KEY_TINV = "TINV";
	public static final String SEQUENCE_KEY_MLOC = "MLOC";
	public static final String SEQUENCE_KEY_MKR = "MKR";
	public static final String SEQUENCE_KEY_MODEL = "MDL";
	public static final String SEQUENCE_KEY_INVOICE = "PUR_INVC";
	public static final String SEQUENCE_KEY_ALLCTN = "ALLCTN";
	public static final String SEQUENCE_KEY_LCINV = "LCINV";
	public static final String SEQUENCE_KEY_FRTSHPNGINV = "FRTSHPNGINV";
	public static final String SEQUENCE_KEY_ACCTR = "ACCTR";
	public static final String SEQUENCE_KEY_YOMNFTR = "YOMNFTR";
	public static final String SEQUENCE_KEY_BNK = "BNK";
	public static final String SEQUENCE_KEY_FOREIGN_BNK = "FN_BNK";
	public static final String SEQUENCE_KEY_DYBK = "DYBK";
	public static final String SEQUENCE_KEY_JOURNAL_ENRTY = "JNL";
	public static final String SEQUENCE_KEY_DAYBOOK_TRANSACTION = "RCPT";
	public static final String SEQUENCE_KEY_CREATE_LOAN = "LOAN";
	public static final String SEQUENCE_KEY_CREATE_LNDTL = "LNDTL";
	public static final String SEQUENCE_KEY_PAYMENT_TRANSACTION_NO = "IPT";
	public static final String SEQUENCE_KEY_PAYMENT_VOUCHER_NO = "PV";
	public static final String SEQUENCE_KEY_SPNGINSTN = "SPNGINSTN";
	public static final String SEQUENCE_KEY_MSHIP = "MSHIP";
	public static final String SEQUENCE_KEY_MPORT = "MPORT";
	public static final String SEQUENCE_KEY_TINSPECTION_INSTRUCTION = "ININ";
	public static final String SEQUENCE_KEY_TINSPECTION_ORDER_REQUEST = "INSOR";
	public static final String SEQUENCE_KEY_SHIPPING_COMP = "SHIPCOMP";
	public static final String SEQUENCE_KEY_GENERAL_SUPPLIER = "GENERALSUPPLIER";
	public static final String SEQUENCE_KEY_PARTS_PURCHASED = "PARTSPURCHASED";
	public static final String SEQUENCE_KEY_STOCK_MODEL_TYPE = "STOCK_MODEL_TYPE";
	public static final String SEQUENCE_TRANSPORTER = "TRANSPORTER";
	public static final String SEQUENCE_KEY_BL_TRANSACTION = "BL_TRANSACTION";
	public static final String SEQUENCE_KEY_RPRT = "RPRT";
	public static final String SEQUENCE_KEY_ADVANCE_DAY_BOOK = "ADV_DYBK";
	public static final String SEQUENCE_KEY_INSPECTION_COMPANY = "INSPECTION-COMPANY";
	public static final String SEQUENCE_KEY_LOCATION = "LOCATION";
	public static final String SEQUENCE_KEY_AUINTGRD = "AUINTGRD";
	public static final String SEQUENCE_KEY_AUEXTGRD = "AUEXTGRD";
	public static final String SEQUENCE_KEY_HSCODE = "HSCODE";
	public static final String SEQUENCE_KEY_INSPECTION_FAILED = "INSPECTION_FAILED";
	public static final String SEQUENCE_KEY_SHIPPING_TERMS = "SHP_TERMS";
	public static final String SEQUENCE_KEY_SHIPPING_MARKS = "SHP_MARKS";
	public static final String SEQUENCE_KEY_INS_INVOICE = "INSPECTION_INVOICE";
	public static final String SEQUENCE_KEY_INS_INVOICE_UNIQUE_CODE = "INSPECTION_INVOICE_UNIQUE_CODE";
	// stock status
	public static final Integer STOCK_STATUS_NEW = 0;
	public static final Integer STOCK_STATUS_PURCHASED_CONFIRMED = 1;
	public static final Integer STOCK_STATUS_RE_AUCTION = 2;
	public static final Integer STOCK_STATUS_SOLD = 5;
	public static final Integer STOCK_STATUS_CANCEL = 6;

	// Payment Approve status
	public static final Integer PAYMENT_NOT_APPROVED = 0;
	public static final Integer PAYMENT_APPROVED = 1;
	public static final Integer PAYMENT_COMPLETED = 2;
	public static final Integer PAYMENT_CANCELED = 3;
	public static final Integer PAYMENT_FREEZE = 4;
	public static final Integer PAYMENT_PARTIAL = 5;

	// Shipping Instruction status
	public static final Integer SHIPPING_INSTRUCTION_GIVEN = 0;
	public static final Integer SHIPPING_INSTRUCTION_INITIATED = 1;
	public static final Integer SHIPPING_INSTRUCTION_CANCELLED = 2;
	// Stock Transport status
	public static final Integer TRANSPORT_IDLE = 0;
	public static final Integer TRANSPORT_INTRANSIT = 1;
	public static final Integer TRANSPORT_COMPLETED = 2;
	// Transportation status
	public static final Integer TRANSPORT_ITEM_NEW = 0;
	public static final Integer TRANSPORT_ITEM_INITIATED = 1;
	public static final Integer TRANSPORT_ITEM_CONFIRMED = 2;
	public static final Integer TRANSPORT_ITEM_CANCELED = 3;
	public static final Integer TRANSPORT_ITEM_REARRANGE = 4;
	public static final Integer TRANSPORT_ITEM_DELIVERY_CONFIRMED = 5;
	public static final Integer TRANSPORT_ITEM_DELIVERED = 6;
	public static final Integer TRANSPORT_ITEM_INTRANSIT = 7;
	// Transport invoice status
	public static final Integer TRANSPORT_ORDER_INITIATED = 0;
	public static final Integer TRANSPORT_ORDER_INVOICE_CREATED = 1;
	public static final Integer TRANSPORT_ORDER_CANCELED = 2;

	// purchase invoice status
	public static final Integer INV_STATUS_NEW = 0;
	public static final Integer INV_STATUS_VERIFIED = 1;
	public static final Integer INV_STATUS_CANCEL = 2;
	public static final Integer INV_CANCEL_CHARGE_UPDATED = 3;
	public static final Integer INV_STATUS_REAUCTION = 4;

	// Customer flag
	public static final Integer CUSTOMER_FLAG_CUSTOMER = 0;
	public static final Integer CUSTOMER_FLAG_BRANCH = 1;
	public static final Integer CUSTOMER_STATUS_IF_NOT_EDITED = 0;
	public static final Integer CUSTOMER_STATUS_IF_EDITED = 1;

	// invoice status
	public static final String PAYMENT_STATUS_INITIATED = "1";
	public static final String ACCOUNTS_AUCTION_PAYMENT = "auction";
	public static final String ACCOUNTS_TRANSPORT_PAYMENT = "transport";
	public static final String ACCOUNTS_SHIPPING_PAYMENT = "shipping";
	public static final String ACCOUNTS_OTHERS_PAYMENT = "others";
	public static final String ACCOUNTS_FREIGHT_PAYMENT = "freight";

	// Is Bidding
	public static final Integer IS_BIDDING = 1;
	public static final Integer IS_NOT_BIDDING = 0;
	// reserved status
	public static final Integer RESERVED = 1;
	public static final Integer NOT_RESERVED = 0;
	// lock status
	public static final Integer IS_LOCKED = 1;
	public static final Integer IS_NOT_LOCKED = 0;
	// show to sales person flag
	public static final Integer SHOW_FOR_SALES = 1;
	public static final Integer NOT_SHOW_FOR_SALES = 0;
	// account aaj, somo
	public static final Integer ACCOUNT_AAJ = 1;
	public static final Integer ACCOUNT_SOMO = 2;
	// purchase invoice item type
	public static final String PURCHASE_INVOICE_ITEM_TYPE_PURCHASE = "PURCHASE";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_REAUCTION = "REAUCTION";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_REAUCTION_SOLD_AND_CANCELLED = "REAUCTION AND CANCELLED";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_PENALTY_CHARGE = "PENALTY";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_DOCUMENT_PENALTY = "DOCUMENT PENALTY";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_LATE_PENALTY = "LATE PENALTY";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_ROADTAX_CLAIMED = "CAR TAX CLAIMED";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_CANCELLATION_CHARGES = "CANCELLATION CHARGES";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_CANCELLATION_PENALTY_CHARGES = "CANCELLATION PENALTY CHARGES";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_CASH_BACK = "CASH BACK";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_RIKUSO_PAYMENT = "RIKUSO PAYMENT";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_MEMBERSHIP = "MEMBERSHIP";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_REAUCTION_CANCELLED = "REAUCTION CANCELLED";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_UNSOLD_AUCTION_CHARGE = "UNSOLD AUCTION CHARGE";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_NAGARE_CHARGE = "NAGARE CHARGE";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_RECYCLE_CLAIMED = "RECYCLE CLAIMED";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_RECYCLE_PAID = "RECYCLE PAID";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_ROADTAX_PAID = "CAR TAX PAID";
	public static final String PURCHASE_INVOICE_ITEM_TYPE_TAKE_OUT_STOCK = "TAKE OUT STOCk";
	// invoice type
	public static final String INVOICE_TYPE_TRANSPORT = "TRANSPORT";
	public static final String INVOICE_TYPE_PAYMENT = "PAYMENT";
	public static final String INVOICE_TYPE_FORWARDER = "FORWARDER";
	public static final String INVOICE_TYPE_FREIGHT = "FREIGHT";
	public static final String INVOICE_TYPE_OTHERS = "OTHERS";
	public static final String INVOICE_TYPE_PURCHASE = "PURCHASE";
	public static final String INVOICE_TYPE_INSPECTION = "INSPECTION";

	// Cost Of Goods Types
	public static final String COST_OF_GOODS_TYPE_PURCHASE_TAX = "PURCHASE_TAX";
	public static final String COST_OF_GOODS_TYPE_PURCHASE_COMM = "PURCHASE_COMM";
	public static final String COST_OF_GOODS_TYPE_PURCHASE_COMM_TAX = "PURCHASE_COMM_TAX";
	public static final String COST_OF_GOODS_TYPE_PURCHASE_RECYCLE = "PURCHASE_RECYCLE";
	public static final String COST_OF_GOODS_TYPE_PURCHASE_ROADTAX = "PURCHASE_ROADTAX";
	public static final String COST_OF_GOODS_TYPE_PURCHASE_OTHERS = "PURCHASE_OTHERS";
	public static final String COST_OF_GOODS_TYPE_PURCHASE_OTHERS_TAX = "PURCHASE_OTHERS_TAX";
	public static final String COST_OF_GOODS_TYPE_PURCHASE = "PURCHASE";
	public static final String COST_OF_GOODS_TYPE_TRANSPORT = "TRANSPORT";
	public static final String COST_OF_GOODS_TYPE_TRANSPORT_TAX = "TRANSPORT_TAX";
	public static final String COST_OF_GOODS_TYPE_FREIGHT = "FREIGHT";
	public static final String COST_OF_GOODS_TYPE_SHIPPING = "SHIPPING";
	public static final String COST_OF_GOODS_TYPE_RADIATION = "RADIATION";
	public static final String COST_OF_GOODS_TYPE_INSPECTION = "INSPECTION";
	public static final String COST_OF_GOODS_TYPE_INSPECTION_TAX = "INSPECTION_TAX";
	public static final String COST_OF_GOODS_TYPE_STORAGE = "STORAGE";
	public static final String COST_OF_GOODS_TYPE_PHOTOS = "PHOTOS";
	public static final String COST_OF_GOODS_TYPE_BLAMEND = "BL AMEND";
	public static final String COST_OF_GOODS_TYPE_REPAIR = "REPAIR";
	// freight Shipping Status
	public static final Integer FREIGHT_NOT_UPDATED = 0;
	public static final Integer FREIGHT_UPDATED = 1;
	// inspection Flag
	public static final Integer COUNTRY_INSPECTION_NOT_NEED = 0;
	public static final Integer COUNTRY_INSPECTION_NEED = 1;
	// inspection statusSTOCK_AVAILABLE_FOR_INSPECTION
	public static final Integer STOCK_AVAILABLE_FOR_INSPECTION = 0;
	public static final Integer STOCK_NOT_AVAILABLE_FOR_INSPECTION = 1;
	// stock photo uploaded
	public static final Integer STOCK_PHOTOS_UPLOADED = 1;
	public static final Integer STOCK_PHOTOS_NOT_UPLOADED = 0;

	// Inspection Arrange Status
	public static final Integer INSPECTION_ORDER_REQUEST_INITIATED = 0;
	public static final Integer INSPECTION_ORDER_REQUEST_FAILED = 2;
	public static final Integer INSPECTION_ORDER_REQUEST_COMPLETE = 3;
	public static final Integer INSPECTION_ORDER_REQUEST_REARRANGE = 4;
	public static final Integer INSPECTION_ORDER_REQUEST_PASSED = 5;
	public static final Integer INSPECTION_ORDER_REQUEST_DELETED = 6;

	// Inspection Order Booking status
	public static final Integer INVOICE_FOR_INSPECTION_ORDER_NOT_BOOKED = 0;
	public static final Integer INVOICE_FOR_INSPECTION_ORDER_BOOKED = 1;
	// Inspection booking, approve and payment status
	public static final Integer INSPECTION_PAYMENT_INVOICE_BOOKING_APPROVED = 1;
	public static final Integer INSPECTION_PAYMENT_INVOICE_PROCESSING = 2;
	public static final Integer INSPECTION_PAYMENT_INVOICE_PROCESSING_PARTIAL = 3;
	public static final Integer INSPECTION_PAYMENT_INVOICE_APPROVAL = 4;
	public static final Integer INSPECTION_PAYMENT_INVOICE_PAYMENT_COMPLETED = 5;
	public static final Integer INSPECTION_PAYMENT_INVOICE_PAYMENT_CANCELLED = 6;
	// inspection document status
	public static final Integer INSPECTION_ORDER_REQUEST_DOC_NOTSENT = 0;
	public static final Integer INSPECTION_ORDER_REQUEST_DOC_SENT_COPY = 1;
	public static final Integer INSPECTION_ORDER_REQUEST_DOC_SENT_ORIGINAL = 2;
	// Shipping request status
	public static final Integer SHIPIING_REQUEST_INITIATED = 0;
	public static final Integer SHIPIING_REQUEST_ACCEPTED = 1;
	public static final Integer SHIPIING_REQUEST_RESCHEDULED = 2;
	public static final Integer SHIPIING_REQUEST_CANCELLED = 3;
	public static final Integer SHIPIING_REQUEST_CONTAINER_CONFIRMED = 4;
	public static final Integer SHIPIING_REQUEST_VESSEL_CONFIRMED = 5;
	public static final Integer SHIPIING_REQUEST_RORO_UPDATED_BL_AND_SHIPPED = 6;
	// shipping request invoice create status
	public static final Integer SHIPIING_REQUEST_INVOICE_NOT_CREATED = 0;
	public static final Integer SHIPIING_REQUEST_INVOICE_CREATED = 1;
	// shipping request shipping status
	public static final Integer SHIPIING_REQUEST_SHIPPING_NOT_CONFIRMED = 0;
	public static final Integer SHIPIING_REQUEST_SHIPPING_INITIATED = 1;
	public static final Integer SHIPIING_REQUEST_SHIPPING_INTRANSIT = 2;
	public static final Integer SHIPIING_REQUEST_SHIPPING_COMPLETED = 3;

	public static final Integer SHIPIING_REQUEST_REC_SUR_STATUS_IDLE = 0;
	public static final Integer SHIPIING_REQUEST_REC_SUR_STATUS_RECEIVE = 1;
	public static final Integer SHIPIING_REQUEST_REC_SUR_STATUS_SURRENDER = 2;
	public static final Integer SHIPIING_REQUEST_REC_SUR_STATUS_HOLD = 3;

	public static final Integer SHIPIING_REQUEST_BL_DOC_STATUS_IDLE = 0;
	public static final Integer SHIPIING_REQUEST_BL_DOC_STATUS_RECEIVE = 1;
	public static final Integer SHIPIING_REQUEST_BL_DOC_STATUS_ISSUED = 2;
	public static final Integer SHIPIING_REQUEST_BL_DOC_STATUS_DISPATCHED = 3;
	public static final Integer SHIPIING_REQUEST_BL_DOC_STATUS_SURRENDER = 4;
	// BL Status
	public static final Integer BL_DRAFT_STATUS_NOT_RECEIVED = 0;
	public static final Integer BL_DRAFT_STATUS_RECEIVED = 1;
	public static final Integer BL_ORIGINAL_STATUS_NOT_RECEIVED = 0;
	public static final Integer BL_ORIGINAL_STATUS_RECEIVED = 1;
	// sales invoice status
	public static final Integer SALES_INV_PAYMENT_NOT_RECEIVED = 0;
	public static final Integer SALES_INV_PAYMENT_RECEIVED = 1;
	public static final Integer SALES_INV_PAYMENT_RECEIVED_PARTIAL = 2;
	public static final Integer SALES_INV_CANCEL = 3;

	// Day Book Entry
	public static final Integer DAYBOOK_ENTRY_CREATED = 0;
	public static final Integer DAYBOOK_ENTRY_OWNED = 1;
	public static final Integer DAYBOOK_ENTRY_APPROVED = 2;
	public static final Integer DAYBOOK_ENTRY_REMOVED = 3;

	// Customer approve Entry
	public static final Integer CUSTOMER_NOT_APPROVED_FLAG = 0;
	public static final Integer CUSTOMER_APPROVED_FLAG = 1;

	// LC UPDATE
	public static final Integer LC_CREATED = 1;
	public static final Integer LC_ALLOCATED = 2;

	// LC Status - Tstock
	public static final Integer STOCK_LC_APPLIED = 1;
	public static final Integer STOCK_LC_NOT_APPLIED = 0;
	// TForwarder Invoice
	public static final Integer TFORWARDER_INVOICE_STATUS_INITIATED = 0;
	// MLocation auction
	public static final Integer MLOCATION_DELETE_STATUS_INITIALLY = 0;
	public static final Integer MLOCATION_DELETE_STATUS_AFTER = 1;
	public static final Integer MLOCATION_SUPPLIER_DELETE_STATUS_INITIALLY = 0;
	public static final Integer MLOCATION_SUPPLIER_DELETE_STATUS_AFTER = 1;
	// TReAuction
	public static final Integer REAUCTION_STATUS_INITIATED = 0;
	public static final Integer REAUCTION_STATUS_INVOICE_GENARATED = 1;
	public static final Integer REAUCTION_STATUS_INVOICE_CONFIRMED = 2;
	public static final Integer REAUCTION_STATUS_CANCELLED = 3;
	public static final Integer REAUCTION_STATUS_SOLD_CANCELLED = 4;

	// TNotification
	public static final Integer TNOTIFICATION_CREATED = 0;
	public static final Integer TNOTIFICATION_OPENED = 1;
	public static final String TNOTIFICATION_SHIPPING_INSTRUCTION_MESSAGE = "Requested shipping for <<destCountry>>";
	public static final String TNOTIFICATION_CONFIRM_PURCHASE_MESSAGE = "<<maker>> - <<model>> car has been purchased";
	public static final String TNOTIFICATION_SHIPPING_ARRANGED = "Shipping arranged for <<destCountry>>";
	public static final String TNOTIFICATION_CUSTOMER_DUPLICATION = "Duplication for <<city>> and <<mobileNo>>";
	public static final String TNOTIFICATION_SALES_INVOICE_CANCEL = "Has cancelled the invoice - <<invoiceNo>>";
	// pickup date
	public static final Integer TRANSPORT_PICKUP_BEFORE_PAYMENT = 0;
	public static final Integer TRANSPORT_PICKUP_AFTER_PAYMENT = 1;
	// Transport request note
	public static final String TRANSPORT_REQUEST_DELIVERY_NOTE_ASAP = "出来るだけ早く納車願います。";
	public static final String TRANSPORT_REQUEST_DELIVERY_NOTE_URGENT = "至急、納車願います。";
	public static final String TRANSPORT_REQUEST_PICKUP_DATE_NOTE = "搬出期限までに引き取り";
	public static final String TRANSPORT_REQUEST_DUE_DATE_NOTE = "入金前引き取りOK";
	// Transport schedule type
	public static final Integer TTRANSPORT_SCHEDULE_TYPE_ASAP = 0;
	public static final Integer TTRANSPORT_SCHEDULE_TYPE_URGENT = 1;
	public static final Integer TTRANSPORT_SCHEDULE_TYPE_TIME = 2;
	// Account Purchase Invoice Recycle Claim Status
	public static final Integer TPURCHASEINVOICE_RECYCLE_NOT_CLAIMED = 0;
	public static final Integer TPURCHASEINVOICE_RECYCLE_CLAIM_APPLIED = 1;
	public static final Integer TPURCHASEINVOICE_RECYCLE_CLAIMED = 2;
	public static final Integer TPURCHASEINVOICE_RECYCLE_RECEIVED = 3;

	// Account Purchase Invoice Car tax Claim Status
	public static final Integer TPURCHASEINVOICE_CARTAX_NOT_CLAIMED = 0;
	public static final Integer TPURCHASEINVOICE_CARTAX_CLAIMED = 1;
	public static final Integer TPURCHASEINVOICE_CARTAX_RECEIVED = 2;

	// Last Lap Vehicle status
	public static final Integer LAST_LAP_STATUS_0 = 0;
	public static final Integer LAST_LAP_STATUS_1 = 1;
	public static final Integer LAST_LAP_STATUS_2 = 2;

	// TDayBookTransaction Status
	public static final Integer TRANSACTION_DEBIT = 0;
	public static final Integer TRANSACTION_CREDIT = 1;
	// Delete flag
	public static final Integer DELETE_FLAG_0 = 0;
	public static final Integer DELETE_FLAG_1 = 1;
	// public stock shipping instruction status
	public static final Integer STOCK_SHIPPING_INSTRUCTION_STATUS_IDLE = 0;
	public static final Integer STOCK_SHIPPING_INSTRUCTION_STATUS_ARRANGED = 1;
	public static final Integer STOCK_SHIPPING_INSTRUCTION_STATUS_GIVEN = 2;
	// public stock shipping status
	public static final Integer STOCK_SHIPPING_STATUS_IDLE = 0;
	public static final Integer STOCK_SHIPPING_STATUS_SHIPPINGARRANGED = 1;
	public static final Integer STOCK_SHIPPING_STATUS_IN_REQUESTED = 2;

	// inspection Status
	public static final Integer INSPECTION_NOT_ARRANGED = 0;
	public static final Integer INSPECTION_DONE = 1;
	// Stock document status
	public static final Integer STOCK_DOCUMENT_NOT_RECEIVED = 0;
	public static final Integer STOCK_DOCUMENT_RECEIVED = 1;
	public static final Integer STOCK_DOCUMENT_RECEIVED_AND_CANCELLED = 3;
	public static final Integer STOCK_DOCUMENT_CONVERT = 2;
	// stock document type
	public static final Integer STOCK_DOCUMENT_TYPE_MASHO = 0;
	public static final Integer STOCK_DOCUMENT_TYPE_SHAKEN = 1;
	// document convert to
	public static final Integer STOCK_DOCUMENT_TYPE_CONVERT_TO_EXPORT_CERTIFICATE = 1;
	public static final Integer STOCK_DOCUMENT_TYPE_CONVERT_TO_NAME_TRANSFER = 2;
	public static final Integer STOCK_DOCUMENT_TYPE_CONVERT_TO_DOMESTIC = 3;
	public static final Integer STOCK_DOCUMENT_TYPE_CONVERT_TO_MASHO = 4;
	public static final Integer STOCK_DOCUMENT_TYPE_CONVERT_TO_PARTS = 5;
	public static final Integer STOCK_DOCUMENT_TYPE_CONVERT_TO_SHUPPIN = 6;
	public static final Integer STOCK_DOCUMENT_TYPE_EXPIRED = 7;
	// rikuji status
	public static final Integer STOCK_RIKUJI_STATUS_0 = 0;
	public static final Integer STOCK_RIKUJI_STATUS_1 = 1;
	// Shipping ScheduleType
	public static final Integer SHIPPMENT_SCHEDULE_TYPE_IMMEDIATE = 0;
	public static final Integer SHIPPMENT_SCHEDULE_TYPE_NEXT_AVAILABLE_SHIP = 1;
	public static final Integer SHIPPMENT_SCHEDULE_TYPE_PREFERRED_MONTH = 2;
	// Export certificate status
	public static final Integer EXPORT_CERTIFICATE_INALAIN = 0;
	public static final Integer EXPORT_CERTIFICATE_SHIPPING_COMPANY = 1;
	public static final Integer EXPORT_CERTIFICATE_INSPECTION_COMPANY = 2;
	// EXPORT CERTIFICATE SHIPPING COMPANY DOCUMENT SENT STATUS
	public static final Integer EXPORT_CERTIFICATE_DOCUMENT_ORIGINAL_NOT_SENT = 0;
	public static final Integer EXPORT_CERTIFICATE_DOCUMENT_ORIGINAL_SENT = 1;
	public static final Integer EXPORT_CERTIFICATE_DOCUMENT_EMAIL_NOT_SENT = 0;
	public static final Integer EXPORT_CERTIFICATE_DOCUMENT_EMAIL_SENT = 1;

	// EXPORT CERTIFICATE DOC RECEIVED STATUS
	public static final Integer EXPORT_CERTIFICATE_ORIGINAL_NOT_RECEIVED = 0;
	public static final Integer EXPORT_CERTIFICATE_ORIGINAL_RECEIVED = 1;
	// EXPORT CERTIFICATE HANDOVER STATUS
	public static final Integer EXPORT_CERTIFICATE_NOT_HANDOVER = 0;
	public static final Integer EXPORT_CERTIFICATE_HANDOVER = 1;
	// EXPORT CERTIFICATE HANDOVER STATUS
	public static final Integer DOCUMENTS_HANDOVER_TO_ACCOUNTS = 0;
	public static final Integer DOCUMENTS_HANDOVER_TO_SALES = 1;
	public static final Integer DOCUMENTS_HANDOVER_TO_RECYCLE_PURPOSE = 2;
	public static final Integer DOCUMENTS_HANDOVER_TO_RIKUJI = 3;
	public static final Integer DOCUMENTS_HANDOVER_TO_SHIPPING = 4;
	public static final Integer DOCUMENTS_HANDOVER_TO_INSPECTION = 5;
	public static final Integer DOCUMENTS_HANDOVER_TO_DOCUMENTS_TEAM = 6;

	// shipping Type
	public static final Integer STOCK_SHIPPING_TYPE_RORO = 1;
	public static final Integer STOCK_SHIPPING_TYPE_CONTAINER = 2;
	// currency type
	public static final int CURRENCY_YEN = 1;// mapped in m_currency
	public static final int CURRENCY_USD = 2;// mapped in m_currency
	public static final int CURRENCY_AUD = 3;// mapped in m_currency
	public static final int CURRENCY_POUND = 4;// mapped in m_currency

	// bank transaction ref no prefix
	public static final String REF_PREFIX_PURCHASE_INVOICE = "PRCHS";
	public static final String REF_PREFIX_TRANSPORT_INVOICE = "TRNSPRT";
	public static final String REF_PREFIX_OTHER_INVOICE = "OTHERS";
	public static final String REF_PREFIX_FORWARDER_INVOICE = "FRWRDR";

	// transport invoice status
	public static final Integer TRANSPORT_INVOICE_NOT_BOOKED = 0;
	public static final Integer TRANSPORT_INVOICE_BOOKED = 1;
	public static final Integer TRANSPORT_INVOICE_MISMATCH_AMOUNT = 2;
	public static final Integer TRANSPORT_INVOICE_MISMATCH_AMOUNT_APPROVED = 3;

	// freight shipping Charge
	public static final Integer FREIGHT_SHIPPING_NO_CHARGES = 0;
	public static final Integer FREIGHT_SHIPPING_CHARGE = 1;
	// freight shipping charge completion
	public static final Integer FREIGHT_SHIPPING_CHARGE_NOT_COMPLETED = 0;
	public static final Integer FREIGHT_SHIPPING_CHARGE_COMPLETION = 1;
	// Invoice Upload Status
	public static final Integer INVOICE_NOT_UPLOADED = 0;
	public static final Integer INVOICE_UPLOADED = 1;
	// File invoice upload
	public static final String ATTACHMENT_DIRECTORY_AUCTION_INVOICE = "auction_invoice";
	public static final String ATTACHMENT_DIRECTORY_SHIPPING_INVOICE = "shipping_invoice";
	public static final String ATTACHMENT_DIRECTORY_TRANSPORT_INVOICE = "transport_invoice";
	public static final String ATTACHMENT_DIRECTORY_OTHER_INVOICE = "other_invoice";
	public static final String ATTACHMENT_DIRECTORY_FORWARDER_INVOICE = "forwarder_invoice";
	public static final String ATTACHMENT_DIRECTORY_INSPECTION_INVOICE = "inspection_invoice";
	// Attachment view status
	public static final Integer ATTACHMENT_NOT_VIEWED = 0;
	public static final Integer ATTACHMENT_VIEWED = 1;
	// File statement upload
	public static final String ATTACHMENT_DIRECTORY_AUCTION_BANK_STATEMENT = "auction_bank_statement";
	public static final String ATTACHMENT_DIRECTORY_TRANSPORT_BANK_STATEMENT = "transport_bank_statement";
	public static final String ATTACHMENT_DIRECTORY_OTHER_BANK_STATEMENT = "other_bank_statement";
	public static final String ATTACHMENT_DIRECTORY_FORWARDER_BANK_STATEMENT = "forwarder_bank_statement";
	public static final String ATTACHMENT_DIRECTORY_FREIGHT_BANK_STATEMENT = "freight_bank_statement";
	public static final String ATTACHMENT_DIRECTORY_DAYBOOK = "daybook_slip_upload";
	public static final String ATTACHMENT_DIRECTORY_INSPECTION_BANK_STATEMENT = "inspection_bank_statement";
	// Daybook Slip Upload Status
	public static final Integer SLIP_NOT_UPLOADED = 0;
	public static final Integer SLIP_UPLOADED = 1;
	// type others
	public static final String TYPE_OTHERS = "OTHERS";

	// COA Status
	public static final Integer COA_STATUS_TYPE_NEW = 1;
	public static final Integer COA_STATUS_TYPE_OLD = 0;

	// Clearing Account Day Book
	public static final Integer DAY_BOOK_CLEARING_ACCOUNT = 1;
	public static final Integer DAY_BOOK_NOT_CLEARED_ACCOUNT = 0;

	// InvoicePaymentTransaction
	public static final String TYPE_PURCHASE = "PUR";
	public static final String TYPE_TRANSPORT = "TRANS";
	public static final String TYPE_FREIGHT_SHIPPING = "FRTSHP";
	public static final String TYPE_GENERAL_EXPENSE = "GENEXP";
	public static final String TYPE_STORAGE_PHOTOS = "STORAGE";
	// Advance Pre Payment Remitter Type
	public static final String PAYMENT_BOOKING_PRE_ADVANCE_REMITTER_TYPE_SUPPLIER = "SUPPLIER";
	public static final String PAYMENT_BOOKING_PRE_ADVANCE_REMITTER_TYPE_FORWARDER = "FORWARDER";
	public static final String PAYMENT_BOOKING_PRE_ADVANCE_REMITTER_TYPE_TRANSPORTER = "TRANSPORTER";
	// Journal Entry Type
	public static final String TYPE_MANUAL = "MANUAL";
	// DayBook TT Approve
	public static final Integer DAYBOOK_TRANSACTION_NOT_APPROVED = 0;
	public static final Integer DAYBOOK_TRANSACTION_APPROVED = 1;
	// DayBook TT Approve
	public static final Integer ADVANCE_AMOUNT_NOT_REFUNDED = 0;
	public static final Integer ADVANCE_AMOUNT_REFUNDED = 1;
	public static final Integer ADVANCE_AMOUNT_REFUNDED_PARTIAL = 2;

	// DayBook TT Approve
	public static final Integer STOCK_INVENTORY_STATUS_INITIAL = 0;
	public static final Integer STOCK_INVENTORY_STATUS_GENERAL = 1;
	public static final Integer STOCK_INVENTORY_STATUS_STAGING = 2;
	public static final Integer STOCK_INVENTORY_STATUS_IN_TRANSIT = 3;
	public static final Integer STOCK_INVENTORY_STATUS_IN_CONSIGNMENT = 4;

	// Commmon Tax
	public static final Double COMMON_TAX = 10.00;
	public static final Double COMMON_TAX_PECENTAGE = 0.1;
	public static final Double COMMON_TAX_DIVISION_VALUE = 1.10;

	// TAKE TO RIKUJI
	public static final String UPDATE_RIKUJI_FROM_RECEIVED = "RECEIVED";
	public static final String UPDATE_RIKUJI_FROM_EXPORT_CERTIFICATE = "EXPORTCERTIFICATE";

	// customer credit limit
	public static final Integer NOT_CHECK_CREDIT_LIMIT = 0;
	public static final Integer CHECK_CREDIT_LIMIT = 1;

	//
	public static final String DEPARTMENT_SALES = "SALES";
	public static final String DEPARTMENT_ADMIN = "ADMIN";
	public static final String OVERALL_ADMIN = "ADMIN_ADMIN";

	// Date Range Constants
	public static final Integer BETWEEN_DATES = 0;
	public static final Integer LAST_FINANCIAL_YEAR = 1;
	public static final Integer CURRENT_FINANCIAL_YEAR = 2;
	public static final Integer LAST_6_MONTHS = 3;
	public static final Integer LAST_3_MONTHS = 4;

	// SALES ROLES
	public static final String ROLE_SALES_ADMIN = "SALES_ADMIN";
	public static final String ROLE_SALES_MANAGER = "SALES_MANAGER";

	// AccountType
	public static final Integer TYPE_FOREIGN_BANK_ACCOUNT = 0;
	public static final Integer TYPE_CLEARING_ACCOUNT = 1;
	public static final Integer TYPE_LOAN_ACCOUNT = 2;
	public static final Integer TYPE_SAVING_ACCOUNT = 3;
	// Daybook transaction type
	public static final Integer DAYBOOK_TRANSACTION_TYPE_BRANCH_REMIT = 1;
	public static final Integer DAYBOOK_TRANSACTION_TYPE_LOCAL_REMIT = 2;
	public static final Integer DAYBOOK_TRANSACTION_TYPE_EXPORT_REMIT = 3;
	public static final Integer DAYBOOK_TRANSACTION_TYPE_LC_REMIT = 4;
	public static final Integer DAYBOOK_TRANSACTION_TYPE_OTHERS = 5;
	public static final Integer DAYBOOK_TRANSACTION_TYPE_CAR_TAX_CLAIMED = 6;
	public static final Integer DAYBOOK_TRANSACTION_TYPE_RECYCLE_CLAIMED = 7;
	public static final Integer DAYBOOK_TRANSACTION_TYPE_INSURANCE_CLAIMED = 8;
	public static final Integer DAYBOOK_TRANSACTION_TYPE_CONSUMPTION_TAX_CLAIMED = 9;

	// Day book allocation type
	public static final int DAYBOOK_ALLOCATION_FIFO = 1;
	public static final int DAYBOOK_ALLOCATION_UNIT = 2;
	public static final int DAYBOOK_ALLOCATION_ADVANCE = 3;
	public static final int DAYBOOK_ALLOCATION_DEPOSITE = 4;
	public static final int DAYBOOK_ALLOCATION_LC = 5;

	// Coa Report Flag
	public static final String BALANCE_SHEET_REPORT_FLAG = "B";
	public static final String PROFIT_LOSS_REPORT_FLAG = "Pl";

	// Account Purchase Invoice Purchase Claim Status
	public static final Integer TPURCHASEINVOICE_PURCHASETAX_NOT_CLAIMED = 0;
	public static final Integer TPURCHASEINVOICE_PURCHASETAX_CLAIMED = 1;
	public static final Integer TPURCHASEINVOICE_PURCHASETAX_RECEIVED = 2;

	// Account Purchase Invoice Commission Claim Status
	public static final Integer TPURCHASEINVOICE_COMMISSIONTAX_NOT_CLAIMED = 0;
	public static final Integer TPURCHASEINVOICE_COMMISSIONTAX_CLAIMED = 1;
	public static final Integer TPURCHASEINVOICE_COMMISSIONTAX_RECEIVED = 2;

	// Repayment Status
	public static final Integer LOAN_REPAYMENT_NOT_PAID = 0;
	public static final Integer LOAN_REPAYMENT_PAID = 1;

	// Inspection Instruction Status
	public static final Integer INSPECTION_INSTRUCTION_GIVEN = 0;
	public static final Integer INSPECTION_INSTRUCTION_INITIATED = 1;
	public static final Integer INSPECTION_INSTRUCTION_CANCELLED = 2;

	// show stock for fb feed
	public static final Integer SHOW_STOCK_FOR_FEED = 1;
	public static final Integer NOT_SHOW_STOCK_FOR_FEED = 0;

	public static final Integer FLAG_NO = 0;
	public static final Integer FLAG_YES = 1;
	public static final String NUMBER_PLATE_NO = "no";

	public static final Integer ACCOUNT_TYPE_BANK = 0;

	// shipping invoice currency type
	public static final Integer RORO_INVOICE_CURRENCY_TYPE_YEN = 1;
	public static final Integer RORO_INVOICE_CURRENCY_TYPE_YEN_USD = 2;
	// shipping roro freight payment status
	public static final Integer SHIPPING_RORO_INVOICE_FREIGHT_PAYMENT_NOT_DONE = 0;
	public static final Integer SHIPPING_RORO_INVOICE_FREIGHT_PAYMENT_DONE = 1;
	// shipping invoice type
	public static final Integer SHIPPING_INVOICE_TYPE_RORO = 1;
	public static final Integer SHIPPING_INVOICE_TYPE_CONTAINER = 2;

	// TLcDetails Bill Of Exchange Status
	public static final Integer BILL_OF_EXCHANGE_NOT_CREATED = 0;
	public static final Integer BILL_OF_EXCHANGE_PARTIALLY_CREATED = 1;
	public static final Integer BILL_OF_EXCHANGE_CREATED = 2;

	// TLcInvoice Bill Of Exchange Status
	public static final Integer BILL_OF_EXCHANGE_NOT_UPDATED = 0;
	public static final Integer BILL_OF_EXCHANGE_UPDATED = 1;

//	TInvoicePaymentTransaction staus
	public static final Integer INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED = 0;
	public static final Integer INVOICE_PAYMENT_TRANSACTION_CANCELLED = 1;
//default reserve id
	public static final String DEFAULT_RESERVE_SALESPERSON_ID = "USR1091";

	public static final String DEFAULT_RESERVE_CUSTOMER = "";
	public static final String EQUIP_AC = "A/C";
	public static final String EQUIP_PS = "P/S";
	public static final String EQUIP_PW = "AP/W";
	public static final String EQUIP_SR = "S/R";
	public static final String EQUIP_AW = "A/W";
	public static final String EQUIP_ABS = "ABS";
	public static final String EQUIP_AIRBAG = "AIR BAG";
	public static final String EQUIP_4WD = "4WD";
	public static final String EQUIP_PM = "P/M";
	public static final String EQUIP_TV = "TV";
	public static final String EQUIP_CD = "CD";
	public static final String EQUIP_NV = "NV";
	public static final String EQUIP_RS = "R/S";
	public static final String EQUIP_FLAMP = "F/LAMP";
	public static final String EQUIP_PD = "POWER DOOR";
	public static final String EQUIP_SPD = "SINGLE POWER DOOR";
	public static final String EQUIP_LEATHER_SEAT = "LEATHER SEAT";
	public static final String EQUIP_h_LEATHER_SEAT = "HALF LEATHER SEAT";

	public static final String EMPTY_STRING = "";
	// daybook amount is paid by customer
	public static final Integer IS_AAJ_PAYABLE_N0 = 0;
	public static final Integer IS_AAJ_PAYABLE_YES = 1;
	// Department
	public static final Integer ROLE_ID_ACCOUNTS = 1;
	public static final Integer ROLE_ID_SHIPPING = 2;
	public static final Integer ROLE_ID_SALES = 3;
	public static final Integer ROLE_ID_DOCUMENTS = 4;
	public static final Integer ROLE_ID_ADMIN = 5;

	public static final Integer INVOICE_NOT_APPLIED = 0;
	public static final Integer INVOICE_APPLIED = 1;

	// inspection invoice Type
	public static final Integer INSPECTION_INVOICE_TYPE_INSPECTION = 1;
	public static final Integer INSPECTION_INVOICE_TYPE_REINSPECTION = 2;
	//lc invoice status
	public static final Integer LC_INVOICE_INIT = 0;
	public static final Integer LC_INVOICE_CANCELLED = 2;

	private Constants() {

	}

}
