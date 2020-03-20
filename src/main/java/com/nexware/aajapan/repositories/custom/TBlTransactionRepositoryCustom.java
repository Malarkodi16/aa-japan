package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.BlTransactionListDto;

public interface TBlTransactionRepositoryCustom {
	
	List<BlTransactionListDto> getBLListTransaction(String shippingInstructionId);

}
