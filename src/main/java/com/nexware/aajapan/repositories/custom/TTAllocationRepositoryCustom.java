package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.TDayBookDto;
import com.nexware.aajapan.models.TDayBookTransaction;
import com.nexware.aajapan.models.TSalesInvoice;

public interface TTAllocationRepositoryCustom {

	TSalesInvoice getSalesinvoiceById(String id);

	List<TDayBookDto> getEntryByStatus();

	// Double getDayBookBalanceAmountById(String id);

	List<TDayBookTransaction> getInvoiceBasedReceivedAmount(String dayBookId, String inviceNo, String stockId);

}
