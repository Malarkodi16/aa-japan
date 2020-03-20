package com.nexware.aajapan.services.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.dto.TShippingRequestedContainerExcelDto;
import com.nexware.aajapan.dto.TShippingRequestedContainerExcelItemDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.repositories.TShippingRequestRepository;
import com.nexware.aajapan.services.ShippingRequestService;
import com.nexware.aajapan.utils.AppUtil;
import com.nexware.aajapan.utils.ExcelUtil;

@Service
@Transactional
public class ShippingRequestServiceImpl implements ShippingRequestService {
	@Autowired
	private TShippingRequestRepository shippingRequestRepository;

	@SuppressWarnings("deprecation")
	@Override
	public void exportContainerRequestExcel(String allocationId, HttpServletResponse response) {
		String todayDateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
		final StringBuilder filename = new StringBuilder(allocationId).append(" - ");
		filename.append(" " + todayDateString).append(".xlsx");
		// Row for Header
		int rowIdx = 1;
		// Header Columns
		String[] COLUMNS = { "", "Chassis #", "CTM", "Maker", "Model", "Year", "Delivery", "CR", "USER", "CUSTOMER",
				"CONSIGNEE", "ID", "TEL", "HS Code" };

		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			final Sheet sheet = workbook.createSheet(allocationId);
			// export excel
			final TShippingRequestedContainerExcelDto data = shippingRequestRepository
					.findAllShippingContainerExcelData(allocationId, 0);

			Row headerRow = ExcelUtil.createRow(workbook, sheet, rowIdx++, 7);
			ExcelUtil.setCellValue(headerRow.getCell(1), data.getDestCountry());
			ExcelUtil.setBoldFont(headerRow.getCell(1), ExcelUtil.getBoldFontInHeights(workbook, 24, true));
			ExcelUtil.autoSizeColumnNumber(sheet, 1);

			ExcelUtil.setCellValue(headerRow.getCell(3), data.getDestPort());
			ExcelUtil.setBoldFont(headerRow.getCell(3),
					ExcelUtil.getBoldFontInHeightsWithColor(workbook, 24, IndexedColors.CORAL.getIndex(), true));
			ExcelUtil.autoSizeColumnNumber(sheet, 3);

			ExcelUtil.createDateStyle(headerRow.getCell(6), data.getAllocationDate(), "MM/dd/yy", workbook, 24, true);
			ExcelUtil.autoSizeColumnNumber(sheet, 6);

			Row containerHeader = ExcelUtil.createRow(workbook, sheet, rowIdx++, 4);

			for (int col = 0; col < COLUMNS.length; col++) {
				Cell cell = containerHeader.createCell(col);
				cell.setCellValue(COLUMNS[col]);
				ExcelUtil.setExcelFontStyles(cell, ExcelUtil.getFontSize(workbook, 15));
				ExcelUtil.autoSizeColumnNumber(sheet, col);
			}

			String containerNo = "";
			int tempIndex = 0;
			for (int headerCol = 0; headerCol < data.getItems().size(); headerCol++) {
				TShippingRequestedContainerExcelItemDto containerDetails = data.getItems().get(headerCol);
				String loopingContainerNo = "";

				Row dataRow = ExcelUtil.createRow(workbook, sheet, rowIdx++, 14);
				Cell containerCell = dataRow.getCell(0);
				ExcelUtil.setCellValue(containerCell, containerDetails.getContainerNo());
				ExcelUtil.setContainerPatterns(containerCell, IndexedColors.GREY_25_PERCENT.getIndex(),
						FillPatternType.DIAMONDS, VerticalAlignment.CENTER,
						ExcelUtil.getBoldFontInHeightsWithColor(workbook, 20, IndexedColors.WHITE.getIndex(), true));

				ExcelUtil.autoSizeColumnNumber(sheet, headerCol);
				ExcelUtil.setColumnWidth(sheet, 0, 2000);

				ExcelUtil.setCellValue(dataRow.getCell(1), containerDetails.getChassisNo());
				ExcelUtil.setCellValue(dataRow.getCell(2), containerDetails.getCtm());
				ExcelUtil.setCellValue(dataRow.getCell(3), containerDetails.getMaker());
				ExcelUtil.setCellValue(dataRow.getCell(4), containerDetails.getModel());
				ExcelUtil.createDateStyle(dataRow.getCell(5), containerDetails.getYear(), "yyyy", workbook, 13, false);
				ExcelUtil.createDateStyle(dataRow.getCell(6), containerDetails.getEta(), "MM/dd/yy", workbook, 13,
						false);
				ExcelUtil.createDateStyle(dataRow.getCell(7), containerDetails.getCr(), "MM/dd/yy", workbook, 13,
						false);

				ExcelUtil.setCellValue(dataRow.getCell(8), containerDetails.getUser());
				ExcelUtil.setCellValue(dataRow.getCell(9), containerDetails.getCustomer());
				ExcelUtil.setCellValue(dataRow.getCell(10), containerDetails.getConsignee());
				ExcelUtil.setCellValue(dataRow.getCell(11), containerDetails.getShippingId());
				ExcelUtil.setCellValue(dataRow.getCell(12), containerDetails.getTel());
				ExcelUtil.setCellValue(dataRow.getCell(13), containerDetails.getHsCode());
				loopingContainerNo = containerDetails.getContainerNo();
				if (AppUtil.isObjectEmpty(containerNo)) {
					containerNo = loopingContainerNo;
				} else if (!containerNo.equals(loopingContainerNo)) {
					if ((rowIdx - tempIndex) < rowIdx - 1) {
						// since cell range address start from 0 minus 1 to get correct cell merged
						int startRow = (rowIdx - tempIndex) - 1;
						// since to merge previous row (-1) and for cell range address start from 0 (-1)
						int endRow = rowIdx - 2;

						int check = ((endRow - startRow) + 1);
						int noOfRows = 6 - check;
						if (check < 6) {
							sheet.shiftRows(endRow + 1, endRow + noOfRows, noOfRows);
						}
						ExcelUtil.mergeRegionsUsingCellAddress(sheet, startRow, endRow + noOfRows, 0, 0);
						ExcelUtil.mergeRegionsWithBorderCellAddress(sheet, startRow, endRow + noOfRows, 0, 13,
								BorderStyle.THICK);
						//
						rowIdx = sheet.getLastRowNum() + 1;
					}
					tempIndex = 0;
					containerNo = "";
				} else if (headerCol == data.getItems().size() - 1) {
					// since cell range address start from 0 minus 1 to get correct cell merged
					int startRow = (rowIdx - tempIndex) - 1;
					// since to merge previous row (-1) and for cell range address start from 0 (-1)
					int endRow = rowIdx - 1;

					int check = ((endRow - startRow) + 1);
					int noOfRows = 6 - check;
					if (check < 6) {
						sheet.shiftRows(endRow + 1, endRow + noOfRows, noOfRows);
					}

					ExcelUtil.mergeRegionsUsingCellAddress(sheet, startRow, endRow + noOfRows, 0, 0);
					ExcelUtil.mergeRegionsWithBorderCellAddress(sheet, startRow, endRow + noOfRows, 0, 13,
							BorderStyle.THICK);
				}
				tempIndex++;
			}
			// create shipper static block
			rowIdx += 6;
			Row staticRow = ExcelUtil.createRow(workbook, sheet, rowIdx++, 1);
			ExcelUtil.setCellValue(staticRow.getCell(0), "SHIPPER");
			staticRow = ExcelUtil.createRow(workbook, sheet, rowIdx++, 1);
			ExcelUtil.setCellValue(staticRow.getCell(0), "AA JAPAN CO.LTD");
			staticRow = ExcelUtil.createRow(workbook, sheet, rowIdx++, 1);
			ExcelUtil.setCellValue(staticRow.getCell(0), "1-28-21,HAYABUCHI,TSUZUKI-KU");
			staticRow = ExcelUtil.createRow(workbook, sheet, rowIdx++, 1);
			ExcelUtil.setCellValue(staticRow.getCell(0), "YOKOHAMA-CITY,KANAGAWA-KEN");
			staticRow = ExcelUtil.createRow(workbook, sheet, rowIdx++, 1);
			ExcelUtil.setCellValue(staticRow.getCell(0), "224-0025 TEL:045-594-0507");

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=" + filename);
			workbook.write(response.getOutputStream());
		} catch (final Exception e) {
			throw new AAJRuntimeException("Exception while creating balance sheet report.", e);
		}
	}
}
