package com.parkingsystem.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
@ExtendWith(MockitoExtension.class)
class ParkingSpotDAOTest {
	@Mock
	private static DataBaseConfig dataBaseConfig;

	ParkingSpotDAO parkingSpotDAO;

	private Connection connection;
	@Mock
	private PreparedStatement preparedStatement;
	private ResultSet rs;

	@BeforeEach
	public void setUp() throws ClassNotFoundException, SQLException {
		parkingSpotDAO = new ParkingSpotDAO();
		connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/prod", "root", "rootroot");

	}

	@Test
	void getNextAvailableSlot_ForCar_ShouldRetrunParkingNumber()
			throws ClassNotFoundException, SQLException {
		ParkingType parkingType = ParkingType.CAR;
		// when(dataBaseConfig.getConnection()).thenReturn(connection);
		// when(connection.prepareStatement(any(String.class)))
		// .thenReturn(any(PreparedStatement.class));
		// when(preparedStatement.executeQuery()).thenReturn(any());
		// when
		int result = parkingSpotDAO.getNextAvailableSlot(parkingType);// the
																		// parking
																		// numbers
																		// for
																		// car
																		// are
																		// 1,2
																		// and 3
		assertThat(result).isBetween(1, 3);
		//verify(dataBaseConfig, times(1)).getConnection();
		// verify(connection, times(1)).prepareStatement(any(String.class));
		// verify(preparedStatement, times(1)).executeQuery();
		//verify(dataBaseConfig, times(1)).closeConnection(any());
	}
	@Test
	void getNextAvailableSlot_ForBike_ShouldRetrunParkingNumber() {
		ParkingType parkingType = ParkingType.BIKE;
		int result = parkingSpotDAO.getNextAvailableSlot(parkingType);// the
		// parking
		// numbers for bike
		// are
		// 4 and 5
		assertThat(result).isBetween(4, 5);
	}
	// @Test
	void getNextAvailableSlot_ShouldthrowException() {

		int result = parkingSpotDAO.getNextAvailableSlot(null);//
		assertThat(result).isBetween(4, 5);
	}

	@Test
	void updateParkingTest() {
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);

		assertTrue(parkingSpotDAO.updateParking(parkingSpot));
	}

	@Test
	void vehicleIsInParking() {
		String vehicleRegNumber = "ABCD";
		assertTrue(parkingSpotDAO.vehicleIsInParking(vehicleRegNumber));
	}
}
