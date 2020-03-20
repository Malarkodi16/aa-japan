package com.nexware.aajapan.services.impl;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
	public static final Integer SESSION_TIMEOUT_IN_SECONDS = 60 * 60;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		request.getSession().setMaxInactiveInterval(SESSION_TIMEOUT_IN_SECONDS);
		Collection<? extends GrantedAuthority> auths = authentication.getAuthorities();
		for (GrantedAuthority authorities : auths) {
			if (authorities.getAuthority().equals("ROLE_SHIPPING")) {
				response.sendRedirect("stock/purchased");
			} else if (authorities.getAuthority().equals("ROLE_SALES")) {
				response.sendRedirect("inquiry/listview");

			} else if (authorities.getAuthority().equals("ROLE_ACCOUNTS")) {
				response.sendRedirect("accounts/dash-board/view");
			} else if (authorities.getAuthority().equals("ROLE_ADMIN")) {
				response.sendRedirect("a/dashboard");
			} else if (authorities.getAuthority().equals("ROLE_DOCUMENTS")) {
				response.sendRedirect("documents/tracking/not-received");
			}
		}
	}
}
