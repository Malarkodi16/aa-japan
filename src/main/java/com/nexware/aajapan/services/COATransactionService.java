package com.nexware.aajapan.services;

import com.nexware.aajapan.models.MCOA;

public interface COATransactionService {

	MCOA modifyAndGetCurrentCOA(Long coaCode, Double amount, int transactionType);

	Long checkStockInventoryStatusAndGetCoaCode(String stockNo, Long coaCode);
}
