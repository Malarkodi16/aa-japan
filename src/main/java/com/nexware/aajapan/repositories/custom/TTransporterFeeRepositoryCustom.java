package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.TTransporterChargeDto;
import com.nexware.aajapan.dto.TTransporterFeeDto;

public interface TTransporterFeeRepositoryCustom {
	List<TTransporterChargeDto> findByLocation(String from, String transportCategory, String to);

	List<TTransporterFeeDto> getListBeforeDeleteAndWithoutCode();

	boolean isExist(String transporter, String from, String to, List<String> categories, int deleteFlag);

	boolean isExist(String id, String transporter, String from, String to, List<String> categories, int deleteFlag);
}
