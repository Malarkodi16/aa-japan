package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.nexware.aajapan.dto.BalanceAndPlDto;
import com.nexware.aajapan.dto.MCOABalanceStatementDto;
import com.nexware.aajapan.dto.MCOADto;
import com.nexware.aajapan.dto.MCOAProfitLossDto;
import com.nexware.aajapan.models.MCOA;

public interface MCOARepositoryCustom {
	List<MCOA> findSubAccountTypes(String reportingCategory);

	List<MCOA> findAllByStatus(Integer status);

	List<MCOABalanceStatementDto> getBalanceStatementList(Date toDate);

	List<MCOAProfitLossDto> getProfitLossList(Date fromDate, Date toDate);

	List<MCOADto> findAllByReportFlag();

	List<BalanceAndPlDto> balanceStatementsearchData(Date toDate);

	List<BalanceAndPlDto> profitAndLossSearchData(Date fromDate, Date toDate);

	Document getBalanceStatementByReportingType(String reportingCategory, Date toDate);

}
