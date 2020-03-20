package com.nexware.aajapan.repositories.custom;

import com.mongodb.client.result.UpdateResult;

public interface TransporterOrderRepositoryCustom {

	UpdateResult updateInvoiceStatus(String orderNo, int status);

}
