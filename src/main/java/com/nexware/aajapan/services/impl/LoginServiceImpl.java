package com.nexware.aajapan.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.models.MLogin;
import com.nexware.aajapan.repositories.LoginRepository;
import com.nexware.aajapan.services.MLoginService;
import com.nexware.aajapan.services.SecurityService;

@Service
@Transactional
public class LoginServiceImpl implements MLoginService {
	@Autowired
	private LoginRepository loginRepository;

	@Autowired
	private SecurityService securityService;

	@Override
	public MLogin findLoginByUsername(String username) {
		return this.loginRepository.findByUsername(username);
	}

	@Override
	public List<String> getSalesPersonIdsByHierarchyLevel() {
		MLoginDto loginDto = this.securityService.findLoggedInUser();
		List<String> list = null;
		if (loginDto.getDepartment().equalsIgnoreCase(Constants.DEPARTMENT_ADMIN)) {
			return Collections.emptyList();
		} else if (loginDto.getDepartment().equalsIgnoreCase(Constants.DEPARTMENT_SALES)
				&& (loginDto.getHierarchy() == 0)) {
			return Collections.emptyList();
		} else {
			Document result = this.loginRepository
					.findUserIdByDepartmentAndHierarchyLevelIsLessThanOrEqual(loginDto.getUserId());
			List<Document> reportingHierarchy = result.get("reportingHierarchy", new ArrayList<Document>());
			list = reportingHierarchy.stream().map(r -> r.getString("userId")).collect(Collectors.toList());
			list.add(result.getString("userId"));
		}

		return list;
	}

}
