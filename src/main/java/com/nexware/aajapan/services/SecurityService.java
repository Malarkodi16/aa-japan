package com.nexware.aajapan.services;

import com.nexware.aajapan.dto.MLoginDto;

public interface SecurityService {
	MLoginDto findLoggedInUser();

}
