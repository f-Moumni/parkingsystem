package com.parkit.parkingsystem.util;

public class InputReaderUtilTest {
	// InputReaderUtil inputReaderUtil;
	// private static LogCaptor logCaptor;
	// InputStream stream;
	//
	// @BeforeAll
	// static void setUp() {
	// // logCaptor.resetLogLevel();
	// }
	// @BeforeEach
	// private void setUpPerTest() {
	// // inputReaderUtil = new InputReaderUtil();
	// logCaptor = LogCaptor.forName("InputReaderUtil");
	// logCaptor.setLogLevelToInfo();
	//
	// }
	//
	// @Test
	// void readSelectionTest_withValidInput() throws IOException {
	// // Given
	// // System.setIn(System.in);
	// String in = "1";
	//
	// stream = new ByteArrayInputStream(in.getBytes());
	// System.setIn(stream);
	// inputReaderUtil = new InputReaderUtil();
	// System.in.read(in.getBytes());
	// // When
	// inputReaderUtil.readSelection();
	// // then
	//
	// assertThat(inputReaderUtil.readSelection()).isEqualTo(1);
	// System.setIn(System.in);
	// stream.close();
	// // String data = "1";
	// // InputStream stdin = System.in;
	// // try {
	// // System.setIn(new ByteArrayInputStream(data.getBytes()));
	// // Scanner scanner = new Scanner(System.in);
	// // System.in.read(scanner.nextLine());
	// // } finally {
	// // System.setIn(stdin);
	// // }
	//
	// }
	// // @Test
	// void readSelectionTest_WithInvalidInput() throws IOException {
	//
	// String input = "p";
	// stream = new ByteArrayInputStream(input.getBytes());
	// System.setIn(stream);
	// inputReaderUtil = new InputReaderUtil();
	//
	// logCaptor = LogCaptor.forName("InputReaderUtil");
	//
	// logCaptor.setLogLevelToInfo();
	// // assertThat(inputReaderUtil.readSelection()).isEqualTo(-1);
	// assertThat(logCaptor.getErrorLogs())
	// .contains("Error while reading user input from Shell");
	// System.setIn(System.in);
	// stream.close();
	// logCaptor.clearLogs();
	// }

}
