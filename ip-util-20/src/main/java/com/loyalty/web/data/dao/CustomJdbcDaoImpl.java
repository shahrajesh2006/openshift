package com.loyalty.web.data.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.stereotype.Repository;

import com.loyalty.web.security.CustomGrantedAuthority;
import com.loyalty.web.vo.UserDetailsVO;

/**
 * <b>This class is used by the spring security to handle the custom requirement
 * around authentication and authorization.</b>
 */
@Repository
public class CustomJdbcDaoImpl extends JdbcDaoImpl {

	private String customGroupAuthoritiesByUsernameQuery; // groupAuthoritiesByUsernameQuery
															// is not exposed by
															// JdbcDaoImpl

	public String getCustomGroupAuthoritiesByUsernameQuery() {
		return customGroupAuthoritiesByUsernameQuery;
	}

	public void setCustomGroupAuthoritiesByUsernameQuery(String customGroupAuthoritiesByUsernameQuery) {
		this.customGroupAuthoritiesByUsernameQuery = customGroupAuthoritiesByUsernameQuery;
	}

	@Autowired
	public void setJT(JdbcTemplate jdbcTemplate) {
		setJdbcTemplate(jdbcTemplate);
	}

	@Override
	protected List<UserDetails> loadUsersByUsername(String username) {
		return getJdbcTemplate().query(getUsersByUsernameQuery(), new String[] { username },
				new RowMapper<UserDetails>() {
					public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
						String username = rs.getString(1);
						String password = rs.getString(2);
						boolean enabled = rs.getBoolean(3);
						String accountLoginId = rs.getString(4);
						String agentFirstName = rs.getString(5);
						String agentLastName = rs.getString(6);
						String passwordType = rs.getString(7);

						return new UserDetailsVO(username, password, enabled, true, true, true,
								AuthorityUtils.NO_AUTHORITIES, accountLoginId, agentFirstName, agentLastName,
								passwordType);
					}

				});
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails ud = super.loadUserByUsername(username);
		return ud;
	}

	@Override
	protected UserDetails createUserDetails(String username, UserDetails userFromUserQuery,
			List<GrantedAuthority> combinedAuthorities) {
		UserDetailsVO userDetailsVO = (UserDetailsVO) userFromUserQuery;
		String returnUsername = userFromUserQuery.getUsername();
		String accountLoginId = userDetailsVO.getAccountLoginId();
		return new UserDetailsVO(returnUsername, userFromUserQuery.getPassword(), userFromUserQuery.isEnabled(), true,
				true, true, combinedAuthorities, accountLoginId, userDetailsVO.getAgentFirstName(),
				userDetailsVO.getAgentLastName(), userDetailsVO.getPasswordType());
	}

	@Override
	protected List<GrantedAuthority> loadGroupAuthorities(String username) {

		return getJdbcTemplate().query(getCustomGroupAuthoritiesByUsernameQuery(), new String[] { username },
				new RowMapper<GrantedAuthority>() {
					public GrantedAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
						String roleName = getRolePrefix() + rs.getString(2);
						String roleType = rs.getString(3);
						String applicationType = rs.getString(4);

						return new CustomGrantedAuthority(roleName, roleType, applicationType);
					}
				});
	}

	@Override
	protected List<GrantedAuthority> loadUserAuthorities(String username) {
		return getJdbcTemplate().query(getAuthoritiesByUsernameQuery(), new String[] { username },
				new RowMapper<GrantedAuthority>() {
					public GrantedAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
						String roleName = getRolePrefix() + rs.getString(2);
						String roleType = rs.getString(3);
						String applicationType = rs.getString(4);

						return new CustomGrantedAuthority(roleName, roleType, applicationType);
					}
				});
	}

}
