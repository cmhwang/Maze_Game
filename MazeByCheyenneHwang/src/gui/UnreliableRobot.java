package gui;

import static org.junit.Assert.assertTrue;

import generation.CardinalDirection;
import gui.Constants.UserInput;
import gui.Robot.Direction;

/**
 * interacts with a driver algorithm, distance sensor class, and the game controller class
 * interaction with control: the robot is the object that actually works with the control class to move around
 * interaction with driver algorithms (wizard, wall-follower, etc): robot's decisions controlled by this algorithm, the robot is the body for the driver that acts as a brain
 * interaction with distance sensor class: uses distance sensor to avoid crashing into walls, if a wall is crashed into the game is over
 * This specific robot is like ReliableRobot but it has handling features for unreliable sensors
 * 
 * has a sensor feature that utilizes the distance sensor
 * can sense directions, whether its in a room, if its in an exit position
 * UnreliableRobot can use unreliable sensors or reliable sensors
 * 
 * can move through actuators
 * can move forward, turn, or jump - all of which take energy
 * 
 * has an energy variable/element that decreases with any movement or sensing process
 * if it runs out of energy the game is over and the robot loses
 * 
 * @author Cheyenne Hwang
 *
 */

public class UnreliableRobot implements Robot {
	
	public Control controller;
	public float energyLevel;
	public int odometerReading;
	public boolean isDead;
	
	
	public DistanceSensor forwardSensor;
	public DistanceSensor leftSensor;
	public DistanceSensor rightSensor;
	public DistanceSensor backSensor;
	
	public String robotType = "unreliable";

	/**
	 * default constructor
	 * blank instantation
	 */
	public UnreliableRobot() {
		//default constructor
		//sets up the starting vals for some vals, leaves the other null
		isDead = false;
		energyLevel = (float) 3500;
		odometerReading = 0;
		
		robotType = "unreliable";

	}
	
	/**
	 * part of class coordination: specifies to the robot what control object to work with
	 * @throw exceptions if controller no, wrong playing state, or if there is no maze for the controller
	 * @param c is the controller object robot will use
	 */
	@Override
	public void setController(Control c) {
		// part of class coordination: specifies to the robot what control object to work with
		// how: intializes the instance variable for the class
		// will use the controller to get positional information, wall information, and exit information
		// throws exceptions if controller no, wrong playing state, or if there is no maze for the controller
		if (c == null) {
			throw new IllegalArgumentException();
		} else if (!(c.currentState.equals((StatePlaying)c.currentState))) { //HOW TO CHECK WHAT STATE
			throw new IllegalArgumentException();
		} else if (c.getMaze() == null) {
			throw new IllegalArgumentException();
		} else {
			controller = c;
		}

	}
	
	/**
	 * adjusting method to add actual distance sensor to the robot so that it can measure distance to wall in the given direction
	 * used for initial setup of robot to get ready for drive
	 * @param sensor is distancesensor to be added and mounted direction is the relative direction
	 */
	@Override
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		// adjusting method to add actual distance sensor to the robot so that it can measure distance to wall in the given direction
		// used for initial setup of robot to get ready for drive
		// how it works: adds distance sensor object that interacts with maze and floorplan aspect with the given direction to calculate wall distance
		// will always have 2 distance sensors for forward, left - if turn made will use this method to add right or back
		// ex: if facing forward wall will use distanceToObstacle method to return the right distance
		// how it works in code: will check if already mounted (will override) or will add in that direction
		// parameters: takes the sensor to be added and the relative direction
		// CONSIDER: actually do this action somewhere in this class not in the generation part so that I can test
		// has assert statement on to do sanity check that final direction option is actually right
		
		if (sensor == null || mountedDirection == null) {
			throw new IllegalArgumentException();
		} else if (mountedDirection == Direction.FORWARD) {
			forwardSensor = sensor;
		} else if (mountedDirection == Direction.LEFT) {
			leftSensor = sensor;
		} else if (mountedDirection == Direction.BACKWARD) {
			backSensor = sensor;
		} else {
			assert mountedDirection == Direction.RIGHT : "This is the last remaning direction";
			rightSensor = sensor;
		}

	}
	
	/**
	 * info accessor method that gives the current position of the robot in x,y coordinates in the same format as the maze/floorplan
	 *  @return: the array of length 2 that represents the current position within the maze
	 *  @throw exception if position is not within maze or some kind of robot failure
	 */
	@Override
	public int[] getCurrentPosition() throws Exception {
		// info accessor method that gives the current position of the robot in x,y coordinates in the same format as the maze/floorplan
		// how it works: will pull from the robot's instance variable for position
		// returns the array of length 2 that represents the current position within the maze
		// will throw exception if position is not within maze or some kind of robot failure
		
		int[] toReturn = controller.getCurrentPosition();
		
		if (!((toReturn[0] >= 0) && (toReturn[0] < controller.getMaze().getWidth()) && (toReturn[1] >= 0) && (toReturn[1] < controller.getMaze().getHeight()))) {
			throw new Exception();
		} else {
			return toReturn;
		}
		
	}
	
	/**
	 * info accessor method that gives the current direction the robot is facing (not relative but cardinal)
	 * @return: the cardinal direction the robot is facing
	 * having this helper method will speed up process of translating to relative direction
	 */
	@Override
	public CardinalDirection getCurrentDirection() {
		// info accessor method that gives the current direction the robot is facing (not relative but cardinal)
		// how it works: will pull from the robot's instance variable for current direction faced
		// returns the cardinal direction the robot is facing
		// having this helper method will speed up process of translating to relative direction
		
		return controller.getCurrentDirection();
	}
	
	
	/**
	 * info accessor method that gives the current battery/energy level of the robot
	 * @return if battery > 0 then it will return energy level as a float or if battery is 0 or below 0 will call hasStopped and set it to true
	 */
	@Override
	public float getBatteryLevel() {
		// info accessor method that gives the current battery/energy level of the robot
		// how it works: will pull from the robot's instance variable for energy level, 
		// returns: if battery > 0 then it will return energy level as a float
		// or if battery is 0 or below 0 will call hasStopped and set it to true
		if (energyLevel <= 0) {
			isDead = true;
			
		}
		return energyLevel;	
	}
	
	
	/**
	 * variable adjustor method that will change the battery level of the robot to the paramter
	 * helper method that takes the energy level bot should be at after action called as input, used by other methods to adjust
	 * if battery falls below 0 then stops robot by setting hasStopped() to true
	 * @param level is a float representing what the level should be
	 * @throw exception if level negative
	 */
	@Override
	public void setBatteryLevel(float level) {
		// variable adjustor method that will change the battery level of the robot to the paramter
		// helper method that takes the energy level bot should be at after action called as input, used by other methods to adjust
		// how it works: will change the robot's instance var for energy level
		// if battery falls below 0 then stops robot by setting hasStopped() to true
		// parameter: a float representing what the level should be
		// throws exception if level negative
		
		if (level < 0) {
			throw new IllegalArgumentException();
		}else {
			energyLevel = level;
			if (energyLevel <= 0) {
				isDead = true;
			}
		}
		
		
	}
	
	
	/**
	 * info accessor method for getting the amount of energy used by the robot after a full rotation
	 * will be called when rotation needed: this return var will be divided by 2, 3, or 4 depending on how much rotation needed in another methods
	 * @return a float that represents the energy used for a full rotation
	 */
	@Override
	public float getEnergyForFullRotation() {
		// info accessor method for getting the amount of energy used by the robot after a full rotation
		// how it works: calculates the energy for full rotation
		// will be called when rotation needed: this return var will be divided by 2, 3, or 4 depending on how much rotation needed in another methods
		// returns a float that represents the energy used for a full rotation
		
		return (float) 12;
	}
	
	
	/**
	 * info accessing method that gives the energy consumed for moving forward one step
	 * how it will be called on: for the drive method or drive1step, this will be called, multiplied, and utilized to represent the energy used by that drive (in addition to other energy used for other functions)
	 * @return a float to represent energy for just a step forward
	 */
	@Override
	public float getEnergyForStepForward() {
		// info accessing method that gives the energy consumed for moving forward one step
		// how it works: just returns energy amount needed for one movement forward
		// how it will be called on: for the drive method or drive1step, this will be called, multiplied, and utilized to represent the energy used by that drive (in addition to other energy used for other functions)
		// returns a float to represent energy for just a step forward
		
		return (float) 6;
	}
	
	
	/**
	 *  info accessing method that gives the the distance traveled by the robot - acts as an odomoter
	 *  @return the distance traveled measured in cells traversed
	 */
	@Override
	public int getOdometerReading() {
		// info accessing method that gives the the distance traveled by the robot - acts as an odomoter
		// how it works: when robot moves forward distance moved changes, will calculate based on input of a drive's path, will adjust the instance var for odometer
		// how it will be called: driver methods will call on these to calculate the distance moved from that path, will gelp the energy level calculator methods
		// can be reset with other method
		// returns the distance traveled measured in cells traversed
		
		return odometerReading;
	}
	
	
	/**
	 * info adjustor method that resets odometer reading to 0
	 * how it will be called: needed at start of game
	 */
	@Override
	public void resetOdometer() {
		// info adjustor method that resets odometer reading to 0
		// how it works: adjusts the instance var pack to 0
		// how it will be called: needed at start of game
		
		odometerReading = 0;
	}
	
	 
	/**
	 * adjustor method that turns the robot on spot for amount of degrees
	 * @param turn is the direction to turn towards relative to fromt
	 * will stop if out of energy - how it will work is that it will check hasStopped() to see if enough energy to do
	 */
	@Override
	public void rotate(Turn turn) {
		// adjustor method that turns the robot on spot for amount of degrees
		// parameter: takes in the direction to turn towards relative to fromt
		// how it will work: changes the direction faced instance var
		// tricky part: needs to translate the relative and the cardinal for this to work
		// reminder: update energy level here
		// will stop if out of energy - how it will work is that it will check hasStopped() to see if enough energy to do
		// has assert statement on to do sanity check that final distance option is actually east
		
		
		if (turn == Turn.LEFT) {
			energyLevel = energyLevel - (getEnergyForFullRotation()/(float)3);
			
			if (energyLevel <= 0) {
				isDead = true;
			} else {
				controller.handleKeyboardInput(UserInput.LEFT, 0);
			}		
			
		} else if (turn == Turn.RIGHT) {
			energyLevel = energyLevel - (getEnergyForFullRotation()/(float)3);
			
			if (energyLevel <= 0) {
				isDead = true;
			} else {
				controller.handleKeyboardInput(UserInput.RIGHT, 0);
			}
			
		} else {
			assert turn == Turn.AROUND : "Last remaining turn option";
			energyLevel = energyLevel - (getEnergyForFullRotation()/(float)2);
			
			if (energyLevel <= 0) {
				isDead = true;
			} else {
				controller.handleKeyboardInput(UserInput.LEFT, 0);
				controller.handleKeyboardInput(UserInput.LEFT, 0);
			}
			
		}

	}
	
	/**
	 * adjustor method that turns moves robot forward a given number of steps/cells
	 * special cases: if sensor down for repair then waits until repair over
	 * special cases: if energy runs out stops (does this through checking hasStopped or battery level
	 * special cases: if robot hits wall then adjusts has stopped to end game
	 * @param takes an int distance to move forward that amount, given by driver/distance sensor
	 * @throw exception if distance negative
	 */
	@Override
	public void move(int distance) {
		// adjustor method that turns moves robot forward a given number of steps/cells
		// how it works: calculates the position needed to get to after travelling that distance
		// added part for p4 to allow for repair processes to take place
		// needs to track: check energy level, whether wall hit
		// special cases: if energy runs out stops (does this through checking hasStopped or battery level
		// special cases: if robot hits wall then adjusts has stopped to end game
		// parameter: takes an int distance to move forward that amount, given by driver/distance sensor
		// throws exception if distance negative
		// has assert statement on to do sanity check that final distance option is actually east
		
		if (distance < 0) {
			throw new IllegalArgumentException();
		} else if (distance > 0) {
			try {
				if (forwardSensor.checkRepairStatus()) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				int distanceToWall = distanceToObstacle(Direction.FORWARD);
				if (distanceToWall == 0) {
					isDead = true;
				} else {
					energyLevel = energyLevel - 6;
					if (energyLevel <= 0) { // CHECK ON THIS
						isDead = true;
					} else {
						controller.handleKeyboardInput(UserInput.UP, 0); 
						odometerReading = odometerReading + 1;
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

	}
	
	
	/**
	 * adjustor method that makes robot move in a forward direction even if there is a wall in front
	 * functionally forces robot to jump it, distance is 1 step and direction is forward
	 * special case: stops if out of energy - do this by using hasStopped and energy level
	 * special case: if tries to jump border then stay in location and trigger has stopped
	 */
	@Override
	public void jump() {
		// adjustor method that makes robot move in a forward direction even if there is a wall in front
		// functionally forces robot to jump it, distance is 1 step and direction is forward
		// special case: stops if out of energy - do this by using hasStopped and energy level
		// special case: if tries to jump border then stay in location and trigger has stopped
		// how it works: just moves it forward one, will be called by move/driving methods to assist
		// has assert statement on to do sanity check that final distance option is actually east
		
		energyLevel = energyLevel - 40;
		if (energyLevel <= 0) { // case for if energy dies
			isDead = true;
		} else {
			try {
				int[] curPos = getCurrentPosition();
				
				
				if (curPos[1] == 0 && getCurrentDirection() == CardinalDirection.North) {// cases for if jump over border
					isDead = true;
				} else if (curPos[0] == 0 && getCurrentDirection() == CardinalDirection.West) {// cases for if jump over border
					isDead = true;
				} else if (curPos[1] == (controller.getMaze().getHeight() - 1) && getCurrentDirection() == CardinalDirection.South) {// cases for if jump over border
					isDead = true;
				} else if (curPos[0] == (controller.getMaze().getWidth() - 1) && getCurrentDirection() == CardinalDirection.East) {// cases for if jump over border
					isDead = true;
				} else {
					if (getCurrentDirection() == CardinalDirection.North) {
						controller.handleKeyboardInput(UserInput.UP, 0);
						odometerReading = odometerReading + 1;
						
					} else if (getCurrentDirection() == CardinalDirection.West) {
						controller.handleKeyboardInput(UserInput.UP, 0);
						odometerReading = odometerReading + 1;
						
					} else if (getCurrentDirection() == CardinalDirection.South) {
						controller.handleKeyboardInput(UserInput.UP, 0);
						odometerReading = odometerReading + 1;
						
					} else {
						assert getCurrentDirection() == CardinalDirection.East : "No direction is correct here";
						controller.handleKeyboardInput(UserInput.UP, 0);
						odometerReading = odometerReading + 1;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		

	}
	
	/**
	 * info retrieval method that tells if the current position is at maze
	 * note: doesn't guarantee right direction
	 * @return boolean true if robot is at exit, false otherwise
	 */
	@Override
	public boolean isAtExit() {
		// info retrieval method that tells if the current position is at maze
		// note: doesn't guarantee right direction
		// how it works; works with controller to examine floorplan and uses current position var to check if exit
		// how it will be called: will be called throughout actuator move and drive methods to see if can stop the drive
		// returns true if robot is at exit, false otherwise
		if (controller.getCurrentPosition() == controller.getMaze().getExitPosition()) {
			return true;
		} else {
			return false;
		}
		
	}
	
	
	/**
	 * info retrieval method that tells if the current position is in maze
	 * @return boolean true if robot is in room, false otherwise
	 */
	@Override
	public boolean isInsideRoom() {
		// info retrieval method that tells if the current position is in maze
		// how it works: works with controller var to examine floorplan and uses current position var to check if in room
		// returns true if robot is in room, false otherwise
		
		int[] position = controller.getCurrentPosition();
		
		if (controller.getMaze().isInRoom(position[0], position[1])) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * info retrieval method that tells if robot has stopped
	 * potential reasons for stop: no energy, hit wall
	 * @return boolean true if robot is stopped, false otherwise
	 */
	@Override
	public boolean hasStopped() {
		// info retrieval method that tells if robot has stopped
		// potential reasons for stop: no energ, hit wall
		// how it will be called: called by rotate or move methods to make sure can be executed anymore, pre-emptively stopped if no energy
		// how it works: accesses energy level instance var or access is dead var
		// returns true if robot is stopped, false otherwise
		// has assert statement on to do sanity check that final distance option is actually east
		if (isDead) {
			return true;
		}else {
			return false;
		}
	}
	
	
	/**
	 * into retrieval method that tells the distance to a wall in given input direction
	 * @param direction to check in relative way to forward direction
	 * note be careful with translating relative to a cardinal direction
	 * special case: gives max integer val if facing exit, calls on canSeeThroughExit()
	 * special case: distance sensor being used is down for repair, pause process during reapir
	 * @return int for the number of steps to obstacle
	 * @throw exception if no sensor in the direction asked for
	 */
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		// into retrieval method that tells the distance to a wall in given input direction
		// parameter: direction to check in relative way to forward direction
		// note be careful with translating relative to a cardinal direction
		// how it works: gives 0 if has wall in that direction of current cell, 1 if one cell away
		// added process to wait 2 seconds if needed sensor is down for repair
		// special case: gives max integer val if facing exit, calls on canSeeThroughExit()
		// how it works functionally: uses distances sensors to get this so it needs to add distance sensor to robot
		// returns the number of steps to obstacle
		// will throw exception if no sensor in the direction asked for
		// has assert statement on to do sanity check that final distance option is actually east
		
		int toReturn;
		
		float[] energyRep = new float[1]; 
		energyRep[0] = energyLevel;
		
		if (direction == Direction.FORWARD) {
			if (forwardSensor == null) {
				throw new UnsupportedOperationException();
			} else if (forwardSensor.checkRepairStatus()){
				throw new UnsupportedOperationException();
			}	else {
				
				if (forwardSensor.checkRepairStatus()) {// has process wait if sensor being repaired
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				try {
					if (canSeeThroughTheExitIntoEternity(Direction.FORWARD)) {
						toReturn = Integer.MAX_VALUE;
					} else {
						toReturn = forwardSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), energyRep);
					}
				} catch (Exception e) {
					e.printStackTrace();
					toReturn = -100;
				} 
			}
			
		} else if (direction == Direction.LEFT) {
			if (leftSensor == null) {
				throw new UnsupportedOperationException();
			} else if (leftSensor.checkRepairStatus()){		
				throw new UnsupportedOperationException();
				
			}else {
				if (leftSensor.checkRepairStatus()) {// has process wait if sensor being repaired
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				CardinalDirection inputCarDir; 
				if (getCurrentDirection() == CardinalDirection.North) {
					inputCarDir = CardinalDirection.East;
				} else if (getCurrentDirection() == CardinalDirection.West) {
					inputCarDir = CardinalDirection.North;
				} else if (getCurrentDirection() == CardinalDirection.South) {
					inputCarDir = CardinalDirection.West;
				} else {
					assert getCurrentDirection() == CardinalDirection.East : "An exit must exist here";
					inputCarDir = CardinalDirection.South;
				}
				
				try {
					if (canSeeThroughTheExitIntoEternity(Direction.LEFT)) {
						toReturn = Integer.MAX_VALUE;
					} else {
						toReturn = leftSensor.distanceToObstacle(getCurrentPosition(), inputCarDir, energyRep);
					}
				} catch (Exception e) {
					e.printStackTrace();
					toReturn = -100;
				} 	
			}
			
		} else if (direction == Direction.BACKWARD) {
			if (backSensor == null) {
				throw new UnsupportedOperationException();
			} else if (backSensor.checkRepairStatus()){
				throw new UnsupportedOperationException();
			} else {
				if (backSensor.checkRepairStatus()) {// has process wait if sensor being repaired
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				CardinalDirection inputCarDir;
				if (getCurrentDirection() == CardinalDirection.North) {
					inputCarDir = CardinalDirection.South;
				} else if (getCurrentDirection() == CardinalDirection.West) {
					inputCarDir = CardinalDirection.East;
				} else if (getCurrentDirection() == CardinalDirection.South) {
					inputCarDir = CardinalDirection.North;
				} else {
					assert getCurrentDirection() == CardinalDirection.East : "An exit must exist here";
					inputCarDir = CardinalDirection.East;
				}
				
				try {
					if (canSeeThroughTheExitIntoEternity(Direction.BACKWARD)) {
						toReturn = Integer.MAX_VALUE;
					} else {
						toReturn = backSensor.distanceToObstacle(getCurrentPosition(), inputCarDir, energyRep);
					}			
				} catch (Exception e) {
					e.printStackTrace();
					toReturn = -100;
				}
				
			}
			
		} else {
			if (rightSensor == null) {
				throw new UnsupportedOperationException();
			} else if (rightSensor.checkRepairStatus()){
				throw new UnsupportedOperationException();
			}else {
				if (rightSensor.checkRepairStatus()) {// has process wait if sensor being repaired
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				CardinalDirection inputCarDir;
				if (getCurrentDirection() == CardinalDirection.North) {
					inputCarDir = CardinalDirection.West;
				} else if (getCurrentDirection() == CardinalDirection.West) {
					inputCarDir = CardinalDirection.South;
				} else if (getCurrentDirection() == CardinalDirection.South) {
					inputCarDir = CardinalDirection.East;
				} else {
					assert getCurrentDirection() == CardinalDirection.East : "An exit must exist here";
					inputCarDir = CardinalDirection.North;
				}
				
				try {
					if (canSeeThroughTheExitIntoEternity(Direction.RIGHT)) {
						toReturn = Integer.MAX_VALUE;
					} else {
						toReturn = rightSensor.distanceToObstacle(getCurrentPosition(), inputCarDir, energyRep);
					}
				} catch (Exception e) {
					e.printStackTrace();
					toReturn = -100;
				}
				
			}
		}
		
		if (toReturn == -100) {
			throw new UnsupportedOperationException();
		} else {
			energyLevel = energyRep[0];
			return toReturn;
		}
	}
	
	/**
	 * info retrival method that tells if a sensor can identify the exit in the direction relative to the robot's current forward direction from the current position.
	 * how it will be used; distanceToObstacle calls it to help out with seeing if it is facing an exit
	 * @param direction isthe direction to check relative to current direction - be careful if need direction translation
	 * @return a boolean true if exit is in line of sight or false otherwise
	 * @throw error if no sensor/not working
	 */
	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
		// info retrival method that tells if a sensor can identify the exit in the direction relative to the robot's current forward direction from the current position.
		// how it will be used; distanceToObstacle calls it to help out with seeing if it is facing an exit
		// parameters: the direction to check relative to current direction - be careful if need direction translation
		// returns a boolean true if ecit is in line of sight or false otherwise
		// will throw error if no sensor/not working
		// has assert statement on to do sanity check that final distance option is actually east
		
		boolean toReturn = false;
		
		float[] energyRep = new float[1];
		energyRep[0] = energyLevel;
		
		if (direction == Direction.FORWARD) {
			if (forwardSensor == null) {
				throw new UnsupportedOperationException();
				
			}else {
				if (forwardSensor.checkRepairStatus()){//pauses if doen for repair
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				try {
					if (forwardSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), energyRep) == Integer.MAX_VALUE) {
						energyLevel = energyRep[0];
						toReturn = true;
					} 
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			
		} else if (direction == Direction.LEFT) {
			if (leftSensor == null) {
				throw new UnsupportedOperationException();
				
			} else {
				if (leftSensor.checkRepairStatus()){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				CardinalDirection inputCarDir; 
				if (getCurrentDirection() == CardinalDirection.North) {
					inputCarDir = CardinalDirection.East;
				} else if (getCurrentDirection() == CardinalDirection.West) {
					inputCarDir = CardinalDirection.North;
				} else if (getCurrentDirection() == CardinalDirection.South) {
					inputCarDir = CardinalDirection.West;
				} else {
					assert getCurrentDirection() == CardinalDirection.East : "An exit must exist here";
					inputCarDir = CardinalDirection.South;
				}
				
				try {
					if (leftSensor.distanceToObstacle(getCurrentPosition(), inputCarDir, energyRep) == Integer.MAX_VALUE) {
						energyLevel = energyRep[0];
						toReturn = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 	
			}
			
		} else if (direction == Direction.BACKWARD) {
			if (backSensor == null) {
				throw new UnsupportedOperationException();
			} else {
				if (backSensor.checkRepairStatus()){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				CardinalDirection inputCarDir;
				if (getCurrentDirection() == CardinalDirection.North) {
					inputCarDir = CardinalDirection.South;
				} else if (getCurrentDirection() == CardinalDirection.West) {
					inputCarDir = CardinalDirection.East;
				} else if (getCurrentDirection() == CardinalDirection.South) {
					inputCarDir = CardinalDirection.North;
				} else {
					assert getCurrentDirection() == CardinalDirection.East : "An exit must exist here";
					inputCarDir = CardinalDirection.West;
				}
				
				try {
					if (backSensor.distanceToObstacle(getCurrentPosition(), inputCarDir, energyRep) == Integer.MAX_VALUE) {
						energyLevel = energyRep[0];
						toReturn = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 	
				
			}
			
		} else {
			if (rightSensor == null) {
				throw new UnsupportedOperationException();
			} else {
				if (rightSensor.checkRepairStatus()){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				CardinalDirection inputCarDir;
				if (getCurrentDirection() == CardinalDirection.North) {
					inputCarDir = CardinalDirection.West;
				} else if (getCurrentDirection() == CardinalDirection.West) {
					inputCarDir = CardinalDirection.South;
				} else if (getCurrentDirection() == CardinalDirection.South) {
					inputCarDir = CardinalDirection.East;
				} else {
					assert getCurrentDirection() == CardinalDirection.East : "An exit must exist here";
					inputCarDir = CardinalDirection.North;
				}
				
				try {
					if (rightSensor.distanceToObstacle(getCurrentPosition(), inputCarDir, energyRep) == Integer.MAX_VALUE) {
						energyLevel = energyRep[0];
						toReturn = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 	
				
			}
		}
		
		return toReturn;
	}
	
	/**
	 * Starts a concurrent, independent failure and repair process that makes the sensor fail and repair itself.
	 * Creates alternating time periods of up time and down time.
	 * Up time: The duration of a time period when the sensor is in operational
	 * Down time: The duration of a time period when the sensor is in repair and not operational
	 * 
	 * @param direction the direction the sensor is mounted on the robot
	 * @param meanTimeBetweenFailures is the mean time in seconds, must be greater than zero
	 * @param meanTimeToRepair is the mean time in seconds, must be greater than zero
	 * @throws UnsupportedOperationException if method not supported
	 * @throws also throws exception if not implemented since optional
	 */
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// Starts a concurrent, independent failure and repair process that makes the sensor fail and repair itself.
		// Creates alternating time periods of up time and down time.
		// How it does this: calls the specific startFailureAndRepair method for the specified distance sensor which then triggers its repair thread
		// Up time: The duration of a time period when the sensor is in operational
		// characterized by a distribution whose mean value is given by parameter meanTimeBetweenFailures (4 here)
		// Down time: The duration of a time period when the sensor is in repair and not operational
		// characterized by a distribution whose mean value is given by parameter meanTimeToRepair (2 here) 
		// takes a Direction paramter which is the direction the sensor is mounted on the robot
		// takes an int parameter meanTimeBetweenFailures is the mean time in seconds, which here is 4
		// takes an int parameter meanTimeToRepair is the mean time in seconds, which here is 2
		// UnsupportedOperationException if method not supported
		
		if (direction == null) {
			throw new UnsupportedOperationException();
		}
		
		if (direction.equals(Direction.FORWARD)) {
			try{
				forwardSensor.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			} catch (Exception e) {
				throw new UnsupportedOperationException();
			}
		} else if (direction.equals(Direction.LEFT)) {
			try{
				leftSensor.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			} catch (Exception e) {
				throw new UnsupportedOperationException();
			}
		} else if (direction.equals(Direction.BACKWARD)) {
			try{
				backSensor.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			} catch (Exception e) {
				throw new UnsupportedOperationException();
			}
		} else {
			assert direction.equals(Direction.RIGHT) : "Last remaining direction optino should be right";
			
			try{
				rightSensor.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			} catch (Exception e) {
				throw new UnsupportedOperationException();
			}
		}

	}
	
	/**
	 * Stops the failure and repair process, 
	 * leaves the sensor in an operational state
	 * must have a start repair process
	 * Stop the process as soon as the sensor is operational.
	 * @param direction the direction the sensor is mounted on the robot
	 * @throws UnsupportedOperationException if method not supported or there was no thread to start
	 */
	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		// Stops the failure and repair process, 
		// leaves the sensor in an operational state
		// must have a start repair process
		// Stop the process as soon as the sensor is operational.
		// how it works: identifies which distance sensor to invoke on with direction parameter, calls that unreliable sensor's stopFialure() method
		// Direction parameter direction the direction the sensor is mounted on the robot
		// throws UnsupportedOperationException if method not supported or there was no thread to start
		
		if (direction == null) {
			throw new UnsupportedOperationException(); 
		}
		
		if (direction.equals(Direction.FORWARD)) {
			try{
				forwardSensor.stopFailureAndRepairProcess();
			} catch (Exception e) {
				throw new UnsupportedOperationException();
			}
		} else if (direction.equals(Direction.LEFT)) {
			try{
				leftSensor.stopFailureAndRepairProcess();
			} catch (Exception e) {
				throw new UnsupportedOperationException();
			}
		} else if (direction.equals(Direction.BACKWARD)) {
			try{
				backSensor.stopFailureAndRepairProcess();
			} catch (Exception e) {
				throw new UnsupportedOperationException();
			}
		} else {
			assert direction.equals(Direction.RIGHT) : "Last remaining direction optino should be right";
			
			try{
				rightSensor.stopFailureAndRepairProcess();
			} catch (Exception e) {
				throw new UnsupportedOperationException();
			}
		}

	}
	
	/**
	 * Returns the reference to the specified distance sensor of the robot.
	 * @param sensorDirection  is the direction of the sensor that is being retrieved, helps specify which sensor wanted
	 * @return the specified distance sensor. 
	 */
	@Override
	public DistanceSensor getRobotSensor(Direction sensorDirection) {
		// method that returns reference to the specified distance sensor of the robot.
		// needed to help with the set up for the robot type in control method
		// how it works, depending on what the parameter direction is, returns the corresponding sensor for the robot
		// parameter sensorDirection  is the direction of the sensor that is being retrieved, helps specify which sensor wanted
		// returns the specified distance sensor
		if (sensorDirection.equals(Direction.FORWARD)) {
			return forwardSensor;
		} else if (sensorDirection.equals(Direction.LEFT)) {
			return leftSensor;
		} else if (sensorDirection.equals(Direction.BACKWARD)) {
			return backSensor;
		} else {
			assert sensorDirection.equals(Direction.RIGHT) : " right sensor is the last remaining direction option";
			return rightSensor;
		}
	}
	
	/**
	 * Returns what the robot's type is based on a string
	 * Used to decide in state playing whether to start the thread process
	 * @return a string unreliable or reliable to represent the type of robot 
	 */
	@Override
	public String getRobotType() {
		//Returns what the robot's type is based on string instance var robotType
		//Used to decide in state playing whether to start the thread process
		//how it works: return a string unreliable or reliable to represent the type of robot this is, this is instantiated in the contructor
		// for this class should return the string "unreliable"
		
		return robotType;
	}

}