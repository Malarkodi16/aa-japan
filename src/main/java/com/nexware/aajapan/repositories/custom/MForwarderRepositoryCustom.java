package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.models.MForwarder;

public interface MForwarderRepositoryCustom {
	
	List<MForwarder> getAllUnDeletedForwarders();
	
	boolean existsByCodeAndName(String code, String name);

	boolean existsByName(String name);

	

}
