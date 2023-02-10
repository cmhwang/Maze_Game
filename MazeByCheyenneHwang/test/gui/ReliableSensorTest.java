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
 * Tests individual methods of the ReliableSensor class. 
 * 
 * 
 * @author Cheyenne Hwang
 *
 */

public class ReliableSensorTest { 
	
	private ReliableRobot robot;
	private Maze testMaze;
	private ReliableSensor forwardSensor;
	private Control controller;
	private StatePlaying tempState;
	
	
	/**
	 * Creates default ReliableSensor object with no specification. Will test setting specification in later tests.
	 */
	@Before
	public void setUp() {
		robot = new ReliableRobot();
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
		
        ReliableSensor forwardSensor = new ReliableSensor();
        forwardSensor.setSensorDirection(Direction.FORWARD);
        forwardSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(forwardSensor, Direction.FORWARD);
        
        ReliableSensor leftSensor = new ReliableSensor();
        leftSensor.setSensorDirection(Direction.LEFT);
        leftSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(leftSensor, Direction.LEFT);
        
        ReliableSensor backSensor = new ReliableSensor();
        backSensor.setSensorDirection(Direction.BACKWARD);
        backSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(backSensor, Direction.BACKWARD);
        
        ReliableSensor rightSensor = new ReliableSensor();
        rightSensor.setSensorDirection(Direction.RIGHT);
        rightSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(rightSensor, Direction.RIGHT);

        controller.driver.setMaze(controller.getMaze());
        controller.driver.setRobot(controller.robot);
		
	}
	
	/**
	 * Resets ReliableSensor object used for testing so no test interferes with other
	 */
	@After
	public void reset() {
		robot = new ReliableRobot();
		Wizard wizard = new Wizard();
		
		controller = new Control();
		controller.setRobotAndDriver(robot, wizard);
		
		StatePlaying tempState = new StatePlaying();
		
		MazeBuilder mazeBuilder = new MazeBuilder();
		MazeFactory testFactory = new MazeFactory();
		DefaultOrder mazeOrder = new DefaultOrder();
		
		testFactory.order(mazeOrder);
		testFactory.waitTillDelivered();
		
		testMaze = mazeOrder.getMaze();
		tempState.setMaze(testMaze);
		
		controller.setState(tempState);
		robot.setController(controller);
 
		
        ReliableSensor forwardSensor = new ReliableSensor();
        forwardSensor.setSensorDirection(Direction.FORWARD);
        forwardSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(forwardSensor, Direction.FORWARD);
        
        ReliableSensor leftSensor = new ReliableSensor();
        leftSensor.setSensorDirection(Direction.LEFT);
        leftSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(leftSensor, Direction.LEFT);
        
        ReliableSensor backSensor = new ReliableSensor();
        backSensor.setSensorDirection(Direction.BACKWARD);
        backSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(backSensor, Direction.BACKWARD);
        
        ReliableSensor rightSensor = new ReliableSensor();
        rightSensor.setSensorDirection(Direction.RIGHT);
        rightSensor.setMaze(controller.getMaze());
        robot.addDistanceSensor(rightSensor, Direction.RIGHT);

        controller.driver.setMaze(controller.getMaze());
        controller.driver.setRobot(controller.robot);
		
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
				
		assertEquals(controller.getMaze(), robot.forwardSensor.maze);
		
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
	 * Test case: checks that the setRelativeDirection() method correctly sets the direction and that the implementation of distance sensors works
	 * <p>
	 * Method under test: setRelDir() and instance var for reliablesnsor's direction
	 * <p>
	 * Correct behavior:
	 * checking the instance var for rel dir should return a dir corresponding to the dir set
	 */
	@Test
	public final void checkDirectionSetting() {
		// checks that the setRelativeDirection() method correctly sets the direction and that the implementation of distance sensors works
		// methods tested, setRelDir() and instance var for reliablesnsor's direction
		// how it works: calls set on the sensor object for the robot whuch should be instantiated, checks the relative direction of that sensor with an assertequals
		// repeat for each direction for good measure
		// if correct: checking the instance var for rel dir should return a dir corresponding to the dir set
		
		
		assertEquals(robot.forwardSensor.relativeDirection, Direction.FORWARD);
		
		assertEquals(robot.leftSensor.relativeDirection, Direction.LEFT);
		
		assertEquals(robot.backSensor.relativeDirection, Direction.BACKWARD);
		
		assertEquals(robot.rightSensor.relativeDirection, Direction.RIGHT);
		
	}

}
