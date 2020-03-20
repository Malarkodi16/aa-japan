package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.TLoanDetailsDto;
import com.nexware.aajapan.dto.TLoanRepaymentDto;

public interface TLoanDetailsRepositoryCustom {
	
	TLoanRepaymentDto findLoanWithMinDateAndNotPaid(String loanId);
	
	TLoanRepaymentDto preCloseLoan(String loanId);
	
	List<TLoanDetailsDto> getEditLoanDetail(String loanId);
	
	boolean existsByStatus(String loanId);
	

}
