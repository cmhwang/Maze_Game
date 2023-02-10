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

public class SmartWizardTest extends WizardTest{
	private SmartWizard merlin;
	private ReliableRobot robot;
	private Maze maze;
	private Control controller;
	
	/**
	 * Creates default SmartWizard object with no specification. Also creates a robot object so that the full implementation of wizard can be tested.
	 */
	@Override
	@Before
	public void setUp() {
		robot = new ReliableRobot();
		merlin = new SmartWizard();
		
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
	 * Resets default SmartWizard object with needed cars for testing. makes sure tests don't interfere with each other.
	 */
	@Override
	@After
	public void reset() {
		robot = new ReliableRobot();
		merlin = new SmartWizard();
		
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
	 * Test case: checks that drive1step fails if the robot doesn't have enough energy
	 * <p>
	 * Method under test: tests drive1step functionality and by proxy interaction with robot
	 * <p>
	 * Correct behavior:
	 * exception should be thrown because dead/killed
	 */
	@Override
	@Test
	public final void check1StepFail() {//TODO fill this
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
	 * Method under test: drive1steptoexit method and by proxy test getNeighbor, jump sensing setup, and maze object interaction to generate cars to test with
	 * <p>
	 * Correct behavior:
	 * drive1step returns true, robot travels 1 cell min, no exception thrown
	 */
	@Override
	@Test
	public final void check1StepComplete() {
		// checks that drive1stepToExit works when conditions are all appropriate (full battery and not at exit yet)
		// tests the drive1steptoexit method and by proxy test getNeighbor, jump decision setup, and maze object interaction to generate cars to test with
		// how it works: collects current position, checks neighbor near exit's direction calls drive1step, checks that drive occured, checks that cells travelled has changed
		// now that there is a jump can't predict for sure where robot will go so use cells travelled and see if updated
		// since this is just the first step should be able to move as full battery and not yet exit
		// if correct: drive1step returns true, robot travels 1 cell min, no exception thrown
		
		try {
			int holder = merlin.cellsTravelled;
			try {
				boolean checker = merlin.drive1Step2Exit();
				assertTrue(checker);
				
				assertTrue(holder < merlin.cellsTravelled);
				
								
			} catch (Exception e) {

			}
		} catch (Exception e) {

		}
		
		
	}
	
	/**
	 * Test case: checks that drive1stepToExit works when conditions are all appropriate (full battery and not at exit yet)
	 * <p>
	 * Method under test: drive1steptoexit method and by proxy test getNeighbor and maze object interaction to generate cars to test with
	 * <p>
	 * Correct behavior:
	 * drive1step returns true, position of robot updates correctly to neighborClosestToExit or jump status, no exception thrown
	 */
	@Test
	public final void check1StepCompleteJump() {
		// checks that drive1stepToExit works when conditions are all appropriate (full battery and not at exit yet) and it jumps
		// tests the drive1steptoexit method and that smart wizard assesses properly when a jump is needed
		// how it works: collects current position and cells travelled, checks wall status and get neighbor for proper move, checks that drive occured, checks that position has changed in according to method
		// or if a jump has occured that the number of cells travelled updated
		// since this is just the first step should be able to move as full battery and not yet exit
		// case for if normal move occur: see if it moved into nextPos to exit, if it has send to autopass branch
		// case for if jump occured: if not in the proper nextPos then that means it was a jump, see if the robot cells travelled has changed
		// if correct: drive1step returns true, position of robot updates correctly to neighborClosestToExit or jump status, no exception thrown

		int[] curPos;
		int[] nextPos;
		boolean checker = false;
		int beforeMoveLength = merlin.cellsTravelled;

		
		try {
			curPos = merlin.robot.getCurrentPosition();
			nextPos = merlin.maze.getNeighborCloserToExit(curPos[0], curPos[1]);

			try {
				checker = merlin.drive1Step2Exit();
				assertTrue(checker);
				
				int[] newPos = merlin.robot.getCurrentPosition();
				
				if (newPos[0] == nextPos[0] && nextPos[1] == curPos[1]) {//auto pass branch for if just moved
					assertTrue(checker);
				} else if (merlin.cellsTravelled > beforeMoveLength) {//if moved but it was a jump then autopass based on cells travelled
					assertTrue(checker);
				} else {
					assertFalse(checker);
				}
				
				
								
			} catch (Exception e) {
				assertFalse(checker);
			}
		} catch (Exception e) {
			assertFalse(checker);
		}
		
		
	}
	/**
	 * Test case: checks that drive1stepToExit fails properly if jump is illegal
	 * <p>
	 * Method under test: drive1step2exit, jump decision sensing, and general interaction with energyLevel var
	 * <p>
	 * Correct behavior:
	 * robot should either die due to lack of energy or perform a normal non-jump move 
	 */
	@Test
	public final void check1StepFailJump() {
		// checks that drive1stepToExit fails properly if jump is illegal
		// testing drive1step2exit, jump decision sensing, and general interaction with energyLevel var
		// how it works: set energy level to below 40, gathers current position, call drive1step, sees new position or status
		// if correct: robot should either die or normal non-jump move 
		
		int[] curPos;
		int[] nextPos;
		boolean checker = false;

		
		try {
			curPos = robot.getCurrentPosition();
			nextPos = merlin.maze.getNeighborCloserToExit(curPos[0], curPos[1]);
			merlin.energyUsed = (float) 3490;

			try {
				checker = merlin.drive1Step2Exit();
				if (checker) {//for if it didn't die - it should be in the nextPosition based on get neighbor and not jump
					assertTrue(checker);
					int[] newPos = robot.getCurrentPosition();
					assertTrue(newPos[0] == curPos[0] || newPos[1] == curPos[1]);
				} else {// this means robot died in this move which is a valid possible branch given low energy set
					assertTrue(merlin.robot.hasStopped());
				}
				
				
								
			} catch (Exception e) {//should never get here autofail
				assertTrue(checker);
			}
		} catch (Exception e) {//should never get here autofail
			assertTrue(checker);
		}
		
		
	}
	/**
	 * Test case: checks that drive1stepToExit properly updates energy if jump occurs
	 * <p>
	 * Method under test: drive1steptoexit method for jump path and normal path, energy level interaction
	 * <p>
	 * Correct behavior:
	 * drive1step returns true, and energy updates according to type of move performed
	 */
	@Test
	public final void check1StepJumpEnergy() {
		//checks that drive1stepToExit properly updates energy when jump or normal move occurs
		//tests drive1steptoexit method for jump and normal path, instance var for energy updating and getEnergyConsumption()
		//how it works : gathers current energy, current position, and calls drive 1 step
		// checks what path it went down and sees if energylevel updated to the minimum or more based on path
		// if correct energyconsumption should have increased at level based on path
		
		int[] curPos;
		int[] nextPos;
		float curEnergy;
		boolean checker = false;
	
		try {
			curPos = merlin.robot.getCurrentPosition();
			nextPos = merlin.maze.getNeighborCloserToExit(curPos[0], curPos[1]);
			curEnergy = merlin.getEnergyConsumption();
			

			try {
				checker = merlin.drive1Step2Exit();
				assertTrue(checker);
				
				int[] newPos = merlin.robot.getCurrentPosition();
				if (newPos[0] == nextPos[0] || nextPos[1] == nextPos[1]) {
					assertTrue(curEnergy + (float) 6 < merlin.getEnergyConsumption());//should take minimum 7 energy level changed
				} else {
					assertTrue(curEnergy + (float) 40 < merlin.getEnergyConsumption());//should take minimum 41 energy level changed
				}
				
								
			} catch (Exception e) {//autofail - shouldn't reach this branch since at start point with full energy
				assertTrue(checker);
			}
		} catch (Exception e) {//autofail - shouldn't reach this branch since at start point with full energy
			assertTrue(checker);
		}
		
		
	}
	/**
	 * Test case: checks that drive1stepToExit properly updates energy when jump or normal move occurs
	 * <p>
	 * Method under test: drive1steptoexit method for jump and normal path, instance var for cells updating and getPathLength()
	 * <p>
	 * Correct behavior:
	 * drive1step returns true, and cells updates
	 */
	@Test
	public final void check1StepJumpLength() {
		//checks that drive1stepToExit properly updates energy when jump or normal move occurs
		//tests drive1steptoexit method for jump and normal path, instance var for cells updating and getPathLength()
		//how it works : gathers path length, current position, and calls drive 1 step and tries
		// checks what path it went down (jump or no jump) and sees if pathlength updated to the minimum or more based on path
		// either way (jump or no jump) should be 1 
		// if correct: path length should have increased by 1

		int[] curPos;
		int curLength;
		boolean checker = false;
	
		try {
			curPos = merlin.robot.getCurrentPosition();
			curLength = merlin.getPathLength();
			

			try {
				checker = merlin.drive1Step2Exit();
				assertTrue(checker);
				
				assertEquals(curLength + 1, merlin.getPathLength());
				
								
			} catch (Exception e) {//autofail - shouldn't reach this branch since at start point with full energy
				assertTrue(checker);
			}
		} catch (Exception e) {//autofail - shouldn't reach this branch since at start point with full energy
			assertTrue(checker);
		}
		
		
	}
	
	/**
	 * Test case: checks that the setRobot method for smartwizard works properly and general setup in controller class works
	 * <p>
	 * Method under test: etRobot method, controller class setup, control class interaction, and robot instance variable
	 * <p>
	 * Correct behavior: smartWizard robot should be the robot object we created to use for test cases
	 * 
	 */
	@Override
	@Test
	public final void settingUpRobot() {
		//checks that the setRobot method for smartwizard works properly and general setup in controller class works
		//tests setRobot method, controller class setup, control class interaction, and robot instance variable
		//how it works: since setRobot called in set up, just need to check here that its outcome executed properly
		//if correct: the smartWizard robot should be the robot object we created to use for test cases
		
		assertEquals(merlin.robot, robot);
	}
	
	/**
	 * Test case: checks that the setMaze method for smartwizard works properly and general setup in controller class works
	 * <p>
	 * Method under test: setMaze method, controller class setup, control class interaction, and robot instance variable
	 * <p>
	 * Correct behavior: the smartWizard maze should be the same maze object of this set up's controller
	 * 
	 */
	@Override
	@Test
	public final void settingUpMaze() {
		//checks that the setMaze method for smartwizard works properly and general setup in controller class works
		//tests setMaze method, controller class setup, control class interaction, and robot instance variable
		//how it works: since setMaze called in set up, just need to check here that its outcome executed properly
		//if correct: the smartWizard maze should be the same maze object of this set up's controller	
		
		assertEquals(merlin.maze, robot.controller.getMaze());
	}
	
	/**
	 * Test case: checks that smart wizard functions properly when at the exit
	 * <p>
	 * Method under test: finaldrive2end method, interactions among these methods and with drive1step, and interaction with maze class
	 * <p>
	 * Correct behavior: finaldrive should not throw an exception
	 * 
	 */
	@Override
	@Test
	public final void check1StepAtExit() {
		// checks that smart wizard functions properly when at the exit
		// tests finaldrive2end method, interactions among these methods and with drive1step, and interaction with maze class
		// how it works:gets exit position from the maze, inputs it to finaldrive method with a check to make sure that it's not just happening to face right direction
		// if correct finaldrive should not throw an exception
		boolean forAutoFail = true;
		
		int[] exitPos = merlin.maze.getExitPosition();
		State currentState = controller.currentState;
		try {
			while(merlin.robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)) {
				merlin.finalDrive2End(exitPos);
			}		
		} catch (Exception e) {//autofail - shouldn't reach this branch
			assertFalse(forAutoFail);
		}	
		

		
	}
	
	/**
	 * Test case: checks that energyUsed by smartwizard starts at the right value
	 * <p>
	 * Method under test: getEnegy() method and instance var set up for energy
	 * <p>
	 * Correct behavior:
	 * nergyUsed and getEnergy should both return (float) 0
	 */
	@Override
	@Test
	public void checkEnergyComsumptionStart() {
		// checks that energyUsed by smartwizard starts at the right value
		// tests the getEnegy() method and instance var set up for energy
		// how it works: calls getEnergy() before any movement on wizard's part, checks the instance var for energyUsed against it
		// if correct: energyUsed and getENergy should both return 0
		
		float startingEnergyUsed = merlin.energyUsed;
		float zeroRep = (float) 0;
		assertTrue(zeroRep == startingEnergyUsed);
		assertTrue(merlin.getEnergyConsumption() == startingEnergyUsed);	
		
	}

	/**
	 * Test case: checks that energyUsed by smartwizard is correctly updated after a drive
	 * <p>
	 * Method under test: getEnegy() method and by proxy tests the drive1step method to created needed conditions
	 * <p>
	 * Correct behavior:
	 * nergyUsed should return a value <= (3500 - 7) which is the minimum energyUsed after a step, no exception should be thrown
	 */
	@Override
	@Test
	public final void checkEnergyConsumptionLater() {
		// checks that energyUsed by smartwizard is correctly updated after a drive
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
	 * Test case: recreates testing environment for full drive where it calls drive1step a few times
	 * <p>
	 * Method under test: tests drive2exit by testing drive1step, robot energy setting, robot position updating and recreating gameplay environment
	 * <p>
	 * Correct behavior:no errors thrown, energy expended after drive1step called, new exit position matches whether exit reached status
	 * 
	 */
	@Override
	@Test
	public final void checkFullDriveWorking() {
		// because fullDriveFor smartWizard can't be run in this contained environment, tested by emulating the code for this environment
		// tests drive2exit by testing drive1step, robot energy setting, robot position updating
		// recreates testing environment for full drive where it calls drive1step a few times
		// after drive1step is called - which shouldn't throw an error, energyExpended should increase
		// if the end was not reached within 5 steps then drive1step should have returned true and the new position should not be the exit position
		// special case: if the end was reached then checker should be false after the while loop
		// if correct: no errors thrown, energy expended after drive1step called, new exit position matches whether exit reached status
		
		boolean autoFail = false;
		float startEnergyUsed = merlin.getEnergyConsumption();
		int stepCounter = 0;
		boolean checker = false;
		
		try {
			while (stepCounter < 10) {
				checker = merlin.drive1Step2Exit();
				assertTrue(checker);
				stepCounter++;
			}
			
			assertTrue(startEnergyUsed != merlin.getEnergyConsumption());
			
			int[] curPos;
			
			try {
				curPos = merlin.robot.getCurrentPosition();
				
				if (checker) {
					assertTrue(curPos[0] != merlin.maze.getExitPosition()[0] || curPos[1] == merlin.maze.getExitPosition()[1]);
				} else {
					assertTrue(curPos[0] == merlin.maze.getExitPosition()[0] && curPos[1] == merlin.maze.getExitPosition()[1]);
				}
			} catch (Exception e) {//autofail - shouldn't reach this branch since at start point with full energy
				assertTrue(autoFail);
			}
		} catch (Exception e) {//autofail - shouldn't reach this branch since at start point with full energy
				assertTrue(autoFail);
		}
		
		
		
	
	}
	/**
	 * Test case: checks that full drive throws error properly and fails when robot and energy improperly set
	 * <p>
	 * Method under test: drive2exit method functionality and interaction with robot class
	 * <p>
	 * Correct behavior: drive2exit should throw an error when the connection to the robot is broken and no energy remains
	 * 
	 */
	@Override
	@Test
	public final void checkFullDriveFailed() {
		// checks that full drive throws error properly and fails when robot and energy improperly set
		// tests the drive2exit method functionality and interaction with robot class
		// how it works: breaks the connection between driver and robot
		// for good measure also sets the enegy level below the amount needed to a complete drive
		// then checks that the error branch is properly entered when drive2exit
		// drive2exit should throw an error when the connection to the robot is broken and no energy remains
		
		boolean autoFail = false;
		merlin.robot = null;
		merlin.energyUsed = (float) 3499;
		try {
			merlin.drive2Exit();
			assertTrue(autoFail);
		} catch (Exception e) {
			assertFalse(autoFail);
		}
		
		
	}
	/**
	 * Test case: checks that pathLength travelled by smart wizard starts at the right value
	 * <p>
	 * Method under test: getPathLength() method and instance var set up for cellsTravelled
	 * <p>
	 * Correct behavior: cellsTravelled and getPath() should both return 0
	 * 
	 */
	@Override
	@Test
	public final void checkPathLengthStart() {
	// checks that pathLength travelled by smart wizard starts at the right value
	// tests the getPathLength() method and instance var set up for cellsTravelled
	// how it works: calls getPathLength() before any movement on smartwizard's part, checks the instance var for cellsTravelled against it
	// if correct: cellsTravelled and getPath() should both return 0

		float startingCellsTravelled = merlin.cellsTravelled;
		float zeroRep = (float) 0;
		assertTrue(zeroRep == startingCellsTravelled);
		assertTrue(merlin.getPathLength() == startingCellsTravelled);		
		
	}

	/**
	 * Test case: checks that energyUsed by smartwizard is correctly updated after a drive
	 * <p>
	 * Method under test: getPathLength() method and by proxy tests the drive1step method to created needed conditions
	 * <p>
	 * Correct behavior:
	 * getPathLength should return a value greater than 0 which is the amount moved, no exception should be thrown
	 */
	@Override
	@Test
	public final void checkPathLengthLater() {
		// checks that energyUsed by smartwizard is correctly updated after a drive
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
	@Override
	@Test
	public final void finalDriveError() {
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