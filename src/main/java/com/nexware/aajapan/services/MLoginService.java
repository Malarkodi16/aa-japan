package com.nexware.aajapan.services;

import java.util.List;

import com.nexware.aajapan.models.MLogin;

public interface MLoginService {
	public MLogin findLoginByUsername(String username);

	public List<String> getSalesPersonIdsByHierarchyLevel();
}
