package com.nexware.aajapan.services.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.CoaDetails;
import com.nexware.aajapan.dto.GenaralExpensesDto;
import com.nexware.aajapan.dto.GlReportDetails;
import com.nexware.aajapan.dto.MCOAProfitLossDto;
import com.nexware.aajapan.dto.PaymentTrackingReportDto;
import com.nexware.aajapan.dto.SubProfitLossDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.repositories.MCOARepository;
import com.nexware.aajapan.repositories.MGeneralLedgerRepository;
import com.nexware.aajapan.repositories.TFreightShippingInvoiceRepository;
import com.nexware.aajapan.repositories.TFwdrInvoiceRepository;
import com.nexware.aajapan.repositories.TInspectionInvoiceRepository;
import com.nexware.aajapan.repositories.TInvoiceRepository;
import com.nexware.aajapan.repositories.TPurchaseInvoiceRepository;
import com.nexware.aajapan.repositories.TTransportInvoiceRepository;
import com.nexware.aajapan.services.AccountReportService;
import com.nexware.aajapan.utils.AppUtil;
import com.nexware.aajapan.utils.ExcelUtil;

@Service
@Transactional
public class AccountReportServiceImpl implements AccountReportService {
	@Autowired
	private MCOARepository coaRepository;
	@Autowired
	private TPurchaseInvoiceRepository purchaseInvoiceRepository;
	@Autowired
	private MGeneralLedgerRepository generalLedgerRepository;
	@Autowired
	private TInvoiceRepository invoiceRepository;
	@Autowired
	private TTransportInvoiceRepository tTransportInvoiceRepository;
	@Autowired
	private TFreightShippingInvoiceRepository freightShippingInvoiceRepository;
	@Autowired
	private TFwdrInvoiceRepository tFwdrInvoiceRepository;
	@Autowired
	private TInspectionInvoiceRepository inspectionInvoiceRepository;

	@Override
	public void exportBalanceSheetReport(Date date, HttpServletResponse response) throws IOException {
		StringBuilder filename = new StringBuilder("balance_sheet").append(" - ").append("test.xlsx");
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			String sDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
			Sheet sheet = workbook.createSheet("Balance Sheet");
			Integer rowNo = 0;
			Row row;
			Document result;
			// create first row
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			// merge title cell
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 2));
			// set cell value
			ExcelUtil.setCellValue(row.getCell(0), "AAJ Statement of Financial Position as at " + sDate);
			ExcelUtil.addHorizontalAlignment(row.getCell(0), HorizontalAlignment.CENTER);
			ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));

			// create asset row
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			ExcelUtil.setCellValue(row.getCell(0), "Asset");
			ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 2));
			// set date
			ExcelUtil.setCellValue(row.getCell(1), "as at " + sDate);
			ExcelUtil.addHorizontalAlignment(row.getCell(0), HorizontalAlignment.CENTER);

			double total = 0;
			// write Current Assets
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			ExcelUtil.setCellValue(row.getCell(0), "Current Assets");
			ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
			result = this.coaRepository.getBalanceStatementByReportingType("Current Assets", date);
			if (!AppUtil.isObjectEmpty(result)) {
				rowNo = this.writeBalanceReportCurrentAssets(workbook, sheet, rowNo, result);
				total += result.get("total", Number.class).doubleValue();
			}
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			// write Non Current Assets
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			ExcelUtil.setCellValue(row.getCell(0), "Non Current Assets");
			ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
			result = this.coaRepository.getBalanceStatementByReportingType("Non Current Assets", date);
			if (!AppUtil.isObjectEmpty(result)) {
				rowNo = this.writeBalanceReport(workbook, sheet, rowNo, result);
				total += result.get("total", Number.class).doubleValue();
			}

			// write total
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			ExcelUtil.setCellValue(row.getCell(0), "Total Assets");
			ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
			ExcelUtil.setCellValue(row.getCell(2), total);
			ExcelUtil.setBoldFont(row.getCell(2), ExcelUtil.getBoldFont(workbook));
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			// Equities & Liablities
			total = 0;
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			ExcelUtil.setCellValue(row.getCell(0), "Equities & Liablities");
			ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
			// write Equity
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			ExcelUtil.setCellValue(row.getCell(0), "Equity");
			ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
			result = this.coaRepository.getBalanceStatementByReportingType("Stake Holders Equity", date);
			if (!AppUtil.isObjectEmpty(result)) {
				rowNo = this.writeBalanceReport(workbook, sheet, rowNo, result);
				total += result.get("total", Number.class).doubleValue();
			}
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			// Liabilities
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			ExcelUtil.setCellValue(row.getCell(0), "Liabilities");
			ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
			// write report - Current Liabilities
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			ExcelUtil.setCellValue(row.getCell(0), "Current Liabilities");
			ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
			result = this.coaRepository.getBalanceStatementByReportingType("Current Liabilities", date);
			if (!AppUtil.isObjectEmpty(result)) {
				rowNo = this.writeBalanceReport(workbook, sheet, rowNo, result);
				total += result.get("total", Number.class).doubleValue();
			}
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			// write report - Non Current Liabilities
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			ExcelUtil.setCellValue(row.getCell(0), "Non Current Liabilities");
			ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
			result = this.coaRepository.getBalanceStatementByReportingType("Non Current Liabilities", date);
			if (!AppUtil.isObjectEmpty(result)) {
				rowNo = this.writeBalanceReport(workbook, sheet, rowNo, result);
				total += result.get("total", Number.class).doubleValue();
			}

			// write total
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			ExcelUtil.setCellValue(row.getCell(0), "Total Equities & Liablities");
			ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
			ExcelUtil.setCellValue(row.getCell(2), total);
			ExcelUtil.setBoldFont(row.getCell(2), ExcelUtil.getBoldFont(workbook));
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			// Profit & Loss
			List<MCOAProfitLossDto> plData = this.coaRepository.getProfitLossList(AppUtil.startDateOfMonth(date), date);
			Double pl = plData.stream().mapToDouble(MCOAProfitLossDto::getYtdAmount).sum();
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			ExcelUtil.setCellValue(row.getCell(0), "Profit/(Loss) for the year");
			ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
			ExcelUtil.setCellValue(row.getCell(1), pl);
			// export excel
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=" + filename);
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			throw new AAJRuntimeException("Exception while creating balance sheet report.", e);
		}

	}

	private int writeBalanceReport(Workbook workbook, Sheet sheet, int rowNo, Document result) {
		Row row;
		double value;
		@SuppressWarnings("unchecked")
		List<Document> items = (List<Document>) result.get("items");
		int count = items.size();
		double total = 0;
		for (Document doc : items) {
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			ExcelUtil.setCellValue(row.getCell(0), "     " + doc.getString("subAccount"));
			value = doc.get("amount", Number.class).doubleValue();
			ExcelUtil.setCellValue(row.getCell(1), value);
			total += value;
			if (--count == 0) {
				ExcelUtil.setCellValue(row.getCell(2), total);
				ExcelUtil.setBoldFont(row.getCell(2), ExcelUtil.getBoldFont(workbook));
			}
		}
		return rowNo;
	}

	private int writeBalanceReportCurrentAssets(Workbook workbook, Sheet sheet, int rowNo, Document result) {

		Row row;
		double value;
		@SuppressWarnings("unchecked")
		List<Document> items = (List<Document>) result.get("items");
		int count = items.size();
		double total = 0;
		double bankBalance = 0;
		for (Document doc : items) {
			value = doc.get("amount", Number.class).doubleValue();
			if (doc.getString("account").equalsIgnoreCase("Bank") && (count > 1)) {
				bankBalance += value;
				count--;
				total += value;
				continue;
			}
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			ExcelUtil.setCellValue(row.getCell(0), "     " + doc.getString("subAccount"));
			ExcelUtil.setCellValue(row.getCell(1), value);
			total += value;
			if (--count == 0) {
				row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
				ExcelUtil.setCellValue(row.getCell(0), "     Bank");
				ExcelUtil.setCellValue(row.getCell(1), bankBalance);
				ExcelUtil.setCellValue(row.getCell(2), total);
				ExcelUtil.setBoldFont(row.getCell(2), ExcelUtil.getBoldFont(workbook));
			}
		}
		return rowNo;
	}

	@Override
	public void exportProfitAndLossReport(Integer flag, Date fromDate, Date toDate, HttpServletResponse response) {
		List<MCOAProfitLossDto> result = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(flag)) {
			if (flag.equals(Constants.LAST_FINANCIAL_YEAR)) {
				Date[] dates = AppUtil.getLastFinancialYearDates(new Date());
				fromDate = dates[0];
				toDate = dates[1];
			} else if (flag.equals(Constants.CURRENT_FINANCIAL_YEAR)) {
				Date[] dates = AppUtil.getCurrentFinancialYearDates(new Date());
				fromDate = dates[0];
				toDate = dates[1];
			} else if (flag.equals(Constants.LAST_6_MONTHS)) {
				Date startDate = AppUtil.addMonths(-6);
				fromDate = startDate;
				toDate = new Date();
			} else if (flag.equals(Constants.LAST_3_MONTHS)) {
				Date startDate = AppUtil.addMonths(-3);
				fromDate = startDate;
				toDate = new Date();
			}
			result = this.coaRepository.getProfitLossList(fromDate, toDate);

			try (XSSFWorkbook workbook = new XSSFWorkbook()) {
				String startDateStr = new SimpleDateFormat("dd-MM-yyyy").format(fromDate);
				String toDateStr = new SimpleDateFormat("dd-MM-yyyy").format(toDate);
				StringBuilder filename = new StringBuilder("profit_and_loss_").append(startDateStr).append(" - ")
						.append(toDateStr).append(" - ").append(".xlsx");
				Sheet sheet = workbook.createSheet("Balance Sheet");
				Integer rowNo = 0;
				Row row;

				// create title row
				row = ExcelUtil.createRow(workbook, sheet, rowNo++, 5);
				// merge title cell
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 4));
				ExcelUtil.setCellValue(row.getCell(0),
						"Income Statement for the Period ended " + startDateStr + " - " + toDateStr);
				ExcelUtil.addHorizontalAlignment(row.getCell(0), HorizontalAlignment.CENTER);
				ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
				rowNo += 1;
				// create header row
				row = ExcelUtil.createRow(workbook, sheet, rowNo++, 5);
				ExcelUtil.setCellValue(row.getCell(3), "PTD");
				ExcelUtil.setBoldFont(row.getCell(3), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.setCellValue(row.getCell(4), "YTD");
				ExcelUtil.setBoldFont(row.getCell(4), ExcelUtil.getBoldFont(workbook));
				// Revenue
				Map<String, Object> output;
				double grossProfitPtd = 0.0;
				double grossProfitYtd = 0.0;
				output = writePLSubAccountDetails(workbook, sheet, rowNo, result, "Revenue", "Revenue", "Net Revenue");
				rowNo = (int) output.get("rowNo");
				grossProfitPtd += (double) output.get("totalPtd");
				grossProfitYtd += (double) output.get("totalYtd");
				rowNo += 1;
				// Cost of Goods Sold
				output = writePLSubAccountDetails(workbook, sheet, rowNo, result, "Cost of Goods Sold",
						"Cost of Goods Sold", "Total Cost of Good Sold");
				rowNo = (int) output.get("rowNo");
				grossProfitPtd -= (double) output.get("totalPtd");
				grossProfitYtd -= (double) output.get("totalYtd");
				rowNo += 1;
				// Gross profit
				row = ExcelUtil.createRow(workbook, sheet, rowNo++, 5);
				ExcelUtil.setCellValue(row.getCell(0), "Gross Profit");
				ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.setCellValue(row.getCell(3), grossProfitPtd);
				ExcelUtil.setBoldFont(row.getCell(3), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.setCellValue(row.getCell(4), grossProfitYtd);
				ExcelUtil.setBoldFont(row.getCell(4), ExcelUtil.getBoldFont(workbook));
				rowNo += 1;
				// Other Income & Gains
				output = writePLSubAccountDetails(workbook, sheet, rowNo, result, "Other Income & Gains",
						"Other Income & Gains", "Total Other Income & Gains");
				rowNo = (int) output.get("rowNo");
				rowNo += 1;
				// Selling Expenses
				double totalSellingOperationalPtd = 0.0;
				double totalSellingOperationalYtd = 0.0;
				output = writePLSubAccountDetails(workbook, sheet, rowNo, result, "Selling Expenses",
						"Selling Expenses", "Total Selling Expenses");
				rowNo = (int) output.get("rowNo");
				totalSellingOperationalPtd += (double) output.get("totalPtd");
				totalSellingOperationalYtd += (double) output.get("totalYtd");
				rowNo += 1;

				// Operational Expenses
				output = writePLSubAccountDetails(workbook, sheet, rowNo, result, "Operational Expenses",
						"Operational Expenses", "Total Operational Expenses");
				rowNo = (int) output.get("rowNo");
				totalSellingOperationalPtd += (double) output.get("totalPtd");
				totalSellingOperationalYtd += (double) output.get("totalYtd");
				rowNo += 1;
				// total selling and operational expenses
				double totalFinanceExpensesPtd = totalSellingOperationalPtd;
				double totalFinanceExpensesYtd = totalSellingOperationalYtd;
				row = ExcelUtil.createRow(workbook, sheet, rowNo++, 5);
				ExcelUtil.setCellValue(row.getCell(0), "Total Selling & Operational Expenses");
				ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.setCellValue(row.getCell(3), totalSellingOperationalPtd);
				ExcelUtil.setBoldFont(row.getCell(3), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.setCellValue(row.getCell(4), totalSellingOperationalYtd);
				ExcelUtil.setBoldFont(row.getCell(4), ExcelUtil.getBoldFont(workbook));
				rowNo += 1;
				// Interest Expenses
				output = writePLSubAccountDetails(workbook, sheet, rowNo, result, "Interest Expenses",
						"Interest Expenses", "Total Interest Expenses");
				totalFinanceExpensesPtd += (double) output.get("totalPtd");
				totalFinanceExpensesYtd += (double) output.get("totalYtd");
				rowNo = (int) output.get("rowNo");
				rowNo += 1;
				// Total Finance Expenses
				row = ExcelUtil.createRow(workbook, sheet, rowNo++, 5);
				ExcelUtil.setCellValue(row.getCell(0), "Total Finance Expenses");
				ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.setCellValue(row.getCell(3), totalFinanceExpensesPtd);
				ExcelUtil.setBoldFont(row.getCell(3), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.setCellValue(row.getCell(4), totalFinanceExpensesYtd);
				ExcelUtil.setBoldFont(row.getCell(4), ExcelUtil.getBoldFont(workbook));
				rowNo += 1;
				// Profit/ (Loss) before tax
				double profitLossPtd = (grossProfitPtd - totalFinanceExpensesPtd);
				double profitLossYtd = (grossProfitYtd - totalFinanceExpensesYtd);
				row = ExcelUtil.createRow(workbook, sheet, rowNo++, 5);
				ExcelUtil.setCellValue(row.getCell(0), "Profit/ (Loss) before tax");
				ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.setCellValue(row.getCell(3), (grossProfitPtd - totalFinanceExpensesPtd));
				ExcelUtil.setBoldFont(row.getCell(3), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.setCellValue(row.getCell(4), (grossProfitYtd - totalFinanceExpensesYtd));
				ExcelUtil.setBoldFont(row.getCell(4), ExcelUtil.getBoldFont(workbook));
				rowNo += 1;
				// Income Taxes
				output = writePLSubAccountDetails(workbook, sheet, rowNo, result, "Income Taxes", "Income Taxes",
						"Total Income Taxes");
				rowNo = (int) output.get("rowNo");
				rowNo += 1;
				// Profit/ (Loss) after tax
				row = ExcelUtil.createRow(workbook, sheet, rowNo++, 5);
				ExcelUtil.setCellValue(row.getCell(0), "Profit/ (Loss) after tax");
				ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.setCellValue(row.getCell(3),
						(profitLossPtd - (profitLossPtd * (Constants.COMMON_TAX / 100.0))));
				ExcelUtil.setBoldFont(row.getCell(3), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.setCellValue(row.getCell(4),
						(profitLossYtd - (profitLossYtd * (Constants.COMMON_TAX / 100.0))));
				ExcelUtil.setBoldFont(row.getCell(4), ExcelUtil.getBoldFont(workbook));
				// export excel
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition", "attachment; filename=" + filename);
				workbook.write(response.getOutputStream());
			} catch (Exception e) {
				throw new AAJRuntimeException("Exception while creating balance sheet report.", e);
			}
		}
	}

	private Map<String, Object> writePLSubAccountDetails(Workbook workbook, Sheet sheet, int rowNo,
			List<MCOAProfitLossDto> result, String account, String accountTitle, String totalTitle) {
		Map<String, Object> output = new HashMap<>();
		Row row = ExcelUtil.createRow(workbook, sheet, rowNo++, 5);
		ExcelUtil.setCellValue(row.getCell(0), accountTitle);
		ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
		ExcelUtil.fillCellColor(row.getCell(0), IndexedColors.LIGHT_ORANGE.getIndex());
		Optional<MCOAProfitLossDto> profitLoss;
		List<SubProfitLossDto> subProfitLoss;

		// write revenue details
		profitLoss = result.stream().filter(report -> report.getAccount().equalsIgnoreCase(account)).findFirst();
		double totalPtd = 0.0;
		double totalYtd = 0.0;
		if (profitLoss.isPresent()) {
			subProfitLoss = profitLoss.get().getItems();
			for (SubProfitLossDto item : subProfitLoss) {
				row = ExcelUtil.createRow(workbook, sheet, rowNo++, 5);
				double ptd = item.getPtdAmount();
				double ytd = item.getYtdAmount();
				ExcelUtil.setCellValue(row.getCell(1), item.getCode());
				ExcelUtil.setCellValue(row.getCell(2), item.getSubAccount());
				ExcelUtil.setCellValue(row.getCell(3), ptd);
				ExcelUtil.setCellValue(row.getCell(4), ytd);
				totalPtd += ptd;
				totalYtd += ytd;
			}
		}
		row = ExcelUtil.createRow(workbook, sheet, rowNo++, 5);
		ExcelUtil.setCellValue(row.getCell(0), totalTitle);
		ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
		ExcelUtil.setCellValue(row.getCell(3), totalPtd);
		ExcelUtil.setBoldFont(row.getCell(3), ExcelUtil.getBoldFont(workbook));
		ExcelUtil.setCellValue(row.getCell(4), totalYtd);
		ExcelUtil.setBoldFont(row.getCell(4), ExcelUtil.getBoldFont(workbook));
		output.put("rowNo", rowNo);
		output.put("totalPtd", totalPtd);
		output.put("totalYtd", totalYtd);
		return output;

	}

	@Override
	public void exportPaymentTrackingExcel(Integer type, String supplier, Date invoiceDateFrom, Date invoiceDateTo,
			HttpServletResponse response) {
		StringBuilder filename = new StringBuilder("payment_tracking").append(" - ").append("test.xlsx");
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Report");
			Integer rowNo = 0;
			Row row;
			// create information row
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 2);
			ExcelUtil.setCellValue(row.getCell(0), "Company");
			ExcelUtil.setCellValue(row.getCell(1), "AA Japan");
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 2);
			ExcelUtil.setCellValue(row.getCell(0), "Title");
			ExcelUtil.setCellValue(row.getCell(1), "Payment Tracking Report");
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 2);
			String reportDate = "";
			if (AppUtil.isObjectEmpty(invoiceDateFrom)) {
				reportDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			} else if (!AppUtil.isObjectEmpty(invoiceDateFrom) && !AppUtil.isObjectEmpty(invoiceDateTo)) {
				reportDate = new SimpleDateFormat("dd-MM-yyyy").format(invoiceDateFrom) + " to "
						+ new SimpleDateFormat("dd-MM-yyyy").format(invoiceDateTo);
			}
			ExcelUtil.setCellValue(row.getCell(0), "Date");
			ExcelUtil.setCellValue(row.getCell(1), reportDate);
			rowNo++;
			// create title row
			row = ExcelUtil.createRow(workbook, sheet, rowNo++, 11);
			ExcelUtil.setCellValue(row.getCell(0), "Payment Voucher No.");
			ExcelUtil.setCellValue(row.getCell(1), "Invoice Date");
			ExcelUtil.setCellValue(row.getCell(2), "Supplier Invoice No.");
			ExcelUtil.setCellValue(row.getCell(3), "Remit To");
			ExcelUtil.setCellValue(row.getCell(4), "Total Amount");
			ExcelUtil.setCellValue(row.getCell(5), "Paid Amount");
			ExcelUtil.setCellValue(row.getCell(6), "Payment Amount");
			ExcelUtil.setCellValue(row.getCell(7), "Balance Amount");
			ExcelUtil.setCellValue(row.getCell(8), "Approved Date");
			ExcelUtil.setCellValue(row.getCell(9), "Status");
			final List<Integer> invoiceStatus = Arrays.asList(Constants.INV_STATUS_VERIFIED,
					Constants.INV_CANCEL_CHARGE_UPDATED);
			List<PaymentTrackingReportDto> reportDtos = new ArrayList<>();
			if (type == 0) {
				reportDtos = purchaseInvoiceRepository.purchasepaymentTrackingReport(invoiceStatus, supplier,
						invoiceDateFrom, invoiceDateTo);
			} else if (type == 1) {
				reportDtos = tTransportInvoiceRepository.purchasepaymentTrackingReport(invoiceStatus, supplier,
						invoiceDateFrom, invoiceDateTo);
			} else if (type == 4) {
				reportDtos = invoiceRepository.purchasepaymentTrackingReport(invoiceStatus, supplier, invoiceDateFrom,
						invoiceDateTo);
			} else if (type == 2) {
				reportDtos = tFwdrInvoiceRepository.purchasepaymentTrackingReport(supplier, invoiceDateFrom,
						invoiceDateTo);
			} else if (type == 5) {
				reportDtos = inspectionInvoiceRepository.purchasepaymentTrackingReport(supplier, invoiceDateFrom,
						invoiceDateTo);
			} else {
			}
			for (PaymentTrackingReportDto paymentTrackingReportDto : reportDtos) {
				row = ExcelUtil.createRow(workbook, sheet, rowNo++, 11);
				ExcelUtil.setCellValue(row.getCell(0), paymentTrackingReportDto.getPaymentVoucherNo());
				ExcelUtil.setCellValue(row.getCell(1), paymentTrackingReportDto.getInvoiceDate());
				ExcelUtil.setCellValue(row.getCell(2), paymentTrackingReportDto.getInvoiceNo());
				ExcelUtil.setCellValue(row.getCell(3), paymentTrackingReportDto.getInvoiceName());
				ExcelUtil.setCellValue(row.getCell(4), paymentTrackingReportDto.getTotalAmount());
				ExcelUtil.setCellValue(row.getCell(5), paymentTrackingReportDto.getPaidAmount());
				ExcelUtil.setCellValue(row.getCell(6), paymentTrackingReportDto.getPaymentAmount());
				ExcelUtil.setCellValue(row.getCell(7), paymentTrackingReportDto.getBalance());
				ExcelUtil.setCellValue(row.getCell(8), paymentTrackingReportDto.getApprovedDate());
				Integer paymentStatus = paymentTrackingReportDto.getPaymentApproveStatus();
				if (type == 5) {
					if (AppUtil.isObjectEmpty(paymentStatus)) {
						ExcelUtil.setCellValue(row.getCell(9), "PAYMENT_NOT_APPROVED");
					} else if (paymentStatus == Constants.INSPECTION_PAYMENT_INVOICE_BOOKING_APPROVED) {
						ExcelUtil.setCellValue(row.getCell(9), "INVOICE_APPROVED");
					} else if (paymentStatus == Constants.INSPECTION_PAYMENT_INVOICE_PROCESSING) {
						ExcelUtil.setCellValue(row.getCell(9), "PAYMENT_PROCESSING");
					} else if (paymentStatus == Constants.INSPECTION_PAYMENT_INVOICE_PROCESSING_PARTIAL) {
						ExcelUtil.setCellValue(row.getCell(9), "PAYMENT_PARTIAL");
					} else if (paymentStatus == Constants.INSPECTION_PAYMENT_INVOICE_APPROVAL) {
						ExcelUtil.setCellValue(row.getCell(9), "PAYMENT_COMPLETED");
					} else if (paymentStatus == Constants.INSPECTION_PAYMENT_INVOICE_PAYMENT_COMPLETED) {
						ExcelUtil.setCellValue(row.getCell(9), "PAYMENT_FREEZE");
					} else if (paymentStatus == Constants.INSPECTION_PAYMENT_INVOICE_PAYMENT_CANCELLED) {
						ExcelUtil.setCellValue(row.getCell(9), "PAYMENT_CANCELED");
					}
				} else {
					if (paymentStatus == Constants.PAYMENT_NOT_APPROVED) {
						ExcelUtil.setCellValue(row.getCell(9), "PAYMENT_NOT_APPROVED");
					} else if (paymentStatus == Constants.PAYMENT_APPROVED) {
						ExcelUtil.setCellValue(row.getCell(9), "PAYMENT_APPROVED");
					} else if (paymentStatus == Constants.PAYMENT_COMPLETED) {
						ExcelUtil.setCellValue(row.getCell(9), "PAYMENT_COMPLETED");
					} else if (paymentStatus == Constants.PAYMENT_CANCELED) {
						ExcelUtil.setCellValue(row.getCell(9), "PAYMENT_CANCELED");
					} else if (paymentStatus == Constants.PAYMENT_FREEZE) {
						ExcelUtil.setCellValue(row.getCell(9), "PAYMENT_FREEZE");
					} else if (paymentStatus == Constants.PAYMENT_PARTIAL) {
						ExcelUtil.setCellValue(row.getCell(9), "PAYMENT_PARTIAL");
					}
				}

			}
			// export excel

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=" + filename);
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			throw new AAJRuntimeException("Exception while creating balance sheet report.", e);
		}

	}

	@Override
	public void exportPaymentTrackingFreightANdShippingExcel(Integer type, String supplier, Date invoiceDateFrom,
			Date invoiceDateTo, HttpServletResponse response) {
		StringBuilder filename = new StringBuilder("payment_tracking").append(" - ").append("test.xlsx");
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Report");
			Integer rowNo = 0;
			Row row;
			// create information row
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 2);
			ExcelUtil.setCellValue(row.getCell(0), "Company");
			ExcelUtil.setCellValue(row.getCell(1), "AA Japan");
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 2);
			ExcelUtil.setCellValue(row.getCell(0), "Title");
			ExcelUtil.setCellValue(row.getCell(1), "Payment Tracking Report");
			row = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 2);
			String reportDate = "";
			if (AppUtil.isObjectEmpty(invoiceDateFrom)) {
				reportDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			} else if (!AppUtil.isObjectEmpty(invoiceDateFrom) && !AppUtil.isObjectEmpty(invoiceDateTo)) {
				reportDate = new SimpleDateFormat("dd-MM-yyyy").format(invoiceDateFrom) + " to "
						+ new SimpleDateFormat("dd-MM-yyyy").format(invoiceDateTo);
			}
			ExcelUtil.setCellValue(row.getCell(0), "Date");
			ExcelUtil.setCellValue(row.getCell(1), reportDate);
			rowNo++;
			// create title row
			row = ExcelUtil.createRow(workbook, sheet, rowNo++, 14);
			ExcelUtil.setCellValue(row.getCell(0), "Payment Voucher No.");
			ExcelUtil.setCellValue(row.getCell(1), "Invoice Date");
			ExcelUtil.setCellValue(row.getCell(2), "Supplier Invoice No.");
			ExcelUtil.setCellValue(row.getCell(3), "Remit To");
			ExcelUtil.setCellValue(row.getCell(4), "Total Amount");
			ExcelUtil.setCellValue(row.getCell(5), "Total Amount Usd");
			ExcelUtil.setCellValue(row.getCell(6), "Paid Amount");
			ExcelUtil.setCellValue(row.getCell(7), "Paid Amount Usd");
			ExcelUtil.setCellValue(row.getCell(8), "Payment Amount");
			ExcelUtil.setCellValue(row.getCell(9), "Balance Amount");
			ExcelUtil.setCellValue(row.getCell(10), "Balance Amount Usd");
			ExcelUtil.setCellValue(row.getCell(11), "Approved Date");
			ExcelUtil.setCellValue(row.getCell(12), "Status");
			List<PaymentTrackingReportDto> reportDtos = freightShippingInvoiceRepository
					.purchasepaymentTrackingReport(supplier, invoiceDateFrom, invoiceDateTo);

			for (PaymentTrackingReportDto paymentTrackingReportDto : reportDtos) {
				row = ExcelUtil.createRow(workbook, sheet, rowNo++, 14);
				ExcelUtil.setCellValue(row.getCell(0), paymentTrackingReportDto.getPaymentVoucherNo());
				ExcelUtil.setCellValue(row.getCell(1), paymentTrackingReportDto.getInvoiceDate());
				ExcelUtil.setCellValue(row.getCell(2), paymentTrackingReportDto.getInvoiceNo());
				ExcelUtil.setCellValue(row.getCell(3), paymentTrackingReportDto.getInvoiceName());
				ExcelUtil.setCellValue(row.getCell(4), paymentTrackingReportDto.getTotalAmount());
				ExcelUtil.setCellValue(row.getCell(5), paymentTrackingReportDto.getTotalAmountUsd());
				ExcelUtil.setCellValue(row.getCell(6), paymentTrackingReportDto.getPaidAmount());
				ExcelUtil.setCellValue(row.getCell(7), paymentTrackingReportDto.getPaidAmountUsd());
				ExcelUtil.setCellValue(row.getCell(8), paymentTrackingReportDto.getPaymentAmount());
				ExcelUtil.setCellValue(row.getCell(9), paymentTrackingReportDto.getBalance());
				ExcelUtil.setCellValue(row.getCell(10), paymentTrackingReportDto.getBalanceAmountUsd());
				ExcelUtil.setCellValue(row.getCell(11), paymentTrackingReportDto.getApprovedDate());
				Integer paymentStatus = paymentTrackingReportDto.getPaymentApproveStatus();
				if (paymentStatus == Constants.PAYMENT_NOT_APPROVED) {
					ExcelUtil.setCellValue(row.getCell(12), "PAYMENT_NOT_APPROVED");
				} else if (paymentStatus == Constants.PAYMENT_APPROVED) {
					ExcelUtil.setCellValue(row.getCell(12), "PAYMENT_APPROVED");
				} else if (paymentStatus == Constants.PAYMENT_COMPLETED) {
					ExcelUtil.setCellValue(row.getCell(12), "PAYMENT_COMPLETED");
				} else if (paymentStatus == Constants.PAYMENT_CANCELED) {
					ExcelUtil.setCellValue(row.getCell(12), "PAYMENT_CANCELED");
				} else if (paymentStatus == Constants.PAYMENT_FREEZE) {
					ExcelUtil.setCellValue(row.getCell(12), "PAYMENT_FREEZE");
				} else if (paymentStatus == Constants.PAYMENT_PARTIAL) {
					ExcelUtil.setCellValue(row.getCell(12), "PAYMENT_PARTIAL");
				}

			}
			// export excel

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=" + filename);
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			throw new AAJRuntimeException("Exception while creating balance sheet report.", e);
		}

	}

	@Override
	public void exportGlReport(Date fromDate, Date toDate, HttpServletResponse response) {
		List<GlReportDetails> result = new ArrayList<>();
		result = this.generalLedgerRepository.getTransactions(fromDate, toDate);

		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			String startDateStr = new SimpleDateFormat("dd-MM-yyyy").format(fromDate);
			String toDateStr = new SimpleDateFormat("dd-MM-yyyy").format(toDate);
			StringBuilder filename = new StringBuilder("gl_report_").append(startDateStr).append(" - ")
					.append(toDateStr).append(" - ").append(".xlsx");
			Sheet sheet = workbook.createSheet("Balance Sheet");
			Integer rowNo = 0;
			Row row;

			// create title row
			row = ExcelUtil.createRow(workbook, sheet, rowNo++, 5);
			// merge title cell
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 4));
			ExcelUtil.setCellValue(row.getCell(0),
					"Income Statement for the Period ended " + startDateStr + " - " + toDateStr);
			ExcelUtil.addHorizontalAlignment(row.getCell(0), HorizontalAlignment.CENTER);
			ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
			rowNo += 1;

			for (GlReportDetails glreport : result) {
				row = ExcelUtil.createRow(workbook, sheet, rowNo++, 5);
				ExcelUtil.setCellValue(row.getCell(0), glreport.getName());
				ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.setCellValue(row.getCell(4), glreport.getTotal());
				ExcelUtil.setBoldFont(row.getCell(4), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.fillCellColor(row.getCell(0), IndexedColors.GREY_25_PERCENT.getIndex());
				ExcelUtil.fillCellColor(row.getCell(1), IndexedColors.GREY_25_PERCENT.getIndex());
				ExcelUtil.fillCellColor(row.getCell(2), IndexedColors.GREY_25_PERCENT.getIndex());
				ExcelUtil.fillCellColor(row.getCell(3), IndexedColors.GREY_25_PERCENT.getIndex());
				ExcelUtil.fillCellColor(row.getCell(4), IndexedColors.GREY_25_PERCENT.getIndex());
				// create header
				row = ExcelUtil.createRow(workbook, sheet, rowNo++, 5);
				ExcelUtil.setCellValue(row.getCell(0), "code");
				ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.setCellValue(row.getCell(1), "category");
				ExcelUtil.setBoldFont(row.getCell(1), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.setCellValue(row.getCell(2), "account");
				ExcelUtil.setBoldFont(row.getCell(2), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.setCellValue(row.getCell(3), "sub account");
				ExcelUtil.setBoldFont(row.getCell(3), ExcelUtil.getBoldFont(workbook));
				ExcelUtil.setCellValue(row.getCell(4), "balance");
				ExcelUtil.setBoldFont(row.getCell(4), ExcelUtil.getBoldFont(workbook));
				for (CoaDetails coa : glreport.getCoa_details()) {
					row = ExcelUtil.createRow(workbook, sheet, rowNo++, 5);
					ExcelUtil.setCellValue(row.getCell(0), coa.getCode());
					ExcelUtil.setBoldFont(row.getCell(0), ExcelUtil.getBoldFont(workbook));
					ExcelUtil.setCellValue(row.getCell(1), coa.getReportingCategory());
					ExcelUtil.setBoldFont(row.getCell(1), ExcelUtil.getBoldFont(workbook));
					ExcelUtil.setCellValue(row.getCell(2), coa.getAccount());
					ExcelUtil.setBoldFont(row.getCell(2), ExcelUtil.getBoldFont(workbook));
					ExcelUtil.setCellValue(row.getCell(3), coa.getSubAccount());
					ExcelUtil.setBoldFont(row.getCell(3), ExcelUtil.getBoldFont(workbook));
					ExcelUtil.setCellValue(row.getCell(4), coa.getBalance());
					ExcelUtil.setBoldFont(row.getCell(4), ExcelUtil.getBoldFont(workbook));
				}

			}

			// export excel
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=" + filename);
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			throw new AAJRuntimeException("Exception while creating balance sheet report.", e);
		}
	}

	@Override
	public void exportGenaralExpensesReport(HttpServletResponse response) throws IOException {
		StringBuilder filename = new StringBuilder("genaral_expenses").append(" - ").append(System.currentTimeMillis())
				.append(".xlsx");
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Report");
			Integer rowNo = 0;
			Row row;
			// create title row
			row = ExcelUtil.createRow(workbook, sheet, rowNo++, 12);
			ExcelUtil.setCellValue(row.getCell(0), "Date");
			ExcelUtil.setCellValue(row.getCell(1), "Invoice No");
			ExcelUtil.setCellValue(row.getCell(2), "Remit To");
			ExcelUtil.setCellValue(row.getCell(3), "Due Date");
			ExcelUtil.setCellValue(row.getCell(4), "Category");
			ExcelUtil.setCellValue(row.getCell(5), "Description");
			ExcelUtil.setCellValue(row.getCell(6), "Amount(¥)");
			ExcelUtil.setCellValue(row.getCell(7), "Tax Amount");
			ExcelUtil.setCellValue(row.getCell(8), "Total Amount");
			ExcelUtil.setCellValue(row.getCell(9), "Source Currency");
			ExcelUtil.setCellValue(row.getCell(10), "Amount");
			ExcelUtil.setCellValue(row.getCell(11), "Exchange Rate");
			List<GenaralExpensesDto> reportDtos = invoiceRepository.findAllGenaralExpensesNotApproved();
			for (GenaralExpensesDto paymentTrackingReportDto : reportDtos) {
				row = ExcelUtil.createRow(workbook, sheet, rowNo++, 12);
				ExcelUtil.setCellValue(row.getCell(0), paymentTrackingReportDto.getDate());
				ExcelUtil.setCellValue(row.getCell(1), paymentTrackingReportDto.getInvoiceNo());
				ExcelUtil.setCellValue(row.getCell(2), paymentTrackingReportDto.getRemitTo());
				ExcelUtil.setCellValue(row.getCell(3), paymentTrackingReportDto.getDueDate());
				ExcelUtil.setCellValue(row.getCell(4), paymentTrackingReportDto.getFormattedCategory());
				ExcelUtil.setCellValue(row.getCell(5), paymentTrackingReportDto.getDescription());
				ExcelUtil.setCellValue(row.getCell(6), paymentTrackingReportDto.getAmountInYen());
				ExcelUtil.setCellValue(row.getCell(7), paymentTrackingReportDto.getTaxAmount());
				ExcelUtil.setCellValue(row.getCell(8), paymentTrackingReportDto.getTotalAmount());
				ExcelUtil.setCellValue(row.getCell(9), paymentTrackingReportDto.getSourceCurrency());
				ExcelUtil.setCellValue(row.getCell(10), paymentTrackingReportDto.getAmount());
				ExcelUtil.setCellValue(row.getCell(11), paymentTrackingReportDto.getExchangeRate());
			}
			// export excel

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=" + filename);
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			throw new AAJRuntimeException("Exception while creating balance sheet report.", e);
		}

	}

}