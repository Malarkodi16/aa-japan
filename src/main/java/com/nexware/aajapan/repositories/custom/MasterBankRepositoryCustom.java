package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.MBankDto;
import com.nexware.aajapan.dto.MCurrencyDto;
import com.nexware.aajapan.dto.MForeignBankDto;

public interface MasterBankRepositoryCustom {

	List<MCurrencyDto> findAllByAccountType();
	
	List<MBankDto> findAllByAccountTypeBank();
	
	

}
