package com.nexware.aajapan.configuration;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.services.SecurityService;

public class CustomWebSecurityExpressionRoot extends SecurityExpressionRoot
		implements MethodSecurityExpressionOperations {
	private SecurityService securityService;

	public CustomWebSecurityExpressionRoot(Authentication authentication, SecurityService securityService) {
		super(authentication);
		this.securityService = securityService;
	}

	public boolean canAccess(Integer accessId) {
		MLoginDto login = securityService.findLoggedInUser();
		return login.getAccess() != null && login.getAccess().contains(accessId);
	}

	public boolean canAccessAny(Integer... accessId) {
		MLoginDto login = securityService.findLoggedInUser();
		if (login.getAccess() == null) {
			return false;
		}
		for (int i = 0; i < accessId.length; i++) {
			if (login.getAccess().contains(accessId[i])) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void setFilterObject(Object filterObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getFilterObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReturnObject(Object returnObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getReturnObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getThis() {
		// TODO Auto-generated method stub
		return null;
	}
}