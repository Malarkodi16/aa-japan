package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import com.nexware.aajapan.dto.TBankTransactionDto;

public interface TBankTransactionRepositoryCustom {

	public Double bankCurrentBalanceByBankIdAndCurrency(String bankid, int currency);

	public Double bankCurrentBankBalanceByBankIdAndCurrency(String bankid, int currency);

	List<TBankTransactionDto> getBankBalanceAccountsTransactionList(String bank, Date fromDate, Date toDate);

}
