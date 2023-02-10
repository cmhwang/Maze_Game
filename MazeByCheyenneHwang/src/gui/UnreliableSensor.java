package gui;

import generation.CardinalDirection;
import generation.Maze;
import gui.Robot.Direction;
import static org.junit.Assert.assertTrue;

/**
 * This is a distance sensor class
 * 
 * interacts with a a robot class and the maze class
 * interaction with robot class: robot uses this class to sense its distance to the nearest wall
 * Robot gives it a direction and it needs to give the correct distance otherwise the robot will use false information and crash into a wall
 * This specific sensor class is utilized by UnreliableRobot robot types
 * interaction with maze class: uses the floorplan object of the maze class and related methods to sense distances from the robot to the wall
 * 
 * its sole responsibility is to measure the distance from the robot to a wall in a given direction
 * key element: needs to be able to take in a robot direction, convert it to a cardinal direction, then calculate
 * This is a variant of the ReliableSensor class but with added mechanism that it can also self repair if downed
 * key difference from reliablesensor class is that it has a thread that makes it temporarily go offline for repairs 
 * works with unreliablerobot only
 * 
 * @author Cheyenne Hwang
 */

public class UnreliableSensor implements DistanceSensor {
	
	public Direction relativeDirection; // currentDirection specifies the direction of the robot
	public Maze maze;
	public boolean working;
	
	public boolean downForRepair;
	public int meanTimeToRepair;
	public int meanTimeBetweenFailures;
	
	public String sensorType = "unreliable";
	
	public Thread repairThread;
//	private UnreliableThread unreliableThread;
	
	/**
	 * default constructor
	 */
	public UnreliableSensor() {
		//default constructor
		//consider initializing with empty/default vars
		working = true;
		downForRepair = false;
		
		sensorType = "unreliable";
		
		
//		try {
//			startFailureAndRepairProcess(4, 2);
//		} catch (Exception e){
//			e.printStackTrace();
//		}
		
	}
	
	/**
	 * interacts with maze object to return the distance from the robot to the nearest wallboard in the currentDirection
	 * @return int var is an int that represents distance to wall
	 * @param currentPosition is the current location as (x,y) coordinates
	 * @param currentDirection specifies the direction of the robot
	 * @param powersupply is an array of length 1, whose content is modified 
	 * throws exceptions when: powersupply runs out, sensor down, parameters wrong
	 */
	@Override
	public int distanceToObstacle(int[] position, CardinalDirection direction, float[] energysupply)
			throws Exception {
		// interacts with maze object to return the distance from the robot to the nearest wallboard in the currentDirection
		// uses maze method of has wall to find distance
		// do this by traversing the direction, counting steps, checking each time if wall
		// return var is an int represents distance to wall
		// parameters: the instance variables for the class object
		// throws exceptions when: powersupply runs out, sensor down, parameters wrong
		// assert statement as sanity check that final direction option is east 
		
		if (position == null || direction == null || energysupply == null) {
			working = false;
			throw new IllegalArgumentException();
		} else if (energysupply[0] <= (float)0) {
			working = false;
			throw new Exception("Power Failure\n");
		} else if (position[0] < 0 || position[1] < 0 || position[0] >= maze.getWidth() || position[1] >= maze.getHeight()) {
			working = false;
			throw new IllegalArgumentException();
		} else if(downForRepair){
			throw new Exception("Sensor Failure\n");
		} else {
			int toReturn = 0;
			int[] checkPos = position;
			boolean distanceFound = false;
			boolean nearestWallIsBorder = false;
			
			if (position[0] == maze.getExitPosition()[0] && position[1] == maze.getExitPosition()[1]) {// for if at exit
				if (!(maze.hasWall(position[0], position[1], direction))) {
					if (direction == CardinalDirection.North && checkPos[1] == 0) {
						return Integer.MAX_VALUE;
					} else if (direction == CardinalDirection.West && checkPos[0] == 0) {
						return Integer.MAX_VALUE;
					} else if (direction == CardinalDirection.South && checkPos[1] == maze.getHeight() - 1) {
						return Integer.MAX_VALUE;
					} else if (direction == CardinalDirection.East && checkPos[1] == maze.getWidth() - 1) { 
						return Integer.MAX_VALUE;
					}
				} 
			}
			if (direction == CardinalDirection.North) {
				while (!distanceFound) {
					if (maze.hasWall(checkPos[0], checkPos[1], direction)) {
						distanceFound = true;
						if(checkPos[1] == 0) {
							nearestWallIsBorder = true;
						}
					} else {
						toReturn = toReturn + 1;
						checkPos[1] = checkPos[1] - 1;
					}
				}
			} else if (direction == CardinalDirection.West) {
				while (!distanceFound) {
					if (maze.hasWall(checkPos[0], checkPos[1], direction)) {
						distanceFound = true;
						if(checkPos[0] == 0) {
							nearestWallIsBorder = true;
						}
					} else {
						toReturn = toReturn + 1;
						checkPos[0] = checkPos[0] - 1;
					}
				}
			} else if (direction == CardinalDirection.South) {
				while (!distanceFound) {
					if (maze.hasWall(checkPos[0], checkPos[1], direction)) {
						distanceFound = true;
						if(checkPos[1] == maze.getHeight() - 1) {
							nearestWallIsBorder = true;
						}
					} else {
						toReturn = toReturn + 1;
						checkPos[1] = checkPos[1] + 1;
					}
				}
			} else {
				assert direction == CardinalDirection.East : "No direction is correct here";
				while (!distanceFound) {
					if (maze.hasWall(checkPos[0], checkPos[1], direction)) {
						distanceFound = true;
						if(checkPos[0] == maze.getWidth() - 1) {
							nearestWallIsBorder = true;
						}
					} else {
						toReturn = toReturn + 1;
						checkPos[0] = checkPos[0] + 1;
					}
				}
			}
			
			return toReturn;
			
		}
			
		
		
	}
	
	/**
	 * gives the sensor the maze to work off of so that it can calculate distances - gives access to floorplan and wall info
	 * @param maze the maze for this game
	 * @throw exception if no maze of floorplan
	 */
	@Override
	public void setMaze(Maze m) {
		// gives the sensor the maze to work off of so that it can calculate distances - gives access to floorplan and wall info
		// takes a specific maze as parameter, sets it to the maze to work off of for sensor
		// throws exception if no maze of floorplan
		if (m == null || m.getFloorplan() == null) {
			throw new IllegalArgumentException();
		} else {
			maze = m;
		}
	}

	/**
	 * gives angle and relative direction that sensor wants to point at
	 * @param mountedDirection is the sensor's relative direction
	 * @throw throws exception if null
	 */
	@Override
	public void setSensorDirection(Direction mountedDirection) {//FEEL UNSURE ABOUT THIS
		// gives angle and relative direction that sensor wants to point at
		// takes the sensor's relative direction as the og establishing point
		// if direction left sets then points sensor 90 degree left, if direction right points 90 degree right
		// challenging aspect: translate direction here in this part, update cardinaldirection being worked with when sensor direction changed
		// how: work off of existing table and create if... then cases to translate
		// throws exception if null
		// assert statement as sanity check that final direction option is right 
		
		if (mountedDirection == null) {
			throw new IllegalArgumentException();
		} else if (mountedDirection == Direction.FORWARD){
			relativeDirection = Direction.FORWARD;
		} else if (mountedDirection == Direction.LEFT){
			relativeDirection = Direction.LEFT;
		} else if (mountedDirection == Direction.BACKWARD){
			relativeDirection = Direction.BACKWARD;
		} else {
			assert mountedDirection == Direction.RIGHT : "That was the last remaining mounted direction";
			relativeDirection = Direction.RIGHT;
		}
	}
	
	/**
	 * gives the amount of energy the sensor for calculating distance
	 * @return current energy level of robot as a float
	 */
	@Override
	public float getEnergyConsumptionForSensing() {
		// gives the amount of energy the sensor for calculating distance
		// amount is fixed so just gives int to return based on instructions
		// returns it as a float
		// later methods will use this to update energysupply car
		return 1.0f;
	}
	
	
	/**
	 * this action method starts a concurrent and independent failure and repair process that makes the sensor fail and repair itself
	 * result: creates alternating time periods of up time and down time
	 * @throws exception if not used since optional 
	 * @throws exception if method not supported
	 * @param meanTimeBetweenFailures is the mean time in seconds, must be greater than zero
	 * @param meanTimeToRepair is the mean time in seconds, must be greater than zero
	 */
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailuresInput, int meanTimeToRepairInput)
			throws UnsupportedOperationException {
		// this action method starts a concurrent and independent failure and repair process that makes the sensor fail and repair itself
		// result: creates alternating time periods of up time and down time
		// how up time functions: duration of a time period when the sensor is in operational state is characterized by a distribution whose mean value is given by parameter meanTimeBetweenFailures.
		// here mean time between failures set to 4
		// how down time functions: duration of a time period when the sensor is in repair and not operational is characterized by a distributionwhose mean value is given by parameter meanTimeToRepair.
		// here mean down time set to 2 secondes
		// throws exception if not used since optional, if method not supported
		// takes int parameter meanTimeBetweenFailures is the mean time in seconds, must be greater than zero - here auto set to 4 for project
		// takes int parameter meanTimeToRepair is the mean time in seconds, must be greater than zero - here auto set to 2 for project
		meanTimeBetweenFailures = meanTimeBetweenFailuresInput;
		meanTimeToRepair = meanTimeToRepairInput;
		
		UnreliableThread unreliableThread = new UnreliableThread();
		unreliableThread.setUnreliableSensor(this);
		repairThread = new Thread(unreliableThread);
		repairThread.start();
		

	}

	/**
	 * adjustor method that stops the failure and repair process
	 * leaves the sensor in an operational state.
	 * how it works: If called after starting switch process, this method will stop the process as soon as the sensor operational
	 * @throws exception it if method called with no running failure and repair process 
	 * @throws exception also if it is not used or supported by the robot/robot driver utilizing it
	 */
	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		// adjustor method that stops the failure and repair process
		// leaves the sensor in an operational state when the title screen is reached
		// how it works: If called after starting switch process, this method will stop the process as soon as the sensor operational
		// then when title/finish screen reached sets the sensor to operational
		// @throws exception it if method called with no running failure and repair process or if it is not used or supported by the robot/robot driver utilizing it
		
		if (repairThread == null || !repairThread.isAlive()) {
			throw new UnsupportedOperationException();
		} else {
			repairThread.interrupt();
			repairThread = null;
			downForRepair = false;
		}
	}
	
	
	/**
	 * gives current status of sensor, whether or not its down
	 * @return boolean to represent whether sensor is down
	 */
	@Override
	public boolean checkRepairStatus() {
		// gives current status of sensor repair based on instance var
		// returns it as a boolean
		return downForRepair;
	}
	
	/**
	 * Returns what the robot's type is based on a string
	 * Used to decide in state playing whether to start the thread process
	 * @return a string unreliable or reliable to represent the type of robot 
	 */
	@Override
	public String getSensorType() {
		//Returns what the sensort's type is based on string instance var robotType
		//Used to decide in state playing whether to start the thread process
		//how it works: return a string unreliable or reliable to represent the type of robot this is, this is instantiated in the contructor
		// for this class should return the string "unreliable"
		
		return sensorType; 
	}
	
	/**
	 * Returns what the robot's relative direction is based on instance var
	 * @return a the sensor's relative direction 
	 */
	@Override
	public Direction getSensorRelativeDirection() {
		//Returns what the robot's relative direction is based on instance var
		//assistant method sprinkled throughout
		//how it works: return a relative direction which the sensor should be facing, this is instantiated in the contructor
		
		return relativeDirection;
	}
}
