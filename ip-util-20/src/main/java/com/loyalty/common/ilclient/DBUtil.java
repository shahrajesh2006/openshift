package com.loyalty.common.ilclient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import com.loyalty.web.application.Application;

/**
 * This class hands out database connections to other classes in iPro. Classes
 * should call the static method DBUtil.getConnection() passing a data source
 * name. The data source name should be a name bound in JNDI to a
 * javax.sql.DataSource object. The first time DBUtil is invoked it does a JNDI
 * lookup and returns a reference to the DataSource object associated with the
 * data source name passed to getConnection(). It caches this reference and
 * subsequent requests use the cached DataSource reference to produce Connection
 * objects. This saves unnecessary JNDI lookups for each request to
 * DBUtil.getConnection().
 * 
 * Additionally, should any error occur during the creation of a Connection a
 * RuntimeException will be thrown with a descriptive message specifying the
 * problem.
 * 
 * @author Ryan Shriver (11/8/01)
 * @author Chris Snyder (5/29/2001)
 */
@Service
public class DBUtil {
	final static Logger log = LoggerFactory.getLogger(DBUtil.class);
	private static volatile Map map = new HashMap(3); // Map of DataSource
														// objects
	private static volatile Context context = null; // Reference to
													// InitialContext
	private final static String DRIVER = "weblogic.jdbc.oci.Driver";

	@Autowired
	DataSource dataSource;
	/**
	 * Method to return a java.sql.Connection to the calling program.
	 * 
	 * @param String
	 *            dataSourceName Name DataSource is bound to under JNDI
	 * @return Connection JDBC Connection to database
	 */
	public Connection getConnection(String dataSourceName) {
		long startTime = System.currentTimeMillis();
		log.debug("DBUtil.getConnection() : Begin");
		if (dataSourceName == null || dataSourceName.equals("")) {
			handleError("getConnection()", "DataSourceName is null or empty string");
		}
		Connection connection = null;
		if ("true".equalsIgnoreCase(System.getProperty("junit"))) {
			connection = getMockConnection();
		} else {
			connection = getSQLConnection(dataSourceName);
		}
		log.info("Returning JDBC connection from DBUtil.getConnection() in " + (System.currentTimeMillis() - startTime)
				+ " ms");
		return connection;
	}

	/**
	 * Return a JDBC Connection without going through the container.
	 */
	private static Connection getMockConnection() {
		try {
			log.info("DBUtil.getMockConnection : Returning mock connection to the database");
			Class.forName(DRIVER);
			return DriverManager.getConnection("jdbc:weblogic:oracle:devl", "loyalty_owner", "lolagoes2web");
		} catch (ClassNotFoundException e) {
			handleError("getMockConnection", "Class not found : " + DRIVER, e);
		} catch (SQLException e) {
			handleError("getMockConnection", "Exception creating JDBC Connection", e);
		}
		return null;
	}

	/**
	 * Method to close a connection and set it to null
	 */
	public static void close(Connection connection) {
		if (connection == null) {
			return;
		}
		try {
			connection.close();
		} catch (SQLException e) {
			log.warn("Exception closing JDBC connection in " + "DBUtil.close(Connection connection) : " + e.toString());
		} finally {
			connection = null;
		}
	}

	/** Method to close a ResultSet and set it to null */
	public static void close(ResultSet rs) {
		if (rs == null) {
			return;
		}
		try {
			rs.close();
		} catch (SQLException e) {
			log.warn("Exception closing result set in " + "DBUtil.close(ResultSet resultset) : " + e.toString());
		} finally {
			rs = null;
		}
	}

	/** Method to close a Statment and set it to null */
	public static void close(Statement stmt) {
		if (stmt == null) {
			return;
		}
		try {
			stmt.close();
		} catch (SQLException e) {
			log.warn("Exception closing Statement in " + "DBUtil.close(Statement statement ) : " + e.toString());
		} finally {
			stmt = null;
		}
	}

	/**
	 * Cleans up after a JDBC query
	 *
	 * @param Connection
	 *            connnection
	 * @param ResultSet
	 *            rs
	 * @param Statement
	 *            stmt
	 */

	public void close(Connection conn, ResultSet rs, Statement stmt) {
		DBUtil.close(rs);
		DBUtil.close(stmt);
		DBUtil.close(conn);
	}

	/**
	 * Return true if a connection is closed, false if it's open. If error
	 * occurs, return true.
	 */
	public boolean isClosed(Connection connection) {
		if (connection == null) {
			return true;
		}
		try {
			return connection.isClosed();
		} catch (SQLException e) {
			return true;
		}
	}

	/**
	 * Method to retreive a java.sql.Connection from a DataSource using the
	 * dataSourceName.
	 * 
	 * @param String
	 *            dataSourceName Name DataSource is bound to under JNDI
	 * @return Connection JDBC Connection to database
	 */
	private Connection getSQLConnection(String dataSourceName) {
		Connection connection = null;
		try {
			connection = getDataSource(dataSourceName).getConnection();
		} catch (SQLException e) {
			handleError("getSQLConnection()", "Error getting connection from DataSource " + dataSourceName, e);
		}
		if (connection == null) {
			handleError("getSQLConnection()", "Connection returned from DataSource " + dataSourceName + " is null!");
		}
		if (isClosed(connection)) {

		}

		return connection;
	} // private static Connection getSQLConnection(DataSource dataSource)

	/**
	 * Returns a reference to the DataSource object for creating a Connection.
	 * The first time a DataSource is requested it's looked up using JNDI and
	 * stored in a local Map with the dataSourceName as the key. Subsequent
	 * requests return the reference from the Map.
	 * 
	 * @param String
	 *            dataSourceName Name DataSource is bound to under JNDI
	 * @return DataSource Reference to DataSource
	 */
	private DataSource getDataSource(String dataSourceName) {
		if (map.containsKey(dataSourceName)) {
			log.debug("DBUtil.getDataSource() : Returning reference to cached DataSource");
			return (DataSource) map.get(dataSourceName);
		} // if
		if(dataSource != null){
			return dataSource;
		}
		try {
			// Perform JNDI Lookup on dataSourceName
			Object object = getInitialContext().lookup(dataSourceName);
			// Use narrow to allow for CORBA and IIOP deployments
			dataSource = (DataSource) PortableRemoteObject.narrow(object, DataSource.class);
		} // try
		catch (NamingException e) {
			handleError("getDataSource()", "Error looking up DataSource = " + dataSourceName, e);
		} // catch
		if (dataSource == null) {
			handleError("getDataSource()", "DataSource object returned from lookup is null!");
		} // if
		log.info("DBUtil.getDataSource() : Caching DataSource reference under key " + dataSourceName);
		// Store reference to DataSource in Map using dataSourceName as key
		map.put(dataSourceName, dataSource);
		return dataSource;
	} // private static DataSource getDataSource(String dataSourceName)

	/**
	 * Method that returns a reference to the InitialContext. The first time
	 * this method is invoked the InitialContext is created and assigned to a
	 * class variable. On subsequent requests a reference to the class variable
	 * is returned.
	 * 
	 * @return Context The initial context
	 */
	public static Context getInitialContext() {
		if (context == null) {
			try {
				context = new InitialContext();
				log.info("DBUtil.getInitialContext() : Context Initialized!");
			} // try
			catch (NamingException e) {
				handleError("getInitialContext()", "Error getting initial context.", e);
			} // catch
			if (context == null) {
				handleError("getInitialContext()", "Context object returned from new InitialContext() is null!");
			} // if
		} // if
		return context;
	} // private static void initializeContext()

	/**
	 * Method for handling errors in this class. Logs the error and throws a
	 * RuntimeException back to the calling program.
	 * 
	 * @param String
	 *            method Name of the method where error occured
	 * @param String
	 *            message Message associated with error
	 * @param Exception
	 *            e Exception thrown by calling method
	 */
	private static void handleError(String method, String message, Exception e) {
		log.error("DBUtil." + method + " : " + message, e);
		throw new RuntimeException(message);
	} // private static void handleError(String message, Exception e)

	/**
	 * Method for handling errors in this class.
	 * 
	 * @param String
	 *            method Name of the method where error occured
	 * @param String
	 *            message Message associated with error
	 */
	private static void handleError(String method, String message) {
		handleError(method, message, null);
	} // private static viod handleError(String method, String message)

} // public class DBUtil