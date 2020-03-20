package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.LoanSearchDto;
import com.nexware.aajapan.dto.TLoanListDto;

public interface TLoanRepositoryCustom {

	List<TLoanListDto> getLoanDetails(String bank, String reference, String sequence);

	List<TLoanListDto> getAllLoanDetails();

	List<LoanSearchDto> findBySearchDto(String search);
}
