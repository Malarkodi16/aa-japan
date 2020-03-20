package com.nexware.aajapan.repositories.custom;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.client.result.UpdateResult;
import com.nexware.aajapan.dto.TShippingInstructionDto;
import com.nexware.aajapan.dto.TShippingInstructionFromSalesDto;

public interface TShippingInstructionRepositoryCustom {
	List<TShippingInstructionFromSalesDto> findAllShippingInstructionFromSales();

	Optional<List<TShippingInstructionDto>> findBySalesPersonShippingData(List<String> salesPersonIds);

	Optional<List<TShippingInstructionDto>> findBySalesPersonShippingStatus(List<String> salesPersonIds);

	Long findByShippingInstructionCount(List<String> salesPersonIds);

	Long findByShippingInstructionStatusCount(List<String> salesPersonIds);

	void updateStatusByShippingInstructionIds(Integer status, List<String> ids);

	UpdateResult updateById(String id, Update update);

	TShippingInstructionFromSalesDto findOneShippingInstructionFromSales(String id);
}
