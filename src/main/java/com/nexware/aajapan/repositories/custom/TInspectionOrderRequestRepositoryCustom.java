package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import com.nexware.aajapan.dto.InspectionApplicationDto;
import com.nexware.aajapan.dto.TInspectionApprovalDataDto;
import com.nexware.aajapan.dto.TInspectionBookingDataDto;
import com.nexware.aajapan.dto.TInspectionCancelledDto;
import com.nexware.aajapan.dto.TInspectionOrderRequestDto;
import com.nexware.aajapan.dto.TInspectionOrderRequestItemDto;

public interface TInspectionOrderRequestRepositoryCustom {

	List<TInspectionOrderRequestDto> findAllInspectionOrderRequestByStatus(Integer... status);

	List<TInspectionOrderRequestItemDto> findAllInspectionCompletedByStatus(Integer status);
	
	List<TInspectionOrderRequestDto> findAllInspectionOrderRequestByTransportNotComplete(Integer... status);

	List<TInspectionCancelledDto> getCancelledOrderByStatus(Integer status);

	TInspectionOrderRequestItemDto findByInspectionOrderRequestId(String id);

	Integer findCountOfInspectionRequested();

	Integer findCountOfInspectionAvailable();

	void updateInspectionDocumentSentStatus(String id, int status, Date date);

	void updateInspectionOrderRequestDate(String id, Date inspectionSentDate, Date inspectedDate,
			Date inspectionRcvdDate);

	InspectionApplicationDto findAllInspectionOrderRequestByParams(Date createdDate, String country,
			String inspectionCompanyId, String forwarderId, String locationId);

	List<TInspectionBookingDataDto> getAllInspectionBookingData();

	List<TInspectionApprovalDataDto> getAllInspectionApprovalData();
}
