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
 * Tests individual methods of the Wizard class. 
 * 
 * 
 * @author Cheyenne Hwang
 *
 */

public class WizardTest {
	
	private Wizard merlin;
	private ReliableRobot robot;
	private Maze maze;
	private Control controller;
	
	/**
	 * Creates default Wizard object with no specification. Also creates a robot object so that the full implementation of wizard can be tested.
	 */
	@Before
	public void setUp() { 
		robot = new ReliableRobot();
		merlin = new Wizard();
		
		controller = new Control();
		controller.setRobotAndDriver(robot, merlin);
		
		StatePlaying tempState = new StatePlaying();
		
		MazeBuilder mazeBuilder = new MazeBuilder();
		MazeFactory testFactory = new MazeFactory();
		DefaultOrder mazeOrder = new DefaultOrder();
		 
		testFactory.order(mazeOrder);
		testFactory.waitTillDelivered();
		
		Maze testMaze = mazeOrder.getMaze();
		tempState.setMaze(testMaze);
		
		controller.setState(tempState); 
		robot.setController(controller);
        
        ReliableSensor forwardSensor = new ReliableSensor();
        forwardSensor.setSensorDirection(Direction.FORWARD);
        forwardSensor.setMaze(controller.getMaze());
        
        ReliableSensor leftSensor = new ReliableSensor();
        leftSensor.setSensorDirection(Direction.LEFT);
        leftSensor.setMaze(controller.getMaze());
        
        ReliableSensor backSensor = new ReliableSensor();
        backSensor.setSensorDirection(Direction.BACKWARD);
        backSensor.setMaze(controller.getMaze());
        
        ReliableSensor rightSensor = new ReliableSensor();
        rightSensor.setSensorDirection(Direction.RIGHT);
        rightSensor.setMaze(controller.getMaze());
        
        controller.robot.addDistanceSensor(forwardSensor, Direction.FORWARD);
        controller.robot.addDistanceSensor(leftSensor, Direction.LEFT);
        controller.robot.addDistanceSensor(backSensor, Direction.BACKWARD);
        controller.robot.addDistanceSensor(rightSensor, Direction.RIGHT);

        merlin.setMaze(controller.getMaze());
        merlin.setRobot(controller.robot);

	}
	
	/**
	 * Resets default Wizard object with needed cars for testing. makes sure tests don't interfere with each other.
	 */
	@After
	public void reset() {
		robot = new ReliableRobot();
		merlin = new Wizard();
		
		controller = new Control();
		controller.setRobotAndDriver(robot, merlin);
		
		StatePlaying tempState = new StatePlaying();
		
		MazeBuilder mazeBuilder = new MazeBuilder();
		MazeFactory testFactory = new MazeFactory();
		DefaultOrder mazeOrder = new DefaultOrder();
		
		testFactory.order(mazeOrder);
		testFactory.waitTillDelivered();
		
		Maze testMaze = mazeOrder.getMaze();
		tempState.setMaze(testMaze);
		
		controller.setState(tempState); 
		robot.setController(controller);
        
        ReliableSensor forwardSensor = new ReliableSensor();
        forwardSensor.setSensorDirection(Direction.FORWARD);
        forwardSensor.setMaze(controller.getMaze());
        
        ReliableSensor leftSensor = new ReliableSensor();
        leftSensor.setSensorDirection(Direction.LEFT);
        leftSensor.setMaze(controller.getMaze());
        
        ReliableSensor backSensor = new ReliableSensor();
        backSensor.setSensorDirection(Direction.BACKWARD);
        backSensor.setMaze(controller.getMaze());
        
        ReliableSensor rightSensor = new ReliableSensor();
        rightSensor.setSensorDirection(Direction.RIGHT);
        rightSensor.setMaze(controller.getMaze());
        
        controller.robot.addDistanceSensor(forwardSensor, Direction.FORWARD);
        controller.robot.addDistanceSensor(leftSensor, Direction.LEFT);
        controller.robot.addDistanceSensor(backSensor, Direction.BACKWARD);
        controller.robot.addDistanceSensor(rightSensor, Direction.RIGHT);

        merlin.setMaze(controller.getMaze());
        merlin.setRobot(controller.robot);

	}
	
	/**
	 * Test case: checks to see if a robot has been set for wizard
	 * <p>
	 * Method under test: checks that setRobot() method works, that implementation correct, and that the instance var for robot works
	 * <p>
	 * Correct behavior:
	 * should assert that the wizard's robot is the robot created
	 */
	@Test
	public void settingUpRobot() {
		//checks to see if a robot has been set for wizard
		//makes sure that the setRobot() method works, that implementation correct, and that the instance var for robot works
		//how it does this: since robot already set by set method in instantiation set up (needed for other tests) - checks that its in place by examining instance var
		// if correctly set up it should assert that the wizard's robot is the robot created
		// if wrong then instance variable and setting process are disjointed
						
		assertEquals(merlin.robot, robot);
	}
	
	/**
	 * Test case: checks to see if a maze has been set for wizard
	 * <p>
	 * Method under test: checks the the setMaze() method works, that implementation correct, and that the instance var for maze works
	 * <p>
	 * Correct behavior:
	 * should assert that the wizard's maze is the maze from the robot created
	 */
	@Test
	public void settingUpMaze() {
		// checks to see if a maze has been set for wizard
		// makes sure that the setMaze() method works, that implementation correct, and that the instance var for maze works
		// how it does this: since maze already set by set method in instantiation set up (needed for other tests) - checks that its in place by examining instance var
		// if correctly set up it should assert that the wizard's maze is the maze from the robot created
		// if wrong then instance variable and setting process are disjointed
		
		assertEquals(merlin.maze, robot.controller.getMaze());
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
	public void checkFullDriveWorking() {
		// checks that drive2Exit works when all starting conditions are all appropriate (full battery and not at exit yet)
		// tests the drive2Exit method and by proxy test drive1step which is independently tested in other tests
		// how it works: collects current position, collects exit position and direction, calls drive2exit
		// if energy doesn't run out: checks that position has changed in appropriate direction
		// if energy does run out: check that robot is dead
		// if correct: drive2exit returns boolean corresponding to outcome, or exception thrown only if dead
		int[] exitPos = merlin.maze.getExitPosition();
		CardinalDirection exitDir;
		
		if (exitPos[1] == 0 && (!merlin.maze.hasWall(exitPos[0], exitPos[1], CardinalDirection.North))) {
			exitDir = CardinalDirection.North;
		} else if (exitPos[0] == 0 && (!merlin.maze.hasWall(exitPos[0], exitPos[1], CardinalDirection.West))) {
			exitDir = CardinalDirection.West;
		} else if ((exitPos[1] == merlin.maze.getHeight() - 1) && (merlin.maze.hasWall(exitPos[0], exitPos[1], CardinalDirection.South))) {
			exitDir = CardinalDirection.South;
		} else {
			exitDir = CardinalDirection.East;
		}

				
		try {
			boolean outcome = merlin.drive2Exit();
			
			assertTrue(outcome);
			int[] curPos = merlin.robot.getCurrentPosition();
			assertEquals(curPos, exitPos);
			assertEquals(exitDir, merlin.robot.getCurrentDirection());
			
		} catch (Exception e) {
			assertTrue(merlin.robot.hasStopped());
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
	public void checkFullDriveFailed() {
		// checks that drive2Exit fails if the robot doesn't have enough energy
		// tests drive2exit functionality and by proxy interaction with robot
		// how it works: set robot's to energyLevel to below 10 (reasonable amount needed for a drive to the exit regardless of difficulty), then tries to call drive2exit
		// if correct: exception should be thrown because robot dead
		boolean toFail = true;
		robot.energyLevel = (float) 10;
				
		try {
			boolean temp = merlin.drive2Exit(); 
			assertFalse(toFail); // should autofail because shouldn't go down this path
		} catch (Exception e) {
			assertTrue(toFail);
		}
	}
	
	/**
	 * Test case: checks that drive1stepToExit works when at exit
	 * <p>
	 * Method under test: drive1steptoexit, finaldrive2exit method functionality at exit and by proxy test getNeighbor and maze object interaction to generate vars to test with
	 * <p>
	 * Correct behavior:
	 * drive1step returns false, direction of robot updates correctly to exitDirection, no exception thrown
	 */
	@Test
	public void check1StepAtExit() {
		// checks that drive1stepToExit works when at exit
		// tests the drive1steptoexit method functionality, and finaldrive 2 exit, at exit and by proxy test getNeighbor and maze object interaction to generate vars to test with
		// how it works: collects exit position and direction, sets robot position at exit and away from exit, calls drive1step
		// then checks that drive returns false for no movement but that direction is now correct for exit
		// if correct: drive1step returns false, direction of robot updates correctly to exitDirection, no exception thrown
		boolean toFail = true;
		int[] exitPos = merlin.maze.getExitPosition();
		CardinalDirection exitDir;
		
		if (exitPos[1] == 0 && (!merlin.maze.hasWall(exitPos[0], exitPos[1], CardinalDirection.North))) {
			exitDir = CardinalDirection.North;
		} else if (exitPos[0] == 0 && (!merlin.maze.hasWall(exitPos[0], exitPos[1], CardinalDirection.West))) {
			exitDir = CardinalDirection.West;
		} else if ((exitPos[1] == merlin.maze.getHeight() - 1) && (merlin.maze.hasWall(exitPos[0], exitPos[1], CardinalDirection.South))) {
			exitDir = CardinalDirection.South;
		} else {
			exitDir = CardinalDirection.East;
		}
		try {
			boolean finished = merlin.drive2Exit();
			if (finished) {
				assertTrue(merlin.robot.getCurrentPosition() == exitPos);
				assertTrue(merlin.robot.getCurrentDirection() == exitDir);
			}
		} catch (Exception e) {
			assertTrue(merlin.robot.hasStopped());
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
	public void check1StepFail() {
		// checks that drive1step fails if the robot doesn't have enough energy
		// tests drive1step functionality and by proxy interaction with robot
		// how it works: set robot's to energyLevel to below 6 (needed for a move), then tries to call check1step, checks to see if exception called
		// if correct: exception should be thrown because dead
		boolean toFail = true;
		robot.energyLevel = (float) 5;
		
		try {
			boolean temp = merlin.drive1Step2Exit(); 
			assertFalse(toFail); // should autofail because shouldn't go down this path
		} catch (Exception e) {
			assertTrue(toFail);
		}
		
		
	}
	
	/**
	 * Test case: checks that drive1stepToExit works when conditions are all appropriate (full battery and not at exit yet)
	 * <p>
	 * Method under test: drive1steptoexit method and by proxy test getNeighbor and maze object interaction to generate cars to test with
	 * <p>
	 * Correct behavior:
	 * drive1step returns true, position of robot updates correctly to neighborClosestToExit, no exception thrown
	 */
	@Test
	public void check1StepComplete() {
		// checks that drive1stepToExit works when conditions are all appropriate (full battery and not at exit yet)
		// tests the drive1steptoexit method and by proxy test getNeighbor and maze object interaction to generate cars to test with
		// how it works: collects current position, checks neighbor near exit's direction calls drive1step, checks that drive occured, checks that position has changed in appropriate direction
		// since this is just the first step should be able to move as full battery and not yet exit
		// if correct: drive1step returns true, position of robot updates correctly to neighborClosestToExit, no exception thrown
		boolean toFail = false;
		boolean jumpOccur = false;
		int[] curPos;
		CardinalDirection curCarDir;
		
		try {
			curPos = merlin.robot.getCurrentPosition();
			curCarDir = controller.getCurrentDirection();
			try {
				toFail = merlin.drive1Step2Exit();
				assertTrue(toFail);
				
				int[] newPos = merlin.robot.getCurrentPosition();
				assertFalse(newPos == curPos);
				
								
			} catch (Exception e) {
				assertTrue(toFail);
			}
		} catch (Exception e) {
			assertTrue(toFail);
		}
		
		
	}

	
	/**
	 * Test case: checks that energyUsed by wizard starts at the right value
	 * <p>
	 * Method under test: getEnegy() method and instance var set up for energy
	 * <p>
	 * Correct behavior:
	 * nergyUsed and getEnergy should both return (float) 0
	 */
	@Test
	public void checkEnergyComsumptionStart() {
		// checks that energyUsed by wizard starts at the right value
		// tests the getEnegy() method and instance var set up for energy
		// how it works: calls getEnergy() before any movement on wizard's part, checks the instance var for energyUsed against it
		// if correct: energyUsed and getENergy should both return 0
		
		float startingEnergyUsed = merlin.energyUsed;
		float zeroRep = (float) 0;
		assertTrue(zeroRep == startingEnergyUsed);
		assertTrue(merlin.getEnergyConsumption() == startingEnergyUsed);
	}
	
	/**
	 * Test case: checks that energyUsed by wizard is correctly updated after a drive
	 * <p>
	 * Method under test: FgetEnegy() method and by proxy tests the drive1step method to created needed conditions
	 * <p>
	 * Correct behavior:
	 * nergyUsed should return a value <= (3500 - 7) which is the minimum energyUsed after a step, no exception should be thrown
	 */
	@Test
	public void checkEnergyConsumptionLater() {
		// checks that energyUsed by wizard is correctly updated after a drive
		// tests the getEnegy() method and by proxy tests the drive1step method to created needed conditions
		// how it works: calls getEnergy() after being driven1step, checks what the getEnergy return val is now
		// since this is just the first step should be able to mvoe and this should return true
		// if correct: energyUsed should return a value <= (3500 - 7) which is the minimum energyUsed after a step, no exception should be thrown
		boolean toFail = false;
		float compVal = (float) (3500 - 7);
		try {
			toFail = merlin.drive1Step2Exit();
			assertTrue(toFail);
			assertTrue(merlin.getEnergyConsumption() <= compVal);
			
		} catch (Exception e) {
			assertTrue(toFail);
		}
	}
	
	/**
	 * Test case: checks that pathLength travelled by wizard starts at the right value
	 * <p>
	 * Method under test: getPathLength() method and instance var set up for cellsTravelled
	 * <p>
	 * Correct behavior:
	 * cellsTravelled and getPath() should both return 0
	 */
	@Test
	public void checkPathLengthStart() {
		// checks that pathLength travelled by wizard starts at the right value
		// tests the getPathLength() method and instance var set up for cellsTravelled
		// how it works: calls getPathLength() before any movement on wizard's part, checks the instance var for cellsTravelled against it
		// if correct: cellsTravelled and getPath() should both return 0
				
		float startingCellsTravelled = merlin.cellsTravelled;
		float zeroRep = (float) 0;
		assertTrue(zeroRep == startingCellsTravelled);
		assertTrue(merlin.getPathLength() == startingCellsTravelled);
		
	}
	
	/**
	 * Test case: checks that energyUsed by wizard is correctly updated after a drive
	 * <p>
	 * Method under test: getPathLength() method and by proxy tests the drive1step method to created needed conditions
	 * <p>
	 * Correct behavior:
	 * getPathLength should return a value greater than 0 which is the amount moved, no exception should be thrown
	 */
	@Test
	public void checkPathLengthLater() {
		// checks that energyUsed by wizard is correctly updated after a drive
		// tests the getPathLength() method and by proxy tests the drive1step method to created needed conditions
		// how it works: calls getPathLength() after being driven1step, checks what the getPathLength() return val is now
		// since this is just the first step should be able to move and drive should return true
		// if correct: getPathLength should return a value greater than 0 which is the amount moved, no exception should be thrown
		
		boolean toFail = false;
		try {
			toFail = merlin.drive1Step2Exit();
			assertTrue(toFail);
			assertTrue(merlin.getPathLength() > (float)0);
			
		} catch (Exception e) {
			assertTrue(toFail);
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
	public void finalDriveError() {
		// checks that fianldrive2end works correctly when not at exit
		// tests the fianldrive2end method a
		// how it works: calls final drive2end and gives an input that is not the exit position
		// since this is just the first step should not be the exit
		// if correct: should throw an error because this is not the exit
		
		boolean toFail = false;
		try {
			merlin.finalDrive2End(merlin.robot.getCurrentPosition());
			assertTrue(toFail);
			
		} catch (Exception e) {
			assertFalse(toFail);
		}
	}
	

}
