package com.nexware.aajapan.form;

import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.models.TStock;

public class StockForm {
	private TStock stock;
	private TPurchaseInvoice purchaseInvoice;

	public TStock getStock() {
		return this.stock;
	}

	public void setStock(TStock stock) {
		this.stock = stock;
	}

	public TPurchaseInvoice getPurchaseInvoice() {
		return this.purchaseInvoice;
	}

	public void setPurchaseInvoice(TPurchaseInvoice purchaseInvoice) {
		this.purchaseInvoice = purchaseInvoice;
	}

}
