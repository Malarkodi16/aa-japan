package com.nexware.aajapan.services;

import java.util.Date;
import java.util.List;

import com.nexware.aajapan.dto.TLoanDetailsDto;
import com.nexware.aajapan.dto.TLoanListDto;
import com.nexware.aajapan.models.TLoan;

public interface TLoanService {
	void createLoan(TLoan loan);

	void advancePaymentCompleteTransactions(TLoan loanCreate);

	void calcEmi(double loanAmount, double monthlyInterestRate, int numberOfMonths);
	
	List<TLoanDetailsDto> getConformLoanDetails(double loanAmount, double monthlyInterestRate, int numberOfMonths, double monthlyEmi, Date paymentDate, int loanType);
}
