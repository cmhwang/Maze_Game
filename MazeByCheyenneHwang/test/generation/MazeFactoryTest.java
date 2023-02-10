package generation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests individual methods of the MazeBuilder class. 
 * 
 * 
 * @author Cheyenne Hwang
 *
 */

public class MazeFactoryTest{
	
	public MazeBuilder mazeBuilder;
	public MazeFactory testFactory;
	public DefaultOrder mazeOrder;
	
	public Floorplan testFloorplan;
	public Maze testMaze;
	
	/**
	 * We create a default (maze object object using default order object created
	 */
	@Before
	public void setUp(){
		// set up a maze to test with these tests
		// want to test different types of mazes
		mazeBuilder = new MazeBuilder();
		testFactory = new MazeFactory();
		mazeOrder = new DefaultOrder();
		
		testFactory.order(mazeOrder);
		testFactory.waitTillDelivered();
		
	}
	

	/**
	 * Test case: tests to see that there's only one exit
		
	* how it will work: check every outer cell of the maze with two for loops
	* store width, height, and exit point in variables to check against
	* if an exit is hit: check if this is the exit point, if it is then continue if not then fail
	 * <p>
	 * Method under test: generate pathways, general construction
	 * <p>
	 * if an exit is hit: check if this is the exit point, if it is then continue if not then fail
	 * It is correct if it doesn't fail because only exit hit when it is the exit
	 */
	@Test
	public final void oneExit() {
		// tests to see that there's only one exit
		
		// how it will work: check every outer cell of the maze with two for loops
		// store width, height, and exit point in variables to check against
		// if an exit is hit: check if this is the exit point, if it is then continue if not then fail
		testMaze = mazeOrder.getMaze();
		testFloorplan = testMaze.getFloorplan();
		
		int mazeWidth = testMaze.getWidth();
		int mazeHeight = testMaze.getHeight();
			
		
		int[] exitPoint = testMaze.getExitPosition();
		
		for (int y = 0; y < mazeHeight; y++) {
			for (int x = 0; x < mazeWidth; x++) {
				if (y == 0) { // North Wall
					if (testFloorplan.hasNoWall(x, y, CardinalDirection.North)) {
						assertTrue(x == exitPoint[0] && y == exitPoint[1]);
					}				
				} if (x == 0) { // West Wall
					if (testFloorplan.hasNoWall(x, y, CardinalDirection.West)) {
						assertTrue(x == exitPoint[0] && y == exitPoint[1]);
					}
				} if (x == mazeWidth - 1) {// East Wall
					if (testFloorplan.hasNoWall(x, y, CardinalDirection.East)) {
						assertTrue(x == exitPoint[0] && y == exitPoint[1]);
					}
				} if (y == mazeHeight - 1) {// South Wall
					if (testFloorplan.hasNoWall(x, y, CardinalDirection.South)) {
						assertTrue(x == exitPoint[0] && y == exitPoint[1]);
					}
				}

			}
		}
		
		
	}
	
	/** 
	 * Test case: tests that any randomly selected point on the maze can reach t
	 * functionally doubles as checking there are no enclosed spaces and making sure every point can reach the other because if they can all reach the exit they all connect somehow
	 * how it will work: use two for loops to cycle through every cell
	 * for every cell, use getdistance to exit method to evaluate if the path exists
	 * <p>
	 * Method under test: getDistance, general construction (setting exit), generatepathways 
	 * <p>
	 * Correct behavior: 
	 * if the path is 0 then the test fails unless this is the exit position
	 * if the path is greater than the total number of cells we have some error and the test fails
	 */
	@Test
	public final void reachAllPoints() {
		// tests that any randomly selected point on the maze can reach t
		// functionally doubles as checking there are no enclosed spaces and making sure every point can reach the other because if they can all reach the exit they all connect somehow
		
		// how it will work: use two for loops to cycle through every cell
		// for every cell, use getdistance to exit method to evaluate if the path exists
		// if the path is 0 then the test fails unless this is the exit position
		// if the path is greater than the total number of cells we have some error and the test fails
		testMaze = mazeOrder.getMaze();
		testFloorplan = testMaze.getFloorplan();
		
		int mazeWidth = testMaze.getWidth();
		int mazeHeight = testMaze.getHeight();
		int[] exitPoint = testMaze.getExitPosition();
		
		for (int x = 0; x < mazeWidth; x++) {
			for (int y = 0; y < mazeHeight; y++) {
				int pathDistance = testMaze.getDistanceToExit(x, y);
				if (pathDistance == 0) {
					assertTrue(x == exitPoint[0] && y == exitPoint[1]);
				} else {
					assertTrue(pathDistance <= mazeWidth*mazeHeight);
				}
			}
		}

	}
	
	/** 
	 * Test case: tests that there are the correct number of cells to walls
	 * functionally doubles as testing that maze is perfect
	 * formula used Kruskal algorithm: (h-1)(w-1) inner walls
	 * how it works: use two for loops to cycle through every cell
	 * check south and right side for inner walls (don't check if its a border cell) starting from top left most cell
	 * up wall count if there is a wall
	 * <p>
	 * Method under test: accessing floorplan elements, generatePathways, generateRooms (when applicable)
	 * <p>
	 * Correct behavior: 
	 * at end if total wall count does not match optimal value for cell count then fail; otherwise pass
	 */
	@Test
	public void bestWallDesign() {
		// tests that there are the correct number of cells to walls
		// functionally doubles as testing that maze is perfect
		// formula used Kruskal algorithm: (h-1)(w-1) inner walls
		
		// how it works: use two for loops to cycle through every cell
		// check south and right side for inner walls (don't check if its a border cell) starting from top left most cell
		// up wall count if there is a wall
		// at end if total wall count does not match optimal value for cell count then fail; otherwise pass
		testMaze = mazeOrder.getMaze();
		testFloorplan = testMaze.getFloorplan();
		
		int mazeWidth = testMaze.getWidth();
		int mazeHeight = testMaze.getHeight();
		int wallCount = 0;
		
		for (int y = 0; y < mazeHeight; y++) {
			for (int x = 0; x < mazeWidth; x++) {
				if (y == mazeHeight - 1) {// exception for bottom row where only the right wall is checked
					if (x != mazeWidth - 1) {// excludes for bottom right corner where nothing needs to be checked
						if (testFloorplan.hasWall(x, y, CardinalDirection.East)) {
							wallCount = wallCount + 1;
						}
					}
				} else {
					if (testFloorplan.hasWall(x, y, CardinalDirection.South)) {
						wallCount = wallCount + 1;
					} if (x != mazeWidth - 1) { // excludes most right column where only bottom wall will be checked
						if (testFloorplan.hasWall(x, y, CardinalDirection.East)) {
							wallCount = wallCount + 1;
						}
					}
				}
			}
		}
		int perfectWallCount = (mazeWidth - 1) * (mazeHeight -1);
		assertTrue(perfectWallCount == wallCount);
		
	}
	
	/** 
	 * Test case: tests that entrance and exit are optimally far from one another
	 * how it works: get starting point value and exit value
	 * use the getDistanceToExit method from MazeContainer class to evaluate if its far as possible
	 * <p>
	 * Method under test: generatePathways, setExit related methods, setStart related methods
	 * <p>
	 * Correct behavior: 
	 * pass test if 1.0 returned, fail otherwise
	 */
	@Test
	public void bestEntryPoint() {
		// tests that entrance and exit are optimally far from one another
		
		// how it works: get starting point value and exit value
		// use the getDistanceToExit method from MazeContainer class to evaluate if its far as possible
		// pass test if 1.0 returned, fail otherwise
		// consider: allowing for a margin of error based on maze generation limitations

		testMaze = mazeOrder.getMaze();
		testFloorplan = testMaze.getFloorplan();
		
		Float distancePerc = testMaze.getPercentageForDistanceToExit(testMaze.getStartingPosition()[0], testMaze.getStartingPosition()[1]);
		Float checker = (float) 1.0;
		assertTrue(Float.compare(distancePerc, checker) == 0);
		
	}
	
	/** 
	 * Test case: tests that unless difficulty level is 0, the path is not literally straight to the exit
	 * how it works: if difficulty level set to 0 then instant pass if not then initiate test
	 * check that it's not just one move away from the exit
	 * <p>
	 * Method under test: generatePathways, general construction (skill level element)
	 * <p>
	 * Correct behavior: 
	 * pass test if it takes at least one move to get to the exit (reflects some work needed) - count kept low in case randomly picked skill level is 0 which could be too easy
	 */
	@Test
	public final void correctDiffLevel() {
		// tests that unless difficulty level is 0, the path is not literally straight to the exit
		
		// how it works: if difficulty level set to 0 then instant pass if not then initiate test
		// check that it's not just one move away from the exit
		// consider making it so that a straight path is acceptable for difficulty level 1
		// also consider making more thorough difficulty check
		testMaze = mazeOrder.getMaze();
		testFloorplan = testMaze.getFloorplan();
		
		if (mazeOrder.getSkillLevel() == 0) {
			assertTrue(mazeOrder.getSkillLevel() == 0);
		} else {
			assertTrue(testMaze.getDistanceToExit(testMaze.getStartingPosition()[0], testMaze.getStartingPosition()[1]) > 1);
		}
	}

}
