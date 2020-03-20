package com.nexware.aajapan.services;

import java.util.List;

import com.nexware.aajapan.dto.PurchasedSaveDto;
import com.nexware.aajapan.dto.TShippingRequestedCreateDto;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.models.TNotification;
import com.nexware.aajapan.models.TShippingInstruction;

public interface TNotificationService {

	void notify(List<TNotification> notification, String username);

	List<TNotification> notifyDupliateCustomerToAdmin(TCustomer tCustomer);

	List<TNotification> purchaseSaveNotificationToSales(List<PurchasedSaveDto> purchasedSaveDtos);

	List<TNotification> notifyAboutShippingInstruction(List<TShippingInstruction> shippingInstruction);

	List<TNotification> acceptShippingRequest(TShippingRequestedCreateDto shippingRequest);

	List<TNotification> cancelSalesOrderNotification(String invoiceNo, String code);

}
