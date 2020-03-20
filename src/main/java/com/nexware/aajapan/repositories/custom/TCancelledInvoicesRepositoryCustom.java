package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.TCancelledInvoicesDto;

public interface TCancelledInvoicesRepositoryCustom {

	List<TCancelledInvoicesDto> findAllData();
}
