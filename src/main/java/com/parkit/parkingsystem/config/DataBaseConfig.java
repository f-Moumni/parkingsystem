package com.parkit.parkingsystem.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/*
 * DataBaseConfig allows to manage the connection to the database
  * 
 */
public class DataBaseConfig {
	/**
	 * LOGGER initialized to send console message.
	 */
	private static final Logger LOGGER = LogManager.getLogger("DataBaseConfig");

	/**
	 * Create a connection to MySQL database.
	 *
	 * @return a Connection instance
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection getConnection()
			throws ClassNotFoundException, SQLException, IOException {
		LOGGER.info("Create DB connection");
		Properties props = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(
					"../parkingsystem/resources/db.properties");
			props.load(in);
			String driver = props.getProperty("jdbc.driver");
			if (driver != null) {
				Class.forName(driver);
			}
		} catch (Exception e) {
			LOGGER.error("Error while getting connection", e);
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return DriverManager.getConnection(props.getProperty("jdbc.url"),
				props.getProperty("jdbc.username"),
				props.getProperty("jdbc.password"));
	}

	/**
	 * Close the given Connection.
	 *
	 * @param con
	 *            the Connection to close
	 */
	public void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
				LOGGER.info("Closing DB connection");
			} catch (SQLException e) {
				LOGGER.error("Error while closing connection", e);
			}
		}
	}
	/**
	 * Close the given PreparedStatement.
	 *
	 * @param ps
	 *            the PreparedStatement to close
	 */
	public void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
				LOGGER.info("Closing Prepared Statement");
			} catch (SQLException e) {
				LOGGER.error("Error while closing prepared statement", e);
			}
		}
	}
	/**
	 * Close the given ResultSet.
	 *
	 * @param rs
	 *            the ResultSet to close
	 */
	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				LOGGER.info("Closing Result Set");
			} catch (SQLException e) {
				LOGGER.error("Error while closing result set", e);
			}
		}
	}
}
