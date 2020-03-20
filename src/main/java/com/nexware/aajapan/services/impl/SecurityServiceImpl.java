package com.nexware.aajapan.services.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.payload.SecurityDetails;
import com.nexware.aajapan.services.SecurityService;

@Service
@Transactional
public class SecurityServiceImpl implements SecurityService {

	@Override
	public MLoginDto findLoggedInUser() {
		SecurityDetails userDetails = (SecurityDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		if (userDetails != null) {
			return userDetails.getLogin();
		}
		return null;
	}

}
