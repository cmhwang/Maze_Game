package gui;

import generation.CardinalDirection;
import gui.Robot.Direction;
import gui.Robot.Turn;

/**
 * A SmartSensorStatus processes information on what sensors of a robot are available and decides how to move robot based on that.
 * performs the actual rotations or movements for the robot but is generally managed by the SmartWallFollower object
 * Needed for unreliable sensors but also compatible with unreliable sensors
 * 
 * Works with SmartWallFollower, a robot driver class and is equivalent to the SensorStatus class for WallFollower.
 * 
 * @author Cheyenne Hwang
 *
 */
public class SmartSensorStatus{
	
	public WallFollower driver;
	
	public boolean[] stateHolder;// false is for NOT in repair
	// 0 index holds front, 1 holds left, 2 holds right, 3 holds back
	
	public boolean frontStatus;
	public boolean leftStatus;
	public boolean rightStatus;
	public boolean backStatus;
	
	
	public Robot robot;
	/**
	 * generic constructor for the sensor status class
	 * @param robotDriver is the wall follower that is calling on this
	 * @param inputRobot the robot that this object represents
	 * @param holder status holder boolean array
	 */
	public SmartSensorStatus(WallFollower robotDriver, Robot inputRobot, boolean[] holder) {
		driver = robotDriver;
		robot = inputRobot;
		stateHolder = holder;
		
		frontStatus = holder[0];
		leftStatus = holder[1];
		rightStatus = holder[2];
		backStatus = holder[3];
	}
	
	/**
	 * performs the next set of movement or rotations based on the available sensor and the robot's placement
	 * @return a boolean to represent whether the needed operations were able to be executed
	 * @throws Exception if something wrong with the sensor states
	 */
	public boolean nextBasedOnSensorStatuses() throws Exception {
		// performs the next set of movement or rotations based on the available sensor and the robot's placement
		// first asses sensor capabilities then performs movements based on that 
		// returns a boolean to represent whether the needed operations were able to be executed
		// true is for completed false is for not
		// addition for smartWallfollower: if no wall left, checks to see if there is a right wall and follows that instead
		
		boolean toReturn;
		
		if (!frontStatus && !leftStatus && !rightStatus && !backStatus) {// all sensors working
			
			driver.energyUsed = driver.energyUsed + 1; // for sensing left
			if (robot.distanceToObstacle(Direction.LEFT) != 0) {// no wall left -
				
				driver.energyUsed = driver.energyUsed + 1;// for sensing right - added for smart wall follower
				if (robot.distanceToObstacle(Direction.RIGHT) == 0) { // if no wall left but one right
					
					robot.rotate(Turn.LEFT);
					driver.energyUsed = driver.energyUsed + 3;
					
					robot.move(1);
					driver.energyUsed = driver.energyUsed + 6;
					driver.cellsTravelled = driver.cellsTravelled + 1;
					toReturn = true;
					
				} else {
					if (robot.distanceToObstacle(Direction.FORWARD) != 0) { // no wall l, r, or f - just move forward
						driver.energyUsed = driver.energyUsed + 6;
						robot.move(1);
						driver.cellsTravelled = driver.cellsTravelled + 1;
						toReturn = true;
						
					} else {// no wall l, r but wall f try turn r which makes the f wall a left wall
						driver.energyUsed = driver.energyUsed + 3;
						
						int coinFlip = (int) (Math.floor(Math.random() * (2) + 1));// randomly pick a direction to turn - this should add enough variation that it won't get stuck in the same room after looping through it a few times
						if (coinFlip == 1) {
							robot.rotate(Turn.RIGHT);
						} else {
							robot.rotate(Turn.LEFT);
						}
						
						
						driver.energyUsed = driver.energyUsed + 6;
						robot.move(1);
						driver.cellsTravelled = driver.cellsTravelled + 1;
						toReturn = true;
						
					}
					
				}			
				
			} else if (robot.distanceToObstacle(Direction.FORWARD) != 0 ) {//has wall to the left and none front - moves forward one along the wall
				driver.energyUsed = driver.energyUsed + 1; // for sensing forward
				
				robot.move(1);
				driver.energyUsed = driver.energyUsed + 6;
				toReturn = true;
			} else {// has wall front and left

				driver.energyUsed = driver.energyUsed + 1; // for sensing forward
				robot.rotate(Turn.RIGHT);
				driver.energyUsed = driver.energyUsed + 3;
				
				toReturn = true;
			}
		} else {
			Direction dirOfWorkingForwardSensor;
			Direction dirOfWorkingLeftSensor;
			Direction dirOfWorkingRightSensor; // - added for smart version
			
			if (!frontStatus) {// if forward sensor works
				dirOfWorkingForwardSensor = Direction.FORWARD;
			} else {
				if (!leftStatus) {//finding replacement for forward sensor
					dirOfWorkingForwardSensor = Direction.LEFT;
				} else if (!rightStatus) {
					dirOfWorkingForwardSensor = Direction.RIGHT;
				} else if (!backStatus) {
					dirOfWorkingForwardSensor = Direction.BACKWARD;
				} else {
					throw new Exception(); // some kind of error
				}
			}
			
			if (!leftStatus) { // if left sensor works
				dirOfWorkingLeftSensor = Direction.LEFT;
			} else {
				if (!frontStatus) {//finding replacement for left sensor
					dirOfWorkingLeftSensor = Direction.FORWARD;
				} else if (!rightStatus) {
					dirOfWorkingLeftSensor = Direction.RIGHT;
				} else if (!backStatus) {
					dirOfWorkingLeftSensor = Direction.BACKWARD;
				} else {
					throw new Exception(); // some kind of error
				}
			}
			
			if (!rightStatus) {//if right sensor works
				dirOfWorkingRightSensor = Direction.RIGHT;
			} else {
				if (!frontStatus) {//finding replacement for right sensor
					dirOfWorkingRightSensor = Direction.FORWARD;
				} else if (!leftStatus) {
					dirOfWorkingRightSensor = Direction.LEFT;
				} else if (!backStatus) {
					dirOfWorkingRightSensor = Direction.BACKWARD;
				} else {
					throw new Exception(); // some kind of error
				} 
			}
			
			
			//part that actually rotates to the working sensors and responds
			switchSensor(Direction.LEFT, dirOfWorkingLeftSensor);
			
			driver.energyUsed = driver.energyUsed + 1;
			if (robot.distanceToObstacle(dirOfWorkingLeftSensor) != 0) {// if no wall to the left
				
				//switch back to og direction
				switchSensor(dirOfWorkingLeftSensor, Direction.LEFT);	
				
				driver.energyUsed = driver.energyUsed + 1;// for sensing right - added for smart wall follower
				switchSensor(Direction.RIGHT, dirOfWorkingRightSensor); // added for smart
				
				
				if (robot.distanceToObstacle(Direction.RIGHT) == 0) { // if no wall left but one right
					
					//switch back to og sensor
					switchSensor(dirOfWorkingRightSensor, Direction.RIGHT);
					
					robot.rotate(Turn.LEFT);
					driver.energyUsed = driver.energyUsed + 3;
					
					robot.move(1);
					driver.energyUsed = driver.energyUsed + 6;
					driver.cellsTravelled = driver.cellsTravelled + 1;
					toReturn = true;
					
				} else {
					switchSensor(Direction.FORWARD, dirOfWorkingForwardSensor);
					driver.energyUsed = driver.energyUsed + 1; // for sensing forward
					
					if (robot.distanceToObstacle(Direction.FORWARD) != 0) { // no wall l, r, or f - just move forward
						switchSensor(dirOfWorkingForwardSensor, Direction.FORWARD);
						
						driver.energyUsed = driver.energyUsed + 6;
						robot.move(1);
						driver.cellsTravelled = driver.cellsTravelled + 1;
						toReturn = true;
						
					} else {// no wall l, r but wall f try turn r which makes the f wall a left wall
						switchSensor(dirOfWorkingForwardSensor, Direction.FORWARD);
						
						driver.energyUsed = driver.energyUsed + 3;
						int coinFlip = (int) (Math.floor(Math.random() * (2) + 1));// randomly pick a direction to turn - this should add enough variation that it won't get stuck in the same room after looping through it a few times
						if (coinFlip == 1) {
							robot.rotate(Turn.RIGHT);
						} else {
							robot.rotate(Turn.LEFT);
						}
						
						
						driver.energyUsed = driver.energyUsed + 6;
						robot.move(1);
						driver.cellsTravelled = driver.cellsTravelled + 1;
						toReturn = true;
						
					}
					
				}
			} else {// if wall to front
				switchSensor(Direction.FORWARD, dirOfWorkingForwardSensor);
				
				driver.energyUsed = driver.energyUsed + 1; // for sensing forward
				if (robot.distanceToObstacle(dirOfWorkingForwardSensor) != 0) {//has wall to the left and none front - moves forward one along the wall
					switchSensor(dirOfWorkingForwardSensor, Direction.FORWARD);
					
					driver.energyUsed = driver.energyUsed + 6;
					robot.move(1);
					toReturn = true;
				} else {// has wall to front and left
					switchSensor(dirOfWorkingForwardSensor, Direction.FORWARD);
					
					driver.energyUsed = driver.energyUsed + 3;
					robot.rotate(Turn.RIGHT);
					toReturn = true;
				}
			} 
			
		}
		
		return toReturn;
	}
	
	/**
	 * helper method to handle rotation operations and energy updates for if a sensor needs to be switched out for a working one
	 * @param curDir is the direction that needs to be swapped out and newDir is the direction with the working sensor that we're switching to
	 */
	private void switchSensor(Direction curDir, Direction newDir) {
		//helper method to handle rotation operations and energy updates for if a sensor needs to be switched out for a working one
		// takes a direction needed and new sensor direction to swtich to as parameter
		if (curDir == Direction.FORWARD) {
			
			if (newDir == Direction.LEFT) {
				driver.energyUsed = driver.energyUsed + 3;
				robot.rotate(Turn.RIGHT);
				
			} else if (newDir == Direction.RIGHT) {
				driver.energyUsed = driver.energyUsed + 3;
				robot.rotate(Turn.LEFT);
				
			} else if (newDir == Direction.BACKWARD){
				driver.energyUsed = driver.energyUsed + 6;
				robot.rotate(Turn.AROUND);
			}		
		} else if (curDir == Direction.LEFT) {
			
			if (newDir == Direction.FORWARD) {
				driver.energyUsed = driver.energyUsed + 3;
				robot.rotate(Turn.LEFT);
				
			} else if (newDir == Direction.RIGHT) {
				driver.energyUsed = driver.energyUsed + 6;
				robot.rotate(Turn.AROUND);
				
				
			} else if (newDir == Direction.BACKWARD) {
				driver.energyUsed = driver.energyUsed + 3;
				robot.rotate(Turn.RIGHT);
				
			}
		} else if (curDir == Direction.RIGHT) {
			
			if (newDir == Direction.FORWARD) {
				driver.energyUsed = driver.energyUsed + 3;
				robot.rotate(Turn.RIGHT);
				
			} else if (newDir == Direction.LEFT) {
				driver.energyUsed = driver.energyUsed + 6;
				robot.rotate(Turn.AROUND);
				
				
			} else if (newDir == Direction.BACKWARD) {
				driver.energyUsed = driver.energyUsed + 3;
				robot.rotate(Turn.LEFT);
				
			}
		} else if (curDir == Direction.BACKWARD) {
			
			if (newDir == Direction.FORWARD) {
				driver.energyUsed = driver.energyUsed + 6;
				robot.rotate(Turn.AROUND);
				
			} else if (newDir == Direction.LEFT) {
				driver.energyUsed = driver.energyUsed + 3;
				robot.rotate(Turn.LEFT);
				
				
			} else if (newDir == Direction.RIGHT) {
				driver.energyUsed = driver.energyUsed + 3;
				robot.rotate(Turn.RIGHT);
				
			}
		}
	}
		
	}
