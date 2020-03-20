package com.nexware.aajapan.services;

import java.util.List;
import java.util.Map;

import com.nexware.aajapan.dto.BlTransactionListDto;
import com.nexware.aajapan.dto.CRDto;
import com.nexware.aajapan.models.TShippingInstruction;

public interface TBlTransactionService {

	void saveBlTransaction(List<TShippingInstruction> shippingInstruction);

	void updateSingleBlTransaction(String shippingInstructionId, String customerId, String consigneeId);

	void updateMultipleBlTransaction(List<Map<String, Object>> data);

	void updateSingleBlTransactionNo(String blNo, String shipmentRequestId);

	void updateMultipleBlTransactionNo(List<Map<String, Object>> data);

	List<BlTransactionListDto> getBLListTransaction(String shippingInstructionId);

	void updateReceiveOrSurrenderStatus(String shipmentRequestId, Integer recSurStatus);

	void updateBlDocStatus(String shipmentRequestId, Integer status);

	List<CRDto> getCrList();

	void updateSingleCrTransaction(String shippingInstructionId, String customerId, String consigneeId);

	void updateMultipleCrTransaction(List<Map<String, Object>> data);

}
