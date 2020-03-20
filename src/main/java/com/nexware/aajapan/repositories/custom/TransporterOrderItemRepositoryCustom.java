package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import com.mongodb.client.result.UpdateResult;
import com.nexware.aajapan.dto.TTransportOrderInvoiceDto;
import com.nexware.aajapan.dto.TTransportOrderItemDto;
import com.nexware.aajapan.dto.TTransportOrderListDto;

public interface TransporterOrderItemRepositoryCustom {

	UpdateResult updateStatusById(String id, int status);

	UpdateResult cancelItemById(String id, String reason);

	UpdateResult updateStatusByOrderNoAndStockNos(String invoiceNo, List<String> stockNos, int status);

	UpdateResult updateStatusByOrderNo(String invoiceNo, int status);

	List<TTransportOrderListDto> findAllTransportOrders();

	List<TTransportOrderListDto> findAllConfirmTransportOrders();

	List<TTransportOrderListDto> findAllCompletedTransportOrders();

	List<TTransportOrderItemDto> findAllDeliveryConfirmedTransportOrders();

	List<TTransportOrderListDto> findAllDeliveredTransportOrders();

	Long findTransportInitiatedCount();
	
	Integer findCountOfTransportRequested();

	Integer findCountOfTransportConfirmed();

	TTransportOrderListDto findOneTransportOrdersById(String invoiceId);

	TTransportOrderInvoiceDto findOneTransportOrdersInvoiceByInvoiceID(String invoiceNo, Date purchaseDate,
			String transporter, List<Integer> matchTransStatus);

	TTransportOrderInvoiceDto findOneTransportOrdersInvoiceByInvoiceID(String[] ids);

}
