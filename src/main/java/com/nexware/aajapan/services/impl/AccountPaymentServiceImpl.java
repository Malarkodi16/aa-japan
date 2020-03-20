package com.nexware.aajapan.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.PaymentCountDto;
import com.nexware.aajapan.dto.PaymentDto;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.services.AccountPaymentService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class AccountPaymentServiceImpl implements AccountPaymentService {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public PaymentCountDto getPaymentsCount() {

		GroupOperation groupOp = Aggregation.group("invoiceType").first("invoiceType").as("invoiceType").count()
				.as("count");

		AggregationResults<PaymentDto> result = this.mongoTemplate.aggregate(Aggregation.newAggregation(groupOp),
				TPurchaseInvoice.class, PaymentDto.class);
		PaymentCountDto countDto = new PaymentCountDto();
		result.getMappedResults().forEach(r -> {
			if (!AppUtil.isObjectEmpty(r.getInvoiceType())) {
				if (r.getInvoiceType().equalsIgnoreCase(Constants.ACCOUNTS_AUCTION_PAYMENT)) {
					countDto.setAuction(r.getCount());
				} else if (r.getInvoiceType().equalsIgnoreCase(Constants.ACCOUNTS_TRANSPORT_PAYMENT)) {
					countDto.setTransport(r.getCount());
				} else if (r.getInvoiceType().equalsIgnoreCase(Constants.ACCOUNTS_SHIPPING_PAYMENT)) {
					countDto.setShipping(r.getCount());
				} else if (r.getInvoiceType().equalsIgnoreCase(Constants.ACCOUNTS_OTHERS_PAYMENT)) {
					countDto.setOthers(r.getCount());
				} else if (r.getInvoiceType().equalsIgnoreCase(Constants.ACCOUNTS_FREIGHT_PAYMENT)) {
					countDto.setFreight(r.getCount());
				}
			}

		});
		return countDto;
	}

}
