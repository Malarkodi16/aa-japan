package com.nexware.aajapan.dto;

import java.util.List;

import com.nexware.aajapan.models.MCustomListFields;
import com.nexware.aajapan.payload.DatatableResponse;

public class CustomReportResponseDto {
	List<MCustomListFields> columnMetaData;
	DatatableResponse data;

	public List<MCustomListFields> getColumnMetaData() {
		return columnMetaData;
	}

	public void setColumnMetaData(List<MCustomListFields> columnMetaData) {
		this.columnMetaData = columnMetaData;
	}

	public DatatableResponse getData() {
		return data;
	}

	public void setData(DatatableResponse data) {
		this.data = data;
	}

}
