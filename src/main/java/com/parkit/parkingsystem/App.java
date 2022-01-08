package com.parkit.parkingsystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.service.InteractiveShell;
/**
 * 
 * @author utilisateur1
 *
 */
public class App {
	private static final Logger LOGGER = LogManager.getLogger("App");

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		LOGGER.info("Initializing Parking System");
		InteractiveShell.loadInterface();
	}
}
