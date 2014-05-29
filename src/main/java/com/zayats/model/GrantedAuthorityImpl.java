package com.zayats.model;

import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityImpl implements GrantedAuthority {

	private String rolename;

	public GrantedAuthorityImpl(String rolename) {
		this.rolename = rolename;
	}

	public String getAuthority() {
		return this.rolename;
	}

}
