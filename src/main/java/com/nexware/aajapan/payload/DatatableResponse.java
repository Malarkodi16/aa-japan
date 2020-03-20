package com.nexware.aajapan.payload;

public class DatatableResponse {
	private Object data;

	public DatatableResponse(Object data) {

		this.data = data;
	}

	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
