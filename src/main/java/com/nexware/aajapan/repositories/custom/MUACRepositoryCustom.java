package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.MUACDto;

public interface MUACRepositoryCustom {

	List<MUACDto> findAllByDepartment(int dept);

	List<MUACDto> findAllUAC();

}
