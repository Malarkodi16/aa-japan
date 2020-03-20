
package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.mongodb.core.query.Criteria;

import com.nexware.aajapan.dto.BillOfLandingDto;
import com.nexware.aajapan.dto.DocumentStatusDto;
import com.nexware.aajapan.dto.FreightVesselDto;
import com.nexware.aajapan.dto.ShippingRequestedItemsDto;
import com.nexware.aajapan.dto.TShippingRequestContainerDto;
import com.nexware.aajapan.dto.TShippingRequestDto;
import com.nexware.aajapan.dto.TShippingRequestEtaEtd;
import com.nexware.aajapan.dto.TShippingRequestedContainerExcelDto;
import com.nexware.aajapan.dto.TShippingRequestedDto;
import com.nexware.aajapan.dto.TShippingRoroExcelDto;
import com.nexware.aajapan.models.TShippingRequest;

public interface TShippingRequestRepositoryCustom {

	List<TShippingRequestDto> findAllFreightItems();

	List<FreightVesselDto> findBySearch(String forwarder, List<String> vessel, List<String> voyageNo);

	List<FreightVesselDto> findAllContainerShippingRequestBySearch(Optional<String> forwarder, String[] scheduleIds);

	void updateFreightExchange(String invoiceNo, Date invoiceDate, String exchangeRate, List<String> invoiceId);

	void updateFreightStatus(int status, List<String> shippingRequestId);

	List<DocumentStatusDto> findAllLesserEtd();

	List<DocumentStatusDto> findAllLesserEta();

	void updateDraftStatus(int status, List<String> shippingRequestId);

	void updateFieldsByShippingRequestId(Map<String, Object> data, String id);

	void updateOriginalStatus(int status, List<String> shippingRequestId);

	List<DocumentStatusDto> findAllBlCustomers();

	List<TShippingRequestedDto> findAllShippingRequestedStock(Integer status);

	TShippingRequestedDto findByShipId(String shipId);

	Long getCountByShipmentType(Integer type);

	Long findDraftBlCount();

	Long findOriginalBlCount();

	TShippingRequestedDto findShippingRequestedStockByScheduleId(String forwarder, Integer status);

	Optional<List<TShippingRequestedDto>> findAllShippingRequestedStatusStock(String forwarderFilter);

	List<TShippingRequestedDto> findAllShippingRequestedStockByStatus(Integer status);

	List<TShippingRequestedDto> findAllShippingArrangedRequestedStock(Integer status);

	Optional<List<TShippingRequestedDto>> findAllShippingRequestedStatusStock(String forwarderFilter,
			String countryFilter, String portFilter, String shipmentTypeFilter);

	Long findAllShippingAcceptedStockCount();

	List<TShippingRequestContainerDto> findAllShippingContainerStock(Integer status);

	TShippingRequestedContainerExcelDto findAllShippingContainerExcelData(String allocationId, Integer status);

	TShippingRequestEtaEtd findEtaEtdByShipmentRequestId(String shipmentRequestId);

	List<ShippingRequestedItemsDto> findAllContainerShippingRequestedItems(List<Criteria> criterias);

	List<TShippingRoroExcelDto> roroConfirmedExcelReport(String scheduleId, String destCountry);

	List<TShippingRoroExcelDto> roroArrangedExcelReport(String forwarderId, String destCountry, String allocationId,
			Integer status, String originPort);

	List<TShippingRoroExcelDto> roroExcelReport(String scheduleId);

	List<TShippingRequest> findAllBlNo();

	List<BillOfLandingDto> findByBlNo(String blNo);

}
