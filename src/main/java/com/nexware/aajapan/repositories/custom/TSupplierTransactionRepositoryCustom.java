package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import com.nexware.aajapan.dto.TSupplierTransactionDto;

public interface TSupplierTransactionRepositoryCustom {

	Double getCreditBalanceAmount(String supplierCode);

	List<TSupplierTransactionDto> findTransactionsBySupplierAndDate(String supplier, Date from, Date to);

}
