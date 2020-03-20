package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.VessalDto;
import com.nexware.aajapan.dto.ViewShipmentScheduleDto;

public interface TShipmentScheduleRepositoryCustom {

	List<ViewShipmentScheduleDto> findAllShippingScheduleDetails(String orginPort, String destPort);

	List<VessalDto> findAllVessal(String destPort);

	List<VessalDto> findAllVessalByOrigin(String originPort, String destPort);

	List<VessalDto> findVessalsAndVoyageNo();

	boolean isExistsByVoyageNo(String shipId, String voyageno, String shippingCompanyNo, Integer deleteFlag);

	boolean isExistsByScheduleIdAndVoyageno(String shippingCompanyNo, String shipId, String voyageno,
			Integer deleteFlag);

	List<VessalDto> findAllScheduleById(String[] shipmentRequestIds);

	VessalDto findOneVesselDetailsByShippmentRequestId(String shipmentRequestIds);
}
