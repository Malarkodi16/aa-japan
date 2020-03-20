package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.models.MTransporter;

public interface MTransporterRepositoryCustom {
	
	List<MTransporter> getAllUnDeletedTransporters();

}
