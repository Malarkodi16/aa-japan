package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.MUserDto;
import com.nexware.aajapan.dto.MUserInfoDto;
import com.nexware.aajapan.models.MUser;

public interface UserRepositoryCustom {

	List<MUser> findAllByRole();

	List<MUserDto> findAllUsers();

	List<MUserInfoDto> getListByDepartment(String department);
}
