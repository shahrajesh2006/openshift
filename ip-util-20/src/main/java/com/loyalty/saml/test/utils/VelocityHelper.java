package com.loyalty.saml.test.utils;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VelocityHelper {

	private static VelocityEngine ve = new VelocityEngine();

	final static Logger log = LoggerFactory.getLogger(VelocityHelper.class);
	
	static {
		try {
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public static String processTemplate(String templateName, Map<String, Object> data) {

		String results = "";
		StringWriter writer = new StringWriter();
		try {
			/* next, get the Template */
			Template t = ve.getTemplate(templateName);
			/* create a context and add data */
			VelocityContext context = new VelocityContext();
			for (String key : data.keySet()) {
				context.put(key, data.get(key));
			}

			/* now render the template into a StringWriter */
			t.merge(context, writer);
			results = writer.toString();
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return results;
	}

}
