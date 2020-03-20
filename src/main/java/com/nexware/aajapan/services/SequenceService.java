package com.nexware.aajapan.services;

import java.util.Date;

public interface SequenceService {

	String getNextSequence(String key);

	String getNextPurchaseInvoiceNo(String key, Date date, String sequenceId);

	String generateSalesCustomerSeqProformaInvioceId(String proformaId, long count);

}
