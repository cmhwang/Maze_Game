package gui;

import java.util.logging.Logger;
import gui.Constants;


/**
 * This class has the responsibility to create a thread to switch on and off the unreliable sensors 
 * The UnreliableThread implements Runnable such that it can be run a separate thread.
 * The UnreliableSensor has a UnreliableThread and handles the on and off repair switch.   
 * 
 * author: Cheyenne Hwang
 */
public class UnreliableThread implements Runnable {
	/**
	 * The logger is used to track execution and report issues.
	 */
	private static final Logger LOGGER = Logger.getLogger(UnreliableSensor.class.getName());
	
	// constants
	static final int TIME_BETWEEN_FAILURES = 4000 ; // amount of time in seconds unreliable sensor is on and not in repair
	static final int TIME_TO_REPAIR = 2000; // time in seconds unreliable sensor is off and being repaired
	
	UnreliableSensor sensor;
	/**
	 * Constructor for a thread for unreliable sensor
	 */
	public UnreliableThread(){
	}
	
	/**
	 * this setter method assigns the unreliable sensor thread to a specific sensor
	 * @param badSensor is the unreliable sensor this thread is attached to
	 */
	public void setUnreliableSensor(UnreliableSensor badSensor) {
		// setter method assigns the unreliable sensor thread to a specific sensor and takes unreliable sensor as a param
		// how it works: instantiates the instance var for the sensor
		sensor = badSensor;
	}
	
	/**
	 * Main method to run construction of a new maze in a thread of its own.
	 * This method is called by the MazeFactory to generate a maze.
	 */
	public void run() {
		while (sensor.working) {
			sensor.downForRepair = true;
			try {
				Thread.sleep(TIME_TO_REPAIR);
				
				sensor.downForRepair = false;
				Thread.sleep(TIME_BETWEEN_FAILURES);
				sensor.downForRepair = true;
			} catch (Exception e) {
				
				e.printStackTrace();
			}		
		}
		
	}
	
	/**
	 * Reset all fields to initial values
	 */
	private void reset() {
		sensor = null;
	}
}