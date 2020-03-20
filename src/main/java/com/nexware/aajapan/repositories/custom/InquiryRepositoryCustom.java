package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.TInquiryDto;

public interface InquiryRepositoryCustom {

	List<TInquiryDto> findAllInquiriesBySalesPersonIn(List<String> salesPersonIds);

	TInquiryDto findOneInquiryById(String inquiryId, String itemId);

	long getCountBySalesPersonId(List<String> salesPersonIds);
}
