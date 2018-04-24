package com.loyalty.web.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.loyalty.web.data.dao.CustomJdbcDaoImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Autowired
	private CustomJdbcDaoImpl customJdbcDaoImpl;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		jdbcTemplate.setDataSource(dataSource);
		customJdbcDaoImpl.setJT(jdbcTemplate);
		customJdbcDaoImpl.setRolePrefix("ROLE_");
		customJdbcDaoImpl.setUsersByUsernameQuery(
				"SELECT LOYALTY_OWNER.ACCOUNT_LOGINS.PRIMARY_LOGIN_ID, lower(loyalty_owner.account_logins.password),1, LOYALTY_OWNER.ACCOUNT_LOGINS.ID, LOYALTY_OWNER.ACCOUNT_LOGINS.AGENT_FIRST_NAME,LOYALTY_OWNER.ACCOUNT_LOGINS.AGENT_LAST_NAME,LOYALTY_OWNER.ACCOUNT_LOGINS.PASSWORD_TYPE FROM LOYALTY_OWNER.ACCOUNT_LOGINS WHERE LOYALTY_OWNER.ACCOUNT_LOGINS.PRIMARY_LOGIN_ID = upper(?) AND WEB_APPLICATION = 'IG' AND LOGIN_TYPE = 'A' AND LOGIN_STATUS='UL' AND	CLIENT_PROGRAM is null AND (EXPIRATION_DATE is null or EXPIRATION_DATE > CURRENT_TIMESTAMP)");
		customJdbcDaoImpl.setAuthoritiesByUsernameQuery(
				"SELECT LOYALTY_OWNER.ACCOUNT_LOGINS.PRIMARY_LOGIN_ID,LOYALTY_OWNER.SECURITY_ROLES.NAME,LOYALTY_OWNER.SECURITY_ROLES.SECURITY_ROLE_TYPE,LOYALTY_OWNER.SECURITY_ROLES.APPLICATION_TYPE FROM LOYALTY_OWNER.ACCOUNT_LOGINS,LOYALTY_OWNER.ACCOUNT_LOGIN_SECURITY_ROLES, LOYALTY_OWNER.SECURITY_ROLES WHERE LOYALTY_OWNER.ACCOUNT_LOGINS.ID = LOYALTY_OWNER.ACCOUNT_LOGIN_SECURITY_ROLES.ACCOUNT_LOGIN  AND	LOYALTY_OWNER.ACCOUNT_LOGIN_SECURITY_ROLES.SECURITY_ROLE = LOYALTY_OWNER.SECURITY_ROLES.ID  AND	LOYALTY_OWNER.ACCOUNT_LOGINS.PRIMARY_LOGIN_ID = upper(?) AND LOYALTY_OWNER.ACCOUNT_LOGINS.WEB_APPLICATION = 'IG'  AND LOYALTY_OWNER.ACCOUNT_LOGINS.LOGIN_TYPE = 'A' AND LOYALTY_OWNER.ACCOUNT_LOGINS.LOGIN_STATUS='UL' AND	LOYALTY_OWNER.ACCOUNT_LOGINS.CLIENT_PROGRAM is null AND (LOYALTY_OWNER.ACCOUNT_LOGINS.EXPIRATION_DATE is null or LOYALTY_OWNER.ACCOUNT_LOGINS.EXPIRATION_DATE > CURRENT_TIMESTAMP) AND (LOYALTY_OWNER.ACCOUNT_LOGIN_SECURITY_ROLES.EXPIRATION_DATE is null or LOYALTY_OWNER.ACCOUNT_LOGIN_SECURITY_ROLES.EXPIRATION_DATE > CURRENT_TIMESTAMP)");
		customJdbcDaoImpl.setCustomGroupAuthoritiesByUsernameQuery(
				"SELECT LOYALTY_OWNER.ACCOUNT_LOGINS.PRIMARY_LOGIN_ID, LOYALTY_OWNER.SECURITY_ROLES.NAME, LOYALTY_OWNER.SECURITY_ROLES.SECURITY_ROLE_TYPE, LOYALTY_OWNER.SECURITY_ROLES.APPLICATION_TYPE FROM  LOYALTY_OWNER.SECURITY_ROLES, LOYALTY_OWNER.SECURITY_ROLE_RELATIONSHIPS, LOYALTY_OWNER.ACCOUNT_LOGINS, LOYALTY_OWNER.ACCOUNT_LOGIN_SECURITY_ROLES WHERE LOYALTY_OWNER.SECURITY_ROLE_RELATIONSHIPS.ASSOCIATED_SR=LOYALTY_OWNER.SECURITY_ROLES.ID AND LOYALTY_OWNER.SECURITY_ROLE_RELATIONSHIPS.PARENT_SR=LOYALTY_OWNER.ACCOUNT_LOGIN_SECURITY_ROLES.SECURITY_ROLE  AND LOYALTY_OWNER.ACCOUNT_LOGINS.ID = LOYALTY_OWNER.ACCOUNT_LOGIN_SECURITY_ROLES.ACCOUNT_LOGIN  AND LOYALTY_OWNER.ACCOUNT_LOGINS.PRIMARY_LOGIN_ID = upper(?) AND LOYALTY_OWNER.SECURITY_ROLE_RELATIONSHIPS.RELATIONSHIP_TYPE_LC='APP_ORG_ENTY' AND LOYALTY_OWNER.ACCOUNT_LOGINS.LOGIN_TYPE = 'A' AND LOYALTY_OWNER.ACCOUNT_LOGINS.LOGIN_STATUS='UL' AND LOYALTY_OWNER.ACCOUNT_LOGINS.CLIENT_PROGRAM is null AND (LOYALTY_OWNER.ACCOUNT_LOGINS.EXPIRATION_DATE is null or LOYALTY_OWNER.ACCOUNT_LOGINS.EXPIRATION_DATE > CURRENT_TIMESTAMP) AND (LOYALTY_OWNER.ACCOUNT_LOGIN_SECURITY_ROLES.EXPIRATION_DATE is null or LOYALTY_OWNER.ACCOUNT_LOGIN_SECURITY_ROLES.EXPIRATION_DATE > CURRENT_TIMESTAMP) AND (LOYALTY_OWNER.SECURITY_ROLE_RELATIONSHIPS.EXPIRATION_DATE is null or LOYALTY_OWNER.SECURITY_ROLE_RELATIONSHIPS.EXPIRATION_DATE > CURRENT_TIMESTAMP)");
		authProvider.setUserDetailsService(customJdbcDaoImpl);
		authProvider.setPasswordEncoder(new ShaPasswordEncoder(256));
		return authProvider;
	}

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll();
		http.authorizeRequests().antMatchers("/failure").permitAll();
		http.authorizeRequests().antMatchers("/login").permitAll();
		http.authorizeRequests().antMatchers("/**").access("hasRole('CC_FINUser')").anyRequest().permitAll().and().formLogin()
				.loginPage("/login").defaultSuccessUrl("/home").failureUrl("/failure").usernameParameter("username")
				.passwordParameter("password").and().logout().logoutSuccessUrl("/login").and().exceptionHandling()
				.accessDeniedPage("/failure").and().csrf().disable();
	}
}