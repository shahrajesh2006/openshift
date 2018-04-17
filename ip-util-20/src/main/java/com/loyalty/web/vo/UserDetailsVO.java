package com.loyalty.web.vo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Wrapper class to wrap the spring security user object and helps to add
 * extended application specific attributes
 * 
 * @author rshah
 *
 */
public class UserDetailsVO extends User {

	private static final long serialVersionUID = -6884445264559278487L;

	public UserDetailsVO(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
			String accountLoginId, String agentFirstName, String agentLastName, String passwordType) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.accountLoginId = accountLoginId;
		this.agentFirstName = agentFirstName;
		this.agentLastName = agentLastName;
		this.passwordType = passwordType;
	}

	private String accountLoginId;

	private String agentFirstName;
	private String agentLastName;

	private String passwordType;

	public String getAccountLoginId() {
		return accountLoginId;
	}

	public void setAccountLoginId(String accountLoginId) {
		this.accountLoginId = accountLoginId;
	}

	public UserDetailsVO(String username, String password, Collection<? extends GrantedAuthority> authorities,
			String accountLoginId) {
		super(username, password, authorities);
		this.accountLoginId = accountLoginId;
	}

	public String getAgentFirstName() {
		return agentFirstName;
	}

	public void setAgentFirstName(String agentFirstName) {
		this.agentFirstName = agentFirstName;
	}

	public String getAgentLastName() {
		return agentLastName;
	}

	public void setAgentLastName(String agentLastName) {
		this.agentLastName = agentLastName;
	}

	public String getPasswordType() {
		return passwordType;
	}

	public void setPasswordType(String passwordType) {
		this.passwordType = passwordType;
	}

}
