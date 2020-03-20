package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.MForwarderDetailDto;

public interface MForwarderDetailRepositoryCustom {

	List<MForwarderDetailDto> findAllFwdrByOrginAndDestination(String orginPort, String destPort);
}
