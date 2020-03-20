package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.models.MForwarder;
import com.nexware.aajapan.models.MGeneralSupplier;

public interface MGeneralSupplierRepositoryCustom {

List<MGeneralSupplier> getAllUnDeletedGeneralSupplier();
	
	boolean existsByCodeAndName(String code, String name);

	boolean existsByName(String name);
}
