package com.loyalty.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * 
 * @author rshah
 *
 *         This class allow to capture the roleType and application type
 *         associated with each role.
 */
public final class CustomGrantedAuthority implements GrantedAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String role; // LOYALTY_OWNER.SECURITY_ROLES.NAME
	private final String roleType;// LOYALTY_OWNER.SECURITY_ROLES.SECURITY_ROLE_TYPE,
	private final String applicationType;// LOYALTY_OWNER.SECURITY_ROLES.APPLICATION_TYPE

	public CustomGrantedAuthority(String role, String roleType, String applicationType) {
		Assert.hasText(role, "A granted authority textual representation is required");
		this.role = role;
		this.roleType = roleType;
		this.applicationType = applicationType;
	}

	public String getRoleType() {
		return roleType;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public String getAuthority() {
		return role;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof CustomGrantedAuthority) {
			return role.equals(((CustomGrantedAuthority) obj).role);
		}

		return false;
	}

	public int hashCode() {
		return this.role.hashCode();
	}

	public String toString() {
		return this.role;
	}

}
