package com.nexware.aajapan.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.CustomerTransactionDto;
import com.nexware.aajapan.dto.CustomerTransactionExcelDto;
import com.nexware.aajapan.dto.CustomerTransactionExcelItemDto;
import com.nexware.aajapan.dto.CustomerTransactionItemsDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.models.TCustomerTransaction;
import com.nexware.aajapan.repositories.TCustomerRepository;
import com.nexware.aajapan.repositories.TCustomerTransactionRepository;
import com.nexware.aajapan.repositories.TSalesInvoiceRepository;
import com.nexware.aajapan.services.TCustomerTransactionService;
import com.nexware.aajapan.utils.AppUtil;
import com.nexware.aajapan.utils.ExcelUtil;

@Service
@Transactional
public class TCustomerTransactionServiceImpl implements TCustomerTransactionService {
	@Autowired
	private TCustomerRepository customerRepository;
	@Autowired
	private TCustomerTransactionRepository customerTransactionRepository;
	@Autowired
	private TSalesInvoiceRepository tSalesInvoiceRepository;

	@Override
	public void customerTransactionEntry(TCustomerTransaction transaction) {
		TCustomer customer = this.updateCustomerBalance(transaction.getCustomerId(), Constants.TRANSACTION_DEBIT,
				transaction.getAmount());
		this.updateCustomerAdvanceAmount(transaction.getCustomerId(), transaction.getTransactionType(),
				transaction.getAmount());
		transaction.setClosingBalance(customer.getBalance());
		this.customerTransactionRepository.insert(transaction);
	}

	@Override
	public TCustomer updateCustomerBalance(String customerId, Integer transactionType, Double amount) {
		return this.customerRepository.updateBalance(customerId, transactionType, amount);

	}

	@Override
	public TCustomer updateCustomerAdvanceAmount(String customerId, Integer transactionType, Double amount) {
		return this.customerRepository.updateAdvanceAmount(customerId, transactionType, amount);

	}

	@Override
	public TCustomer updateCustomerDepositAmount(String customerId, Integer transactionType, Double amount) {
		return this.customerRepository.updateDepositeAmount(customerId, transactionType, amount);

	}

	@Override
	public Double getAmountReceivedForStockByCustomer(String customerId, String stockNo) {
		return this.customerTransactionRepository.getAmountReceivedForStockByCustomer(customerId, stockNo);
	}

	@Override
	public void exportAllCustomerTransactions(String customerId, Date minDate, Date maxDate,
			HttpServletResponse response) throws IOException {
		StringBuilder filename = new StringBuilder("customer_transactions").append(" - ").append("test.xlsx");
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			// String sDate = new SimpleDateFormat("dd-MM-yyyy").format(minDate);
			Sheet sheet = workbook.createSheet("Customer Sheet");
			Integer rowNo = 0;

			// create first row
			/*
			 * List<CustomerTransactionDto> list = this.tSalesInvoiceRepository
			 * .findAllCustomerTransactionsInExcel(customerId, minDate, maxDate);
			 */

			List<CustomerTransactionDto> list = this.tSalesInvoiceRepository.findAllCustomerTransactions(customerId,
					minDate, maxDate);
			List<CustomerTransactionExcelDto> finalList = new ArrayList<CustomerTransactionExcelDto>();
			List<Date> dateArray = new ArrayList<Date>();
			if (!list.isEmpty()) {
				String custId = list.stream().findFirst().get().getCustomerId();
				String currencySymbol = list.stream().findFirst().get().getCurrencySymbol();
				Date startDate = AppUtil.isObjectEmpty(minDate) ? list.stream().findFirst().get().getDate() : minDate;
				Date lastDate = AppUtil.isObjectEmpty(maxDate) ? list.get(list.size() - 1).getDate() : maxDate;
				dateArray = AppUtil.getArrayOfMonthsBetweenDates(startDate, lastDate);
				dateArray.stream().forEach(date -> {
					CustomerTransactionExcelDto obj = new CustomerTransactionExcelDto();
					obj.setCustomerId(custId);
					obj.setDate(date);
					obj.setCurrencySymbol(currencySymbol);

					CustomerTransactionExcelItemDto excelData = new CustomerTransactionExcelItemDto();

					Double allPaidAmount = this.customerTransactionRepository
							.getBroughtForwardAmountForCustomer(customerId, date);
					Double totalReservedPrice = this.tSalesInvoiceRepository.getPreviousMonthReservedPrice(customerId,
							date);
					excelData.setBroughtForward(totalReservedPrice + allPaidAmount);
					excelData.setStockItems(
							this.tSalesInvoiceRepository.findAllCustomerTransactionsOnExpand(customerId, date));

					Double paidAmount = this.customerTransactionRepository.getpaymentAmountForCustomer(customerId,
							date);
					excelData.setPaidAmount(paidAmount);
					obj.setDataItems(excelData);
					finalList.add(obj);
				});
			}

			String[] COLUMNS = { "#", "PURCHASED DATE", "STOCK NO", "LOT NO", "AUCTION", "CHASSY NO", "PURCHASED PRICE",
					"COMMISSION", "ADDITION", "RESERVE PRICE", "RECEIVED AMOUNT", "SHIIPING STATUS" };

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLUE.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			Row headerRow1 = sheet.createRow(rowNo++);
			for (int col = 0; col < COLUMNS.length; col++) {
				ExcelUtil.autoSizeColumnNumber(sheet, col);
				Cell cell = headerRow1.createCell(col);
				cell.setCellValue(COLUMNS[col]);
				cell.setCellStyle(headerCellStyle);
			}

			for (int i = 0; i < finalList.size(); i++) {
				CustomerTransactionExcelDto data = finalList.get(i);
				Row dateRow = ExcelUtil.createRow(workbook, sheet, rowNo++, 7);
				ExcelUtil.createDateStyle(dateRow.getCell(1), data.getDate(), "MMMM yyyy", workbook, 15, true);

				DataFormat dataformat = workbook.createDataFormat();

				CellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setDataFormat(dataformat.getFormat("¥ ###0,00"));

				CellStyle defaultUSDCurrency = workbook.createCellStyle();
				defaultUSDCurrency.setDataFormat(dataformat.getFormat("[$$-409]#,##0.00"));

				CustomerTransactionExcelItemDto stockList = data.getDataItems();
				Row broughtForwardRow = sheet.createRow(rowNo++);
				broughtForwardRow.createCell(8).setCellValue("Brought Forward");
				ExcelUtil.getBoldFontInHeightsWithColor(workbook, 10, (short) 8, true);
				Cell broughtForwardCell = broughtForwardRow.createCell(9);
				broughtForwardCell.setCellValue(stockList.getBroughtForward());
				broughtForwardCell.setCellStyle(defaultUSDCurrency);
				int sNo = 0;

				for (int j = 0; j < stockList.getStockItems().size(); j++) {
					CustomerTransactionItemsDto stock = stockList.getStockItems().get(j);
					Row stockRow = sheet.createRow(rowNo++);
					stockRow.createCell(0).setCellValue(sNo++);
					stockRow.createCell(1).setCellValue(AppUtil.ifNull(stock.getsPurchasedDate(), ""));
					stockRow.createCell(2).setCellValue(AppUtil.ifNull(stock.getStockNo(), ""));
					stockRow.createCell(3).setCellValue(AppUtil.ifNull(stock.getLotNo(), ""));
					stockRow.createCell(4).setCellValue(AppUtil.ifNull(stock.getAuction(), ""));
					stockRow.createCell(5).setCellValue(AppUtil.ifNull(stock.getChassisNo(), ""));

					Cell purchaseCell = stockRow.createCell(6);
					purchaseCell.setCellValue(AppUtil.ifNull(stock.getPurchaseCost(), 0.0));
					purchaseCell.setCellStyle(cellStyle);

					Cell commissionCell = stockRow.createCell(7);
					commissionCell.setCellValue(AppUtil.ifNull(stock.getCommision(), 0.0));
					commissionCell.setCellStyle(cellStyle);

					Cell addnCell = stockRow.createCell(8);
					addnCell.setCellValue(AppUtil.ifNull(stock.getAdditionalCharges(), 0.0));
					addnCell.setCellStyle(cellStyle);

					Cell priceCell = stockRow.createCell(9);
					priceCell.setCellValue(AppUtil.ifNull(stock.getPrice(), 0.0));
					priceCell.setCellStyle(defaultUSDCurrency);

					Cell recvdPriceCell = stockRow.createCell(10);
					recvdPriceCell.setCellValue(AppUtil.ifNull(stock.getReceivedAmount(), 0.0));
					recvdPriceCell.setCellStyle(defaultUSDCurrency);
					stockRow.createCell(11).setCellValue(
							AppUtil.ifNull(stock.getShippingStatus() == 0 ? "IDLE" : "SHIPPING INSTRUCTION GIVEN", ""));
				}

				Row totalSales = sheet.createRow(rowNo++);
				totalSales.createCell(8).setCellValue("Total Sales");
				ExcelUtil.getBoldFontInHeightsWithColor(workbook, 10, (short) 8, true);
				Double totalSalesValue = stockList.getStockItems().stream()
						.mapToDouble(CustomerTransactionItemsDto::getPrice).sum();
				Double totalBroughtForward = totalSalesValue + stockList.getBroughtForward();
				Cell totalBroughtFrwdCell = totalSales.createCell(9);
				totalBroughtFrwdCell.setCellValue(totalBroughtForward);
				totalBroughtFrwdCell.setCellStyle(defaultUSDCurrency);

				Row paidAmountRow = sheet.createRow(rowNo++);
				paidAmountRow.createCell(8).setCellValue("Paid");
				ExcelUtil.getBoldFontInHeightsWithColor(workbook, 10, (short) 8, true);
				Cell paidAmountCell = paidAmountRow.createCell(9);
				paidAmountCell.setCellValue(stockList.getPaidAmount());
				paidAmountCell.setCellStyle(defaultUSDCurrency);

				Row balanceAmountRow = sheet.createRow(rowNo++);
				balanceAmountRow.createCell(8).setCellValue("Balance");
				ExcelUtil.getBoldFontInHeightsWithColor(workbook, 10, (short) 8, true);
				Cell balanceAmountCell = balanceAmountRow.createCell(9);
				balanceAmountCell.setCellValue(totalBroughtForward + stockList.getPaidAmount());
				balanceAmountCell.setCellStyle(defaultUSDCurrency);

			}

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=" + filename);
			workbook.write(response.getOutputStream());
		} catch (final Exception e) {
			throw new AAJRuntimeException("Exception while creating balance sheet report.", e);
		}
	}

}
