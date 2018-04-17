package com.loyalty.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WelcomeController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return "Login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginUrl(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		return "Login";
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String homeUrl(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return "Home";
	}

	@RequestMapping(value = "/failure", method = RequestMethod.GET)
	public String error(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return "NotAuthorized";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		return "Login";

	}
}
