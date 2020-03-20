package com.nexware.aajapan.services.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.payload.SecurityDetails;
import com.nexware.aajapan.repositories.LoginRepository;

@Service
@Transactional
public class AuthendicationDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
	@Autowired
	private LoginRepository loginRepository;
	@Autowired
	private HttpServletRequest request;

	@Override
	public UserDetails loadUserByUsername(String username) {
		MLoginDto login = this.loginRepository.findOneByUsername(username);
		if (login == null) {
			throw new UsernameNotFoundException("Username is not found");
		}
//		String ipaddress = request.getRemoteAddr();
//		if (ipaddress.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
//			try {
//				ipaddress = InetAddress.getLocalHost().getHostAddress();
//			} catch (UnknownHostException e) {
//				throw new AAJRuntimeException(e.getMessage());
//			}
//		}
//		System.out.println(ipaddress);
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + login.getDepartment()));
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + login.getRole()));
		org.springframework.security.core.userdetails.User userSecurity = new org.springframework.security.core.userdetails.User(
				login.getUsername(), login.getPassword(), grantedAuthorities);

		return new SecurityDetails(login, userSecurity, grantedAuthorities);
	}

}
