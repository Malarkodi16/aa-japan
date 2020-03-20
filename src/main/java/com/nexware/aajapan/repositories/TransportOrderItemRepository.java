package com.nexware.aajapan.repositories;

import java.util.Date;
import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TTransportOrderItem;
import com.nexware.aajapan.repositories.custom.TransporterOrderItemRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TransportOrderItemRepository
		extends MongoRepository<TTransportOrderItem, String>, TransporterOrderItemRepositoryCustom {
	@Query("{$and:[{'invoiceNo':?0},{'CreatedDate' : { $gte: ?1, $lte: ?2 } }]}")
	List<TTransportOrderItem> findAllOrdersToConfirm(String transporter, Date start, Date end);

	List<TTransportOrderItem> findAllByIdIn(List<String> id);

	TTransportOrderItem findOneByInvoiceNoAndStockNo(String invoiceNo, String stockNo);
}
