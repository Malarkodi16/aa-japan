package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.TTUnitAllocationDto;

public interface TLcInvoiceRepositoryCustom {

//	void updateInvoiceById(String id, Map<String, String> data);
	
	List<TTUnitAllocationDto> findAllInvoiceByLcInvoiceNo(String lcInvoiceNo,String customerId);

}