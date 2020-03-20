package com.nexware.aajapan.repositories.custom;

import java.util.List;
import java.util.Optional;

import org.bson.Document;

import com.nexware.aajapan.dto.ProformaInvoiceDto;
import com.nexware.aajapan.models.ProformaInvoiceItem;

public interface TProformaInvoiceCustom {
	Optional<List<ProformaInvoiceDto>> findAllProformaInvoiceDetailsByCustId(String custId);

	Optional<List<ProformaInvoiceDto>> findAllProformaInvoiceBySalesPerson(List<String> salesPersonIds);

	ProformaInvoiceDto findByInvoiceNo(String invoiceNo);

	Document findByProformaInvoiceNo(String invoiceNo);
	
	List<ProformaInvoiceItem> findAllProformaInvoiceByCustId(String custId);
	
	long getCountBySalesPersonId(List<String> salesPersonIds);
}
