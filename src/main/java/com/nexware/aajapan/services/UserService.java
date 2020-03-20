package com.nexware.aajapan.services;

import com.nexware.aajapan.models.MUser;

public interface UserService {
	public MUser findUserByEmail(String email);

	public void saveUser(MUser user);
}
