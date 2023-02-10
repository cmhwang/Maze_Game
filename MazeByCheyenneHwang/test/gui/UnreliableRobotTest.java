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
import gui.Constants.UserInput;
import gui.Robot.Direction;
import gui.Robot.Turn;



/**
 * Tests individual methods of the UnreliableRobot class. 
 * 
 * 
 * @author Cheyenne Hwang
 *
 */

public class UnreliableRobotTest {
	
	private UnreliableRobot robot;
	private UnreliableSensor testerDefaultSensor;
	private Maze testMaze;
	private Control controller;
	private StatePlaying tempState;
	
	
	/**
	 * Creates default UnreliableRobot object with no specification. Will test setting specification in later tests.
	 */
	@Before
	public void setUp() {
		robot = new UnreliableRobot();
		Wizard wizard = new Wizard();
		
		controller = new Control();
		controller.setRobotAndDriver(robot, wizard);
		
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
        
        UnreliableSensor backSensor = new UnreliableSensor();
        backSensor.setSensorDirection(Direction.BACKWARD);
        backSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(backSensor, Direction.BACKWARD);
        
        UnreliableSensor rightSensor = new UnreliableSensor();
        rightSensor.setSensorDirection(Direction.RIGHT);
        rightSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(rightSensor, Direction.RIGHT);

        controller.driver.setMaze(controller.getMaze());
        controller.driver.setRobot(controller.robot);
        
        controller.robot.startFailureAndRepairProcess(Direction.FORWARD, 4, 2); // starts thread processes
        try {
			Thread.sleep(1300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        controller.robot.startFailureAndRepairProcess(Direction.LEFT, 4, 2);
        try {
			Thread.sleep(1300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        controller.robot.startFailureAndRepairProcess(Direction.RIGHT, 4, 2);
        try {
			Thread.sleep(1300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        controller.robot.startFailureAndRepairProcess(Direction.BACKWARD, 4, 2);
        
		
	}
	
	/**
	 * Resets default ReliableRobot object and controller object so that the ReliableRobot can be tested without interference from other tests 
	 */
	@After
	public void reset() {
		controller.robot.getRobotSensor(Direction.FORWARD).stopFailureAndRepairProcess();// stops the previous thread
		controller.robot.getRobotSensor(Direction.LEFT).stopFailureAndRepairProcess();
		controller.robot.getRobotSensor(Direction.RIGHT).stopFailureAndRepairProcess();
		controller.robot.getRobotSensor(Direction.BACKWARD).stopFailureAndRepairProcess();
		
		robot = new UnreliableRobot();
		Wizard wizard = new Wizard();
		
		controller = new Control();
		controller.setRobotAndDriver(robot, wizard);
		
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
        
        UnreliableSensor backSensor = new UnreliableSensor();
        backSensor.setSensorDirection(Direction.BACKWARD);
        backSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(backSensor, Direction.BACKWARD);
        
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
        
        controller.robot.getRobotSensor(Direction.BACKWARD).startFailureAndRepairProcess(4, 2);
        try {
			Thread.sleep(1300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        controller.robot.getRobotSensor(Direction.RIGHT).startFailureAndRepairProcess(4, 2);
	}
	
	/**
	 * Test case: checks to see if a controller has been properly set for the robot
	 * <p>
	 * Method under test: setController() method and instance variable for controller
	 * <p>
	 * Correct behavior:
	 * should assert that the robot's controller is the controller object created
	 */
	@Test
	public final void isControllerSet() {
		//checks to see if a controller has been properly set for the robot
		//makes sure that the setController() method works and that the instance var for controller works
		//how it does this: since controller already set by set method in instantiation set up (needed for other tests) - checks that its in place by examining instance var
		//if correctly set up it should assert that the robot's controller is the controller object created
		// if wrong then instance variable and setting process are disjointed
		
		assertEquals(robot.controller, controller);
		
	}
	
	/**
	 * Test case: checks to see that all of the instance variable fields are set to their starting values before the game begins
	 * <p>
	 * Method under test: instance variable fields for isDead, energyLevel, robot type and odometer reading, constructor - unreliableRobot()
	 * <p>
	 * Correct behavior:
	 * It is correct if isDead is false, robotType is unreliable energyLevel set to full 3500, and odometer set to 0
	 */
	@Test
	public final void startingInstanceVarsReset() {
		// checks to see that all of the instance variable fields are set to their starting values before the game begins
		// makes sure that instance variable fields for isDead, energyLevel, robot type, and odometer reading are all correct
		// tests the constructor
		// how it works: checks that isDead is false, energyLevel set to full 3500, and odometer set to 0 with assert trues or equals
		// if correct: all of these assert trues should pass, if wrong then constructor faulty and instance variables dysfunctional
		
		assertEquals(robot.isDead, false);
		
		assertTrue(robot.energyLevel == (float)3500);
		
		assertEquals(robot.odometerReading, 0);
		
		assertEquals(robot.getRobotType(), "unreliable");
		
	}
	
	/**
	 * Test case: checks to see that set up for the distance sensors in each direction works
	 * <p>
	 * Method under test: addDistanceSensor() method, setDistanceSensorDirection(), getRobotSensor(), instance vars for the sensors, setup process for distance sensors
	 * <p>
	 * Correct behavior:
	 * each instance variable for the robot's distance sensors should not be null and should be facing the corresponding titled direction
	 */
	@Test
	public final void distanceSensorsAdditions() {
		// checks to see that set up for the distance sensors in each direction works
		// how it works: since the distance sensors have already been added in test set up (needed for other tests) just need to run assertTrues that these instance vars are not null and are facing the corresponding direction
		// then checks that the instance vars for the sensors is not null and are in the correct direction
		// tests that the addDistanceSensor() method works, setDistanceSensorDIr(), getRobotSensor(), works that the instance variable for the sensors works
		// if correct: each instance variable for the robot's distance sensors should not be null and should be facing the corresponding titled direction
		// this is done with assert trues and assert equals for the fields and the sensor's directions
		// if failure: this means that the addition process is faulty, there's a disconnect in the process
		
		assertTrue(robot.forwardSensor != null);
		assertEquals(robot.getRobotSensor(Direction.FORWARD).getSensorRelativeDirection(), Direction.FORWARD);
		
		assertTrue(robot.leftSensor != null);
		assertEquals(robot.getRobotSensor(Direction.LEFT).getSensorRelativeDirection(), Direction.LEFT);
		
		assertTrue(robot.backSensor != null);
		assertEquals(robot.getRobotSensor(Direction.BACKWARD).getSensorRelativeDirection(), Direction.BACKWARD);
		
		assertTrue(robot.rightSensor != null);
		assertEquals(robot.getRobotSensor(Direction.RIGHT).getSensorRelativeDirection(), Direction.RIGHT);
		
		 
		
	}
	
	/**
	 * Test case: checks to see that the position retrieval method works and that the robot is correctly interacting with the control object's maze field
	 * <p>
	 * Method under test: getCurrentPosition(), double checks on setController, general interaction with control object functional
	 * <p>
	 * Correct behavior:
	 * getCurrentPosition() should return an int[] object that is within the bounds of the maze's height and width
	 */
	@Test
	public final void currentPostionRetrievalCorrect() {
		// checks to see that the position retrieval method works and that the robot is correctly interacting with the control object's maze field
		// tests that the getCurrentPosition() method works, double checks on setController, and the general interaction with control object functional
		// how it works: check that it works by calling getPosition method and checking that the returned value is in valid parameters
		// checks that exception is not thrown and it is not null by making sure toCheck is reinitialized and that process occured
		// if correct: should return an int[] object that is within the bounds of the maze's hieght and width
		// if it fails that means interaction with maze object through getposition method is wrong
		
		int[] toCheck = new int[]{-1, -1};
		
		try {
			toCheck = robot.getCurrentPosition();
			assertTrue(toCheck[0] >= 0 && toCheck[0] < controller.getMaze().getWidth());
			assertTrue(toCheck[1] >= 0 && toCheck[1] < controller.getMaze().getHeight());
			
		} catch (Exception e) {
			assertTrue(toCheck[0] < 0); // this should fail because we shouldn't even get to this point
		}
	}
	
	/**
	 * Test case: checks to see that the position retrieval method works and that the robot is correctly interacting with the control object's maze field
	 * <p>
	 * Method under test: getCurrentPosition(), double checks on setController, general interaction with control object functional
	 * <p>
	 * Correct behavior:
	 * getCurrentPosition() should return an int[] object that is within the bounds of the maze's height and width
	 */
	@Test
	public final void currentDirectionRetrieval() {
		// checks to see that the current CD retrieval method works and that the robot is correctly interacting with the control object
		// tests that the getCurrentDirection() method works, double checks on setController, and the general interaction with control object functional
		// how it works: check that it works by calling getCD method and checking 
		// if correct: should return an CD object of some type
		// if it fails that means interaction with maze object through getCD method wrong
		CardinalDirection toCheck = robot.getCurrentDirection();
		
		assertTrue(toCheck == CardinalDirection.North || toCheck == CardinalDirection.West || toCheck == CardinalDirection.South || toCheck == CardinalDirection.East);
	}
	
	/**
	 * Test case: checks to see that the position retrieval method works and calls an error if the maze object hasn't been properly established
	 * <p>
	 * Method under test: getCurrentPosition() method works, and the general interaction with control object functional
	 * <p>
	 * Correct behavior:
	 * should throw an exception that the get current position is null which is checked by seeing if it went along the exception caught branch of the code
	 */
	@Test
	public final void currentPostionRetrievalWhenNull() {
		// checks to see that the position retrieval method works and calls an error if the maze object hasn't been properly established
		// tests that the getCurrentPosition() method works, and the general interaction with control object functional
		// how it works: check that it works by breaking the maze object's connection to the control object, calling getPosition method, and checking on the result
		// makes sure that getCurrentPosition is not successfully run with a boolean that is or isn't updated
		// if correct: should throw an exception that the get current position is null
		// if it fails that means error catching on get position is wrong
		robot.controller = null;
		
		boolean errorThrown = true;
		
		try {
			int[] toCheck = robot.getCurrentPosition();
			assertFalse(errorThrown);
			
		} catch (Exception e) {
			assertTrue(errorThrown);// always fails because shouldn't reach this point
		}
		
	}
	
	/**
	 * Test case: checks that batteryLevel retrieval method works at the beginning before movement occured
	 * <p>
	 * Method under test: getBatterylevel() method and double checks energylevel instance var setup
	 * <p>
	 * Correct behavior:
	 * getBattery should return a float for 3500, the initial energy
	 */
	@Test
	public final void batteryLevelRetrieval() {
		// checks that batteryLevel retrieval method works at the beginning before movement occured
		// tests the getBatterylevel() method and double checks energylevel instance var setup
		// how it works: calls getbattery, checks that it is the default value with an assert true
		// if correct: getBattery should return a float for 3500, the initial energy
		// failure means getBattery is disconnected from the instance var for battery
		
		Float toCheck = robot.getBatteryLevel();
		assertTrue(toCheck == (float) 3500);
	}
	
	/**
	 * Test case: checks that getEnergyForStepForward() retrieval method works
	 * <p>
	 * Method under test: calls getEnergy() and checks that its the correct amount with assertTrue
	 * <p>
	 * Correct behavior:
	 * etEnergy() should return a float for 6
	 */
	@Test
	public final void stepForwardEnergyRetrieval() {
		// checks that getEnergyForStepForward() retrieval method works
		// how it works: calls getEnergy() and checks that its the correct amount with assertTrue
		// if correct: getEnergy() should return a float for 6
		
		Float toCheck = robot.getEnergyForStepForward();
		assertTrue(toCheck == (float) 6);
	}
	
	/**
	 * Test case: checks that batteryLevel retrieval method works after energy costing things have been performed
	 * <p>
	 * Method under test: mostly tests the getBatterylevel() method, energylevel var functionality, and the energy consumption aspects of move and turn
	 * by proxy double checks but not focused on distance to wall checker, and turn operations
	 * <p>
	 * Correct behavior:
	 * getBattery should return a float less than or equal to 3491, the minimum costs for a turn and move (may need a jump if wall, and a distance sense operation so potential for less)
	 */
	@Test
	public final void adjustableBattery() {
		// checks that batteryLevel retrieval method works after energy costing things have been performed
		// how it works: forces robot to move and turn, makes sure the energyLevel retrieved by method is correct to what the costs should be
		// because I just need to check battery adjustment need to make sure I don't crash into wall here so also makes sure that movement isn't towards wall
		// mostly tests the getBatterylevel() method, energylevel var functionality, and the energy consumption aspects of move and turn
		// by proxy double checks but not focused on distance to wall checker, and turn operations
		// if correct: getBattery should return a float less than or equal to 3491, the costs for a turn and move 
		// failure means getBattery is disconnected from the instance var for battery and not collecting updated value, also means instance var for battery dysfunctional and not updated
		float toCheck;
		
		
		if(robot.distanceToObstacle(Direction.FORWARD) != 0) {
			robot.move(1);
			robot.rotate(Robot.Turn.LEFT);
			
			toCheck = robot.getBatteryLevel();
			assertTrue(toCheck <= (float) 3491);
		} else if(robot.distanceToObstacle(Direction.LEFT) != 0) {
			robot.rotate(Robot.Turn.LEFT);
			robot.move(1);
			
			toCheck = robot.getBatteryLevel();
			assertTrue(toCheck <= (float) 3491);
		} else if(robot.distanceToObstacle(Direction.BACKWARD) != 0) {
			robot.rotate(Robot.Turn.AROUND);
			robot.move(1);
			
			toCheck = robot.getBatteryLevel();
			assertTrue(toCheck <= (float) 3491);
		}else if(robot.distanceToObstacle(Direction.RIGHT) != 0) {
			robot.rotate(Robot.Turn.RIGHT);
			robot.move(1);
			
			toCheck = robot.getBatteryLevel();
			assertTrue(toCheck <= (float) 3491);
		}
		
	}
	
	/**
	 * Test case: checks that batteryLevel retrieval method works at the end when the robot should be dead or out of battery
	 * <p>
	 * Method under test: getBatterylevel() method and double checks energylevel instance var setup
	 * <p>
	 * Correct behavior:
	 * getBattery should return a float for 0 or a value less than 0
	 */
	
	@Test
	public final void deadBattery() {
		// checks that batteryLevel retrieval method works at the end when the robot should be dead or out of battery
		// tests the getBatterylevel() method and double checks energylevel instance var setup
		// how it works: set energyLevel to 0, see if the getBatteryLevel() can recognize this and return a valid value for this scenario
		// if correct: getBattery should return a float for 0 or a value less than 0
		// failure means getBattery is disconnected from the instance var for battery
		robot.energyLevel = (float) -1;
		
		float toCheck = robot.getBatteryLevel();
		assertTrue(toCheck <= (float)0);
	}
	
	/**
	 * Test case: checks that batteryLevel adjustor method works properly
	 * <p>
	 * Method under test: getBatterylevel() method and double checks energylevel instance var setup
	 * <p>
	 * Correct behavior:
	 * getBattery should return a float for 0 or a value less than 0
	 */
	
	@Test
	public final void batterySet() {
		// checks that batteryLevel adjustor method works properly
		// tests the setBatterylevel() method and double checks energylevel instance var setup
		// by proxy checks get battery level method which is independently checked in above tests
		// how it works: calls setbatterylevel and then checks that the energyLevel var now matches that
		// if correct: getBattery should return the float 500 which is what setEnergy put it to
		// failure means setBattery is disconnected from the instance var for battery
		float testVal = (float) 500;
		robot.setBatteryLevel(testVal);
		
		float toCheck = robot.getBatteryLevel();
		assertTrue(toCheck == testVal);
	}
	
	/**
	 * Test case: checks that the energylevel var is properly updated after a rotation occurs and that the float being returned by getEnergyForRotation written and used correctly
	 * <p>
	 * Method under test: getEnergyForFullRotation() method mostly and rotate's call of it by proxy
	 * <p>
	 * Correct behavior:
	 * energyLevel should be 3497 after left turn, 3491 after the around turn, and 3488 after a right turn
	 */
	@Test
	public final void correctEnergyForRotation() {
		// checks that the energylevel var is properly updated after a rotation occurs and that the float being returned by getEnergyForRotation written and used correctly
		// tests the getEnergyForFullRotation() method mostly and rotate's call of it by proxy
		// how it works: has the robot turns and then check that the energyLevel var is correspondingly
		// check for energy after turn left, around, and right with a assert statement after each
		// if correct: energyLevel should be 3497 after left turn, 3491 after the around turn, and 3488 after a right turn
		// if failure then the getEnergyForFull rotation is not returning correct and not being implemented correctly in rotate operations
		
		robot.rotate(Robot.Turn.LEFT);
		assertTrue(robot.energyLevel <= (float) 3497);
		
		robot.rotate(Robot.Turn.AROUND);
		assertTrue(robot.energyLevel <= (float) 3491);
		
		robot.rotate(Robot.Turn.RIGHT);
		assertTrue(robot.energyLevel <= (float) 3488);
	}
	
	/**
	 * Test case: checks that the energylevel var is properly updated after a move occurs and that the float being returned by getEnergyForStep written and used correctly
	 * <p>
	 * Method under test: getEnergyForStepForward() method mostly and move's call of it by proxy		
	 * <p>
	 * Correct behavior:
	 * energyLevel should be less than or equal to 3,494 after (less than is possible if turn was needed)
	 */
	@Test
	public final void correctEnergyForMovement() {
		// checks that the energylevel var is properly updated after a move occurs and that the float being returned by getEnergyForStep written and used correctly
		// tests the getEnergyForStepForward() method mostly and move's call of it by proxy		
		// how it works: has the robot turns and then check that the energyLevel var is correspondingly
		// since I am focusing on just the retrieval of info after a move need to make sure not hitting any walls
		// check for energy after a move forward 
		// if correct: energyLevel should be less than or equal to 3,494 after (less than is possible if turn was needed)
		// if failure then the getEnergyForStepForward is not returning correct and not being implemented correctly in move operations
		
		float toCheck;
		
		
		if(robot.distanceToObstacle(Direction.FORWARD) != 0) {
			robot.move(1);
			
			toCheck = robot.energyLevel;
			assertTrue(toCheck == (float) 3494);
		} else if(robot.distanceToObstacle(Direction.LEFT) != 0) {
			robot.rotate(Robot.Turn.LEFT);
			robot.move(1);
			
			toCheck = robot.energyLevel;
			assertTrue(toCheck == (float) 3491);
		} else if(robot.distanceToObstacle(Direction.BACKWARD) != 0) {
			robot.rotate(Robot.Turn.AROUND);
			robot.move(1);
			
			toCheck = robot.energyLevel;
			assertTrue(toCheck == (float) 3491); 
		}else if(robot.distanceToObstacle(Direction.RIGHT) != 0) {
			robot.rotate(Robot.Turn.RIGHT);
			robot.move(1);
			
			toCheck = robot.energyLevel;
			assertTrue(toCheck == (float) 3491);
		}
	}
	
	/**
	 * Test case: checks that the odometer is being correctly updates and can be properly retreived by the odometer information retreival method
	 * <p>
	 * Method under test: focuses on the getOdometerReading() method and odometerReading vars, by proxy also checks that move is correctly working
	 * <p>
	 * Correct behavior:
	 * should return 1 after 1 move and 2 the second move
	 */
	@Test
	public final void odomoterPickingUpDistance() {
		// checks that the odometer is being correctly updates and can be properly retreived by the odometer information retreival method
		// test focuses on the getOdometerReading() method and odometerReading vars, by proxy also checks that move is correctly working
		// how it works: moves the robot, then checks that the odometer has the updates reading, moves robot again and checks again for good measure
		// since i need to test the move method without the bot crashing need to check that no walls in place
		// if correct should return 1 after 1 move and 2 the second move
		// if failure then getOdomoter disconnected from the instance var, instance var also implemented wrong
		
		int toCheck;
		if(robot.distanceToObstacle(Direction.FORWARD) != 0) {
			robot.move(1);
			
			toCheck = robot.getOdometerReading();
			assertTrue(toCheck >= 1);
		} else if(robot.distanceToObstacle(Direction.LEFT) != 0) {
			robot.rotate(Robot.Turn.LEFT);
			robot.move(1);
			
			toCheck = robot.getOdometerReading();
			assertTrue(toCheck >= 1);
		} else if(robot.distanceToObstacle(Direction.BACKWARD) != 0) {
			robot.rotate(Robot.Turn.AROUND);
			robot.move(1);
			
			toCheck = robot.getOdometerReading();
			assertTrue(toCheck >= 1);
		}else if(robot.distanceToObstacle(Direction.RIGHT) != 0) {
			robot.rotate(Robot.Turn.RIGHT);
			robot.move(1);
			
			toCheck = robot.getOdometerReading();
			assertTrue(toCheck >= 1);
		}
		
		//second move
		if(robot.distanceToObstacle(Direction.FORWARD) != 0) {
			robot.move(1);
			
			toCheck = robot.getOdometerReading();
			assertTrue(toCheck >= 2);
		} else if(robot.distanceToObstacle(Direction.LEFT) != 0) {
			robot.rotate(Robot.Turn.LEFT);
			robot.move(1);
			
			toCheck = robot.getOdometerReading();
			assertTrue(toCheck >= 2);
		} else if(robot.distanceToObstacle(Direction.BACKWARD) != 0) {
			robot.rotate(Robot.Turn.AROUND);
			robot.move(1);
			
			toCheck = robot.getOdometerReading();
			assertTrue(toCheck >= 2);
		}else if(robot.distanceToObstacle(Direction.RIGHT) != 0) {
			robot.rotate(Robot.Turn.RIGHT);
			robot.move(1);
			
			toCheck = robot.getOdometerReading();
			assertTrue(toCheck >= 2);
		}
	}
	
	/**
	 * Test case: checks that odometer can be reset, odometer is being correctly updated and can be properly retreived by the odometer information retreival method
	 * <p>
	 * Method under test: ocuses on the odometerReest() method and odometerReading var, by proxy also checks that move is correctly working
	 * <p>
	 * Correct behavior:
	 * should return 1 after 1 move and 0 after the reset
	 */
	@Test
	public final void isOdometerResettable() {
		// checks that odometer can be reset, odometer is being correctly updated and can be properly retreived by the odometer information retreival method
		// test focuses on the odometerReest() method and odometerReading var, by proxy also checks that move is correctly working
		// how it works: moves the robot, then checks that the odometer has the updates reading, then calls the reset and checks that odometer reading again
		// since i need to test the move method without the bot crashing need to check that no walls in place
		// if correct should return 1 after 1 move and 0 after the reset
		// if failure then reset is not resetting the instance var properly
		
		int toCheck;
		if(robot.distanceToObstacle(Direction.FORWARD) != 0) {
			robot.move(1);
			
			toCheck = robot.getOdometerReading();
			assertEquals(toCheck, 1);
		} else if(robot.distanceToObstacle(Direction.LEFT) != 0) {
			robot.move(1);
			
			toCheck = robot.getOdometerReading();
			assertEquals(toCheck, 1);
		} else if(robot.distanceToObstacle(Direction.BACKWARD) != 0) {
			robot.move(1);
			
			toCheck = robot.getOdometerReading();
			assertEquals(toCheck, 1);
		}else if(robot.distanceToObstacle(Direction.RIGHT) != 0) {
			robot.move(1);
			
			toCheck = robot.getOdometerReading();
			assertEquals(toCheck, 1);
		}
		
		//reset here
		robot.resetOdometer();
		assertEquals(robot.odometerReading, 0);
	}
	
	/**
	 * Test case: checks that the rotate method actually turns the robot in the correct direction
	 * <p>
	 * Method under test: mostly focuses on the rotate method but by proxy also checks instance vars for direction faced,
	 * also utilizes getCD() method which is independently checked in another test 
	 * <p>
	 * Correct behavior:
	 * should have a correspondingly updated CD for the turn type and energy level
	 */
	@Test
	public final void rotateSuccess() {
		// checks that the rotate method actually turns the robot in the correct direction
		// test mostly focuses on the rotate method but by proxy also checks instance vars for direction faced
		// also utilizes getCD() method which is independently checked in another test 
		// how it works: gathers the initial CD faced, calls rotate, checks that the current CD faced has been updated
		// repeats for each potential type of turn
		// if correct: should have a correspondingly updated energy level based on turn type
		// if failure: the rotate method is not actually rotating and updating the vars for direction faced properly
		
		CardinalDirection ogCarDir = controller.robot.getCurrentDirection(); 
		CardinalDirection newCarDir;
		
		if (ogCarDir == CardinalDirection.North) {
			controller.robot.rotate(Turn.LEFT);
			assertTrue(robot.energyLevel < 3500f);

		} else if (ogCarDir == CardinalDirection.West) {
			controller.robot.rotate(Turn.LEFT);
			assertTrue(robot.energyLevel < 3500f);

		} else if (ogCarDir == CardinalDirection.South) {
			controller.robot.rotate(Turn.LEFT);
			assertTrue(robot.energyLevel < 3500f);

		} else if (ogCarDir == CardinalDirection.East){
			controller.robot.rotate(Turn.LEFT);
			assertTrue(robot.energyLevel < 3500f);

		}
		
		//repeat for turn around
		ogCarDir = robot.getCurrentDirection(); 
		robot.rotate(Turn.AROUND);
		
		if (ogCarDir == CardinalDirection.North) {
			assertTrue(robot.energyLevel < 3492f);
		} else if (ogCarDir == CardinalDirection.West) {
			assertTrue(robot.energyLevel < 3492f);
		} else if (ogCarDir == CardinalDirection.South) {
			assertTrue(robot.energyLevel < 3492f);
		} else {
			assertTrue(robot.energyLevel < 3492f);
		}
		
		//repeat for turn right
		ogCarDir = robot.getCurrentDirection(); 
		robot.rotate(Robot.Turn.RIGHT);
				
		if (ogCarDir == CardinalDirection.North) {
			assertTrue(robot.energyLevel < 3489f);
		} else if (ogCarDir == CardinalDirection.West) {
			assertTrue(robot.energyLevel < 3489f);
		} else if (ogCarDir == CardinalDirection.South) {
			assertTrue(robot.energyLevel < 3489f);
		} else {
			assertTrue(robot.energyLevel < 3489f);
		}
		
		
	}
	
	/**
	 * Test case: checks that rotate method doesn't take place if the robot is dead
	 * <p>
	 * Method under test: rotate method's ability to interact with robot's energyLevel
	 * <p>
	 * Correct behavior:
	 * the orginal cd should match the cd after rotate is called because nothing should have happenned
	 */
	@Test
	public final void rotateDie() {
		// checks that rotate method doesn't take place if the robot is dead
		// tests the rotate method's ability to interact with robot's energyLevel
		// how it works: set energy to 0, check the robot's cd, call rotate, check the cd again 
		// if correct the orginal cd should match the cd after rotate is called because nothing should have happenned
		// if failure that means rotate isn't correctly interacting with energy var
		
		CardinalDirection ogCarDir = robot.getCurrentDirection();
		robot.energyLevel = 0;
		
		robot.rotate(Robot.Turn.LEFT);
		CardinalDirection carDirToCheck = robot.getCurrentDirection();
		
		assertEquals(ogCarDir, carDirToCheck);
	}
	
	/**
	 * Test case: checks that the move method actually moves the robot's position
	 * <p>
	 * Method under test: mostly focuses on the move method
	 * by proxy also checks getCurPos() and distanceToObstacle methods which are independently checked in other methods
	 * <p>
	 * Correct behavior:
	 * should have a correspondingly updated position and energy for direction faced, no exception should be thrown
	 */
	@Test
	public final void moveSuccess() {
		// checks that the move method actually moves the robot's position
		// test mostly focuses on the move method 
		// by proxy also checks getCurPos() and distanceToObstacle methods which are independently checked in other methods
		// how it works: gathers the initial position and direction, calls move, checks that new position different and corresponds to forward direction
		// since not testing crash mechanism here may need to call turn, distance to wall, and update forward direction
		// also since exception should not be thrown here, auto fail is sent down that branch
		// if correct: should have a correspondingly updated position and energy level for direction faced, no exception should be thrown
		// if failure: the move method is not actually moving and interacting with position/direction vars and control object correctly
		try {
			int[] curPos = robot.getCurrentPosition();
			CardinalDirection dirToMove;
			int[] newPos;
			
			if(robot.distanceToObstacle(Direction.FORWARD) != 0) {
				dirToMove = robot.getCurrentDirection();
				robot.move(1);
				
				newPos = robot.getCurrentPosition();
				if (dirToMove == CardinalDirection.North) {
					assertTrue(robot.energyLevel <= 34994f);
				} else if (dirToMove == CardinalDirection.West) {
					assertTrue(robot.energyLevel <= 34994f);
				} else if (dirToMove == CardinalDirection.South) {
					assertTrue(robot.energyLevel <= 34994f);
				} else {
					assertTrue(robot.energyLevel <= 34994f);
				}
				
			} else if(robot.distanceToObstacle(Direction.LEFT) != 0) {
				robot.rotate(Robot.Turn.LEFT);
				dirToMove = robot.getCurrentDirection();
				robot.move(1);
				
				newPos = robot.getCurrentPosition();
				if (dirToMove == CardinalDirection.North) {
					assertTrue(robot.energyLevel <= 34991f);
				} else if (dirToMove == CardinalDirection.West) {
					assertTrue(robot.energyLevel <= 34991f);
				} else if (dirToMove == CardinalDirection.South) {
					assertTrue(robot.energyLevel <= 34991f);
				} else {
					assertTrue(robot.energyLevel <= 34991f);
				}
				
			} else if(robot.distanceToObstacle(Direction.BACKWARD) != 0) {
				robot.rotate(Robot.Turn.AROUND);
				dirToMove = robot.getCurrentDirection();
				robot.move(1);
				
				newPos = robot.getCurrentPosition();
				if (dirToMove == CardinalDirection.North) {
					assertTrue(robot.energyLevel <= 34988f);
				} else if (dirToMove == CardinalDirection.West) {
					assertTrue(robot.energyLevel <= 34988f);
				} else if (dirToMove == CardinalDirection.South) {
					assertTrue(robot.energyLevel <= 34988f);
				} else {
					assertTrue(robot.energyLevel <= 34988f);
				}
				
			}else {
				robot.rotate(Robot.Turn.RIGHT);
				dirToMove = robot.getCurrentDirection();
				robot.move(1);
				
				newPos = robot.getCurrentPosition();
				if (dirToMove == CardinalDirection.North) {
					assertTrue(robot.energyLevel <= 34991f);
				} else if (dirToMove == CardinalDirection.West) {
					assertTrue(robot.energyLevel <= 34991f);
				} else if (dirToMove == CardinalDirection.South) {
					assertTrue(robot.energyLevel <= 34991f);
				} else {
					assertTrue(robot.energyLevel <= 34991f);
				}
			}
			
		} catch (Exception e) {
			boolean toFail = false;
			assertTrue(toFail);
		}
		
		
	}
	
	/**
	 * Test case: checks that the move method dies correctly in crash scenario
	 * <p>
	 * Method under test: focuses on the move method 
	 * by proxy also checks hasStopped() and distanceToObstacle methods which are independently checked in other methods
	 * <p>
	 * Correct behavior:
	 * robot should crash and thus position should be unchanged as robot hiw wall
	 */
	@Test
	public final void moveCrash() {
		// checks that the move method dies correctly in crash scenario
		// test mostly focuses on the move method 
		// by proxy also checks hasStopped() and distanceToObstacle methods which are independently checked in other methods
		// how it works: find if wall is in a specific direction, crashes into the wall with move and turn methods, checks the isDead
		// if no wall need to repeat move until there is a wall in one direction
		// needs a for loop to move the max number of times possible (max between ,aze width and height)
		// also since exception should not be thrown here, auto fail is sent down that branch
		// if correct: robot should crash and thus position should be unchanged as robot hiw wall
		// if failure: the move method is not actually crashing correctly
		
		boolean toFail = false;
		try {
			int[] curPos = robot.getCurrentPosition();
			int maxVal = robot.controller.getMaze().getHeight();
			if (robot.controller.getMaze().getHeight() < robot.controller.getMaze().getWidth()) {
				maxVal = robot.controller.getMaze().getWidth();
			}
			
			for (int i = 0; i < maxVal; i++) {
				if(robot.distanceToObstacle(Direction.FORWARD) == 0) {
					robot.move(1);
					
					assertEquals(curPos[0], robot.getCurrentPosition()[0]);
					assertEquals(robot.hasStopped(), true);
					toFail = true;
					
				} else if(robot.distanceToObstacle(Direction.LEFT) == 0) {
					robot.rotate(Robot.Turn.LEFT);
					robot.move(1);
					
					assertEquals(curPos[0], robot.getCurrentPosition()[0]);
					toFail = true;
					
				} else if(robot.distanceToObstacle(Direction.BACKWARD) == 0) {
					robot.rotate(Robot.Turn.AROUND);
					robot.move(1);
					
					assertEquals(curPos[0], robot.getCurrentPosition()[0]);
					assertEquals(robot.hasStopped(), true);
					toFail = true;
					
				}else if(robot.distanceToObstacle(Direction.RIGHT) == 0) {
					robot.rotate(Robot.Turn.RIGHT);
					robot.move(1);
					
					assertEquals(curPos[0], robot.getCurrentPosition()[0]);
					assertEquals(robot.hasStopped(), true);
					toFail = true;
				}
				robot.move(1);
			}
			
			if(!toFail) {
				assertTrue(toFail); // shouldn't reach this point so auto-fail
			}
			
			
		} catch (Exception e) {
			toFail = true;
			assertTrue(toFail);
		}
	}
	
	/**
	 * Test case: checks that move doesn't take place if the robot doesn't have enough energy to do a move
	 * <p>
	 * Method under test: move method's ability to interact with robot's energyLevel
	 * by proxy also checks that energyLevel and isDead instance vars
	 * <p>
	 * Correct behavior:
	 * if correct robot should be dead after energylevel lowered and the move called
	 */
	@Test
	public final void moveDie() {
		// checks that move doesn't take place if the robot doesn't have enough energy to do a move
		// tests the move method's ability to interact with robot's energyLevel
		// by proxy also checks that energyLevel and isDead instance vars
		// how it works: set energy to value less than 6, call move, check isDead var 
		// since not testing crash, needs to make sure not trying to move into wall otherwise fail for other reasons
		// if correct robot should be dead after the move called
		// if failure that means move isn't correctly interacting with energy var
		
		robot.energyLevel = (float) 5;
		if(robot.distanceToObstacle(Direction.FORWARD) != 0) {
			robot.move(1);
			
			assertEquals(robot.isDead, true);
		} else if(robot.distanceToObstacle(Direction.LEFT) != 0) {
			robot.rotate(Robot.Turn.LEFT);
			robot.move(1);
			
			assertEquals(robot.isDead, true);
		} else if(robot.distanceToObstacle(Direction.BACKWARD) != 0) {
			robot.rotate(Robot.Turn.AROUND);
			robot.move(1);
			
			assertEquals(robot.isDead, true);
		}else if(robot.distanceToObstacle(Direction.RIGHT) != 0) {
			robot.rotate(Robot.Turn.RIGHT);
			robot.move(1);
			
			assertEquals(robot.isDead, true);
		}
		
	}
	
	/**
	 * Test case: checks that if illegal parameter given to move method then exception is called
	 * <p>
	 * Method under test: move method
	 * <p>
	 * Correct behavior:
	 * exception branch should be moved into which contains an autopass test
	 */
	@Test
	public final void moveIllegal() {
		// checks that if illegal parameter given to move method then exception is called
		// test move method functionality
		// how it works: gives move method a negative var and calls it, make sure exception branch is called on with an auto fail/auto pass test for the branches
		// if correct: exception branch should be moved into which contains an autopass test
		boolean toFail = true;
		try {
			robot.move(-1);
			assertFalse(toFail);
			
		} catch(Exception e) {
			assertTrue(toFail);
		}
		
	}
	
	/**
	 * Test case: checks that move doesn't take place if the robot doesn't have enough energy to do a jump
	 * <p>
	 * Method under test: jump method's ability to interact with robot's energyLevel
	 * by proxy also checks that energyLevel and isDead instance vars
	 * <p>
	 * Correct behavior:
	 * robot should be dead after jump called, no exception should be called
	 */
	@Test
	public final void jumpDie() {
		// checks that move doesn't take place if the robot doesn't have enough energy to do a jump
		// tests the jump method's ability to interact with robot's energyLevel
		// by proxy also checks that energyLevel and isDead instance vars
		// rotate is also needed here
		// how it works: set energy to value less than 40, call jump, check isDead var 
		// also since not testing illegal bounds double checks that not at border at start and moves accordingly if needed
		// if correct robot should be dead after jump called, no exception should be called
		// if failure that means jump isn't correctly interacting with energy var
		
		try {
			int[] curPos = robot.getCurrentPosition();
			CardinalDirection curCarDir = robot.getCurrentDirection();
			robot.energyLevel = (float)39;
			
			if (curPos[1] == 0 && curCarDir == CardinalDirection.North) {
				robot.rotate(Robot.Turn.AROUND);
				robot.jump();
				
				assertEquals(robot.isDead, true);
			} else if (curPos[0] == 0 && curCarDir == CardinalDirection.West) {
				robot.rotate(Robot.Turn.AROUND);
				robot.jump();
				
				assertEquals(robot.isDead, true);
			} else if (curPos[1] == (controller.getMaze().getHeight() - 1) && curCarDir == CardinalDirection.South) {
				robot.rotate(Robot.Turn.AROUND);
				robot.jump();
				
				assertEquals(robot.isDead, true);
			} else if (curPos[0] == (controller.getMaze().getWidth() - 1) && curCarDir == CardinalDirection.East) {
				robot.rotate(Robot.Turn.AROUND);
				robot.jump();
				
				assertEquals(robot.isDead, true);
			} else {
				robot.jump();
				
				assertEquals(robot.isDead, true);
			}
			
		} catch (Exception e) {
			boolean toFail = true;
			assertFalse(toFail);
		}
		
	}
	
	/**
	 * Test case: checks that the jump method actually moves the robot's position
	 * <p>
	 * Method under test: focuses on the jump method 
	 * by proxy also checks getCurPos() and distanceToObstacle methods which are independently checked in other methods
	 * <p>
	 * Correct behavior:
	 * should have a correspondingly updated position for direction faced after jump, no exception should be thrown
	 */
	@Test
	public final void jumpSuccess() {
		// checks that the jump method actually moves the robot's position
		// test mostly focuses on the jump method 
		// by proxy also checks getCurPos() and distanceToObstacle methods which are independently checked in other methods
		// how it works: gathers the initial position and direction, calls move, checks that new position different and corresponds to forward direction
		// since not testing illegal jump here may need to call turn and check if not border wall
		// this means I may need to check, move and repeat check process
		// also since exception should not be thrown here, auto fail is sent down that branch
		// if correct: should have a correspondingly updated position and energy level for direction faced, no exception should be thrown
		// if failure: the move method is not actually moving and interacting with position/direction vars and control object correctly
		boolean toFail = true;
		try {
			int[] curPos = robot.getCurrentPosition();
			CardinalDirection dirToMove;
			int[] newPos;
			
			
			int maxVal = robot.controller.getMaze().getHeight();
			if (robot.controller.getMaze().getHeight() < robot.controller.getMaze().getWidth()) {
				maxVal = robot.controller.getMaze().getWidth();
			}
			
			for (int i = 0; i < maxVal; i++) {
				if(robot.distanceToObstacle(Direction.FORWARD) == 0) {
					dirToMove = robot.getCurrentDirection();
					if (dirToMove == CardinalDirection.North && curPos[1] == 0) {// border cases
						robot.rotate(Robot.Turn.AROUND);
					} else if (dirToMove == CardinalDirection.West && curPos[0] == 0){ 
						robot.rotate(Robot.Turn.AROUND);
					} else if (dirToMove == CardinalDirection.South && curPos[1] == (controller.getMaze().getHeight() - 1)){ 
						robot.rotate(Robot.Turn.AROUND);
					} else if (dirToMove == CardinalDirection.East && curPos[0] == (controller.getMaze().getWidth() - 1)){
						robot.rotate(Robot.Turn.AROUND);
					} else {
						robot.jump();
						
						newPos = robot.getCurrentPosition();
						if (dirToMove == CardinalDirection.North) {
							assertTrue(robot.energyLevel <= 3460f);
							assertTrue(curPos[0] == newPos[0] && curPos[1] == newPos[1] - 1);
							toFail = false;
						} else if (dirToMove == CardinalDirection.West) {
							assertTrue(robot.energyLevel <= 3460f);
							assertTrue(curPos[0] == newPos[0] - 1 && curPos[1] == newPos[1]);
							toFail = false;
						} else if (dirToMove == CardinalDirection.South) {
							assertTrue(robot.energyLevel <= 3460f);
							assertTrue(curPos[0] == newPos[0] && curPos[1] == newPos[1] + 1);
							toFail = false;
						} else {
							assertTrue(robot.energyLevel <= 3460f);
							assertTrue(curPos[0] == newPos[0] + 1 && curPos[1] == newPos[1]);
							toFail = false;
						}
					}
					
					
				} else if(robot.distanceToObstacle(Direction.LEFT) == 0) {
					robot.rotate(Robot.Turn.LEFT);
					dirToMove = robot.getCurrentDirection();
					if (dirToMove == CardinalDirection.North && curPos[1] == 0) {// border cases
						robot.rotate(Robot.Turn.AROUND);
					} else if (dirToMove == CardinalDirection.West && curPos[0] == 0){ 
						robot.rotate(Robot.Turn.AROUND);
					} else if (dirToMove == CardinalDirection.South && curPos[1] == (controller.getMaze().getHeight() - 1)){ 
						robot.rotate(Robot.Turn.AROUND);
					} else if (dirToMove == CardinalDirection.East && curPos[0] == (controller.getMaze().getWidth() - 1)){
						robot.rotate(Robot.Turn.AROUND);
					} else {
						robot.jump();
						
						newPos = robot.getCurrentPosition();
						if (dirToMove == CardinalDirection.North) {
							assertTrue(robot.energyLevel <= 3460f);
							assertTrue(curPos[0] == newPos[0] && curPos[1] == newPos[1] - 1);
							toFail = false;
						} else if (dirToMove == CardinalDirection.West) {
							assertTrue(robot.energyLevel <= 3460f);
							assertTrue(curPos[0] == newPos[0] - 1 && curPos[1] == newPos[1]);
							toFail = false;
						} else if (dirToMove == CardinalDirection.South) {
							assertTrue(robot.energyLevel <= 3460f);
							assertTrue(curPos[0] == newPos[0] && curPos[1] == newPos[1] + 1);
							toFail = false;
						} else {
							assertTrue(robot.energyLevel <= 3460f);
							toFail = false;
						}
					}
					
					
					
				} else if(robot.distanceToObstacle(Direction.BACKWARD) == 0) {
					robot.rotate(Robot.Turn.AROUND);
					dirToMove = robot.getCurrentDirection();
					if (dirToMove == CardinalDirection.North && curPos[1] == 0) {// border cases
						robot.rotate(Robot.Turn.AROUND);
					} else if (dirToMove == CardinalDirection.West && curPos[0] == 0){ 
						robot.rotate(Robot.Turn.AROUND);
					} else if (dirToMove == CardinalDirection.South && curPos[1] == (controller.getMaze().getHeight() - 1)){ 
						robot.rotate(Robot.Turn.AROUND);
					} else if (dirToMove == CardinalDirection.East && curPos[0] == (controller.getMaze().getWidth() - 1)){
						robot.rotate(Robot.Turn.AROUND);
					} else {
						robot.jump();
						
						newPos = robot.getCurrentPosition();
						if (dirToMove == CardinalDirection.North) {
							assertTrue(curPos[0] == newPos[0] && curPos[1] == newPos[1] - 1);
							toFail = false;
						} else if (dirToMove == CardinalDirection.West) {
							assertTrue(curPos[0] == newPos[0] - 1 && curPos[1] == newPos[1]);
							toFail = false;
						} else if (dirToMove == CardinalDirection.South) {
							assertTrue(curPos[0] == newPos[0] && curPos[1] == newPos[1] + 1);
							toFail = false;
						} else {
							assertTrue(curPos[0] == newPos[0] + 1 && curPos[1] == newPos[1]);
							toFail = false;
						}
					}
					
				}else if(robot.distanceToObstacle(Direction.RIGHT) == 0) {
					robot.rotate(Robot.Turn.RIGHT);
					dirToMove = robot.getCurrentDirection();
					if (dirToMove == CardinalDirection.North && curPos[1] == 0) {// border cases
						robot.rotate(Robot.Turn.AROUND);
					} else if (dirToMove == CardinalDirection.West && curPos[0] == 0){ 
						robot.rotate(Robot.Turn.AROUND);
					} else if (dirToMove == CardinalDirection.South && curPos[1] == (controller.getMaze().getHeight() - 1)){ 
						robot.rotate(Robot.Turn.AROUND);
					} else if (dirToMove == CardinalDirection.East && curPos[0] == (controller.getMaze().getWidth() - 1)){
						robot.rotate(Robot.Turn.AROUND);
					} else {
						robot.jump();
						
						newPos = robot.getCurrentPosition();
						if (dirToMove == CardinalDirection.North) {
							assertTrue(curPos[0] == newPos[0] && curPos[1] == newPos[1] - 1);
							toFail = false;
						} else if (dirToMove == CardinalDirection.West) {
							assertTrue(curPos[0] == newPos[0] - 1 && curPos[1] == newPos[1]);
							toFail = false;
						} else if (dirToMove == CardinalDirection.South) {
							assertTrue(curPos[0] == newPos[0] && curPos[1] == newPos[1] + 1);
							toFail = false;
						} else {
							assertTrue(curPos[0] == newPos[0] + 1 && curPos[1] == newPos[1]);
							toFail = false;
						}
					}
				} else { // for if the wall to jump was a border or if there was no wall
					if(robot.distanceToObstacle(Direction.FORWARD) == 0) {// for if it was a border wall and i've rotated now and need to check for wall again
						dirToMove = robot.getCurrentDirection();
						robot.jump();
							
						newPos = robot.getCurrentPosition();
						if (dirToMove == CardinalDirection.North) {
							assertTrue(curPos[0] == newPos[0] && curPos[1] == newPos[1] - 1);
							toFail = false;
						} else if (dirToMove == CardinalDirection.West) {
							assertTrue(curPos[0] == newPos[0] - 1 && curPos[1] == newPos[1]);
							toFail = false;
						} else if (dirToMove == CardinalDirection.South) {
							assertTrue(curPos[0] == newPos[0] && curPos[1] == newPos[1] + 1);
							toFail = false;
						} else {
							assertTrue(curPos[0] == newPos[0] + 1 && curPos[1] == newPos[1]);
							toFail = false;
						}
						
					} else {
						robot.move(1);	
					}
				}
			}	
			
			if(toFail) {
				assertFalse(toFail); // shouldn't reach this point so autofail
			}
			
		} catch (Exception e) {
			toFail = false;
			assertFalse(toFail);
		}
	}
	
	/**
	 * Test case: checks that if the robot tries to jump over a border wall that the robot is killed
	 * <p>
	 * Method under test: tests the jump method full functionality
	 * by proxy checks get and set current position and interaction with controll object
	 * <p>
	 * Correct behavior:
	 * if correct: robot is dead after jump and so jump didn't occur and in same position
	 */
	@Test
	public final void jumpIllegal() {
		// checks that if the robot tries to jump over a border wall that the robot is killed
		// tests the jump method full functionality
		// by proxy checks get and set current position and interaction with controll object
		// how it works: sets position to corresponding border to current direction, tries to jump wall, checks that bot is dead
		// if correct: robot is dead after jump and so jump didn't occur and in same position
		 
		CardinalDirection carDirFaced = robot.getCurrentDirection();
		boolean jumpDone = false; 
		try {		
			int[] curPos = robot.getCurrentPosition();
			
			int maxVal = robot.controller.getMaze().getHeight();
			if (robot.controller.getMaze().getHeight() < robot.controller.getMaze().getWidth()) {
				maxVal = robot.controller.getMaze().getWidth();
			}
			int counter = 0;
			while (counter < maxVal && !jumpDone) {
				if (curPos[1] == 0) {// at North Border
					if (robot.getCurrentDirection() == CardinalDirection.North) {
						robot.jump();
						jumpDone = true;
					} else if (robot.getCurrentDirection() == CardinalDirection.West) {
						robot.rotate(Turn.RIGHT);
						robot.jump();
						jumpDone = true;
					} else if (robot.getCurrentDirection() == CardinalDirection.South) {
						robot.rotate(Turn.AROUND);
						robot.jump();
						jumpDone = true;
					} else {
						robot.rotate(Turn.LEFT);
						robot.jump();
						jumpDone = true;
					}
				} else if (curPos[1] == controller.getMaze().getHeight() - 1) {// at south border
					if (robot.getCurrentDirection() == CardinalDirection.South) {
						robot.jump();
						jumpDone = true;
					} else if (robot.getCurrentDirection() == CardinalDirection.East) {
						robot.rotate(Turn.RIGHT);
						robot.jump();
						jumpDone = true;
					} else if (robot.getCurrentDirection() == CardinalDirection.North) {
						robot.rotate(Turn.AROUND);
						robot.jump();
						jumpDone = true;
					} else {
						robot.rotate(Turn.LEFT);
						robot.jump();
						jumpDone = true;
					}
					
				} else if (curPos[0] == controller.getMaze().getWidth() - 1) {// at east border
					if (robot.getCurrentDirection() == CardinalDirection.East) {
						robot.jump();
						jumpDone = true;
					} else if (robot.getCurrentDirection() == CardinalDirection.North) {
						robot.rotate(Turn.RIGHT);
						robot.jump();
						jumpDone = true;
					} else if (robot.getCurrentDirection() == CardinalDirection.West) {
						robot.rotate(Turn.AROUND);
						robot.jump();
						jumpDone = true;
					} else {
						robot.rotate(Turn.LEFT);
						robot.jump();
						jumpDone = true;
					}
				} else if (curPos[0] == 0) {// at West border
					if (robot.getCurrentDirection() == CardinalDirection.West) {
						robot.jump();
						jumpDone = true;
					} else if (robot.getCurrentDirection() == CardinalDirection.South) {
						robot.rotate(Turn.RIGHT);
						robot.jump();
						jumpDone = true;
					} else if (robot.getCurrentDirection() == CardinalDirection.East) {
						robot.rotate(Turn.AROUND);
						robot.jump();
						jumpDone = true;
					} else {
						robot.rotate(Turn.LEFT);
						robot.jump();
						jumpDone = true;
					}
				} else {
					robot.jump();
					counter = counter + 1;
				}
			}
			if (jumpDone) {
				assertTrue(controller.robot.getCurrentPosition()[0] == curPos[0]);
				assertTrue(controller.robot.getCurrentPosition()[1] == curPos[1]);
			}
			
			
		} catch (Exception e) {
			assertTrue(jumpDone);// auto fails shouldn't be here
		}
		
		
		
	}
	
	/**
	 * Test case: checks whether robot can correctly sense whether its at the exit
	 * <p>
	 * Method under test: isAtExit method and interaction with control object
	 * <p>
	 * Correct behavior:
	 * check for whether at exit should match boolean for isAtExit(), no exception should be called
	 */
	@Test
	public final void checkExit() {
		// checks whether robot can correctly sense whether its at the exit
		// tests the isAtExit method and interaction with control object
		// how it works: checks if currently at exit, checks the isAtExit method
		// if correct: the check for whether at exit should match boolean for isAtExit(), no exception should be called
		
		try {
			int[] curPos = robot.getCurrentPosition();
			if (curPos == controller.getMaze().getExitPosition()) {
				assertTrue(robot.isAtExit());
			} else {
				assertFalse(robot.isAtExit());
			}
		} catch (Exception e) {
			boolean toFail = true;
			assertFalse(toFail);
		}
		
		
		
	}
	
	/**
	 * Test case: checks whether hasStopped() is functional when the robot is dead
	 * <p>
	 * Method under test: hasStopepd method and instance var isDead
	 * <p>
	 * Correct behavior:
	 * should return true because the robot is dead
	 */
	@Test
	public final void checkIfDeadWhenDead() {
		// checks whether hasStopped() is functional when the robot is dead
		// tests the hasStopepd method and instance var isDead
		// how it works: calls hasStopped() after isDead set to true
		// if correct: should return true because the robot is dead
		
		robot.isDead = true;
		assertTrue(robot.hasStopped());

	}
	
	/**
	 * Test case: checks whether hasStopped() is functional when the robot is dead
	 * <p>
	 * Method under test: tests the hasStopepd method and instance var isDead
	 * <p>
	 * Correct behavior:
	 * should return false because nothing happened to the robot to kill it
	 */
	@Test
	public final void checkIfDeadWhenAlive() {
		// checks whether hasStopped() is functional when the robot is still alive
		// tests the hasStopepd method and instance var isDead
		// how it works: calls hasStopped() after just a turn
		// if correct: should return false because nothing happenend to the robot to kill it
				
		robot.rotate(Robot.Turn.AROUND);
		assertTrue(!robot.hasStopped());
		
	}
	
	/**
	 * Test case: checks that the distance to obstacle method works when there is a wall in the direction faced
	 * <p>
	 * Method under test: distancetowall method
	 * also utilizes to get to specification needed the move, can see through to eternity, and isAtExit methods which are independently tested
	 * 
	 * <p>
	 * Correct behavior:
	 * should reach a wall, should return a 0 when at position where facing a wall, shouldn't throw exception
	 */
	@Test
	public final void checkDistanceToWallNeeded() {
		// checks that the distance to obstacle method works when there is a wall in the direction faced
		// tests the distancetowall method
		// also utilizes to get to specification needed the move, can see through to eternity, and isAtExit methods which are independently tested
		// how it works: sees if there is a wall in the direction faced through maze object, then checks to see what distancetoObstacle method returns
		// if in a space with no wall in that direction move until forward (repeat until hit a wall or autofail)
		// won't autofail if exceed maximum travel time only if we're at the exit
		// if reach exit then turn and repeat process, the max val purposely set to accomadate for this case
		// if correct: should return a 0 when at position where facing a wall, shouldn't throw exception		
		boolean toFail = true;
		
		try {
			int maxVal = robot.controller.getMaze().getHeight();
			if (robot.controller.getMaze().getHeight() < robot.controller.getMaze().getWidth()) {
				maxVal = robot.controller.getMaze().getWidth();
			}
			
			int[] curPos;
			CardinalDirection curCarDir;
			boolean hasWall;
			
			for (int i = 0; i < maxVal * 2; i++) {
				curPos = robot.getCurrentPosition();
				curCarDir = robot.getCurrentDirection();
				hasWall = robot.controller.getMaze().hasWall(curPos[0], curPos[1], curCarDir);
				
				if (hasWall) {
					assertTrue(robot.distanceToObstacle(Direction.FORWARD) == 0);
					toFail = false;
				} else if (robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)){
					robot.rotate(Robot.Turn.AROUND);
				} else {
					robot.move(1);
				}
			}
			
		} catch (Exception e) {
			assertFalse(toFail);//autofail cuz shouldn't reach this point
		}
		
	}
	
	/**
	 * Test case: checks that the distance to obstacle method works when there is no wall in the direction faced
	 * <p>
	 * Method under test: distancetowall method
	 * may utilize to get to specification needed the rotate and move which are independently tested elsewhere
	 * <p>
	 * Correct behavior:
	 * should find a direction with no wall, should return a value > 0, shouldn't throw exception 
	 */
	@Test
	public final void checkDistanceToWallFar() {
		// checks that the distance to obstacle method works when there is no wall in the direction faced
		// tests the distancetowall method
		// also may utilize to get to specification needed the rotate and move
		// how it works: sees if there is a wall in the direction faced through maze object, then checks to see what distancetoObstacle method returns
		// if in a space with no wall in that direction checks to see what distancetoObstacle method returns
		// otherwise rotates, if wall on all 4 sides then there's some kind of failure
		// if correct: should find a direction with no wall, should return a value > 0, shouldn't throw exception 
		
		boolean toFail = true;
		
		try {
			
			int[] curPos = robot.getCurrentPosition();
			CardinalDirection curCarDir;
			boolean hasWall;
			
			for (int i = 0; i < 4; i++) {
				curCarDir = robot.getCurrentDirection();
				hasWall = controller.getMaze().hasWall(curPos[0], curPos[1], curCarDir);
				
				if(hasWall) {
					robot.rotate(Robot.Turn.LEFT);
				} else {
					assertTrue(robot.distanceToObstacle(Direction.FORWARD) > 0);
					toFail = false;
				}
			}
					
			if (toFail) {
				assertFalse(toFail);//autofail cuz shouldn't reach this point
			}
			
		} catch (Exception e) {
			assertFalse(toFail);//autofail cuz shouldn't reach this point
		}
	}
	
	/**
	 * Test case: checks the seeIntoEternity method works when exit in line of sight
	 * <p>
	 * Method under test: FseeIntoEternity and interaction with controller var
	 * may need rotate methods to get into right spot
	 * <p>
	 * Correct behavior:
	 * can see method should return true after the robot set to the right point, shouldn't throw excpetion
	 */
	@Test
	public final void seeExitAtExit() {
		//checks the seeIntoEternity method works when exit in line of sight
		//test seeIntoEternity and interaction with controller var
		// may need rotate methods to get into right spot
		// how it works: sets the robot position to be at the exit position, sees what seeIntoEternity returns
		// sets position by interacting with control and state vars
		// has a error catcher to make sure i'm actually at the exit and facing right direction
		// if correct: can see method should return true after the robot set to the right point, shouldn't throw excpetion
		boolean toFail = true;
		
		int[] exitPos = controller.getMaze().getExitPosition();
		CardinalDirection exitDir;
		
		if (exitPos[1] == 0 && (!controller.getMaze().hasWall(exitPos[0], exitPos[1], CardinalDirection.North))) {
			exitDir = CardinalDirection.North;
		} else if (exitPos[0] == 0 && (!controller.getMaze().hasWall(exitPos[0], exitPos[1], CardinalDirection.West))) {
			exitDir = CardinalDirection.West;
		} else if ((exitPos[1] == controller.getMaze().getHeight() - 1) && (!controller.getMaze().hasWall(exitPos[0], exitPos[1], CardinalDirection.South))) {
			exitDir = CardinalDirection.South;
		} else {
			exitDir = CardinalDirection.East;
		}
		
		StatePlaying testState = (StatePlaying) controller.currentState;
		testState.setCurrentPosition(exitPos[0], exitPos[1]);
		
		try {
			if (robot.getCurrentPosition() == exitPos) {
				for (int i = 0; i < 4; i ++) {
					if (robot.getCurrentDirection() != exitDir) {
						robot.rotate(Robot.Turn.LEFT);
					}
				}
				
				assertTrue(robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD));
			}
		} catch (Exception e) {
			assertFalse(toFail); // shouldn't reach auto fail
		}
		
	}
	
	/**
	 * Test case: checks the seeIntoEternity method works when exit not in line of sight
	 * <p>
	 * Method under test: seeIntoEternity and interaction with controller var
	 * <p>
	 * Correct behavior:
	 * can see method should return false after the robot set to the right point, shouldn't throw excpetion
	 */
	@Test
	public final void seeExitFarFromExit() {
		//checks the seeIntoEternity method works when exit not in line of sight
		//test seeIntoEternity and interaction with controller var
		// may need rotate methods to get into right spot
		// how it works: checks if robot is not potentially facing exit, then checks what canSee returns
		// if it is potentially facing exit then needs to move
		// if correct: can see method should return false after the robot set to the right point, shouldn't throw excpetion
		boolean toFail = true;
		
		int[] exitPos = controller.getMaze().getExitPosition();
		CardinalDirection exitDir;
		
		if (exitPos[1] == 0 && (!controller.getMaze().hasWall(exitPos[0], exitPos[1], CardinalDirection.North))) {
			exitDir = CardinalDirection.North;
		} else if (exitPos[0] == 0 && (!controller.getMaze().hasWall(exitPos[0], exitPos[1], CardinalDirection.West))) {
			exitDir = CardinalDirection.West;
		} else if ((exitPos[1] == controller.getMaze().getHeight() - 1) && (!controller.getMaze().hasWall(exitPos[0], exitPos[1], CardinalDirection.South))) {
			exitDir = CardinalDirection.South;
		} else {
			exitDir = CardinalDirection.East;
		}
		
		try {
			if (robot.getCurrentDirection() == exitDir) {
				robot.rotate(Robot.Turn.AROUND);
			}
			assertFalse(robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD));
		} catch (Exception e) {
			assertFalse(toFail); // shouldn't reach auto fail
		}
		
		
		
	}
	
	/**
	 * Test case: checks that IsInsiderRoom returns the right val
	 * <p>
	 * Method under test: isInsideROom and interaction with maze object
	 * <p>
	 * Correct behavior:
	 * should return coolean corresponsing to what maze floorplan check says, no excpetion thrown
	 */
	@Test
	public final void checkIsInsideRoom() {
		// checks that IsInsiderRoom returns the right val
		// tests isInsideROom and interaction with maze object
		// how it works = check if current pos is in room based on maze floor plan, checks what isInRoom() returns
		// if correct: should return coolean corresponsing to what maze floorplan check says, no excpetion thrown
		try {
			int[] curPos = robot.getCurrentPosition();
			
			if(controller.getMaze().isInRoom(curPos[0], curPos[1])) {
				assertTrue(robot.isInsideRoom());
			} else {
				assertFalse(robot.isInsideRoom());
			}
		} catch (Exception e) {
			boolean toFail = true;
			assertFalse(toFail);
		}
		

	}
	
	/**
	 * Test case: checks that the robot's instance var properly set to unreliable in constructor and the getMethod for robot type
	 * <p>
	 * Method under test: getRobotType(), and instance var for robot's type
	 * <p>
	 * Correct behavior:
	 * checking the instance var for sensor type should return the string unreliable
	 */
	
	@Test
	public final void checkUnreliableRobotType() {
		// checks that the robot's instance var properly set to unreliable in constructor and the getMethod for robot type
		// methods tested: getRobotType(), and instance var for robot's type
		// how it works: calls get method on the robotType instance var and compares it to correct setting
		// if correct: checking the instance var for robotType should return "Unreliable"
		
		String toTest = "unreliable";
		assertEquals(robot.getRobotType(), toTest);
		
	}
	
	/**
	 * Test case: checks that the getRobotSensor() for specified distance sensor returns the correct sensor and setup process for sensors is correct
	 * <p>
	 * Method under test: getRobotSensor() method and sensor instance var instantiation,getSensorRelativeDirection()
	 * <p>
	 * Correct behavior:
	 * am unreliable sensor should be returned whose relative direction matches the specified one
	 */
	
	@Test
	public final void checkGetSensor() {
		// checks that the getRobotSensor() for specified distance sensor returns the correct sensor and setup process for sensors is correct
		// methods tested, getRobotSensor() method and sensor instance var instantiation,getSensorRelativeDirection()
		// how it works: calls get sensor() method on a specified sensor and ensures that a sensor returned and has the specified rel dir
		// if correct: am unreliable sensor should be returned whose relative direction matches the specified one
		
		DistanceSensor testerFor = robot.getRobotSensor(Direction.FORWARD);
		assertTrue(testerFor.getSensorRelativeDirection() == Direction.FORWARD);
		
		DistanceSensor testerLeft = robot.getRobotSensor(Direction.LEFT);
		assertTrue(testerLeft.getSensorRelativeDirection() == Direction.LEFT);
		
		DistanceSensor testerRight = robot.getRobotSensor(Direction.RIGHT);
		assertTrue(testerRight.getSensorRelativeDirection() == Direction.RIGHT);
		
		DistanceSensor testerBack = robot.getRobotSensor(Direction.BACKWARD);
		assertTrue(testerBack.getSensorRelativeDirection() == Direction.BACKWARD);
	}
	
	/**
	 * Test case: checks that the startFailure() for Unreliable robot method works when sensor is unreliable and general set up for starting threads
	 * <p>
	 * Method under test: startFailure() method, and sensor var maniuplation
	 * <p>
	 * Correct behavior:
	 * the tester unreliableSensor's thread should be alive
	 */
	
	@Test
	public final void checkUnreliableRobotThreadStartWhenUnreliable() {
		// checks that the startFailure() for Unreliable robot method works when sensor is unreliable and general set up for starting threads
		// methods tested: startFailure() method, and sensor var maniuplation
		// how it works: the code from the set up is the code from the start method, by testing that the threads for the specified sensors have begun can check this
		// see if thread for the unreliable sensor test instanve var has been started with alive
		// if correct: the unreliable's sensor's unreliable thread should be alive
		
		assertTrue(testerDefaultSensor.repairThread.isAlive());
	}
	
	/**
	 * Test case: checks that the startFailure() for Unreliable robot method properly throws error if a sensor is reliable
	 * <p>
	 * Method under test: tartFailure() method, and sensor var maniuplation
	 * <p>
	 * Correct behavior:
	 * exception should be thrown so exception branch should be gone down
	 */
	
	@Test
	public final void checkUnreliableRobotThreadStartWhenReliableSensor() {
		// checks that the startFailure() for Unreliable robot method properly throws error if a sensor is reliable
		// methods tested: startFailure() method, and sensor var maniuplation
		// how it works: the code from the set up is the code from the start method, by testing that the threads for the specified sensors have begun can check the thread
		// create a reliable sensor to test with, try to call the start() on it and use auto passes or auto fails to assess the succes
		// if correct: exception should be thrown but not completely destroy everything because contatined
		

		ReliableSensor tester = new ReliableSensor();
		DistanceSensor holder = robot.forwardSensor;
		robot.forwardSensor = holder;
		
		assertFalse(robot.forwardSensor.checkRepairStatus());
		
		robot.forwardSensor = holder;
		holder.startFailureAndRepairProcess(4, 2);
	}
	
	/**
	 * Test case: checks that the robot version of the startFailure() method actually alternates the repair status properly 
	 * <p>
	 * Method under test: startfailure() method in UnreliableRobot
	 * <p>
	 * Correct behavior:
	 * the specified sensor's unreliable thread should be different at each collection point
	 */
	
	@Test
	public final void checkUnreliableRobotThreadSwitch() {
		// checks that the robot version of the startFailure() method actually alternates the repair status properly 
		// methods tested: startfailure() method in UnreliableRobot
		// how it works: start() already done in the setUp, gathers the sensor setting once
		// uses the instance var of testerDefaultSensor which should be the front sensor, needed since declared as UnreliableSensor rather than generic
		// based on current status, wait a certain time interval then recollect status at a time when it should be different
		// if correct: the specified sensor's unreliable thread should be different at each collection point
		
		boolean firstStatus = testerDefaultSensor.downForRepair;
		if (firstStatus) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		boolean secondStatus = testerDefaultSensor.downForRepair;
		assertFalse(firstStatus == secondStatus);
	}
	
	/**
	 * Test case: checks that the stop process for the unreliable thread works
	 * <p>
	 * Method under test: stopfailure() method, stop thread process in stateWinning after the game has ended and switched to title
	 * <p>
	 * Correct behavior:
	 * the forward sensor's unreliable thread should be null and downForRepair should be false
	 */
	
	@Test
	public final void checkUnreliableRobotThreadStop() {
		// checks that the stop process for the unreliable thread works 
		// methods tested, stopfailure() method, stop thread process in stateWinning after the game has ended and switched to title
		// how it works: force quits the thread by calling the stop method() on the sensor's thread
		// then check on the instance var for the checkForRepair
		// uses testDefualt sensor instance var because that is set to be the forward sensor and is declared as an UnreliableSensor with repairThread instance var
		// the forward sensor's unreliable thread should be null and downForRepair should be false
		
		robot.stopFailureAndRepairProcess(Direction.FORWARD);
		
		assertEquals(testerDefaultSensor.repairThread, null);
		assertFalse(robot.forwardSensor.checkRepairStatus());
		
		robot.startFailureAndRepairProcess(Direction.FORWARD, 4, 2);// needed otherwise reset process will throw an error
	}
	
	/**
	 * Test case: checks that the stopFailure() for Unreliable robot method properly throws error if a sensor is reliable
	 * <p>
	 * Method under test: stopFailure() method, and sensor var maniuplation
	 * <p>
	 * Correct behavior:
	 * exception should be thrown so exception branch should be gone down
	 */
	
	@Test
	public final void checkUnreliableRobotThreadStopWhenReliableSensor() {
		// checks that the stopFailure() for Unreliable robot method properly throws error if a sensor is reliable
		// methods tested: stopFailure() method, and sensor var maniuplation
		// how it works: replaces forward sensor with reliable sensor, call stop method on it
		// if correct: exception should be thrown but not completely destroy everything because contatined, checking the repair status should always be fault cuz default set to false
		

		ReliableSensor tester = new ReliableSensor();
		DistanceSensor holder = robot.forwardSensor;
		robot.forwardSensor = holder;
		
		robot.stopFailureAndRepairProcess(Direction.FORWARD);
		assertFalse(robot.forwardSensor.checkRepairStatus());
		
		robot.forwardSensor = holder;
		holder.startFailureAndRepairProcess(4, 2);
	}

}

	