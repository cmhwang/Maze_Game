package generation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


import org.junit.Before;
import org.junit.Test;

import generation.Order.Builder;

/**
 * Tests individual methods of the MazeBuilderBoruvka class. 
 * Expands upon or overrides tests for general mazebulder
 * 
 * @author Cheyenne Hwang
 *
 */
public class MazeBuilderBoruvkaTest extends MazeFactoryTest {
	
	public MazeBuilderBoruvka mazeBuilderBoruvka;

	/**
	 * create a default, maze object using default order object created
	 * this default order uses boruvka though
	 * set up a maze to test with these tests
	 */
	@Override
	@Before
	public void setUp(){
		// set up a maze to test with these tests
		// want to test different types of mazes
		mazeBuilderBoruvka = new MazeBuilderBoruvka();
		testFactory = new MazeFactory();
		DefaultOrder baseOrder = new DefaultOrder();
		
		mazeOrder = new DefaultOrder(baseOrder.getSkillLevel(), Builder.Boruvka, baseOrder.isPerfect(), baseOrder.getSeed());
		
		testFactory.order(mazeOrder);
		testFactory.waitTillDelivered();
		
	}
	
	/** 
	 * Test case: tests that there are the correct number of cells to walls
	 * need to override cuz different from kruskal algorithm for inner walls now
	 * more like an appropriate wall design checker now
	 * counts number of inner walls
	 * then checks if its a reasonable amount given skill level;
	 * <p>
	 * Method under test: generatePathways, general construction of Boruvka, test treejoiner helper too
	 * <p>
	 * Correct behavior: 
	 * at end if total wall count does not exceed a reasonable amount for that skill level will fail, otherwise correct
	 */
	@Override
	@Test
	public void bestWallDesign() {
		// tests that there are the correct number of cells to walls
		// need to override cuz different from kruskal algorithm for inner walls now - more like an appropriate wall design checker now
		
		// counts number of inner walls
		// then checks if its a reasonable amount given skill level;
		testMaze = mazeOrder.getMaze();
		testFloorplan = testMaze.getFloorplan();

		int mazeWidth = testMaze.getWidth();
		int mazeHeight = testMaze.getHeight();
		int wallCount = 0;
				
		for (int y = 0; y < mazeHeight; y++) {
			for (int x = 0; x < mazeWidth; x++) {
				if (testFloorplan.hasWall(x, y, CardinalDirection.North) && testFloorplan.canTearDown(new Wallboard(x, y, CardinalDirection.North))) {
					wallCount = wallCount + 1;
				}
				if (testFloorplan.hasWall(x, y, CardinalDirection.South) && testFloorplan.canTearDown(new Wallboard(x, y, CardinalDirection.South))) {
					wallCount = wallCount + 1;
				}
				if (testFloorplan.hasWall(x, y, CardinalDirection.East) && testFloorplan.canTearDown(new Wallboard(x, y, CardinalDirection.East))) {
					wallCount = wallCount + 1;
				}
				if (testFloorplan.hasWall(x, y, CardinalDirection.West) && testFloorplan.canTearDown(new Wallboard(x, y, CardinalDirection.West))) {
					wallCount = wallCount + 1;
				}
				
			}
		}
		
		int testSkillLevel = mazeOrder.getSkillLevel();
		
		if (testSkillLevel == 0) {
			assertTrue(wallCount > 5);
		} else if (testSkillLevel == 1) {
			assertTrue(wallCount > 10);
		} else if (testSkillLevel == 2) {
			assertTrue(wallCount > 15);
		} else if (testSkillLevel == 3) {
			assertTrue(wallCount > 20);
		} else if (testSkillLevel == 4) {
			assertTrue(wallCount > 25);
		} else if (testSkillLevel == 5) {
			assertTrue(wallCount > 30);
		} else if (testSkillLevel == 6) {
			assertTrue(wallCount > 35);
		} else if (testSkillLevel == 7) {
			assertTrue(wallCount > 40);
		} else if (testSkillLevel == 8) {
			assertTrue(wallCount > 45);
		} else if (testSkillLevel == 9) {
			assertTrue(wallCount > 50);
		}
	}
	
	/** 
	 * Test case:tests that entrance and exit are optimally far from one another
	 * need to override this because entry now that I use boruvka, the distances are not always ideal because wall tear down method is different
	 * how it works: get starting point value and exit value
	 * use the getDistanceToExit method from MazeContainer class to evaluate if its far as possible
	 * <p>
	 * Method under test: generatePathways, general construction of Boruvka, test treejoiner helper too to see if walls set up appropriately and not tearing down too much
	 * <p>
	 * Correct behavior: 
	 * pass test if 0.5 - 1.0  to represent distance returned, fail otherwise
	 */
	@Override
	@Test
	public final void bestEntryPoint() {
		// tests that entrance and exit are optimally far from one another
		// need to override this because entry now that I use boruvka, the distances are not always ideal because wall tear down method is different
		
		// how it works: get starting point value and exit value
		// use the getDistanceToExit method from MazeContainer class to evaluate if its far as possible
		// pass test if 0.5 - 1.0 returned, fail otherwise
		// consider: allowing for a margin of error based on maze generation limitations
		testMaze = mazeOrder.getMaze();
		testFloorplan = testMaze.getFloorplan();

		Float distancePerc = testMaze.getPercentageForDistanceToExit(testMaze.getStartingPosition()[0], testMaze.getStartingPosition()[1]);
		Float checker = (float) 1.0;
		assertTrue(Float.compare(distancePerc, checker) == 0);
		
	}

	/** 
	 * Test case: tests that edge weights were randomly assigned by checking to to see if anything torn down
	 * logic is that if multiple walls are able to have been torn down , while some are still up, that means that there are walls with different weights
	 * <p>
	 * Method under test: generatePathways, setEdgeWeight
	 * <p>
	 * Correct behavior: 
	 * assert true if at least 2 walls have been torn down and there is a wall still standing
	 * this means that walls are being selectively torn down - a process that relies on random generation
	 */
	@Test
	public final void edgeWeightRandom() {
		// tests that edge weights were randomly assigned by checking to to see if anything torn down
		// if multiple walls are able to have been torn down , while some are still up, that means that there are walls with different weights
		// so assert true if at least 2 walls have been torn down and there is a wall still standing
		testMaze = mazeOrder.getMaze();
		testFloorplan = testMaze.getFloorplan();
		
		int mazeWidth = testMaze.getWidth();
		int mazeHeight = testMaze.getHeight();
		
		int wallDownCounter = 0;
		int wallUpCounter = 0;
		
		for (int x = 0; x < mazeWidth; x++) {
			for (int y = 0; y < mazeHeight; y++) {
				if (testFloorplan.hasWall(x, y, CardinalDirection.North) && testFloorplan.canTearDown(new Wallboard(x, y, CardinalDirection.North))) {
					wallUpCounter = wallUpCounter + 1;
				}
				if (testFloorplan.hasWall(x, y, CardinalDirection.South) && testFloorplan.canTearDown(new Wallboard(x, y, CardinalDirection.South))) {
					wallUpCounter = wallUpCounter + 1;
				}
				if (testFloorplan.hasWall(x, y, CardinalDirection.East) && testFloorplan.canTearDown(new Wallboard(x, y, CardinalDirection.East))) {
					wallUpCounter = wallUpCounter + 1;
				}
				if (testFloorplan.hasWall(x, y, CardinalDirection.West) && testFloorplan.canTearDown(new Wallboard(x, y, CardinalDirection.West))) {
					wallUpCounter = wallUpCounter + 1;
				} else if (testFloorplan.hasWall(x, y, CardinalDirection.North) || testFloorplan.hasWall(x, y, CardinalDirection.South) || testFloorplan.hasWall(x, y, CardinalDirection.West) || testFloorplan.hasWall(x, y, CardinalDirection.East)){
					wallDownCounter = wallDownCounter + 1;
				}
			}
		}
		
		assertTrue(wallDownCounter >= 2);
		assertTrue(wallUpCounter >= 1);
	}
	
	/** 
	 * Test case: hecks that if get edgeWeight is called on something initialized with a weight already, it returns the same weight
	 * <p>
	 * Method under test: generatePathways, setEdgeWeight, getEdgeWieght
	 * <p>
	 * Correct behavior: 
	 * should pass if it returns that same edgewieght value when getedgeweight is called on the same wall at diff times
	 */
	@Test
	public final void edgeWeightRepeat() {
		//checks that if get edgeWeight is called on something initialized with a weight already, it returns the same weight
		testMaze = mazeOrder.getMaze();
		testFloorplan = testMaze.getFloorplan();
		
		int mazeWidth = testMaze.getWidth();
		int mazeHeight = testMaze.getHeight();
		
		for (int x = 0; x < mazeWidth; x++) {
			for (int y = 0; y < mazeHeight; y++) {
				if (testFloorplan.hasWall(x, y, CardinalDirection.North) && testFloorplan.canTearDown(new Wallboard(x, y, CardinalDirection.North))) {
					int check1 = this.mazeBuilderBoruvka.getEdgeWeight(x, y, CardinalDirection.North);
					if (check1 == 1){
						assertTrue(mazeBuilderBoruvka.getEdgeWeight(x, y, CardinalDirection.North) == 1);
					} else if (check1 == 2){
						assertTrue(mazeBuilderBoruvka.getEdgeWeight(x, y, CardinalDirection.North) == 2);
					} else if (check1 == 3){
						assertTrue(mazeBuilderBoruvka.getEdgeWeight(x, y, CardinalDirection.North) == 3);
					} else if (check1 == 4){
						assertTrue(mazeBuilderBoruvka.getEdgeWeight(x, y, CardinalDirection.North) == 4);
					} else if (check1 == 5){
						assertTrue(mazeBuilderBoruvka.getEdgeWeight(x, y, CardinalDirection.North) == 5);
					} else if (check1 == 6){
						assertTrue(mazeBuilderBoruvka.getEdgeWeight(x, y, CardinalDirection.North) == 6);
					} else if (check1 == 7){
						assertTrue(mazeBuilderBoruvka.getEdgeWeight(x, y, CardinalDirection.North) == 7);
					} else if (check1 == 8){
						assertTrue(mazeBuilderBoruvka.getEdgeWeight(x, y, CardinalDirection.North) == 8);
					} else if (check1 == 9){
						assertTrue(mazeBuilderBoruvka.getEdgeWeight(x, y, CardinalDirection.North) == 9);
					}
					
				}
				
			}
		}
		
	}
	
	

}
