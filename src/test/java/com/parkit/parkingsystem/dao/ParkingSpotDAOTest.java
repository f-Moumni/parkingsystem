package com.parkit.parkingsystem.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;

import nl.altindag.log.LogCaptor;
@ExtendWith(MockitoExtension.class)
class ParkingSpotDAOTest {

	private ParkingSpotDAO parkingSpotDAO;
	private static LogCaptor logCaptor;

	@Mock
	private static DataBaseTestConfig dataBaseTestConfig;

	@Mock
	private static PreparedStatement ps;
	@Mock
	private static ResultSet rs;
	@Mock
	private static Connection connection;
	@BeforeEach
	public void setUp()
			throws ClassNotFoundException, SQLException, IOException {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		logCaptor = LogCaptor.forName("ParkingSpotDAO");
		logCaptor.setLogLevelToInfo();
		when(dataBaseTestConfig.getConnection()).thenReturn(connection);
	}
	@Test
	void getNextAvailableSlot_ForCar_ShouldRetrunParkingNumber()
			throws Exception {
		when(connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT))
				.thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(rs.getInt(1)).thenReturn(2);

		assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR))
				.isEqualTo(2);
	}

	@Test
	void getNextAvailableSlot_ForBike_ShouldRetrunParkingNumber()
			throws Exception {
		when(connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT))
				.thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(rs.getInt(1)).thenReturn(4);

		assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE))
				.isEqualTo(4);
	}
	@Test
	void getNextAvailableSlot_WithDatabaseAccesError__ShouldLogError()
			throws SQLException {
		when(connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT))
				.thenReturn(ps);
		when(ps.executeQuery()).thenThrow(SQLException.class);

		int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
		assertThat(logCaptor.getErrorLogs())
				.contains("Error fetching next available slot");
		assertThat(result).isEqualTo(-1);

	}

	@Test
	void getNextAvailableSlot_WithFullParking_ShouldReturnMinusOne()
			throws SQLException {
		when(connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT))
				.thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(false);
		int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

		assertThat(result).isEqualTo(-1);

	}

	@Test
	void updateParking_withInvalidParkingNumber_ShouldReturnFlase()
			throws SQLException {
		ParkingSpot parkingSpot = new ParkingSpot(0, ParkingType.CAR, true);
		when(connection.prepareStatement(DBConstants.UPDATE_PARKING_SPOT))
				.thenReturn(ps);
		when(ps.executeUpdate()).thenReturn(0);

		assertFalse(parkingSpotDAO.updateParking(parkingSpot));

	}
	@Test
	void updateParking_withAvalidParkingSpot_ShouldReturnTrue()
			throws SQLException {
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, true);
		when(connection.prepareStatement(DBConstants.UPDATE_PARKING_SPOT))
				.thenReturn(ps);
		when(ps.executeUpdate()).thenReturn(1);

		assertTrue(parkingSpotDAO.updateParking(parkingSpot));
	}

	@Test
	void updateParking_withDatabaseAccesError_shouldReturnFalseAndLogError()
			throws SQLException {
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		when(connection.prepareStatement(DBConstants.UPDATE_PARKING_SPOT))
				.thenThrow(SQLException.class);
		// when(ps.executeUpdate()).thenThrow(SQLException.class);

		assertFalse(parkingSpotDAO.updateParking(parkingSpot));
		assertThat(logCaptor.getErrorLogs())
				.contains("Error updating parking info");

	}

	@Test
	void vehicleIsInParking_withvehicleIsInParking_shouldReturnFalse()
			throws SQLException {
		String vehicleRegNumber = "AAA";
		when(connection.prepareStatement(DBConstants.GET_VEHICLES_IN_PARKING))
				.thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(rs.getString(1)).thenReturn("AAA");
		assertTrue(parkingSpotDAO.vehicleIsInParking(vehicleRegNumber));
	}
	@Test
	void vehicleIsInParking_withvehicleNOTInParking_shouldReturnTrue()
			throws SQLException {
		String vehicleRegNumber = "ABCDE";
		when(connection.prepareStatement(DBConstants.GET_VEHICLES_IN_PARKING))
				.thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(rs.getString(1)).thenReturn(null);
		assertFalse(parkingSpotDAO.vehicleIsInParking(vehicleRegNumber));
	}
	@Test
	void vehicleIsInParking_withNullvehicleRegNumber_shouldReturnFalseAndLogError()
			throws Exception, SQLException {
		String vehicleRegNumber = null;
		when(connection.prepareStatement(DBConstants.GET_VEHICLES_IN_PARKING))
				.thenReturn(ps);
		doThrow(SQLException.class).when(ps).setString(1, vehicleRegNumber);
		assertFalse(parkingSpotDAO.vehicleIsInParking(vehicleRegNumber));
		assertThat(logCaptor.getErrorLogs())
				.contains("Error vehicule exit controlling");
	}
	@Test
	void vehicleIsInParking_withEmptyData_shouldReturnFalse()
			throws Exception, SQLException {
		String vehicleRegNumber = "ABCDE";
		when(connection.prepareStatement(DBConstants.GET_VEHICLES_IN_PARKING))
				.thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(false);

		assertFalse(parkingSpotDAO.vehicleIsInParking(vehicleRegNumber));

	}

}
