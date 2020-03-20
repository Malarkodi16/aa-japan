package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import com.nexware.aajapan.dto.CustomReportDto;
import com.nexware.aajapan.models.MCustomListFields;

public interface TCustomReportRepositoryCustom {
	List<CustomReportDto> findByCustomField(List<MCustomListFields> customFields, String type, Date from, Date to);
}
