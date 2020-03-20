package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.TDocumentConversionDto;

public interface TDocumentConversionRepositoryCustom {

	List<TDocumentConversionDto> findAllByStatus(Integer docType);

	List<TDocumentConversionDto> findAllExportCertificatesByExportCertificateStatusAndDocSentStatus(
			Integer exportCertificateStatus, Integer docSentStatus);

	/*
	 * List<TDocumentConversionDto>
	 * findAllExportCertificatesByExportCertificateTracking(Integer handOverStatus,
	 * Integer docConvertTo);
	 */

	List<TDocumentConversionDto> findAllCrReceivedDocument();

	Long findAllByExportCertificateCount();
}
