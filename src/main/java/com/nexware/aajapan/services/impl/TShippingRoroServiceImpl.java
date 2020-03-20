package com.nexware.aajapan.services.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.TShippingRoroExcelDto;
import com.nexware.aajapan.dto.TShippingRoroExcelItemsDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.repositories.TShippingRequestRepository;
import com.nexware.aajapan.services.TShippingRoroService;
import com.nexware.aajapan.utils.AppUtil;

@Service
public class TShippingRoroServiceImpl implements TShippingRoroService {

	@Autowired
	private TShippingRequestRepository shippingRequestRepository;

	@Override
	public void roroConfirmedExcelReport(String scheduleId, String destCountry, HttpServletResponse httpResponse)
			throws IOException {
		String todayDateString = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date());
		// Row for Header
		int rowIdx = 1;
		List<TShippingRoroExcelDto> response = this.shippingRequestRepository.roroConfirmedExcelReport(scheduleId,
				destCountry);
		String[] COLUMNS = { "", "CHASSY NO", "DEST COUNTRY", "DEST PORT", "YARD", "INSPECTION DATE",
				"INSPECTION RESULT", "INSPECTION ISSUE DATE", "SALES", "MODEL", "YEAR", "LENGTH", "WIDTH", "HEIGHT",
				"WEIGHT", "M3", "DOC", "CONSIGNEE", "NOTIFYPARTY", "YARD", "Payment Type", "Voyage No", "Boat Name",
				"Value" };
		StringBuilder filename = new StringBuilder(response.get(0).getItems().get(0).getDestPort())
				.append(" CONFIRMED LIST");

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Sheet1");

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLUE.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			CellStyle centerAlign = workbook.createCellStyle();
			centerAlign.setAlignment(HorizontalAlignment.CENTER);

			for (int headerCol = 0; headerCol < response.size(); headerCol++) {
				// append origin port to file name
				// filename.append(" " + response.get(headerCol).getOriginPortName())
				// grouped origin port row
				int rowValue = rowIdx++;
				Row headerRow = sheet.createRow(rowValue);
				sheet.addMergedRegion(new CellRangeAddress(rowValue, rowValue, 0, COLUMNS.length));
				headerRow.createCell(0).setCellStyle(centerAlign);
				headerRow.createCell(0).setCellValue(response.get(headerCol).getOriginPortName());
				// origin port header values
				Row headerRow1 = sheet.createRow(rowIdx++);
				for (int col = 0; col < COLUMNS.length; col++) {
					Cell cell = headerRow1.createCell(col);
					cell.setCellValue(COLUMNS[col]);
					cell.setCellStyle(headerCellStyle);
				}
				int sNo = 1;
				int tempIndex = 0;
				String custDtls = "";
				CellStyle style = workbook.createCellStyle();
				style.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

				for (int a = 0; a < response.get(headerCol).getItems().size(); a++) {
					TShippingRoroExcelItemsDto excelData = response.get(headerCol).getItems().get(a);
					String loopingCustomerString = "";
					Row row = sheet.createRow(rowIdx++);
					row.createCell(0).setCellValue(sNo++);
					row.createCell(1).setCellValue(AppUtil.ifNull(excelData.getChassisNo(), ""));
					row.createCell(2).setCellValue(AppUtil.ifNull(excelData.getDestCountry(), ""));
					row.createCell(3).setCellValue(AppUtil.ifNull(excelData.getDestPort(), ""));
					row.createCell(4).setCellValue(AppUtil.ifNull(excelData.getLocationName(), ""));
					row.createCell(5).setCellValue(AppUtil.ifNull(excelData.getInspectionDate(), ""));
					row.createCell(6).setCellValue(AppUtil.ifNull(excelData.getInspectionStatus(), ""));
					row.createCell(7).setCellValue(AppUtil.ifNull(excelData.getInspectionDateOfIssue(), ""));
					row.createCell(8).setCellValue(AppUtil.ifNull(excelData.getShippingInstructionGivenBy(), ""));
					row.createCell(9).setCellValue(AppUtil.ifNull(excelData.getModel(), ""));
					row.createCell(10).setCellValue(AppUtil.ifNull(excelData.getsFirstRegDate(), ""));
					row.createCell(11).setCellValue(AppUtil.ifNull(excelData.getLength(), 0.0));
					row.createCell(12).setCellValue(AppUtil.ifNull(excelData.getWidth(), 0.0));
					row.createCell(13).setCellValue(AppUtil.ifNull(excelData.getHeight(), 0.0));
					row.createCell(14).setCellValue(AppUtil.ifNull(excelData.getWeight(), ""));
					Cell cell = row.createCell(15);
					cell.setCellStyle(style);
					cell.setCellValue(AppUtil.ifNull(excelData.getM3(), 0.0));
					row.createCell(16).setCellValue(AppUtil.ifNull(excelData.getDocDetails(), ""));
					row.createCell(17).setCellValue(AppUtil.ifNull(excelData.getCustomerDetails(), ""));
					row.createCell(18).setCellValue(AppUtil.ifNull(excelData.getNotifypartyDetails(), ""));
					row.createCell(19).setCellValue(AppUtil.ifNull(excelData.getYard(), ""));
					row.createCell(20).setCellValue(AppUtil.ifNull(excelData.getPaymentType(), ""));
					row.createCell(21).setCellValue(AppUtil.ifNull(excelData.getVoyageNo(), ""));
					row.createCell(22).setCellValue(AppUtil.ifNull(excelData.getShipName(), ""));
					row.createCell(23).setCellValue(AppUtil.ifNull(excelData.getPurchaseValue(), 0L));
					loopingCustomerString = excelData.getCustomerDetails();
					if (AppUtil.isObjectEmpty(custDtls)) {
						custDtls = loopingCustomerString;
						if (a == response.get(headerCol).getItems().size() - 1) {
							// since cell range address start from 0 minus 1 to get correct cell merged
							int startRow = (rowIdx - tempIndex) - 1;
							// since its the last row cell range address start from 0 (-1)
							int endRow = rowIdx - 1;
							if (startRow != endRow) {
								sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 16, 16));
							}
						}
					} else if (!custDtls.equals(loopingCustomerString)) {
						if ((rowIdx - tempIndex) < rowIdx - 1) {
							// since cell range address start from 0 minus 1 to get correct cell merged
							int startRow = (rowIdx - tempIndex) - 1;
							// since to merge previous row (-1) and for cell range address start from 0 (-1)
							int endRow = rowIdx - 2;
							sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 16, 16));
						}
						tempIndex = 0;
						custDtls = "";
					} else if (a == response.get(headerCol).getItems().size() - 1) {
						// since cell range address start from 0 minus 1 to get correct cell merged
						int startRow = (rowIdx - tempIndex) - 1;
						// since to merge previous row (-1) and for cell range address start from 0 (-1)
						int endRow = rowIdx - 1;
						sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 16, 16));
					}

					tempIndex++;

				}

				rowIdx++;

			}

			filename.append(" " + todayDateString).append(".xlsx");
			httpResponse.setContentType("application/vnd.ms-excel");
			httpResponse.setHeader("Content-Disposition", "attachment; filename=" + filename);
			workbook.write(httpResponse.getOutputStream());

		} catch (Exception e) {
			throw new AAJRuntimeException("Exception while creating report.", e);
		}

	}

	@Override
	public void roroConfirmedExportExcel(String scheduleId, HttpServletResponse httpResponse) throws IOException {
		String todayDateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());

		// Row for Header
		int rowIdx = 1;

		List<TShippingRoroExcelDto> response = this.shippingRequestRepository.roroExcelReport(scheduleId);

		String[] COLUMNS = { "", "CHASSY NO", "DEST COUNTRY", "DEST PORT", "", "INSPECTION DATE", "SALES", "MODEL",
				"YEAR", "LENGTH", "WIDTH", "HEIGHT", "WEIGHT", "M3", "DOC", "CUSTOMER" };
		StringBuilder filename = new StringBuilder(response.get(0).getItems().get(0).getDestPort())
				.append(" BOOK LIST");

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Sheet1");

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLUE.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			CellStyle centerAlign = workbook.createCellStyle();
			centerAlign.setAlignment(HorizontalAlignment.CENTER);

			for (int headerCol = 0; headerCol < response.size(); headerCol++) {
				// append origin port to file name
				// filename.append(" " + response.get(headerCol).getOriginPortName())
				// grouped origin port row
				int rowValue = rowIdx++;
				Row headerRow = sheet.createRow(rowValue);
				sheet.addMergedRegion(new CellRangeAddress(rowValue, rowValue, 0, COLUMNS.length));
				headerRow.createCell(0).setCellStyle(centerAlign);
				headerRow.createCell(0).setCellValue(response.get(headerCol).getOriginPortName());
				// origin port header values
				Row headerRow1 = sheet.createRow(rowIdx++);
				for (int col = 0; col < COLUMNS.length; col++) {
					Cell cell = headerRow1.createCell(col);
					cell.setCellValue(COLUMNS[col]);
					cell.setCellStyle(headerCellStyle);
				}
				int sNo = 1;
				int tempIndex = 0;
				String custDtls = "";
				for (int a = 0; a < response.get(headerCol).getItems().size(); a++) {
					TShippingRoroExcelItemsDto excelData = response.get(headerCol).getItems().get(a);
					String loopingCustomerString = "";
					Row row = sheet.createRow(rowIdx++);
					row.createCell(0).setCellValue(sNo++);
					row.createCell(1).setCellValue(AppUtil.ifNull(excelData.getChassisNo(), ""));
					row.createCell(2).setCellValue(AppUtil.ifNull(excelData.getDestCountry(), ""));
					row.createCell(3).setCellValue(AppUtil.ifNull(excelData.getDestPort(), ""));
					row.createCell(4).setCellValue(AppUtil.ifNull(excelData.getLastLap(), ""));
					row.createCell(5).setCellValue(AppUtil.ifNull(excelData.getInspectionDate(), ""));
					row.createCell(6).setCellValue(AppUtil.ifNull(excelData.getShippingInstructionGivenBy(), ""));
					row.createCell(7).setCellValue(AppUtil.ifNull(excelData.getModel(), ""));
					row.createCell(8).setCellValue(AppUtil.ifNull(excelData.getsFirstRegDate(), ""));
					row.createCell(9).setCellValue(AppUtil.ifNull(excelData.getLength(), 0.0));
					row.createCell(10).setCellValue(AppUtil.ifNull(excelData.getWidth(), 0.0));
					row.createCell(11).setCellValue(AppUtil.ifNull(excelData.getHeight(), 0.0));
					row.createCell(12).setCellValue(AppUtil.ifNull(excelData.getWeight(), ""));
					row.createCell(13).setCellValue(AppUtil.ifNull(excelData.getM3(), 0.0));
					row.createCell(14).setCellValue(AppUtil.ifNull(excelData.getDocDetails(), ""));
					row.createCell(15).setCellValue(AppUtil.ifNull(excelData.getCustomerDetails(), ""));
					loopingCustomerString = excelData.getCustomerDetails();
					if (AppUtil.isObjectEmpty(custDtls)) {
						custDtls = loopingCustomerString;
						if (a == response.get(headerCol).getItems().size() - 1) {
							// since cell range address start from 0 minus 1 to get correct cell merged
							int startRow = (rowIdx - tempIndex) - 1;
							// since its the last row cell range address start from 0 (-1)
							int endRow = rowIdx - 1;
							if (startRow != endRow) {
								sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 16, 16));
							}
						}
					} else if (!custDtls.equals(loopingCustomerString)) {
						if ((rowIdx - tempIndex) < rowIdx - 1) {
							// since cell range address start from 0 minus 1 to get correct cell merged
							int startRow = (rowIdx - tempIndex) - 1;
							// since to merge previous row (-1) and for cell range address start from 0 (-1)
							int endRow = rowIdx - 2;
							sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 16, 16));
						}
						tempIndex = 0;
						custDtls = "";
					} else if (a == response.get(headerCol).getItems().size() - 1) {
						// since cell range address start from 0 minus 1 to get correct cell merged
						int startRow = (rowIdx - tempIndex) - 1;
						// since to merge previous row (-1) and for cell range address start from 0 (-1)
						int endRow = rowIdx - 1;
						sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 16, 16));
					}

					tempIndex++;

				}

				rowIdx++;

			}

			filename.append(" " + todayDateString).append(".xlsx");
			httpResponse.setContentType("application/vnd.ms-excel");
			httpResponse.setHeader("Content-Disposition", "attachment; filename=" + filename);
			workbook.write(httpResponse.getOutputStream());

		} catch (Exception e) {
			throw new AAJRuntimeException("Exception while creating report.", e);
		}

	}

	@Override
	public void roroArrangedExcelReport(String forwarder, String destCountry, String allocationId, Integer status,
			String originPort, HttpServletResponse httpResponse) throws IOException {

		String todayDateString = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date());

		// Row for Header
		int rowIdx = 1;

		List<TShippingRoroExcelDto> response = this.shippingRequestRepository.roroArrangedExcelReport(forwarder,
				destCountry, allocationId, status, originPort);

		String[] COLUMNS = { "", "CHASSY NO", "DEST COUNTRY", "DEST PORT", "YARD", "INSPECTION DATE",
				"INSPECTION RESULT", "INSPECTION ISSUE DATE", "SALES", "MODEL", "YEAR", "LENGTH", "WIDTH", "HEIGHT",
				"WEIGHT", "M3", "DOC", "CONSIGNEE", "NOTIFYPARTY", "YARD", "Payment Type", "Voyage No", "Boat Name",
				"Value" };
		StringBuilder filename = new StringBuilder(response.get(0).getItems().get(0).getDestPort())
				.append(" BOOK LIST");

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Sheet1");

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLUE.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			CellStyle centerAlign = workbook.createCellStyle();
			centerAlign.setAlignment(HorizontalAlignment.CENTER);

			for (int headerCol = 0; headerCol < response.size(); headerCol++) {
				// append origin port to file name
				// filename.append(" " + response.get(headerCol).getOriginPortName())
				// grouped origin port row
				int rowValue = rowIdx++;
				Row headerRow = sheet.createRow(rowValue);
				sheet.addMergedRegion(new CellRangeAddress(rowValue, rowValue, 0, COLUMNS.length));
				headerRow.createCell(0).setCellStyle(centerAlign);
				headerRow.createCell(0).setCellValue(response.get(headerCol).getOriginPortName());
				// origin port header values
				Row headerRow1 = sheet.createRow(rowIdx++);
				for (int col = 0; col < COLUMNS.length; col++) {
					Cell cell = headerRow1.createCell(col);
					cell.setCellValue(COLUMNS[col]);
					cell.setCellStyle(headerCellStyle);
				}
				int sNo = 1;
				int tempIndex = 0;
				String custDtls = "";
				CellStyle style = workbook.createCellStyle();
				style.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

				for (int a = 0; a < response.get(headerCol).getItems().size(); a++) {
					TShippingRoroExcelItemsDto excelData = response.get(headerCol).getItems().get(a);
					String loopingCustomerString = "";
					Row row = sheet.createRow(rowIdx++);
					row.createCell(0).setCellValue(sNo++);
					row.createCell(1).setCellValue(AppUtil.ifNull(excelData.getChassisNo(), ""));
					row.createCell(2).setCellValue(AppUtil.ifNull(excelData.getDestCountry(), ""));
					row.createCell(3).setCellValue(AppUtil.ifNull(excelData.getDestPort(), ""));
					row.createCell(4).setCellValue(AppUtil.ifNull(excelData.getLocationName(), ""));
					row.createCell(5).setCellValue(AppUtil.ifNull(excelData.getInspectionDate(), ""));
					row.createCell(6).setCellValue(AppUtil.ifNull(excelData.getInspectionStatus(), ""));
					row.createCell(7).setCellValue(AppUtil.ifNull(excelData.getInspectionDateOfIssue(), ""));
					row.createCell(8).setCellValue(AppUtil.ifNull(excelData.getShippingInstructionGivenBy(), ""));
					row.createCell(9).setCellValue(AppUtil.ifNull(excelData.getModel(), ""));
					row.createCell(10).setCellValue(AppUtil.ifNull(excelData.getsFirstRegDate(), ""));
					row.createCell(11).setCellValue(AppUtil.ifNull(excelData.getLength(), 0.0));
					row.createCell(12).setCellValue(AppUtil.ifNull(excelData.getWidth(), 0.0));
					row.createCell(13).setCellValue(AppUtil.ifNull(excelData.getHeight(), 0.0));
					row.createCell(14).setCellValue(AppUtil.ifNull(excelData.getWeight(), ""));
					Cell cell = row.createCell(15);
					cell.setCellStyle(style);
					cell.setCellValue(AppUtil.ifNull(excelData.getM3(), 0.0));
					row.createCell(16).setCellValue(AppUtil.ifNull(excelData.getDocDetails(), ""));
					row.createCell(17).setCellValue(AppUtil.ifNull(excelData.getCustomerDetails(), ""));
					row.createCell(18).setCellValue(AppUtil.ifNull(excelData.getNotifypartyDetails(), ""));
					row.createCell(19).setCellValue(AppUtil.ifNull(excelData.getYard(), ""));
					row.createCell(20).setCellValue(AppUtil.ifNull(excelData.getPaymentType(), ""));
					row.createCell(21).setCellValue(AppUtil.ifNull(excelData.getVoyageNo(), ""));
					row.createCell(22).setCellValue(AppUtil.ifNull(excelData.getShipName(), ""));
					row.createCell(23).setCellValue(AppUtil.ifNull(excelData.getPurchaseValue(), 0L));
					loopingCustomerString = excelData.getCustomerDetails();
					if (AppUtil.isObjectEmpty(custDtls)) {
						custDtls = loopingCustomerString;
						if (a == response.get(headerCol).getItems().size() - 1) {
							// since cell range address start from 0 minus 1 to get correct cell merged
							int startRow = (rowIdx - tempIndex) - 1;
							// since its the last row cell range address start from 0 (-1)
							int endRow = rowIdx - 1;
							if (startRow != endRow) {
								sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 16, 16));
							}
						}
					} else if (!custDtls.equals(loopingCustomerString)) {
						if ((rowIdx - tempIndex) < rowIdx - 1) {
							// since cell range address start from 0 minus 1 to get correct cell merged
							int startRow = (rowIdx - tempIndex) - 1;
							// since to merge previous row (-1) and for cell range address start from 0 (-1)
							int endRow = rowIdx - 2;
							sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 16, 16));
						}
						tempIndex = 0;
						custDtls = "";
					} else if (a == response.get(headerCol).getItems().size() - 1) {
						// since cell range address start from 0 minus 1 to get correct cell merged
						int startRow = (rowIdx - tempIndex) - 1;
						// since to merge previous row (-1) and for cell range address start from 0 (-1)
						int endRow = rowIdx - 1;
						sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 16, 16));
					}

					tempIndex++;

				}

				rowIdx++;

			}

			filename.append(" " + todayDateString).append(".xlsx");
			httpResponse.setContentType("application/vnd.ms-excel");
			httpResponse.setHeader("Content-Disposition", "attachment; filename=" + filename);
			workbook.write(httpResponse.getOutputStream());

		} catch (Exception e) {
			throw new AAJRuntimeException("Exception while creating report.", e);
		}

	}

}
