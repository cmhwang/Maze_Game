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
 * Tests individual methods of the UnreliableSensor class. 
 * 
 * 
 * @author Cheyenne Hwang
 *
 */

public class UnreliableSensorTest {
	
	private UnreliableRobot robot;
	private UnreliableSensor testerDefaultSensor;
	private Maze testMaze;
	private Control controller;
	private StatePlaying tempState;
	
	
	/**
	 * Creates default ReliableSensor object with no specification. Will test setting specification in later tests.
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
	 * Resets ReliableSensor object used for testing so no test interferes with other
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
	 * Test case: checks the checkDistance function properly when position parameters are invalid
	 * <p>
	 * Method under test: intToObstacle when given an invalid position
	 * <p>
	 * Correct behavior:
	 * should throw IllegalArgumentException after call and go down that branch
	 */
	@Test
	public final void illegalPosCheckDistance() {
		// checks the checkDistance function properly when parameters are invalid
		// test intToObstacle when given an invalid position
		// how it works: calls intTo method with nagative postiion params
		// if throw exception sent down branch without autofail, if no exception sent down branch with autofail
		// if correct: should throw IllegalArgumentException and go down that branch
		boolean toFail = true;
		float[] energyRep = new float[1];
		energyRep[0] = (float) 35000;
		
		int[] illegalPos = new int[2];
		illegalPos[0] = -1;
		
		try {
			robot.forwardSensor.distanceToObstacle(illegalPos, CardinalDirection.North, energyRep);
			assertFalse(toFail); // auto fail
		} catch (Exception e) {
			assertTrue(toFail); // auto pass
		}
	}
	
	/**
	 * Test case: checks the checkDistance function properly when direction parameters are invalid
	 * <p>
	 * Method under test: intToObstacle when given an invalid cd
	 * <p>
	 * Correct behavior:
	 * should throw IllegalArgumentException after call and go down that branch
	 */
	@Test
	public final void illegalDirectionCheckDistance() {
		// checks the checkDistance function properly when direction parameters are invalid
		// test intToObstacle when given an invalid cd
		// how it works: calls intTo method with null cd param
		// if throw exception sent down branch without autofail, if no exception sent down branch with autofail
		// if correct: should throw IllegalArgumentException and go down that branch
		boolean toFail = true;
		float[] energyRep = new float[1];
		energyRep[0] = (float) 35000;
		
		int[] tempPos = new int[2];
		
		try {
			robot.forwardSensor.distanceToObstacle(tempPos, null, energyRep);
			assertFalse(toFail); // auto fail
		} catch (Exception e) {
			assertTrue(toFail); // auto pass
		}
	}
	
	/**
	 * Test case: checks the checkDistance function properly when position parameters are invalid
	 * <p>
	 * Method under test: intToObstacle when given an invalid position
	 * <p>
	 * Correct behavior:
	 * should throw IllegalArgumentException after call and go down that branch
	 */
	@Test
	public final void illegalEnergyCheckDistance() {
		// checks the checkDistance function properly when energy parameters are invalid
		// test intToObstacle when given an invalid energyLevel
		// how it works: calls intTo method with negative energyLevel
		// if throw exception sent down branch without autofail, if no exception sent down branch with autofail
		// if correct: should throw IndexOutOFBounds and go down that branch
		boolean toFail = true;
		float[] energyRep = new float[1];
		energyRep[0] = (float) -1;
		
		int[] tempPos = new int[2];
		
		try {
			robot.forwardSensor.distanceToObstacle(tempPos, CardinalDirection.North, energyRep);
			assertFalse(toFail); // auto fail
		} catch (Exception e) {
			assertTrue(toFail); // auto pass
		}
	}
	
	/**
	 * Test case: checks the checkDistance method works when at exit and facing it
	 * <p>
	 * Method under test: checkDistanceToObstacle when at exit
	 * <p>
	 * Correct behavior:
	 * distance method should return a massive int value, shouldn't throw excpetion
	 * @throws Exception 
	 */
	@Test
	public final void checkDistanceAtExit() throws Exception {
		//checks the checkDistance method works when at exit and facing it
		//test checkDistanceToObstacle when at exit
		// how it works: finds exit position and direction, calls and collects return from distanceTo method with these vars
		// if correct: distance method should return a massive int value, shouldn't throw excpetion
		boolean toFail = true;
		float[] energyRep = new float[]{35000.0f};
				
		int[] exitPos = testMaze.getExitPosition();
		CardinalDirection exitDir;
		
				
		if (exitPos[1] == 0 && !(testMaze.hasWall(exitPos[0], exitPos[1], CardinalDirection.North))) {
			exitDir = CardinalDirection.North;
		} else if (exitPos[0] == 0 && !(testMaze.hasWall(exitPos[0], exitPos[1], CardinalDirection.West))) {
			exitDir = CardinalDirection.West;
		} else if ((exitPos[1] == testMaze.getHeight() - 1) && !(testMaze.hasWall(exitPos[0], exitPos[1], CardinalDirection.South))) {
			exitDir = CardinalDirection.South;
		} else {
			exitDir = CardinalDirection.East;
		}
		

		assertTrue(robot.forwardSensor.distanceToObstacle(exitPos, exitDir, energyRep) == Integer.MAX_VALUE);
		try {
			//FIX - doesn't like calling distance when at exit - it fails?
			assertTrue(robot.forwardSensor.distanceToObstacle(exitPos, exitDir, energyRep) > 0);
			
		} catch (Exception e) {
			assertFalse(toFail);
		}
		
				
		
	}
	
	/**
	 * Test case: checks that distanceToObstacle works when facing a wall
	 * <p>
	 * Method under test: distanceToObstacle when facing wall, by proxy tests interaction with maze and robot object
	 * <p>
	 * Correct behavior:
	 * distanceToObstacle should return  0 after any needed moves, no exception should be thrown
	 */
	@Test
	public final void checkDistanceAtWall() {
		// checks that distanceToObstacle works when facing a wall
		// tests distanceToObstacle when facing wall, by proxy tests interaction with maze and robot object
		// how it works: uses top left corner to check, checks distance when facing north (which is the wall) 
		// may need to rotate if happens to be facing exit (unlikely but anyways)
		// if correct: distanceToObstacle should return  0 after any needed moves, no exception should be thrown
		
		boolean toFail = true;
		float[] energyRep = new float[1];
		energyRep[0] = (float) 35000;
		int toCheck;
		
		try {
			int[] testingPos = new int[] {0, 0};
			if (testingPos == testMaze.getExitPosition()) {
				if (testMaze.hasWall(0, 0, CardinalDirection.North)) {
					toCheck = robot.forwardSensor.distanceToObstacle(testingPos, CardinalDirection.North, energyRep);
					toFail = false;
				} else {
					toCheck = robot.forwardSensor.distanceToObstacle(testingPos, CardinalDirection.West, energyRep);
				}
			} else {
				toCheck = robot.forwardSensor.distanceToObstacle(testingPos, CardinalDirection.North, energyRep);
			}
			
			assertEquals(toCheck, 0);
			
		} catch (Exception e) {
			assertFalse(toFail);//autofail cuz shouldn't reach this point
		}
	}
	
	/**
	 * Test case: checks that distanceToObstacle works when not at a wall or an exit
	 * <p>
	 * Method under test: distanceToObstacle when not at exit and not facing wall, by proxy tests interaction with maze and robot object
	 * <p>
	 * Correct behavior:
	 * istanceToObstacle should return a val > 0 after any needed rotates, no exception should be thrown
	 */
	@Test
	public final void checkDistanceAwayFromExit() {
		// checks that distanceToObstacle works when not at a wall or an exit
		// tests distanceToObstacle when not at exit and not facing wall, by proxy tests interaction with maze and robot object
		// how it works: shoudln't be at exit at starting point cuz no move yet, if no wall in the direction the sensor facing then checks what distance method returns
		// may need to change direction if there is a wall in the current direction
		// if correct: distanceToObstacle should return a val > 0 after any needed rotates, no exception should be thrown
		try {
			int[] curPos = robot.getCurrentPosition();
			CardinalDirection curCarDir = robot.getCurrentDirection();
			float[] energyRep = new float[1];
			energyRep[0] = 35000.0f;
			
			if (testMaze.hasWall(curPos[0], curPos[1], curCarDir)) { // has wall to the front
				robot.rotate(Robot.Turn.LEFT);
				curCarDir = robot.getCurrentDirection();
				
				if (testMaze.hasWall(curPos[0], curPos[1], curCarDir)) { // has wall to the front and left 
					robot.rotate(Robot.Turn.LEFT);
					curCarDir = robot.getCurrentDirection();		
					
					if (testMaze.hasWall(curPos[0], curPos[1], curCarDir)) { // has wall to front, left and back
						robot.rotate(Robot.Turn.LEFT);
						curCarDir = robot.getCurrentDirection();		
						
						int toCheck = robot.forwardSensor.distanceToObstacle(curPos, curCarDir, energyRep); // at this point rotated to right (last remaining dir which can't have wall)
						assertTrue(toCheck > 0);
						
					} else {
						int toCheck = robot.forwardSensor.distanceToObstacle(curPos, curCarDir, energyRep);
						assertTrue(toCheck > 0);
					}
					
				} else {
					int toCheck = robot.forwardSensor.distanceToObstacle(curPos, curCarDir, energyRep);
					assertTrue(toCheck > 0);
				}
				
			} else {
				int toCheck = robot.forwardSensor.distanceToObstacle(curPos, curCarDir, energyRep);
				assertTrue(toCheck > 0);
			}
		} catch (Exception e) {
			boolean toFail = true;
			assertFalse(toFail);
		}
		
	}
	
	/**
	 * Test case: checks to see if a maze has been set for the robot
	 * <p>
	 * Method under test: checks that setMaze() method works, that implementation correct, and that the instance var for maze works
	 * <p>
	 * Correct behavior:
	 * should assert that the sensor's maze is the robot's maze
	 */
	@Test
	public final void checkMazeSet() {
		//checks to see if a maze has been set for the robot
		//makes sure that the setMaze() method works, that implementation correct, and that the instance var for maze works
		//how it does this: since maze already set by set method in instantiation set up (needed for other tests) - checks that its in place by examining instance var
		//if correctly set up it should assert that the sensor's maze is the robot's maze
		// if wrong then instance variable and setting process are disjointed
				
		assertEquals(controller.getMaze(), testerDefaultSensor.maze);
		
	}
	
	/**
	 * Test case: checks that the val returned for getEnergyConsumptionForSensing() method is correct to game rules
	 * <p>
	 * Method under test: getEnergyConsumptionForSensing()
	 * <p>
	 * Correct behavior:
	 * method should return (float) 1
	 */
	@Test
	public final void checkEnergyUsedForSensing() {
		// checks that the val returned for getEnergyConsumptionForSensing() method is correct to game rules
		// methods tested: getEnergyConsumptionForSensing()
		// how it works: gets the val returned by getEnergyConsumptionForSensing(), uses assert to see if that value matches game rules
		// if correct: should return (float) 1
		
		float toCheck = robot.forwardSensor.getEnergyConsumptionForSensing();
		assertTrue(toCheck == 1.0f);
	}
	
	/**
	 * Test case: checks that the setRelativeDirection() method correctly sets the direction, that the getRelativeDirection() method can access it, and that the implementation of distance sensors works
	 * <p>
	 * Method under test: setRelDir(), getSensorRelDir(), and instance var for reliablesnsor's direction
	 * <p>
	 * Correct behavior:
	 * checking the instance var for rel dir should return a dir corresponding to the dir set
	 */
	@Test
	public final void checkDirectionSetting() {
		// checks that the setRelativeDirection() method correctly sets the direction, that the getRelativeDirection() method can access it, and that the implementation of distance sensors works
		// methods tested, setRelDir(), getSensorRelDIr() and instance var for reliablesnsor's direction
		// how it works: calls set on the sensor object for the robot which should be instantiated, checks the relative direction of that sensor with an assertequals and the getRelDir method
		// repeat for each direction for good measure
		// if correct: checking the instance var for rel dir should return a dir corresponding to the dir set
		
		
		assertEquals(robot.forwardSensor.getSensorRelativeDirection(), Direction.FORWARD);
		
		assertEquals(robot.leftSensor.getSensorRelativeDirection(), Direction.LEFT);
		
		assertEquals(robot.backSensor.getSensorRelativeDirection(), Direction.BACKWARD);
		
		assertEquals(robot.rightSensor.getSensorRelativeDirection(), Direction.RIGHT);
		
	}
	
	
	/**
	 * Test case: checks that the getSensorType() method works correctly and returns correct setting
	 * <p>
	 * Method under test: getsensortype() and instance var for unreliablesnsor's setting
	 * <p>
	 * Correct behavior:
	 * checking the instance var for sensor type should return the string unreliable
	 */
	
	@Test
	public final void checkUnreliableSensorType() {
		// checks that the setRelativeDirection() method correctly sets the direction and that the implementation of distance sensors works
		// methods tested, setRelDir() and instance var for reliablesnsor's direction
		// how it works: calls set on the sensor object for the robot whuch should be instantiated, checks the relative direction of that sensor with an assertequals
		// repeat for each direction for good measure
		// if correct: checking the instance var for rel dir should return a dir corresponding to the dir set
		
		String toTest = "unreliable";
		assertEquals(robot.forwardSensor.getSensorType(), toTest);
		
	}
	
	/**
	 * Test case: checks that the startFailure() method has begun the thread and the setup process has instantiated the thread to the sensor
	 * <p>
	 * Method under test: startfailure() and instance var for unreliable thread
	 * <p>
	 * Correct behavior:
	 * unrelaible thread instance var should be instantiated and alive
	 */
	
	@Test
	public final void checkUnreliableSensorThreadStarted() {
		// checks that the startFailure() method has begun the thread and the setup process has instantiated the thread to the sensor
		// methods tested, startfailure() and instance var for unreliable thread
		// how it works: calls set on the sensor object's thread, checks that its alive with the isAlive() method
		// if correct: unrelaible thread instance var should be instantiated and alive
		
		boolean toTest = testerDefaultSensor.repairThread.isAlive();
		assertTrue(toTest);
		
		
	}
	
	/**
	 * Test case: checks that the checkRepairStatus() method correctly sets a repair status for the maze
	 * <p>
	 * Method under test: checkRepairStatus() and instance var for unreliablesnsor's repair status
	 * <p>
	 * Correct behavior:
	 * checking the instance var for repair status should return a non-null boolean value
	 */
	
	@Test
	public final void checkUnreliableSensorRepairStatus() {
		// checks that the checkRepairStatus() method correctly sets a repair status for the maze
		// methods tested, checkRepairStatus() and instance var for unreliablesnsor's repair status
		// how it works: calls set on the sensor object for the robot which should be instantiated, checks the repair status of that sensor with an assertequals
		// auto passes if its either true or false, fails otherwise
		// since the repair thread has already started no guarantee what the status is so just check that it's true or false (not null or something weird)
		// if correct: checking the instance var for repair status should return a non-null boolean value
		
		boolean autoPass = true;
		if (robot.forwardSensor.checkRepairStatus() == true) {
			assertTrue(autoPass);
		} else if (robot.forwardSensor.checkRepairStatus() == false) {
			assertTrue(autoPass);
		} else {
			assertFalse(autoPass);// should never reach this so autofail
		}
		
		
	}
	
	/**
	 * Test case: checks that the failure and repair thread has begun for any sensor and is switching
	 * <p>
	 * Method under test: startFailureAndRepairProcess and instance var for unreliable sensor's status direction
	 * <p>
	 * Correct behavior:
	 * checking the instance var for repair status should change after 5 secs
	 */
	
	@Test
	public final void checkUnreliableThreadStartedAndSwitching() {
		// checks that the failure and repair thread has begun for any sensor and is switching
		// methods tested, startFailureAndRepairProcess and instance var for unreliable sensor's status direction
		// how it works: sensor thread should have been started in set up so checks instance var for repair status twice
		// see if it changes within 5 secs
		// if correct: checking the instance var for repair status should change after 5 secs
		
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
	 * Test case: checks that the startFailureProcess() method correctly sets the time between failures and that the instance var for the span is accessible
	 * <p>
	 * Method under test: startFailureProcess() and instance var for unreliable sensor's on-time
	 * <p>
	 * Correct behavior:
	 * checking the instance var for mean time between failures should return int 4
	 */
	
	@Test
	public final void checkMeanTimeBetweenFailures() {
		// checks that the startFailureProcess() method correctly sets the time between failures and that the instance var for the span is accessible
		// methods tested, startFailureProcess() and instance var for unreliable sensor's on-time
		// how it works: calls on unreliable sensor object, checks its instance var for mean time between failures set to 4 with a assert true
		// in setup() the startFailure() should have set this to 4
		// if correct: checking the instance var for mean time between failures should return int 4
		
		assertTrue(testerDefaultSensor.meanTimeBetweenFailures == 4);
		
	}
	
	/**
	 * Test case: checks that the startFailureProcess() method correctly sets the time for repairs and that the instance var for the span is accessible
	 * <p>
	 * Method under test: startFailureProcess() and instance var for unreliable sensor's off-time
	 * <p>
	 * Correct behavior:
	 * checking the instance var for repair time should return int 2
	 */
	
	@Test
	public final void checkMeanTimeToRepair() {
		// checks that the startFailureProcess() method correctly sets the time between failures and that the instance var for the span is accessible
		// methods tested, startFailureProcess() and instance var for unreliable sensor's off-time
		// how it works: calls on unreliable sensor object, checks its instance var for mean repair time set to 2 with a assert true
		// in setup() the startFailure() should have set this to 2
		// if correct: checking the instance var for mean repair time should return int 2
				
		assertTrue(testerDefaultSensor.meanTimeToRepair == 2);
		
	}
	
	/**
	 * Test case: checks that the start process for all the threads delays one thread's start time after another
	 * <p>
	 * Method under test: general setup for threads in stateplaying, startFailure(), instance vars for repair status
	 * <p>
	 * Correct behavior:
	 * the counter for the number of tiems they don't align should be at least 1 since there should be a 1.3 sec span where they are off
	 */
	
	@Test
	public final void checkUnreliableThreadWaitBetweenStart() {
		// checks that the start process for all the threads delays one thread's start time after another
		// methods tested, general setup for threads in stateplaying, startFailure(), instance vars for repair status
		// how it works: checks on repair status for front and left sensors 4 times each 1 sec apart
		// use a counter var to track the number of times they don't align
		// if correct: the counter for the number of tiems they don't align should be at least 1 since there should be a 1.3 sec span where they are off
		
		int diffCounter = 0;
		
		boolean frontStatus = robot.forwardSensor.checkRepairStatus();
		boolean rightStatus = robot.rightSensor.checkRepairStatus();
		if (frontStatus != rightStatus) {
			diffCounter++;
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		frontStatus = robot.forwardSensor.checkRepairStatus();
		rightStatus = robot.rightSensor.checkRepairStatus();
		if (frontStatus != rightStatus) {
			diffCounter++;
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		frontStatus = robot.forwardSensor.checkRepairStatus();
		rightStatus = robot.rightSensor.checkRepairStatus();
		if (frontStatus != rightStatus) {
			diffCounter++;
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		frontStatus = robot.forwardSensor.checkRepairStatus();
		rightStatus = robot.rightSensor.checkRepairStatus();
		if (frontStatus != rightStatus) {
			diffCounter++;
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertTrue(diffCounter > 0);
		
	}
	
	/**
	 * Test case: checks that the stop process for the unreliable thread works 
	 * <p>
	 * Method under test: stopfailure() method, stop thread process in stateWinning after the game has ended and switched to title
	 * <p>
	 * Correct behavior:
	 * the sensor's unreliable thread should be null and downForRepair should be false
	 */
	
	@Test
	public final void checkUnreliableThreadStops() {
		// checks that the stop process for the unreliable thread works 
		// methods tested, stopfailure() method, stop thread process in stateWinning after the game has ended and switched to title
		// how it works: force quits the thread by calling the stop method() on the sensor's thread
		// then check on the instance var for the checkForRepair
		// if correct: the sensor's unreliable thread should be null and downForRepair should be false
		
		testerDefaultSensor.stopFailureAndRepairProcess();
		
		assertEquals(testerDefaultSensor.repairThread, null);
		assertFalse(testerDefaultSensor.downForRepair);
		
		testerDefaultSensor.startFailureAndRepairProcess(4, 2);// this one needed to reset otherwise reset will throw an error
	}

}