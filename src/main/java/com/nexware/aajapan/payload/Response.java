package com.nexware.aajapan.payload;

public class Response {
	private String status;
	private String message;
	private Object data;

	public Response(String status) {

		this.status = status;

	}

	public Response(String status, Object data) {

		this.status = status;
		this.data = data;

	}

	public Response(String status, String message, Object data) {

		this.status = status;
		this.data = data;
		this.message = message;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
