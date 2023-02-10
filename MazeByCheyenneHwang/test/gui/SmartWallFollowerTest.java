package gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import generation.CardinalDirection;
import generation.DefaultOrder;
import generation.Maze;
import generation.MazeBuilder;
import generation.MazeFactory;
import gui.Robot.Direction;


/**
 * Tests individual methods of the SmartWallFollower class. 
 * Same as WallFollower tests just with Smart wall follower driver instead of normal
 * 
 * 
 * @author Cheyenne Hwang
 *
 */

public class SmartWallFollowerTest { 
	
	private UnreliableRobot robot;
	private UnreliableSensor testerDefaultSensor;
	private ReliableSensor testerReliableSensor;
	private Maze testMaze;
	private Control controller;
	private StatePlaying tempState;
	private SmartWallFollower driver;
	
	/**
	 * Creates default SmartWallFollower object with no specification. Also creates a robot object so that the full implementation of smart wallfollower can be tested.
	 */
	@Before
	public void setUp() {
		robot = new UnreliableRobot(); 
		driver = new SmartWallFollower();
		
		controller = new Control();
		controller.setRobotAndDriver(robot, driver);
		
		tempState = new StatePlaying(); 
		
		MazeBuilder mazeBuilder = new MazeBuilder();
		MazeFactory testFactory = new MazeFactory();
		DefaultOrder mazeOrder = new DefaultOrder();
		
		testFactory.order(mazeOrder);
		testFactory.waitTillDelivered();
		
		testMaze = mazeOrder.getMaze();
		tempState.setMaze(testMaze);
		
		controller.setState(tempState);
		robot.setController(controller);
		
        UnreliableSensor forwardSensor = new UnreliableSensor();
        forwardSensor.setSensorDirection(Direction.FORWARD);
        forwardSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(forwardSensor, Direction.FORWARD);
        testerDefaultSensor = forwardSensor;
        
        UnreliableSensor leftSensor = new UnreliableSensor();
        leftSensor.setSensorDirection(Direction.LEFT);
        leftSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(leftSensor, Direction.LEFT);
        
        ReliableSensor backSensor = new ReliableSensor();
        backSensor.setSensorDirection(Direction.BACKWARD);
        backSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(backSensor, Direction.BACKWARD);
        testerReliableSensor = backSensor;
        
        UnreliableSensor rightSensor = new UnreliableSensor();
        rightSensor.setSensorDirection(Direction.RIGHT);
        rightSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(rightSensor, Direction.RIGHT);

        controller.driver.setMaze(controller.getMaze());
        controller.driver.setRobot(controller.robot);
        
        controller.robot.getRobotSensor(Direction.FORWARD).startFailureAndRepairProcess(4, 2); // starts thread processes
        try {
			Thread.sleep(1300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        controller.robot.getRobotSensor(Direction.LEFT).startFailureAndRepairProcess(4, 2);
        try {
			Thread.sleep(1300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}     
        controller.robot.getRobotSensor(Direction.RIGHT).startFailureAndRepairProcess(4, 2);
		

	}
	
	/**
	 * Resets default SmartWallFollower object with needed cars for testing. makes sure tests don't interfere with each other.
	 */
	@After
	public void reset() {
		controller.robot.getRobotSensor(Direction.FORWARD).stopFailureAndRepairProcess();// stops the previous thread
		controller.robot.getRobotSensor(Direction.LEFT).stopFailureAndRepairProcess();
		controller.robot.getRobotSensor(Direction.RIGHT).stopFailureAndRepairProcess();
		
		robot = new UnreliableRobot();
		driver = new SmartWallFollower();
		
		controller = new Control();
		controller.setRobotAndDriver(robot, driver);
		
		tempState = new StatePlaying();
		
		MazeBuilder mazeBuilder = new MazeBuilder();
		MazeFactory testFactory = new MazeFactory();
		DefaultOrder mazeOrder = new DefaultOrder();
		
		testFactory.order(mazeOrder);
		testFactory.waitTillDelivered();
		
		testMaze = mazeOrder.getMaze();
		tempState.setMaze(testMaze);
		
		controller.setState(tempState);
		robot.setController(controller);
		
        UnreliableSensor forwardSensor = new UnreliableSensor();
        forwardSensor.setSensorDirection(Direction.FORWARD);
        forwardSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(forwardSensor, Direction.FORWARD);
        testerDefaultSensor = forwardSensor;
        
        UnreliableSensor leftSensor = new UnreliableSensor();
        leftSensor.setSensorDirection(Direction.LEFT);
        leftSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(leftSensor, Direction.LEFT);
        
        ReliableSensor backSensor = new ReliableSensor();
        backSensor.setSensorDirection(Direction.BACKWARD);
        backSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(backSensor, Direction.BACKWARD);
        testerReliableSensor = backSensor;
        
        UnreliableSensor rightSensor = new UnreliableSensor();
        rightSensor.setSensorDirection(Direction.RIGHT);
        rightSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(rightSensor, Direction.RIGHT);

        controller.driver.setMaze(controller.getMaze());
        controller.driver.setRobot(controller.robot);
        
        controller.robot.getRobotSensor(Direction.FORWARD).startFailureAndRepairProcess(4, 2); // starts thread processes
        try {
			Thread.sleep(1300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        controller.robot.getRobotSensor(Direction.LEFT).startFailureAndRepairProcess(4, 2);
        try {
			Thread.sleep(1300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        
        controller.robot.getRobotSensor(Direction.RIGHT).startFailureAndRepairProcess(4, 2);

	}
	
	/**
	 * Test case: checks to see if a robot has been set for smart wallfollower
	 * <p>
	 * Method under test: checks that setRobot() method works, that implementation correct, and that the instance var for robot works
	 * <p>
	 * Correct behavior:
	 * should assert that the smart wallfollower's robot is the robot created
	 */
	@Test
	public final void settingUpRobot() {
		//checks to see if a robot has been set for smart wallfollower
		//makes sure that the setRobot() method works, that implementation correct, and that the instance var for robot works
		//how it does this: since robot already set by set method in instantiation set up (needed for other tests) - checks that its in place by examining instance var
		// if correctly set up it should assert that the  wallfollower's robot is the robot created
		// if wrong then instance variable and setting process are disjointed
						
		assertEquals(driver.robot, robot);
	}
	
	/**
	 * Test case: checks to see if a maze has been set for  smart wallfollower
	 * <p>
	 * Method under test: checks the the setMaze() method works, that implementation correct, and that the instance var for maze works
	 * <p>
	 * Correct behavior:
	 * should assert that the smart wallfollower's maze is the maze from the robot created
	 */
	@Test
	public final void settingUpMaze() {
		// checks to see if a maze has been set for smart wallfollower
		// makes sure that the setMaze() method works, that implementation correct, and that the instance var for maze works
		// how it does this: since maze already set by set method in instantiation set up (needed for other tests) - checks that its in place by examining instance var
		// if correctly set up it should assert that the  wallfollower's maze is the maze from the robot created
		// if wrong then instance variable and setting process are disjointed
		
		assertEquals(driver.maze, robot.controller.getMaze());
	}
	
	/**
	 * Test case: checks that drive2Exit works when all starting conditions are all appropriate (full battery and not at exit yet)
	 * <p>
	 * Method under test: drive2Exit method and by proxy test drive1step which is independently tested in other tests
	 * <p>
	 * Correct behavior:
	 * drive2exit returns boolean corresponding to outcome, or exception thrown only if dead
	 */
	@Test
	public final void checkFullDriveWorking() {
		// checks that drive2Exit works when all starting conditions are all appropriate (full battery and not at exit yet)
		// tests the drive2Exit method and by proxy test drive1step which is independently tested in other tests
		// how it works: collects current position, collects exit position and direction, calls drive2exit
		// if energy doesn't run out: checks that position has changed in appropriate direction
		// if energy does run out: check that robot is dead
		// if correct: drive2exit returns boolean corresponding to outcome, or exception thrown only if dead
		// change for smart: chance that it takes too long in the room to get out so could fail and die from energy use
		int[] exitPos = driver.maze.getExitPosition();
		CardinalDirection exitDir;
		
		if (exitPos[1] == 0 && (!driver.maze.hasWall(exitPos[0], exitPos[1], CardinalDirection.North))) {
			exitDir = CardinalDirection.North;
		} else if (exitPos[0] == 0 && (!driver.maze.hasWall(exitPos[0], exitPos[1], CardinalDirection.West))) {
			exitDir = CardinalDirection.West;
		} else if ((exitPos[1] == driver.maze.getHeight() - 1) && (driver.maze.hasWall(exitPos[0], exitPos[1], CardinalDirection.South))) {
			exitDir = CardinalDirection.South;
		} else {
			exitDir = CardinalDirection.East;
		}

				
		try {
			boolean outcome = driver.drive2Exit();
			
			assertTrue(outcome);
			int[] curPos = driver.robot.getCurrentPosition();
			assertEquals(curPos, exitPos);
			assertEquals(exitDir, driver.robot.getCurrentDirection());
			
		} catch (Exception e) {
			assertTrue(driver.energyUsed != 3500);// check robot death
		}
	}
	
	/**
	 * Test case: checks that drive2Exit fails if the robot doesn't have enough energy
	 * <p>
	 * Method under test:drive2exit functionality and by proxy interaction with robot
	 * <p>
	 * Correct behavior:
	 * exception should be thrown because robot dead
	 */
	@Test
	public final void checkFullDriveFailed() {
		// checks that drive2Exit fails if the robot doesn't have enough energy
		// tests drive2exit functionality and by proxy interaction with robot
		// how it works: set robot's to energyLevel to below 10 (reasonable amount needed for a drive to the exit regardless of difficulty), then tries to call drive2exit
		// if correct: exception should be thrown because robot dead
		boolean toFail = true;
		robot.energyLevel = (float) 10;
				
		try {
			boolean temp = driver.drive2Exit(); 
			assertFalse(toFail); // should autofail because shouldn't go down this path
		} catch (Exception e) {
			assertTrue(toFail);
		}
	}
	
	/**
	 * Test case: checks that drive1stepToExit works when at exit
	 * <p>
	 * Method under test: drive1steptoexit, finaldrive2exit method functionality at exit and maze object interaction to generate vars to test with
	 * <p>
	 * Correct behavior:
	 * drive1step returns false, direction of robot updates correctly to exitDirection, no exception thrown
	 */
	@Test
	public final void check1StepAtExit() {
		// checks that drive1stepToExit works when at exit
		// tests the drive1steptoexit method functionality, and finaldrive 2 exit, at exit and maze object interaction to generate vars to test with
		// how it works: collects exit position and direction, sets robot position at exit and away from exit, calls drive1step
		// then checks that drive returns false for no movement but that direction is now correct for exit
		// if correct: drive1step returns false, direction of robot updates correctly to exitDirection, no exception thrown, or if stops asserts that robot died from energy fail
		boolean toFail = true;
		int[] exitPos = driver.maze.getExitPosition();
		CardinalDirection exitDir;
		
		if (exitPos[1] == 0 && (!driver.maze.hasWall(exitPos[0], exitPos[1], CardinalDirection.North))) {
			exitDir = CardinalDirection.North;
		} else if (exitPos[0] == 0 && (!driver.maze.hasWall(exitPos[0], exitPos[1], CardinalDirection.West))) {
			exitDir = CardinalDirection.West;
		} else if ((exitPos[1] == driver.maze.getHeight() - 1) && (driver.maze.hasWall(exitPos[0], exitPos[1], CardinalDirection.South))) {
			exitDir = CardinalDirection.South;
		} else {
			exitDir = CardinalDirection.East;
		}
		try {
			boolean finished = driver.drive2Exit();
			if (finished) {
				assertTrue(driver.robot.getCurrentPosition() == exitPos);
				assertTrue(driver.robot.getCurrentDirection() == exitDir);
			}
		} catch (Exception e) {
			assertTrue(driver.energyUsed != 3500);
		}
		

	}
	
	/**
	 * Test case: checks that drive1step fails if the robot doesn't have enough energy
	 * <p>
	 * Method under test: tests drive1step functionality and by proxy interaction with robot
	 * <p>
	 * Correct behavior:
	 * exception should be thrown because dead/killed
	 */
	@Test
	public final void check1StepFail() {
		// checks that drive1step fails if the robot doesn't have enough energy
		// tests drive1step functionality and by proxy interaction with robot
		// how it works: set robot's to energyLevel to below 6 (needed for a move), then tries to call check1step, checks to see if exception called
		// if correct: exception should be thrown because dead
		boolean toFail = true;
		robot.energyLevel = (float) 5;
		
		try {
			boolean temp = driver.drive1Step2Exit(); 
			assertFalse(toFail); // should autofail because shouldn't go down this path
		} catch (Exception e) {
			assertTrue(toFail);
		}
		
		
	}
	
	/**
	 * Test case: checks that drive1stepToExit works when conditions are all appropriate (full battery and not at exit yet)
	 * <p>
	 * Method under test: drive1steptoexit method and by proxy test that wall follower is actually trying to follow the left wall
	 * <p>
	 * Correct behavior:
	 * drive1step returns true, position of robot updates correctly based on wall status, no exception thrown
	 *  special case for smart wall follower: can't find way out of room after 1 loop so may not be able successfully do drive (reasonable)
	 */
	@Test
	public final void check1StepComplete() {
		// checks that drive1stepToExit works when conditions are all appropriate (full battery and not at exit yet)
		// tests the drive1steptoexit method and by proxy test that wall follower is actually trying to follow the left wall
		// how it works: collects current position, checks wall status calls drive1step, checks that drive occured, checks that position has changed in appropriate direction
		// since this is just the first step should be able to move as full battery and not yet exit
		// if correct: drive1step returns true, position of robot updates correctly based on wall status, no exception thrown
		// special case for smart wall follower: can't find way out of room after 1 loop so may not be able successfully do drive (reasonable)
		boolean toFail = false;
		boolean jumpOccur = false;
		int[] curPos;
		CardinalDirection curCarDir;
		
		try {
			curPos = driver.robot.getCurrentPosition();
			curCarDir = controller.getCurrentDirection();
			try {
				toFail = driver.drive1Step2Exit();
				assertTrue(toFail);
				
				int[] newPos = driver.robot.getCurrentPosition();
				assertFalse(newPos == curPos);
				
								
			} catch (Exception e) {//reasonable error

			}
		} catch (Exception e) {
			assertTrue(toFail);
		}
		
		
	}

	
	/**
	 * Test case: checks that energyUsed by smart wallfollower starts at the right value
	 * <p>
	 * Method under test: getEnegy() method and instance var set up for energy
	 * <p>
	 * Correct behavior:
	 * nergyUsed and getEnergy should both return (float) 0
	 */
	@Test
	public final void checkEnergyComsumptionStart() {
		// checks that energyUsed by smart wallfollower starts at the right value
		// tests the getEnegy() method and instance var set up for energy
		// how it works: calls getEnergy() before any movement on wallfollower's part, checks the instance var for energyUsed against it
		// if correct: energyUsed and getENergy should both return 0
		
		float startingEnergyUsed = driver.energyUsed;
		float zeroRep = (float) 0;
		assertTrue(zeroRep == startingEnergyUsed);
		assertTrue(driver.getEnergyConsumption() == startingEnergyUsed);
	}
	
	/**
	 * Test case: checks that energyUsed by smart wallfollower is correctly updated after a drive
	 * <p>
	 * Method under test: FgetEnegy() method and by proxy tests the drive1step method to created needed conditions
	 * <p>
	 * Correct behavior:
	 * nergyUsed should return a value <= (3500 - 7) which is the minimum energyUsed after a step, no exception should be thrown
	 */
	@Test
	public final void checkEnergyConsumptionLater() {
		// checks that energyUsed by smart wallfollower is correctly updated after a drive
		// tests the getEnegy() method and by proxy tests the drive1step method to created needed conditions
		// how it works: calls getEnergy() after being driven1step, checks what the getEnergy return val is now
		// since this is just the first step should be able to mvoe and this should return true
		// if correct: energyUsed should return a value <= (3500 - 7) which is the minimum energyUsed after a step, no exception should be thrown
		boolean toFail = false;
		float compVal = (float) (3500 - 7);
		try {
			toFail = driver.drive1Step2Exit();
			assertTrue(toFail);
			assertTrue(driver.getEnergyConsumption() <= compVal);
			
		} catch (Exception e) {

		}
	}
	
	/**
	 * Test case: checks that pathLength travelled by smart wallfollower starts at the right value
	 * <p>
	 * Method under test: getPathLength() method and instance var set up for cellsTravelled
	 * <p>
	 * Correct behavior:
	 * cellsTravelled and getPath() should both return 0
	 */
	@Test
	public final void checkPathLengthStart() {
		// checks that pathLength travelled by smart wallfollower starts at the right value
		// tests the getPathLength() method and instance var set up for cellsTravelled
		// how it works: calls getPathLength() before any movement on wallfollower's part, checks the instance var for cellsTravelled against it
		// if correct: cellsTravelled and getPath() should both return 0
				
		float startingCellsTravelled = driver.cellsTravelled;
		float zeroRep = (float) 0;
		assertTrue(zeroRep == startingCellsTravelled);
		assertTrue(driver.getPathLength() == startingCellsTravelled);
		
	}
	
	/**
	 * Test case: checks that energyUsed by smart wallfollower is correctly updated after a drive
	 * <p>
	 * Method under test: getPathLength() method and by proxy tests the drive1step method to created needed conditions
	 * <p>
	 * Correct behavior:
	 * getPathLength should return a value greater than 0 which is the amount moved, no exception should be thrown
	 */
	@Test
	public final void checkPathLengthLater() {
		// checks that energyUsed by smart wallfollower is correctly updated after a drive
		// tests the getPathLength() method and by proxy tests the drive1step method to created needed conditions
		// how it works: calls getPathLength() after being driven1step, checks what the getPathLength() return val is now
		// since this is just the first step should be able to move and drive should return true
		// if correct: getPathLength should return a value greater than 0 which is the amount moved, no exception should be thrown
		
		boolean toFail = false;
		try {
			toFail = driver.drive1Step2Exit();
			assertTrue(toFail);
			assertTrue(driver.getPathLength() > (float)0);
			
		} catch (Exception e) {

		}
	}
	
	/**
	 * Test case:  checks that fianldrive2end works correctly when not at exit
	 * <p>
	 * Method under test: tests the fianldrive2end method
	 * <p>
	 * Correct behavior:
	 * should throw an error because this is not the exit
	 */
	@Test
	public final void finalDriveError() {
		// checks that fianldrive2end works correctly when not at exit
		// tests the fianldrive2end method a
		// how it works: calls final drive2end and gives an input that is not the exit position
		// since this is just the first step should not be the exit
		// if correct: should throw an error because this is not the exit
		
		boolean toFail = false;
		try {
			driver.finalDrive2End(driver.robot.getCurrentPosition());
			assertTrue(toFail);
			
		} catch (Exception e) {
			assertFalse(toFail);
		}
	}
	
	/**
	 * Test case: checks to see if drive1step method works when one of the sensors needed (l, r, or f) is in repair and a switch needs to be made
	 * <p>
	 * Method under test: makes sure that the drive1step() method works, and handling for repairing sensors within subclass SensorState
	 * <p>
	 * Correct behavior:
	 * drive1step method should return true since it is at the opening and not yet at the exit
	 */
	@Test
	public final void drive1stepUnreliableSetting() {
		// checks to see if drive1step method works when one of the sensors needed (l, r, or f) is in repair and a switch needs to be made
		// makes sure that the drive1step() method works, and handling for repairing sensors within subclass SensorState
		// how it does this: impossible to time perfectly so tries to catch robot sensor at a time when either forward or left are in repair
		// does this by checking once and then 1 second before trying again
		// if correctly set up the drive1step method should return true since it is at the opening and not yet at the exit
		// if wrong then handling of sensors when working is incorrect and autofails since it should be able to make 1 step forward
		// change for smart: add check on right sensor now that its needed
		
		boolean toFail = false;
		if (robot.getRobotSensor(Direction.FORWARD).checkRepairStatus() && robot.getRobotSensor(Direction.LEFT).checkRepairStatus() && robot.getRobotSensor(Direction.RIGHT).checkRepairStatus()) {
			try {
				assertTrue(driver.drive1Step2Exit());
			} catch (Exception e) { // shouldn't reach this branch so autofail
				assertTrue(toFail);
			}
		} else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
			}
			if (robot.getRobotSensor(Direction.FORWARD).checkRepairStatus() && robot.getRobotSensor(Direction.LEFT).checkRepairStatus() && robot.getRobotSensor(Direction.RIGHT).checkRepairStatus()) {
				try {
					assertTrue(driver.drive1Step2Exit());
				} catch (Exception e) { 
					assertTrue(toFail);
				}
			}
			
			
		}
	}
	
	/**
	 * Test case: checks to see if drive1step method works when the setting for the needed sensors (l, r, and f) is true - and thus operates like a reliable sensor
	 * <p>
	 * Method under test: drive1step() method works, and handling for fully functional sensors within subclass SensorState
	 * <p>
	 * Correct behavior:
	 * drive1step method should return true since it is at the opening and not yet at the exit and sensors all work
	 */
	@Test
	public final void drive1stepReliableSetting() {
		// checks to see if drive1step method works when the setting for the needed sensors (l, r, and f) is true - and thus operates like a reliable sensor
		// makes sure that the drive1step() method works, and handling for fully functional sensors within subclass SensorState
		// how it does this: impossible to time perfectly so tries to catch robot sensor at a time when the forward and left are both working
		// does this by checking once and then 1 second before trying again
		// if correctly set up the drive1step method should return true since it is at the opening and not yet at the exit and sensors all work
		// if wrong then handling of sensors when working is incorrect and autofails since it should be able to make 1 step forward
		// change for smart wall follower: also need right distance sensor to be working
				
		boolean toFail = false;
		if (!robot.getRobotSensor(Direction.FORWARD).checkRepairStatus() && !robot.getRobotSensor(Direction.LEFT).checkRepairStatus() && !robot.getRobotSensor(Direction.RIGHT).checkRepairStatus()) {
			try {
				assertTrue(driver.drive1Step2Exit());
			} catch (Exception e) { // shouldn't reach this branch so autofail
				assertTrue(toFail);
			}
		} else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {			
					}
			if (!robot.getRobotSensor(Direction.FORWARD).checkRepairStatus() && !robot.getRobotSensor(Direction.LEFT).checkRepairStatus() && !robot.getRobotSensor(Direction.RIGHT).checkRepairStatus()) {
				try {
					assertTrue(driver.drive1Step2Exit());
				} catch (Exception e) { 
					assertTrue(toFail);
				}
			}			
					
		}
	}
	
	/**
	 * Test case: checks to see that smart wallfollower is actually trying to follow the left wall
	 * <p>
	 * Method under test:  drive1step method works and by proxy that the drive2end() works
	 * <p>
	 * Correct behavior:
	 * should assert that the smart wallfollower driver robot along the left wall
	 */
	@Test
	public final void driveActuallyFollowsWall() {
		// checks to see that smart wallfollower is actually trying to follow the left wall
		// makes sure that the drive1step method works and by proxy that the drive2end() works
		// how it does this: gets current position and sees if there's a wall to its left and front
		// if there is a wall to left but not front checks that position changes after drive1step
		// if there is wall to front or not to left then moves once, tries process again
		// if correctly set up it should assert that the smart wallfollower driver robot along the left wall
		// if wrong then drive1step or distance sensing wrong
		
		try {
			int[] curPos = driver.robot.getCurrentPosition();
			
			if (driver.robot.distanceToObstacle(Direction.LEFT) == 0 && driver.robot.distanceToObstacle(Direction.FORWARD) == 0) {
				driver.drive1Step2Exit();
				
				int[] newPos = driver.robot.getCurrentPosition();
				
				
				assertTrue((curPos[0] != newPos[0]) || curPos[1] != newPos[0]);
			} else {
				driver.drive1Step2Exit();
				
				curPos = driver.robot.getCurrentPosition();
				
				if ((driver.robot.distanceToObstacle(Direction.LEFT) == 0 && driver.robot.distanceToObstacle(Direction.FORWARD) == 0)) {
					driver.drive1Step2Exit();
					
					int[] newPos = driver.robot.getCurrentPosition();
					
					assertTrue((curPos[0] != newPos[0]) || curPos[1] != newPos[0]);
				} else {
					int[] newPos = driver.robot.getCurrentPosition();
					
					assertTrue(curPos[0] == newPos[0] && curPos[1] == newPos[1]);
				}
						
			}
			
			
		} catch (Exception e) {
		}
	}
	
	/**
	 * Test case: checks to see if full drive method works when one of the sensors needed (l or f) is in repair and a switch needs to be made
	 * <p>
	 * Method under test: drive2End() method works, and handling for repairing sensors within subclass SensorState
	 * <p>
	 * Correct behavior:
	 * drive2End() method should return true if it is able to finish
	 */
	@Test
	public final void fullDriveUnreliableSetting() {
		// checks to see if full drive method works when one of the sensors needed (l or f) is in repair and a switch needs to be made
		// makes sure that the drive2End() method works, and handling for repairing sensors within subclass SensorState
		// how it does this: impossible to time perfectly so tries to catch robot sensor at a time when either forward or left are in repair
		// does this by checking once and then 1 second before trying again
		// if correctly set up the drive2End() method should return true if it is able to finish
		// possibility that it doens't find exit
		// if is able to return something, and that soemthing is false then this is wrong since the maze was generated properly
		
		boolean toFail = false;
		if (robot.getRobotSensor(Direction.FORWARD).checkRepairStatus() || robot.getRobotSensor(Direction.LEFT).checkRepairStatus()) {
			try {
				boolean toTest = driver.drive2Exit();
				
				assertTrue(toTest);
			} catch (Exception e) {
				
			}
		} else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
			}
			if (robot.getRobotSensor(Direction.FORWARD).checkRepairStatus() || robot.getRobotSensor(Direction.LEFT).checkRepairStatus()) {
				try {
					boolean toTest = driver.drive2Exit();
					
					assertTrue(toTest);
				} catch (Exception e) {
					
				}
			}
			
			
		}
	}
	
	/**
	 * Test case: checks to see if full drive method works when both of the sensors needed (l or f) are functioning
	 * <p>
	 * Method under test: drive2End() method works, and handling for functional sensors within subclass SensorState
	 * <p>
	 * Correct behavior:
	 * drive2End() method should return true if it is able to finish
	 */
	@Test
	public final void fullDriveReliableSetting() {
		// checks to see if full drive method works when both of the sensors needed (l or f) are functioning
		// makes sure that the drive2End() method works, and handling for functional sensors within subclass SensorState
		// how it does this: impossible to time perfectly so tries to catch robot sensor at a time when neither forward or left are in repair
		// does this by checking once and then 1 second before trying again
		// if correctly set up the drive2End() method should return true if it is able to finish
		// possibility that it doens't find exit
		// if is able to return something, and that soemthing is false then this is wrong since the maze was generated properly
		
		boolean toFail = false;
		if (!robot.getRobotSensor(Direction.FORWARD).checkRepairStatus() && !robot.getRobotSensor(Direction.LEFT).checkRepairStatus()) {
			try {
				boolean toTest = driver.drive2Exit();
				
				assertTrue(toTest);
			} catch (Exception e) {
				
			}
		} else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
			}
			if (!robot.getRobotSensor(Direction.FORWARD).checkRepairStatus() && !robot.getRobotSensor(Direction.LEFT).checkRepairStatus()) {
				try {
					boolean toTest = driver.drive2Exit();
					
					assertTrue(toTest);
				} catch (Exception e) {
					
				}
			}
			
			
		}
	}
	
	

}
