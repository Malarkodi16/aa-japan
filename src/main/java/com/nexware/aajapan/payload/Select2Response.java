package com.nexware.aajapan.payload;

public class Select2Response {
	private Object results;

	public Select2Response(Object results) {
		this.results = results;
	}

	public Object getResults() {
		return this.results;
	}

	public void setResults(Object results) {
		this.results = results;
	}

}
