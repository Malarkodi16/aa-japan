package com.nexware.aajapan.services.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.AccountTransactionConstants;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TLoanDetailsDto;
import com.nexware.aajapan.models.MBank;
import com.nexware.aajapan.models.TAccountsTransaction;
import com.nexware.aajapan.models.TLoan;
import com.nexware.aajapan.models.TLoanDetails;
import com.nexware.aajapan.repositories.MasterBankRepository;
import com.nexware.aajapan.repositories.TLoanDetailsRepository;
import com.nexware.aajapan.repositories.TLoanRepository;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.TAccountsTransactionService;
import com.nexware.aajapan.services.TLoanService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class LoanCreateServiceImpl implements TLoanService {

	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private TLoanRepository loanRepository;

	@Autowired
	private MasterBankRepository mBankRepository;
	@Autowired
	private TAccountsTransactionService accountsTransactionService;
	@Autowired
	private TLoanDetailsRepository loanDetailsRepository;

	@Override
	public void advancePaymentCompleteTransactions(TLoan loanCreate) {
		// loanCreate.setSequence(this.sequenceService.getNextSequence(Constants.SEQUENCE_KEY_CREATE_LOAN));
		loanRepository.save(loanCreate);

		final MBank mBank = mBankRepository.findOneByBankSeq(loanCreate.getBank());
		// create account transaction entry - Amount - Credit
		// TODO: Calculate Total buy adding Amount and Interest
		accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(null, null, mBank.getCoaCode(),
				Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT, loanCreate.getLoanAmount(),
				AccountTransactionConstants.ACCOUNT_TRANSACTION_LOAN, loanCreate.getDate()));
		// create account transaction entry - Amount - debit
		accountsTransactionService.accountTransactionEntry(
				new TAccountsTransaction(null, null, AccountTransactionConstants.LOAN_PRINCIPLE, Constants.CURRENCY_YEN,
						Constants.TRANSACTION_DEBIT, loanCreate.getLoanAmount(),
						AccountTransactionConstants.ACCOUNT_TRANSACTION_LOAN, loanCreate.getDate()));
		// TODO: Interest Calculation

	}

	@Transactional
	@Override
	public void createLoan(TLoan loan) {
		Date dueDate = loan.getFirstPaymentDate();
		int month;
		final int loanTerm = loan.getLoanTerm();
		double interestPaid = 0.0, totalPayable = 0.0;
		double principalPaid = 0.0;
		final String loanId = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_CREATE_LOAN);
		if (StringUtils.equalsIgnoreCase(loan.getLoanType(), "4")) {
			totalPayable = 0.0;
			Double tempCapital = loan.getLoanAmount();
			principalPaid = 0.0;
			interestPaid = 0.0;
			Double monthlyEmi = 0.0;
			final Double capital = loan.getLoanAmount(), interst = loan.getRateOfInterest();
			Double totalPaid = 0.0, newBalance = 0.0;
			final Double intr = interst / 12 / 100;
			monthlyEmi = capital * intr * ((Math.pow((1 + intr), loanTerm) / ((Math.pow((1 + intr), loanTerm) - 1))));
			totalPayable = monthlyEmi * loanTerm;

			loan.setTotalPayable(totalPayable);
			loan.setClosingBalance(totalPayable);
			loan.setLoanId(loanId);
			loanRepository.insert(loan);
			for (month = 1; month <= loanTerm; month++) {
				if (month != 1)
					dueDate = AppUtil.addMonths(dueDate, 1);

				interestPaid = (tempCapital * interst) / (100 * 12);
				// Double n = interst.

				totalPaid = monthlyEmi;
				principalPaid = monthlyEmi - interestPaid;
				newBalance = totalPayable - totalPaid;
				tempCapital = tempCapital - principalPaid;
				if (totalPaid > 0) {
					final TLoanDetails loanDetails = new TLoanDetails(loanId, dueDate, principalPaid, interestPaid,
							totalPaid, totalPayable, newBalance, Constants.LOAN_REPAYMENT_NOT_PAID);
					loanDetails.setLoanDtlId(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_CREATE_LNDTL));
					loanDetailsRepository.insert(loanDetails);
					totalPayable = totalPayable - totalPaid;

				}
			}
		} else {
			loan.setLoanId(loanId);
			// create loan

			double newBalance;
			double monthlyInterestRate;
			double monthlyPayment, totalPaid, tempPrincipal, totalInterest = 0.0;
			totalPayable = 0.0;
			final int numMonths = loan.getLoanTerm();

			final double principal = loan.getLoanAmount();

			monthlyInterestRate = loan.getRateOfInterest() / 12;
			monthlyPayment = monthlyPayment(loan.getLoanAmount(), monthlyInterestRate, numMonths / 12);

			final int paymentFrequency = 1;
			final int interestFreq = loan.getInterestPaymentFrequency();
			int principalFreq = loan.getPrincipalPaymentFrequency();
			int prnFre = 0;
			tempPrincipal = principal;
			if (principalFreq == 3)
				prnFre = 4;
			else if (principalFreq == 1)
				prnFre = 12;
			else if (principalFreq == 12)
				prnFre = 1;
			else if (principalFreq == 6)
				prnFre = 2;
			else if (principalFreq == 10) {
				prnFre = 1;
				principalFreq = loan.getLoanTerm();
			}

			for (month = 1; month <= numMonths; month++) {
				if (month != 1)
					dueDate = AppUtil.addMonths(dueDate, interestFreq);
				// Compute amount paid and new balance for each payment period

				interestPaid = tempPrincipal * (monthlyInterestRate / 100);
				totalInterest = totalInterest + interestPaid;
				if (month % principalFreq == 0) {

					principalPaid = (principal / ((numMonths / loan.getLoanTerm()) * prnFre));
					tempPrincipal = tempPrincipal - principalPaid;
				}
			}
			totalPayable = principal + totalInterest;
			tempPrincipal = loan.getLoanAmount();
			loan.setTotalPayable(totalPayable);
			loan.setClosingBalance(totalPayable);
			loanRepository.insert(loan);

			for (month = 1; month <= numMonths; month++) {
				interestPaid = 0.0;
				principalPaid = 0.0;
				if (month != 1)
					dueDate = AppUtil.addMonths(dueDate, interestFreq);
				// Compute amount paid and new balance for each payment period

				if (month % interestFreq == 0) {
					interestPaid = tempPrincipal * (monthlyInterestRate / 100) * interestFreq;
				}
				if (month % principalFreq == 0) {

					principalPaid = (principal / ((numMonths / loan.getLoanTerm()) * prnFre));
					tempPrincipal = tempPrincipal - principalPaid;
				}
				totalPaid = interestPaid + principalPaid;
				newBalance = totalPayable - totalPaid;
				if (totalPaid > 0) {
					final TLoanDetails loanDetails = new TLoanDetails(loanId, dueDate, principalPaid, interestPaid,
							totalPaid, totalPayable, newBalance, Constants.LOAN_REPAYMENT_NOT_PAID);
					loanDetails.setLoanDtlId(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_CREATE_LNDTL));
					loanDetailsRepository.insert(loanDetails);
				}
				// Update the balance
				totalPayable = newBalance;

			}
		}
		// GL transaction entry
		final MBank bank = mBankRepository.findOneByBankSeq(loan.getBank());
		accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(loan.getLoanId(), null,
				bank.getCoaCode(), Constants.CURRENCY_YEN, Constants.TRANSACTION_DEBIT, loan.getLoanAmount(),
				AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_LOAN_CREATE, null));
		accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(loan.getLoanId(), null,
				AccountTransactionConstants.LOAN_PRINCIPLE, Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT,
				loan.getLoanAmount(), AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_LOAN_CREATE, null));
		accountsTransactionService.accountTransactionEntry(
				new TAccountsTransaction(loan.getLoanId(), null, AccountTransactionConstants.LOAN_INTEREST,
						Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT, totalPayable - loan.getLoanAmount(),
						AccountTransactionConstants.ACCOUNT_TRANSACTION_SOURCE_LOAN_CREATE, null));
	}

	private static void getSchedule(double principal, double annualInterestRate, int numYears) {
		double interestPaid;
		double principalPaid;
		double newBalance;
		double monthlyInterestRate;
		double monthlyPayment;		int month;
		final int numMonths = numYears * 12;

		// Output monthly payment and total payment
		monthlyInterestRate = annualInterestRate / 12;
		monthlyPayment = monthlyPayment1(principal, annualInterestRate, numYears, 1);
		System.out.format("Monthly Payment: %8.2f%n", monthlyPayment);
		System.out.format("Total Payment:   %8.2f%n", monthlyPayment * numYears * 12);
		printTableHeader();
		for (month = 1; month <= numMonths; month++) {
			// Compute amount paid and new balance for each payment period
			interestPaid = principal * (monthlyInterestRate / 100);
			principalPaid = monthlyPayment - interestPaid;
			newBalance = principal - principalPaid;

			// Output the data item
			printScheduleItem(month, interestPaid, principalPaid, newBalance);

			// Update the balance
			principal = newBalance;
		}
	}

	/**
	 * @param loanAmount
	 * @param monthlyInterestRate in percent
	 * @param numberOfYears
	 * @return the amount of the monthly payment of the loan
	 */
	private static double monthlyPayment(double loanAmount, double monthlyInterestRate, int numberOfYears) {
		monthlyInterestRate /= 100; // e.g. 5% => 0.05

		return loanAmount * monthlyInterestRate / (1 - 1 / Math.pow(1 + monthlyInterestRate, numberOfYears * 12));
	}

	private static double monthlyPayment1(double loanAmount, double yearlyInterestRate, int numberOfYears,
			double time) {
		//
		final double R = loanAmount;
		final double i = yearlyInterestRate / time;

		return R * ((1 - 1 / Math.pow(1 + i, numberOfYears * 12)) / i);
	}

	/**
	 * Prints a table data of the amortization schedule as a table row.
	 */
	private static void printScheduleItem(int month, double interestPaid, double principalPaid, double newBalance) {
		System.out.format("%8d%10.2f%10.2f%12.2f\n", month, interestPaid, principalPaid, newBalance);
	}

	/**
	 * Prints the table header for the amortization schedule.
	 */
	private static void printTableHeader() {
		System.out.println("\nAmortization schedule");
		for (int i = 0; i < 40; i++) { // Draw a line
			System.out.print("-");
		}
		System.out.format("\n%8s%10s%10s%12s\n", "Payment#", "Interest", "Principal", "Balance");
		System.out.format("%8s%10s%10s%12s\n\n", "", "paid", "paid", "");
	}

	@Override
	public void calcEmi(double loanAmount, double annualInterestRate, int numberOfMonths) {
		getSchedule(loanAmount, annualInterestRate, numberOfMonths);

	}

	@Override
	public List<TLoanDetailsDto> getConformLoanDetails(double loanAmount, double monthlyInterestRate,
			int numberOfMonths, double monthlyEmi, Date paymentDate, int loanType) {
//		TLoanListDto tLoanListDto = new TLoanListDto();
		Date dueDate = paymentDate;
		Date tempDate = paymentDate;
		
		int month;
		final int loanTerm = numberOfMonths;
		double interestPaid = 0.0;
		double totalPayable = 0.0;
		double principalPaid = 0.0;
		double interestRate = monthlyInterestRate / 100;
		List<TLoanDetailsDto> loanListDto = new ArrayList<TLoanDetailsDto>();
		TLoanDetailsDto tLoanDetailsDto = null;
		if (loanType == 4) {

			Double monthlyEmiPaid = monthlyEmi;
			double capital = loanAmount;
			double closingBalance = loanAmount;
			for (month = 1; month <= loanTerm; month++) {
				Date dueDateTemp = paymentDate;
				principalPaid = monthlyEmiPaid;
				if (month != 1) {
					tempDate = AppUtil.addMonths(tempDate, 1);
					LocalDate due = tempDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					DayOfWeek dayOfWeek = DayOfWeek.from(due);

					switch (dayOfWeek.getValue()) {

					case 6:
						dueDate = tempDate;
						dueDate = AppUtil.addDays(dueDate, 2);
						break;
					case 7:
						dueDate = tempDate;
						dueDate = AppUtil.addDays(dueDate, 1);
						break;
					default:
						dueDate = tempDate;
						break;
					}
					capital = closingBalance;
					if (month == loanTerm) {
						principalPaid = closingBalance;
						closingBalance = capital - closingBalance;
					} else {
						closingBalance = capital - principalPaid;
					}
				} else {
					LocalDate due = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					DayOfWeek dayOfWeek = DayOfWeek.from(due);
					switch (dayOfWeek.getValue()) {

					case 6:
						dueDate = AppUtil.addDays(dueDate, 2);
						break;
					case 7:
						dueDate = AppUtil.addDays(dueDate, 1);
						break;
					default:
						break;
					}
					capital = loanAmount;
					closingBalance = capital - principalPaid;
				}
				dueDateTemp = AppUtil.addMonths(dueDateTemp, month);
				LocalDate dueD = dueDateTemp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				DayOfWeek dayOfWeek = DayOfWeek.from(dueD);
				switch (dayOfWeek.getValue()) {

				case 6:
					dueDateTemp = AppUtil.addDays(dueDateTemp, 2);
					break;
				case 7:
					dueDateTemp = AppUtil.addDays(dueDateTemp, 1);
					break;
				default:
					break;
				}
				Date dueDate1 = AppUtil.addMonths(tempDate, 1);
				LocalDate due1 = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate due2 = dueDateTemp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				long days = ChronoUnit.DAYS.between(due1, due2);
				System.out.println(days);
				interestPaid = (closingBalance * interestRate / 365) * (days);
				totalPayable = principalPaid + interestPaid;
				tLoanDetailsDto = new TLoanDetailsDto();
				tLoanDetailsDto.setAmount(month == loanTerm ? principalPaid : totalPayable);
				tLoanDetailsDto.setDueDate(dueDate);
				tLoanDetailsDto.setInterestAmount(month == loanTerm ? 0 : interestPaid);
				tLoanDetailsDto.setOpeningBalance(capital);
				tLoanDetailsDto.setPrincipalAmt(principalPaid);
				tLoanDetailsDto.setClosingBalance(closingBalance);
				tLoanDetailsDto.setStatus(0);
				tLoanDetailsDto.setMonth(month);
				loanListDto.add(tLoanDetailsDto);
			}
		}
		return loanListDto;
	}
}
