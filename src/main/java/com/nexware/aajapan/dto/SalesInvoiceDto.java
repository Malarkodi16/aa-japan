package com.nexware.aajapan.dto;

import java.util.List;

import com.nexware.aajapan.models.SalesInvoiceDetail;
import com.nexware.aajapan.models.TSalesInvoice;

public class SalesInvoiceDto {

	private TSalesInvoice invoiceDtl;
	private List<SalesInvoiceDetail> invoiceItems;

	public TSalesInvoice getInvoiceDtl() {
		return this.invoiceDtl;
	}

	public void setInvoiceDtl(TSalesInvoice invoiceDtl) {
		this.invoiceDtl = invoiceDtl;
	}

	public List<SalesInvoiceDetail> getInvoiceItems() {
		return this.invoiceItems;
	}

	public void setInvoiceItems(List<SalesInvoiceDetail> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}

}
