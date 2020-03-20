package com.nexware.aajapan.services.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.CustomerTransactionDto;
import com.nexware.aajapan.dto.TSalesInvoiceDto;
import com.nexware.aajapan.dto.TSalesInvoiceItemDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.repositories.TSalesInvoiceRepository;
import com.nexware.aajapan.services.MLoginService;
import com.nexware.aajapan.services.SalesReportService;
import com.nexware.aajapan.utils.AppUtil;
import com.nexware.aajapan.utils.ExcelUtil;

@Service
@Transactional
public class SalesReportServiceImpl implements SalesReportService {

	@Autowired
	private MLoginService loginService;
	@Autowired
	private TSalesInvoiceRepository tsalesInvoiceRepository;

	@Override
	public Workbook exportCustomerTransaction(List<CustomerTransactionDto> list) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Report");
		String[] header = { "StockNo", };
		ExcelUtil.writeRow(sheet, 0, header);

		return workbook;

	}

	@Override
	public void exportAllSalesOrders(HttpServletResponse response) throws IOException {
		StringBuilder filename = new StringBuilder("Sales_orders").append(" - ").append("test.xlsx");
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			// String sDate = new SimpleDateFormat("dd-MM-yyyy").format(minDate);
			Sheet sheet = workbook.createSheet("Sales Order");
			Integer rowNo = 1;

			Row firstRow = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			ExcelUtil.setCellValue(firstRow.getCell(1), "Company");
			ExcelUtil.setCellValue(firstRow.getCell(2), "AA Japan");
			Row secondRow = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			ExcelUtil.setCellValue(secondRow.getCell(1), "Title");
			ExcelUtil.setCellValue(secondRow.getCell(2), "Sales Order Report");
			Row thirdRow = ExcelUtil.createRowWithBorder(workbook, sheet, rowNo++, 3);
			ExcelUtil.setCellValue(thirdRow.getCell(1), "Date");
			ExcelUtil.createDateStyle(thirdRow.createCell(2), new Date(), "MM/dd/yyyy", workbook, 11, false);

			int currentRow = rowNo;
			ExcelUtil.mergeRegionsWithBorderCellAddress(sheet, (currentRow - 3), (currentRow - 1), 1, 2,
					BorderStyle.MEDIUM);

			List<String> salesPersonIds = this.loginService.getSalesPersonIdsByHierarchyLevel();

			List<TSalesInvoiceDto> salesOrders = this.tsalesInvoiceRepository
					.getListWithCustomerDetails(salesPersonIds);

			String[] COLUMNS = { "S NO.", "INVOICE NO.", "CUSTOMER", "CONSIGNEE", "NOTIFY PARTY", "Payment TYPE",
					"ORDERED BY", "GRAND TOTAL", "SOLD DATE" };

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLACK.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);
			rowNo = currentRow + 1;
			Row headerRow = sheet.createRow(rowNo++);
			for (int col = 0; col < COLUMNS.length; col++) {
				ExcelUtil.autoSizeColumnNumber(sheet, col);
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(COLUMNS[col]);
				cell.setCellStyle(headerCellStyle);
			}

			DataFormat dataformat = workbook.createDataFormat();

			int sNo = 0;

			for (int i = 0; i < salesOrders.size(); i++) {
				TSalesInvoiceDto order = salesOrders.get(i);
				Row orderRow = sheet.createRow(rowNo++);
				orderRow.createCell(0).setCellValue(sNo++);
				orderRow.createCell(1).setCellValue(AppUtil.ifNull(order.getInvoiceNo(), ""));
				ExcelUtil.autoSizeColumnNumber(sheet, 1);
				orderRow.createCell(2).setCellValue(AppUtil.ifNull(order.getfCustomerName(), ""));
				ExcelUtil.autoSizeColumnNumber(sheet, 2);
				orderRow.createCell(3).setCellValue(AppUtil.ifNull(order.getfConsigneeName(), ""));
				ExcelUtil.autoSizeColumnNumber(sheet, 3);
				orderRow.createCell(4).setCellValue(AppUtil.ifNull(order.getfNotifyName(), ""));
				ExcelUtil.autoSizeColumnNumber(sheet, 4);
				orderRow.createCell(5).setCellValue(AppUtil.ifNull(order.getPaymentType(), ""));
				ExcelUtil.autoSizeColumnNumber(sheet, 5);
				orderRow.createCell(6).setCellValue(AppUtil.ifNull(order.getOrderedBy(), ""));
				ExcelUtil.autoSizeColumnNumber(sheet, 6);

				CellStyle cellStyle = workbook.createCellStyle();
				if (order.getCurrencyType() == Constants.CURRENCY_YEN) {
					cellStyle.setDataFormat(dataformat.getFormat("¥ ###0,00"));
				} else if (order.getCurrencyType() == Constants.CURRENCY_USD) {
					cellStyle.setDataFormat(dataformat.getFormat("[$$-409]#,##0.00"));
				}

				Cell totalValueCell = orderRow.createCell(7);
				totalValueCell.setCellValue(order.getAllTtotal());
				totalValueCell.setCellStyle(cellStyle);
				ExcelUtil.autoSizeColumnNumber(sheet, 7);

				ExcelUtil.createDateStyle(orderRow.createCell(8), order.getCreatedDate(), "MM/dd/yy", workbook, 11,
						false);
				ExcelUtil.autoSizeColumnNumber(sheet, 8);

				for (int j = 0; j < order.getSalesInvoiceDetails().size(); j++) {

					TSalesInvoiceItemDto stock = order.getSalesInvoiceDetails().get(j);

					Row stockRow = sheet.createRow(rowNo++);
					stockRow.createCell(3).setCellValue(AppUtil.ifNull(stock.getStockNo(), ""));
					stockRow.createCell(4).setCellValue(AppUtil.ifNull(stock.getChassisNo(), ""));
					stockRow.createCell(5).setCellValue(AppUtil.ifNull(stock.getMaker(), ""));
					stockRow.createCell(6).setCellValue(AppUtil.ifNull(stock.getModel(), ""));

					Cell stockTotalCell = stockRow.createCell(7);
					stockTotalCell.setCellValue(stock.getTotal());
					stockTotalCell.setCellStyle(cellStyle);

				}
			}

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=" + filename);
			workbook.write(response.getOutputStream());
		} catch (final Exception e) {
			throw new AAJRuntimeException("Exception while creating balance sheet report.", e);
		}
	}
}
