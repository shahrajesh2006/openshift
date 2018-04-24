package com.loyalty.common.ilclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class ConfigReader {
	URL configurationPath;

	public ConfigReader() {

	}

	public URL getConfigurationPath() {
		return this.configurationPath;
	}

	public void setConfigurationPath(URL configurationPath) {
		this.configurationPath = configurationPath;
	}

	public List getOMAURLObjects(String webAppName, String environment) {
		return getURLObjects(webAppName, environment, "oma");
	}

	public List getIAgentURLObjects(String webAppName, String environment) {
		return getURLObjects(webAppName, environment, "adm");
	}

	private List getURLObjects(String webAppName, String environment, String app) {
		List urlObjects = new ArrayList();
		InputStream is = null;
		try {
			// Determine if running in passive mode
			boolean passiveMode = false;
			if ("TRUE".equalsIgnoreCase(System.getProperty("cxl.passive"))) {
				passiveMode = true;
			}
			is = this.getClass().getClassLoader().getResourceAsStream("ip-util-20.xml");
			Document doc = new SAXReader().read(is);
			final String expression = "/applications/application[@value='" + webAppName + "']/environment[@value='"
					+ environment.toLowerCase() + "']/urls/" + app + "/url";

			List urlNodes = doc.selectNodes(expression);
			for (Iterator i = urlNodes.iterator(); i.hasNext();) {
				Node urlNode = (Node) i.next();
				String urlValue = urlNode.selectSingleNode("@value") == null ? null
						: urlNode.selectSingleNode("@value").getText();
				String urlDisplay = urlNode.selectSingleNode("@display").getText();

				final String defaultParametersXpath = "../default-parameters/parameter";
				Map defaultParametersMap = new HashMap();
				List defaultParameters = urlNode.selectNodes(defaultParametersXpath);
				for (Iterator ii = defaultParameters.iterator(); ii.hasNext();) {
					Node parameter = (Node) ii.next();
					addParametersToMap(defaultParametersMap, parameter, passiveMode);
				}

				Map specificParametersMap = new HashMap();
				String specificParametersXpath = "parameter";
				List specificParameters = urlNode.selectNodes(specificParametersXpath);
				for (Iterator ii = specificParameters.iterator(); ii.hasNext();) {
					Node parameter = (Node) ii.next();
					addParametersToMap(specificParametersMap, parameter, passiveMode);
				}

				Map mergedParametersMap = new HashMap();
				mergedParametersMap.putAll(defaultParametersMap);
				mergedParametersMap.putAll(specificParametersMap);

				URLObject urlObject = new URLObject();
				urlObject.value = urlValue;
				urlObject.display = urlDisplay;
				urlObject.parameters = mergedParametersMap;
				urlObjects.add(urlObject);
			}
		} catch (DocumentException x) {
			throw new RuntimeException(x.getMessage(), x);
		} finally {
			try {
				is.close();
			} catch (IOException e) {

			}
		}

		return urlObjects;
	}

	/**
	 * Add values to the map
	 * 
	 * @param parameterMap
	 * @param parameter
	 * @param passiveMode
	 */
	private void addParametersToMap(Map parameterMap, Node parameter, boolean isPassiveMode) {
		// If passive attribute has a value and running in passive mode, use its
		// value rather than the value attribute
		String passiveValue = retrieveNodeIfNotEmpty(parameter.selectSingleNode("@passive"));
		String parameterValue = null;
		if (isPassiveMode && StringUtils.isNotBlank(passiveValue)) {
			parameterValue = passiveValue;
		} else {
			parameterValue = parameter.selectSingleNode("@value").getText();
		}
		String parameterName = parameter.selectSingleNode("@name").getText();
		parameterMap.put(parameterName, parameterValue);
	}

	private String retrieveNodeIfNotEmpty(Node node) {
		if (node != null) {
			return node.getText();
		}
		return null;
	}

	public static class URLObject {
		public String value;
		public String display;
		public String id;
		public Map parameters;

		public int hashCode() {
			return (value == null ? 0 : value.hashCode()) + display.hashCode() + parameters.hashCode()
					+ (id != null ? id.hashCode() : 0);
		}

		public String toString() {
			return "value: " + value + " display: " + display + " parameters: " + parameters + " id: " + id
					+ " hashCode: " + hashCode();
		}
	}

}
