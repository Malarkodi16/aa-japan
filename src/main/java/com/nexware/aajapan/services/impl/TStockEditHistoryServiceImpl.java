package com.nexware.aajapan.services.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.models.TStockEditHistory;
import com.nexware.aajapan.repositories.TStockEditHistoryRepository;
import com.nexware.aajapan.services.TStockEditHistoryService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class TStockEditHistoryServiceImpl implements TStockEditHistoryService {

	@Autowired
	private TStockEditHistoryRepository stockEditHistoryRepository;

	@Override
	public void compareStockObject(TStock oldObject, TStock newObject) throws IllegalAccessException {

		List<String> values = new ArrayList<>();
		for (Field field : oldObject.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Object value1 = field.get(oldObject);
			Object value2 = field.get(newObject);

			if (!AppUtil.isObjectEmpty(value1) && !AppUtil.isObjectEmpty(value2)) {

				if (value1.getClass().isInstance(oldObject.getPurchaseInfo())) {
					for (Field innerFields : value1.getClass().getDeclaredFields()) {
						innerFields.setAccessible(true);
						Object oldPurchasefield = innerFields.get(oldObject.getPurchaseInfo());
						Object newPurchasefield = innerFields.get(newObject.getPurchaseInfo());
						if (!AppUtil.isObjectEmpty(oldPurchasefield) && !oldPurchasefield.equals(newPurchasefield)) {

							if (oldPurchasefield.getClass().isInstance(oldObject.getPurchaseInfo().getAuctionInfo())) {
								for (Field innerNestedFields : oldPurchasefield.getClass().getDeclaredFields()) {
									innerNestedFields.setAccessible(true);
									Object oldAuctionField = innerNestedFields
											.get(oldObject.getPurchaseInfo().getAuctionInfo());
									Object newAuctionField = innerNestedFields
											.get(newObject.getPurchaseInfo().getAuctionInfo());
									if (!AppUtil.isObjectEmpty(oldAuctionField)
											&& !oldAuctionField.equals(newAuctionField)) {
										values.add(String.valueOf(field.getName() + ": " + value1 + " -> " + value2));
										String oldAuctionFieldChecked = AppUtil.isObjectEmpty(oldAuctionField) ? null
												: oldAuctionField.toString();
										String newAuctionFieldChecked = AppUtil.isObjectEmpty(newAuctionField) ? null
												: newAuctionField.toString();

										stockEditHistoryRepository.insert(new TStockEditHistory(
												innerNestedFields.getName(), oldObject.getStockNo(),
												oldAuctionFieldChecked, newAuctionFieldChecked));
									}

								}
							} else {
								values.add(String.valueOf(field.getName() + ": " + value1 + " -> " + value2));
								stockEditHistoryRepository
										.insert(new TStockEditHistory(innerFields.getName(), oldObject.getStockNo(),
												oldPurchasefield.toString(), newPurchasefield.toString()));
							}
						}
					}
				} else if (value1.getClass().isInstance(oldObject.getTransportInfo())) {
					for (Field innerFields : value1.getClass().getDeclaredFields()) {
						innerFields.setAccessible(true);
						Object oldTransField = innerFields.get(oldObject.getTransportInfo());
						Object newTransField = innerFields.get(newObject.getTransportInfo());
						if (!AppUtil.isObjectEmpty(oldTransField) && !oldTransField.equals(newTransField)) {
							values.add(String.valueOf(field.getName() + ": " + value1 + " -> " + value2));
							String fieldName = innerFields.getName();
							if (innerFields.getName().equalsIgnoreCase("remarks")) {
								fieldName = "transportRemarks";
							} else if (innerFields.getName().equalsIgnoreCase("status")) {
								fieldName = "transportStatus";
							}

							stockEditHistoryRepository.insert(new TStockEditHistory(fieldName, oldObject.getStockNo(),
									oldTransField.toString(), newTransField.toString()));
						}
					}
				} else if (value1.getClass().isInstance(oldObject.getReservedInfo())) {
					for (Field innerFields : value1.getClass().getDeclaredFields()) {
						innerFields.setAccessible(true);
						Object oldReservedField = innerFields.get(oldObject.getReservedInfo());
						Object newReservedField = innerFields.get(newObject.getReservedInfo());
						if (!AppUtil.isObjectEmpty(oldReservedField) && !oldReservedField.equals(newReservedField)) {
							values.add(String.valueOf(field.getName() + ": " + value1 + " -> " + value2));
							String fieldName = innerFields.getName();
							if (innerFields.getName().equalsIgnoreCase("customerId")) {
								fieldName = "reservedCustomer";
							} else if (innerFields.getName().equalsIgnoreCase("date")) {
								fieldName = "reservedDate";
							}

							String oldReservedFieldChecked = AppUtil.isObjectEmpty(oldReservedField) ? null
									: oldReservedField.toString();
							String newReservedFieldChecked = AppUtil.isObjectEmpty(newReservedField) ? null
									: newReservedField.toString();

							stockEditHistoryRepository.insert(new TStockEditHistory(fieldName, oldObject.getStockNo(),
									oldReservedFieldChecked, newReservedFieldChecked));
						}
					}
				} else if (value1.getClass().isInstance(oldObject.getShippingInstructionInfo())) {
					for (Field innerFields : value1.getClass().getDeclaredFields()) {
						innerFields.setAccessible(true);
						Object oldInstructionField = innerFields.get(oldObject.getShippingInstructionInfo());
						Object newInstructionField = innerFields.get(newObject.getShippingInstructionInfo());
						if (!AppUtil.isObjectEmpty(oldInstructionField)
								&& !oldInstructionField.equals(newInstructionField)) {
							values.add(String.valueOf(field.getName() + ": " + value1 + " -> " + value2));
							String fieldName = innerFields.getName();
							if (innerFields.getName().equalsIgnoreCase("customerId")) {
								fieldName = "shippingCustomer";
							} else if (innerFields.getName().equalsIgnoreCase("salesPersonId")) {
								fieldName = "shippingSalesPerson";
							}
							stockEditHistoryRepository.insert(new TStockEditHistory(fieldName, oldObject.getStockNo(),
									oldInstructionField.toString(), newInstructionField.toString()));
						}
					}
				} else {
					if (!value1.equals(value2)) {
						values.add(String.valueOf(field.getName() + ": " + value1 + " -> " + value2));
						if (!field.getName().equalsIgnoreCase("modelId")) {
							stockEditHistoryRepository.insert(new TStockEditHistory(field.getName(),
									oldObject.getStockNo(), value1.toString(), value2.toString()));
						}
					}
				}

			}
		}
	}
}
