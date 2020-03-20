/**
 *
 */
package com.nexware.aajapan.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import com.nexware.aajapan.exceptions.AAJRuntimeException;

/**
 * The Class AppUtil.
 *
 * @author Karthik Selvaraj
 */
public class ExcelUtil {
	private static final String DATE_FORMAT = "dd-MM-yyyy";
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);

	private ExcelUtil() {

	}

	public static void writeRow(Sheet sheet, int index, Object[] data) {
		Row row = sheet.createRow(index);
		for (int i = 0; i < data.length; i++) {
			Cell cell = row.createCell(i);
			if (AppUtil.isObjectEmpty(data[i])) {
				cell.setCellValue("");
			} else if (data[i] instanceof String) {
				cell.setCellValue(data[i].toString());
			} else if (data[i] instanceof Boolean) {
				cell.setCellValue(data[i].toString());
			} else if (data[i] instanceof Number) {
				cell.setCellValue(Double.parseDouble(data[i].toString()));
			}

		}
	}

	public static CellStyle getBorderStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		return style;

	}

	public static Row createRowWithBorder(Workbook workbook, Sheet sheet, int rowNo, int noOfCell) {
		Row row = sheet.createRow(rowNo);
		for (int i = 0; i < noOfCell; i++) {
			row.createCell(i).setCellStyle(getBorderStyle(workbook));
		}
		return row;
	}

	public static Row createRow(Workbook workbook, Sheet sheet, int rowNo, int noOfCell) {
		Row row = sheet.createRow(rowNo);
		for (int i = 0; i < noOfCell; i++) {
			row.createCell(i).setCellStyle(workbook.createCellStyle());
		}
		return row;
	}

	public static void setCellValue(Cell cell, Object value) {
		if (value == null) {
			cell.setCellValue("");
		} else if (value instanceof String) {
			cell.setCellValue(value.toString());
		} else if (value instanceof Number) {
			cell.setCellValue(((Number) value).doubleValue());
		} else {
			cell.setCellValue(value.toString());
		}
	}

	public static void setCellValue(Cell cell, Date value) {
		if (value == null) {
			cell.setCellValue("");
		} else {
			cell.setCellValue(dateFormatter.format(value));
		}
	}

	public static void fillCellColor(Cell cell, short colorIndex) {
		CellStyle cellStyle = cell.getCellStyle();
		cellStyle.setFillForegroundColor(colorIndex);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cell.setCellStyle(cellStyle);
	}

	public static Font getBoldFont(Workbook workbook) {
		Font font = workbook.createFont();
		font.setBold(true);
		return font;
	}

	public static void setBoldFont(Cell cell, Font font) {
		CellStyle cellStyle = cell.getCellStyle();
		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);
	}

	public static void addHorizontalAlignment(Cell cell, HorizontalAlignment alignment) {
		CellStyle currentCellStyle = cell.getCellStyle();
		currentCellStyle.setAlignment(alignment);
		cell.setCellStyle(currentCellStyle);
	}

	/*
	 * public static String getCellValueAsString(Row row, int cellIndex) { Cell cell
	 * = row.getCell(cellIndex); if (!AppUtil.isObjectEmpty(cell)) {
	 * cell.getStringCellValue().trim(); } return null; }
	 */

	@SuppressWarnings("deprecation")
	public static <T> T getCellValue(Row row, int cellIndex, Class<T> clazz) {
		Cell cell = row.getCell(cellIndex);
		if (!AppUtil.isObjectEmpty(cell)) {
			switch (cell.getCellType()) {
			case STRING:
				return clazz.cast(cell.getStringCellValue().trim());
			case NUMERIC:
				if (clazz.isInstance(Double.valueOf(0))) {
					return clazz.cast(cell.getNumericCellValue());
				} else if (clazz.isInstance(new Date())) {
					return clazz.cast(cell.getDateCellValue());
				} else if (clazz.isInstance(String.valueOf(""))) {
					cell.setCellType(CellType.STRING);
					return clazz.cast(cell.getStringCellValue());
				}
				break;
			default:
				throw new AAJRuntimeException("Invalid cell type found in excel : " + cell.getCellType());

			}

		}
		return null;
	}

	public static Font getBoldFontInHeights(Workbook workbook, int fontHeight, boolean isBold) {
		Font font = workbook.createFont();
		font.setBold(isBold);
		font.setFontHeightInPoints((short) fontHeight);
		return font;
	}

	public static Font getBoldFontInHeightsWithColor(Workbook workbook, int fontHeight, short index, boolean isBold) {
		Font font = workbook.createFont();
		font.setBold(isBold);
		font.setFontHeightInPoints((short) fontHeight);
		font.setColor(index);
		return font;
	}

	public static void autoSizeColumnNumber(Sheet sheet, int col) {
		sheet.autoSizeColumn(col);
	}

	public static void createDateStyle(Cell cell, Date date, String dateFormat, Workbook workbook, int fontHeight,
			boolean isBold) {
		CellStyle cellStyle = workbook.createCellStyle();
		CreationHelper createHelper = workbook.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(dateFormat));
		cellStyle.setFont(getBoldFontInHeights(workbook, fontHeight, isBold));
		cell.setCellValue(date);
		cell.setCellStyle(cellStyle);
	}

	public static Font getFontSize(Workbook workbook, int fontHeight) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) fontHeight);
		return font;
	}

	public static void setExcelFontStyles(Cell cell, Font font) {
		CellStyle cellStyle = cell.getCellStyle();
		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);
	}

	public static void setContainerPatterns(Cell cell, short fillPatternIndex, FillPatternType type,
			VerticalAlignment alignment, Font font) {
		CellStyle cellStyle = cell.getCellStyle();
		cellStyle.setFont(font);
		cellStyle.setFillBackgroundColor(fillPatternIndex);
		cellStyle.setFillPattern(type);
		cellStyle.setVerticalAlignment(alignment);
		cell.setCellStyle(cellStyle);
	}

	public static void setColumnWidth(Sheet sheet, int startPoint, int endPoint) {
		sheet.setColumnWidth(startPoint, endPoint);
	}

	public static void mergeRegionsUsingCellAddress(Sheet sheet, int startRow, int endRow, int startCol, int endCol) {
		CellRangeAddress cellRangeAddress = new CellRangeAddress(startRow, endRow, startCol, endCol);
		sheet.addMergedRegion(cellRangeAddress);
	}

	public static void mergeRegionsWithBorderCellAddress(Sheet sheet, int startRow, int endRow, int startCol,
			int endCol, BorderStyle borderStyle) {
		CellRangeAddress cellRangeAddress = new CellRangeAddress(startRow, endRow, startCol, endCol);

		RegionUtil.setBorderTop(borderStyle, cellRangeAddress, sheet);
		RegionUtil.setBorderLeft(borderStyle, cellRangeAddress, sheet);
		RegionUtil.setBorderRight(borderStyle, cellRangeAddress, sheet);
		RegionUtil.setBorderBottom(borderStyle, cellRangeAddress, sheet);
	}

	public static boolean isRowEmpty(Row row) {
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellType() != CellType.BLANK)
				return false;
		}
		return true;
	}
}
