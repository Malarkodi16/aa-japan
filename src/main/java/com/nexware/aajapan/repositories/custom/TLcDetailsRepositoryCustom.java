package com.nexware.aajapan.repositories.custom;

import java.util.List;
import java.util.Optional;

import org.bson.Document;

import com.nexware.aajapan.dto.LcInvoiceDto;
import com.nexware.aajapan.dto.TLcInvoiceDto;

public interface TLcDetailsRepositoryCustom {
	Optional<List<LcInvoiceDto>> findAllLcInvoice();

	Optional<List<LcInvoiceDto>> findOneLcInvoiceByLcNO(String lcNo);

	Optional<LcInvoiceDto> findOneLcInvoiceById(String id);

	List<TLcInvoiceDto> findOneLcDetailsId(String lcInvoiceNo);

	List<Document> getBillOfExchangeDetails();

	List<Document> updateFields();

	List<LcInvoiceDto> findAllBillOfExchangeUpdated();

}
