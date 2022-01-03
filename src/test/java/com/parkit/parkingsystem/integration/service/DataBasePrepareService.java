package com.parkit.parkingsystem.integration.service;

import java.sql.Connection;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

public class DataBasePrepareService {

	DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

	public void clearDataBaseEntries() {
		Connection connection = null;
		try {
			connection = dataBaseTestConfig.getConnection();

			// set parking entries to available
			connection.prepareStatement("update parking set available = true")
					.execute();

			// clear ticket entries;
			connection.prepareStatement("truncate table ticket").execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dataBaseTestConfig.closeConnection(connection);
		}
	}
	// public void changeTheInTime(String vehicleRegNumber,
	// LocalDateTime dateTime) {
	// Connection connection = null;
	// try {
	// connection = dataBaseTestConfig.getConnection();
	//
	// // change the entry date to an earlier date
	// PreparedStatement ps = connection.prepareStatement(
	// "update ticket set IN_TIME = ? where VEHICLE_REG_NUMBER = ? order by ID
	// DESC LIMIT 1");
	// ps.setTimestamp(1, Timestamp.valueOf(dateTime));
	// ps.setString(2, vehicleRegNumber);
	// ps.execute();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// dataBaseTestConfig.closeConnection(connection);
	// }
	// }

}
