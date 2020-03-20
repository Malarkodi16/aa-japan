package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TShippingRequest;
import com.nexware.aajapan.repositories.custom.TShippingRequestRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TShippingRequestRepository
		extends MongoRepository<TShippingRequest, String>, TShippingRequestRepositoryCustom {

	Long countByStatus(Integer status);

	Long countByBlDraftStatus(Integer status);

	Long countByBlOriginalStatus(Integer status);

	TShippingRequest findOneByShipmentRequestId(String shipmentRequestId);

	TShippingRequest findOneByShipmentRequestIdAndStatus(String shipmentRequestId, Integer status);

	List<TShippingRequest> findAllByShipmentRequestIdIn(List<String> shipmentRequestId);

	List<TShippingRequest> findAllByAllocationId(String allocationId);

	List<TShippingRequest> findAllByAllocationIdAndContainerNo(String allocationId, String containerNo);

	List<TShippingRequest> findAllByAllocationIdAndContainerNoAndStatus(String allocationId, String containerNo,
			Integer status);

	List<TShippingRequest> findAllByAllocationIdAndStatus(String allocationId, Integer status);

	List<TShippingRequest> findAllByScheduleIdAndStatus(String scheduleId, Integer status);

}
