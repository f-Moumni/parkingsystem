package com.parkit.parkingsystem.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.altindag.log.LogCaptor;
@ExtendWith(MockitoExtension.class)
class DataBaseConfigTest {
	DataBaseConfig dataBaseConfig;
	private static LogCaptor logCaptor;

	@Mock
	Connection connection;
	@Mock
	PreparedStatement preparedStatement;
	@Mock
	ResultSet resultSet;

	@BeforeEach
	private void setUpTest() throws ClassNotFoundException, SQLException {
		dataBaseConfig = new DataBaseConfig();
		logCaptor = LogCaptor.forName("DataBaseConfig");
		logCaptor.setLogLevelToInfo();
		// when(dataBaseConfig.getConnection()).thenReturn(con);
	}

	@Test
	void closeConnection_withExpetion_shouldlogError() throws SQLException {
		doThrow(SQLException.class).when(connection).close();
		dataBaseConfig.closeConnection(connection);
		assertThat(logCaptor.getErrorLogs())
				.contains("Error while closing connection");
		assertThat(connection.isClosed()).isFalse();

	}
	@Test
	void closePreparedStatement_withExpetion_shouldlogError()
			throws SQLException {
		doThrow(SQLException.class).when(preparedStatement).close();
		dataBaseConfig.closePreparedStatement(preparedStatement);
		assertThat(logCaptor.getErrorLogs())
				.contains("Error while closing prepared statement");
		assertThat(connection.isClosed()).isFalse();
	}
	@Test
	void closeResultSet_withExpetion_shouldlogError() throws SQLException {
		doThrow(SQLException.class).when(resultSet).close();
		dataBaseConfig.closeResultSet(resultSet);
		assertThat(logCaptor.getErrorLogs())
				.contains("Error while closing result set");
		assertThat(connection.isClosed()).isFalse();
	}

}
