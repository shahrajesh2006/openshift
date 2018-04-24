package com.loyalty.web.application;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.loyalty")
public class Application extends SpringBootServletInitializer {

	final static Logger log = LoggerFactory.getLogger(Application.class);
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) throws Exception {
		// hack for JCE Unlimited Strength since this application should
		// support 256 key size for encryption
		Field field;
		try {
			field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
			field.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

			field.set(null, false);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
		}
		SpringApplication.run(Application.class, args);
	}

}