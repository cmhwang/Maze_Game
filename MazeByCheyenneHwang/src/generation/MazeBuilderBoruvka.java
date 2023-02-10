package generation;

import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

import gui.Constants;

/**
 * This class has the responsibility to create a maze of given dimensions (width, height) 
* together with a solution based on a distance matrix.
* The MazeBuilder implements Runnable such that it can be run a separate thread.
* The MazeFactory has a MazeBuilder and handles the thread management.   

* 
* The maze is built with a randomized version of Boruvka's algorithm. 
* This means a spanning tree is expanded into a set of cells by removing wallboards from the maze.
* Algorithm sets up every inner wall, tears down these walls according to boruvka until perfect maze
* Boruvka aspect: each cell is treated like an unconnected node, all the cells start with wallboards between them the same way that unconnected nodes would
* then I begin the process of connecting nodes based on the which edge connecting the node to another node is the cheapest
* by tearing down the cheapest wallboard, that is a proxy for creating the cheapest edge for a node. 
*   
* @author Jones.Andrew, refactored by Peter Kemper
*/

public class MazeBuilderBoruvka extends MazeBuilder implements Runnable {
	
	protected int[][][] edgeWeight;//for storing edge weights in
	protected int treeGroupTracker; // for storing total number of groups during wall teardown session
	protected int[][] partOfTree;
	protected int[][] treeAssigner; // for storing what tree a cell is part of
	protected int treeId = 0; // for creating a call number for each tree
	
	
	private static final Logger LOGGER = Logger.getLogger(MazeBuilderBoruvka.class.getName());
	
	public MazeBuilderBoruvka() {

		super();
	}
	
	

	/**
	 * This method generates pathways into the maze by using Boruvka's algorithm to generate a tree using cells boxed in as unconnected starting nodes.

	 * adjusting method will tear down walls according to Borvuka's algorithms until we have one set that includes every cell 
	 * start by iterating through every cell once and connecting it to at least one other cell (cheapest side)
	 * then at the end check if all the cell groups are connected in one or if there are multiple with treejoiner variab;e
	 * end if there are multiple, re-do process treating each cell group as new cell if not done
	 */
	@Override
	protected void generatePathways() {
		// advice - be careful about finding neighbors, be careful about finding edgeweights
		// use hashmap to store edgeweights, 
		// be careful with amount of arrays
		// potentially use list of groups for tree joiner rather than using iterators to search for each individual cell
		edgeWeight = new int[width][height][4];
		partOfTree = new int[width][height];
		treeAssigner = new int[width][height];
		treeGroupTracker = width*height;
		
		
		for (int x = 0; x < width; x++) {// for loop sequence to assign edge weights to all the inner walls
			for (int y = 0; y < height; y++) {
				for (int d = 0; d < 4; d++) {
					if (edgeWeight[x][y][d] == 0) {
						
						if (!(y == 0 && d == 0) && !(x == 0 && d == 1) && !(y == height - 1 && d == 2) && !(x == width - 1 && d == 3)) {// makes sure none of the border walls get a weight
							int newWeight = getEdgeWeight(x, y, intToDir(d));
							edgeWeight[x][y][d] = newWeight;
							
							if ((x != width - 2) && (d == 3)) { // needs to set west edge for its east neighbor too
								edgeWeight[x + 1][y][1] = newWeight;
							} 
							if ((y != height - 2)  && (d == 2)){ // needs to set north edge for its south neighbor too
								edgeWeight[x][y + 1][0] = newWeight;
							}
						}
						
						
					}
				}
			}
		}
		
		// initial for loop sequence to begin borvuka algorithm to tear down walls and start forming trees
		// only does initial tear down, section later will join trees created
		boolean cheapestFound;
		int cheapestValue;
		for (int x = 0; x < width; x++) { 
			for (int y = 0; y < height; y++) {
				if (partOfTree[x][y] != 1) {// since this is the initial run - will only work with cells that aren't already part of a tree group
					treeAssigner[x][y] = treeId;
					cheapestFound = false;
					cheapestValue = 100; // restart at 100 every time new pathway being burrowed so that the first edge found will always be the cheapest so far
					int curX = x;
					int curY = y;
					while (!cheapestFound) {
						CardinalDirection cheapest = isPreferred(curX, curY, cheapestValue);
						
						if (cheapest != null) {
							
							Wallboard toDelete = new Wallboard(curX, curY, cheapest);//deletes the wallboard
							cheapestValue = edgeWeight[curX][curY][dirToInt(cheapest)];//updates what the cheapest edge weight so far is
							floorplan.deleteWallboard(toDelete);
							
							partOfTree[curX][curY] = 1;
							treeGroupTracker = treeGroupTracker - 1;
							
							if (cheapest == CardinalDirection.North) {//resets and moves into the next cell
								treeAssigner[curX][curY-1] = treeId;
								curY = curY - 1;
							} else if (cheapest == CardinalDirection.West) {
								treeAssigner[curX-1][curY] = treeId;
								curX = curX - 1;
							} else if (cheapest == CardinalDirection.South) {
								treeAssigner[curX][curY+1] = treeId;
								curY = curY + 1;
							} else {
								treeAssigner[curX+1][curY] = treeId;
								curX = curX + 1;
							}
						}else {
							cheapestFound = true;
							treeId = treeId + 1;
						}
					}
				}
				
				
			}
		}
		
		while(treeGroupTracker > 1) {
			for (int g = 0; g <= treeId; g++) {
				treeJoiner(g);
			}
			
			
		}
		
	}
	/**
	 * adjusting method will be used to generate a deterministic edge weight
	 * check if this edge has been visited
	 * if it has been visited then retrieve previous weight
	 * if it has not been visited then assign a new edge weight and store that in an array somewhere
	 * @param int for the x value of cell, int for y value of cell, cardinal direction of the wall for that cell
	 * @return int that represents edge weight for that wall
	 */
	public int getEdgeWeight(int x, int y, CardinalDirection cd) {
		// adjusting method will be used to generate a deterministic edge weight
		// check if this edge has been visited
		// if it has been visited then retrieve previous weight
		// if it has not been visited then assign a new edge weight and store that in an array somewhere
		int toReturn;
		int cdRep = dirToInt(cd);
		
		if (edgeWeight[x][y][cdRep] != 0) {// if it's already stored then return what's stored
			toReturn = edgeWeight[x][y][cdRep];
		} else {
			Random random = new Random();
			toReturn = random.nextInt(9) + 1;
		}
		
		return toReturn;
	}
	
	/**
	 * helper function will return 1 or 0 to represent an assigned direction
	 * utilize random number generator
	 * deals with identical wall edge weight values
	 * @return an int either 1 or 2 that will be used to break ties will tearing down walls randomly 
	 */	
	public int tieBreaker() {
		// helper function will return 1 or 0 to represent an assigned direction
		// utilize random number generator
		
		Random random = new Random();
		int toReturn = random.nextInt(2) + 1;
		
		return toReturn;
	}

	/**
	 * helper function will return the direction that has the lightest weight
	 * returns null if the cheapest weight was already found and the remaining weights are all heavier than the previous cheaper weight 
	 * with each comparison of the minimum makes sure that the wall can actually be torn down
	 * @param x is the x value of cell, y is y value of cell, toCompare is the current minimum value of all the edge weights burrowed through this run
	 * @return null if cheapest edge weight has already been found or a cardinal direction for the wallboard with the cheapest edge weight for that cell for this burrow run 
	 */	
	public CardinalDirection isPreferred(int x, int y, int toCompare) {
		// helper function will return the direction that has the lightest weight
		//returns null if the cheapest weight was already found and the remaining weights are all heavier than the previous cheaper weight 
		// with each comparison of the minimum makes sure that the wall can actually be torn down
		CardinalDirection bestDir = null;
		int curMin = 100;
		
		if (floorplan.hasWall(x, y, CardinalDirection.North) && floorplan.canTearDown(new Wallboard(x, y, CardinalDirection.North))) {
			curMin = edgeWeight[x][y][0];
			bestDir = CardinalDirection.North;
		}
		if (floorplan.hasWall(x, y, CardinalDirection.South) && floorplan.canTearDown(new Wallboard(x, y, CardinalDirection.South))) {
			if (edgeWeight[x][y][2] < curMin) {
				curMin = edgeWeight[x][y][0];
				bestDir = CardinalDirection.South;
			} 
		}
		if (floorplan.hasWall(x, y, CardinalDirection.East) && floorplan.canTearDown(new Wallboard(x, y, CardinalDirection.East))) {
			if (edgeWeight[x][y][3] < curMin) {
				bestDir = CardinalDirection.East;
			} 
		}
		if (floorplan.hasWall(x, y, CardinalDirection.West) && floorplan.canTearDown(new Wallboard(x, y, CardinalDirection.West))) {
			if (edgeWeight[x][y][1] < curMin) {
				bestDir = CardinalDirection.West;
			}
		}
		
		
		if (bestDir != null && (edgeWeight[x][y][dirToInt(bestDir)] > toCompare)){// final check to make sure that this cell's cheapest edge is the cheapest edge found in total for this clear out cycle
			bestDir = null;
		} else if (bestDir != null && (edgeWeight[x][y][dirToInt(bestDir)] == toCompare)) {
			if (tieBreaker() == 1) {
				bestDir = null;
			}
		} 
		return bestDir;
	}
	
	/**
	 * helper function that translates the int representation of a direction value in the edge weights array to a direction
	 * used throughout as an index/hashmap helper
	 * @param d is the integer representing a certain cardinal direction
	 * @return the cardinal direction represented by that integer
	 */	
	public CardinalDirection intToDir(int d) {
		//helper function that translates the int representation of a direction value in the edge weights array to a direction
		if (d == 0) { 
			return CardinalDirection.North;
		} else if (d == 1) {
			return CardinalDirection.West;
		} else if (d == 2) {
			return CardinalDirection.South;
		} else {
			return CardinalDirection.East;
		}
	}
	
	/**
	 * helper function that translates a direction value to an int representation of a direction value
	 * used throughout as an index/hashmap helper
	 * @param d is a cardinal direction that needs be turned into an index
	 * @return the integer representing a certain cardinal direction
	 */	
	public int dirToInt(CardinalDirection d) {
		//helper function that translates a direction value to an int representation of a direction value
		if (d == CardinalDirection.North) { 
			return 0;
		} else if (d == CardinalDirection.West) {
			return 1;
		} else if (d == CardinalDirection.South) {
			return 2;
		} else {
			return 3;
		}
	}

	/**
	 * adjustor method to be used after the first initial set of wall teardowns
	 * cycles through and find all cells part of tree group 1 ( will cycle through different groups)
	 * find all of them that have border with other tree (by tree id diff)
	 *  select cheapest edge from that border listing
	 * deletes the border it an merge everything (take a for loop to run through every cell)
	 * @param treeGroup is an integer representing the tree group that I want to merge with its cheapest neighboring tree
	 */	
	public void treeJoiner(int treeGroup) {
		// adjustor method to be used after the first initial set of wall teardowns
		// will join the minimal tree start cell is a part of to the nearest minimal tree 
		
		//new plan: cycle through and find all cells part of tree group 1; find all of them that have border with other tree (by tree id diff), select cheapest edge from that - delete it an merge everything (take a for loop to run through every cell
		int[][] inTreeGroup = new int[width][height];
		int[][][] treeGroupBorders = new int[width][height][4]; // like edgeWeights array for the whole thing but only stores wallboard weights that are the border of two trees
		int[] toDelete = new int[3];
		int groupToMerge;
		
		for (int x = 0; x < width; x++) {// initial for loop sequence to cycle through every cell and find which cells are in the treeGroup we're starting with
			for (int y = 0; y < height; y++) {
				if (treeAssigner[x][y] == treeGroup) {
					inTreeGroup[x][y] = 1;
				}
			}
		}
		boolean groupMerged = true;
		for (int x = 0; x < width; x++) {// for loop sequence to check if there are any cells remaining in the tree group (if there are none then this group has already been merged and we should move on - the other code will not be activated)
			for (int y = 0; y < height; y++) {
				if (inTreeGroup[x][y] == 1) {
					groupMerged = false;
				}
			}
		}
		if (!groupMerged) {
			for (int x = 0; x < width; x++) {// iterator to find out the wallboard locations of cells that are the border for this loop
				for (int y = 0; y < height; y++) {
					if (inTreeGroup[x][y] == 1) {
						
						if (floorplan.hasWall(x, y, CardinalDirection.North) && floorplan.canTearDown(new Wallboard(x, y, CardinalDirection.North))) {
							if (treeAssigner[x][y] != treeAssigner[x][y-1]) { 
								treeGroupBorders[x][y][0] = edgeWeight[x][y][0];
							}
						}
						if (floorplan.hasWall(x, y, CardinalDirection.South) && floorplan.canTearDown(new Wallboard(x, y, CardinalDirection.South))) {
							if (treeAssigner[x][y] != treeAssigner[x][y+1]) { 
								treeGroupBorders[x][y][2] = edgeWeight[x][y][2];
							}
						}
						if (floorplan.hasWall(x, y, CardinalDirection.East) && floorplan.canTearDown(new Wallboard(x, y, CardinalDirection.East))) {
							if (treeAssigner[x][y] != treeAssigner[x+1][y]) {
								treeGroupBorders[x][y][3] = edgeWeight[x][y][3];
							}
						}
						if (floorplan.hasWall(x, y, CardinalDirection.West) && floorplan.canTearDown(new Wallboard(x, y, CardinalDirection.West))) {
							if (treeAssigner[x][y] != treeAssigner[x-1][y]) {
								treeGroupBorders[x][y][1] = edgeWeight[x][y][1];
							}
						}

					}
				}
			}
			
			int curMinEdgeWeight = 100; // this section will find the border wall with the cheapest edge weight
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					for (int d = 0; d < 4; d++) {
						int toExamine = treeGroupBorders[x][y][d];
						if (toExamine != 0 && toExamine < curMinEdgeWeight) {
							curMinEdgeWeight = toExamine;
							toDelete[0] = x;
							toDelete[1] = y;
							toDelete[2] = d;
						} else if (toExamine != 0 && toExamine == curMinEdgeWeight) {//tie breaker
							if (tieBreaker() == 1) {
								curMinEdgeWeight = toExamine;
								toDelete[0] = x;
								toDelete[1] = y;
								toDelete[2] = d;
							}
						}
					}
				}
			}
			
			Wallboard willDelete = new Wallboard(toDelete[0], toDelete[1], intToDir(toDelete[2]));// this section deletes the wallboard that's the cheapest
			if(floorplan.canTearDown(willDelete)) {
				floorplan.deleteWallboard(willDelete); //TODO this is the line throwing the error
				
				if (toDelete[2] == 0) {// this section finds the group that is getting merged with the group called in the method argument - for if it's getting merged with group to the north
					groupToMerge = treeAssigner[toDelete[0]][toDelete[1] - 1];
				} else if (toDelete[2] == 1) {// if group being merged is to the West
					groupToMerge = treeAssigner[toDelete[0] - 1][toDelete[1]];
				} else if (toDelete[2] == 2) {// if group being merged is to the South
					groupToMerge = treeAssigner[toDelete[0]][toDelete[1] + 1];
				} else { // if group being merged is to the East
					groupToMerge = treeAssigner[toDelete[0] + 1][toDelete[1]];
				}
				
				for (int x = 0; x < width; x++) {// this section sets the treeID assignment for the group that got merged to the one in the method argument
					for (int y = 0; y < height; y++) {
						if (treeAssigner[x][y] == groupToMerge) {
							treeAssigner[x][y] = treeGroup;
						}
					}
				}
				treeGroupTracker = treeGroupTracker - 1; // updates the variable counting the amount of tree groups
			}
			
			
		}
		
	}

}

