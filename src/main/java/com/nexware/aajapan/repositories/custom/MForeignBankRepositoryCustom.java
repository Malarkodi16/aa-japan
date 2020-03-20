package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.models.MForeignBank;

public interface MForeignBankRepositoryCustom {
	
	List<MForeignBank> getAllUnDeletedForeignBank();

}
