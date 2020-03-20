package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import com.nexware.aajapan.dto.DayBookListDto;
import com.nexware.aajapan.dto.OwnedTransactionTTDto;
import com.nexware.aajapan.dto.TTApproveDto;

public interface TDayBookTransactionCustom {

	List<TTApproveDto> fetchDayBookTransactionToApprove();

	List<TTApproveDto> fetchDayBookTransactionToApproveOwned(Date fromDate, Date toDate);

	List<TTApproveDto> findAllByAllocationType(Integer type);

	TTApproveDto fetchOneDayBookTransactionToApprove(String id);

	List<DayBookListDto> listDayBookEntry();

	List<DayBookListDto> listDayBookEntryApproved(Date fromDate, Date toDate);

	List<OwnedTransactionTTDto> getOwnedTransactionList(String salesPersonId);
	
	List<DayBookListDto> findCarTaxClaimedList();
}
