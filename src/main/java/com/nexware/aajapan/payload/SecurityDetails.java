package com.nexware.aajapan.payload;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nexware.aajapan.dto.MLoginDto;

public class SecurityDetails implements UserDetails {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private MLoginDto login;
	private org.springframework.security.core.userdetails.User userSecurity;
	private Set<GrantedAuthority> authorities = null;

	public SecurityDetails(MLoginDto login, org.springframework.security.core.userdetails.User userSecurity,
			Set<GrantedAuthority> grantedAuthorities) {
		this.login = login;
		this.userSecurity = userSecurity;
		this.authorities = grantedAuthorities;
	}

	public void setAuthorities(Set<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.userSecurity.getPassword();
	}

	@Override
	public String getUsername() {
		return this.userSecurity.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.userSecurity.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.userSecurity.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.userSecurity.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return this.userSecurity.isEnabled();
	}

	public org.springframework.security.core.userdetails.User getUserSecurity() {
		return this.userSecurity;
	}

	public void setUserSecurity(org.springframework.security.core.userdetails.User userSecurity) {
		this.userSecurity = userSecurity;
	}

	public MLoginDto getLogin() {
		return this.login;
	}

	public void setLogin(MLoginDto login) {
		this.login = login;
	}

}