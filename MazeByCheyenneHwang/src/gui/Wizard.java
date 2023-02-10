package gui;

import generation.CardinalDirection;
import generation.Maze;
import static org.junit.Assert.assertTrue;

/**
 * This is a robot driver algorithm, the main one used for project 3
 * 
 * interacts with a a robot class and the maze class
 * interaction with robot class: controls/decides the robot's movements
 * needs the robot's sensors to gather info and be able to respond to potential errors/walls
 * interaction with maze class: uses maze class methods to find the exit
 * ex: Maze.getNieghborClosestToExit() to decide next move

 * 
 * if it drives the robot into a wall the game is over and the player loses
 * 
 * @author Cheyenne Hwang
 *
 */

public class Wizard implements RobotDriver { 
	
	public Robot robot;
	public Maze maze;
	public float energyUsed;
	public int cellsTravelled;
	
	/**
	 * default constructor
	 */
	public Wizard() {
		//default constructor
		//blank instantation
		energyUsed = 0;
		cellsTravelled = 0;
	}
	
	/**
	 * part of class coordination: gives driver the robot to make decisions for
	 * @param r is robot object to use
	 */
	@Override
	public void setRobot(Robot r) {
		// part of class coordination: gives driver the robot to make decisions for
		// will use this robot to perform actual movement
		// takes a robot object as parameter and sets it as in stance var
		robot = r;

	} 
	
	/**
	 * part of class coordination: gives driver maze to work with so that it can access right info
	 * @param takes a Maze, maze as parameter
	 * @throw exception if null
	 */
	@Override
	public void setMaze(Maze m) {
		// part of class coordination: gives driver maze to work with so that it can access right info
		// wizard will use maze + floorplan info to find exit using methods such as hasWall, getNeighborClosestToExit(), and more
		// utilize helper methods later in class
		// takes a maze as parameter
		// will throw exception if null
		
		if (m == null || m.getFloorplan() == null) {
			throw new IllegalArgumentException();
		} else {
			maze = m;
		}

	}	
	
	/**
	 * will drive robot to exit based on current general strategy
	 * if energy supply dies first or robot crashes throw exception
	 * checks first: use maze method to see if no exit, returns fails if so
	 * @return boolean true for exit reached, false for no exit, exception is dies
	 * @throws exception if robot dies here
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		// current general strategy: getNeighborClosest(), then move in that direction until hit a wall, then check L, back, R until one of these has no wall, then drive in that direction
		// will drive robot to exit based on current general strategy
		// utilizes drive1step() throughout to check for successful movement
		// will continue until energy supply dies or exit is reached
		// if exit reached terminate + return true
		// if energy supply dies first or robot crashes throw exception
		// check: use maze method to see if no exit, returns fails if so
		// returns: true for exit reached, false for no exit, exception is dies
		// also ends the game if the robot crashes
		boolean toReturn = false;
		boolean notReachedThisStep = true;
		 
		
		while (!robot.hasStopped() && notReachedThisStep) {
			notReachedThisStep = drive1Step2Exit();
			
		}
		if (!notReachedThisStep) {
			toReturn = true;
		} else if (energyUsed > (float) 3500) {
			
			throw new Exception();
		}
		if (toReturn) {
			try {
				finalDrive2End(robot.getCurrentPosition());
			} catch (Exception e) {
				throw new Exception();
			}
		} 
		return toReturn;
	}
	
	/**
	 * this is helper method to drive robot to exit based on current general strategy
	 * role: checks each individual move success based on energy supply and whether movement actuall occurs
	 * if energy supply dies first or robot crashes throw exception
	 * @return boolean true for robot moved, false for no movement, exception if dies from crash, error, or no energy
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		// current general strategy: getNeighborClosest(), then move in that direction until hit a wall, then check L, back, R until one of these has no wall, then drive in that direction
		// this is helper method to drive robot to exit based on current general strategy
		// role: checks each individual move success based on energy supply and whether movement actuall occurs
		// how: checks robot energy levels to check energy and uses getpathlength method to check movement success
		// if exit reached rotates robot to face exit
		// how: moves robot and forces it to use its distance sensors
		// if energy supply dies first or robot crashes throw exception
		// returns: true for robot moved, false for no movement, exception if dies from crash, error, or no energy
		
		boolean toReturn = false;
		if (robot.hasStopped()) {
			throw new Exception();
		} else if (robot.getCurrentPosition() == maze.getExitPosition()){ 
			return toReturn;
		} else { 
			int[] curPos = robot.getCurrentPosition();
			int[] nextPos = maze.getNeighborCloserToExit(curPos[0], curPos[1]);
			
			if (nextPos == null) {
				return toReturn;
			}
			
			CardinalDirection curCarDir = robot.getCurrentDirection();
			int[] relDirToTurn = {nextPos[0] - curPos[0], nextPos[1] - curPos[1] };
			
			if (curCarDir == CardinalDirection.North) {
				if (relDirToTurn[0] == 1) {
					robot.rotate(Robot.Turn.LEFT);
					energyUsed = energyUsed + 3;
				} else if (relDirToTurn[1] == 1) {
					robot.rotate(Robot.Turn.AROUND);
					energyUsed = energyUsed + 3;
				} else if (relDirToTurn[0] == -1) {
					robot.rotate(Robot.Turn.RIGHT);
					energyUsed = energyUsed + 3;
				} 
			}else if (curCarDir == CardinalDirection.West) {
				if (relDirToTurn[1] == -1) {
					robot.rotate(Robot.Turn.LEFT);
					energyUsed = energyUsed + 3;
				} else if (relDirToTurn[0] == 1) {
					robot.rotate(Robot.Turn.AROUND);
					energyUsed = energyUsed + 3;
				} else if (relDirToTurn[1] == 1) {
					robot.rotate(Robot.Turn.RIGHT);
					energyUsed = energyUsed + 3;
				}
			} else if (curCarDir == CardinalDirection.South) {
				if (relDirToTurn[0] == -1) {
					robot.rotate(Robot.Turn.LEFT);
					energyUsed = energyUsed + 3;
				} else if (relDirToTurn[1] == -1) {
					robot.rotate(Robot.Turn.AROUND);
					energyUsed = energyUsed + 3;
				} else if (relDirToTurn[0] == 1) {
					robot.rotate(Robot.Turn.RIGHT);
					energyUsed = energyUsed + 3;
				}
			} else if (curCarDir == CardinalDirection.East) {
				assert curCarDir == CardinalDirection.East : "This was the last direction"; 
				if (relDirToTurn[1] == 1) {
					robot.rotate(Robot.Turn.LEFT);
					energyUsed = energyUsed + 3;
				} else if (relDirToTurn[0] == -1) {
					robot.rotate(Robot.Turn.AROUND);
					energyUsed = energyUsed + 3;
				} else if (relDirToTurn[1] == -1) {
					robot.rotate(Robot.Turn.RIGHT);
					energyUsed = energyUsed + 3;
				}
			}
			
			energyUsed = energyUsed + 1;
			robot.move(1);
			energyUsed = energyUsed + 6;
			cellsTravelled = cellsTravelled + 1;
			toReturn = true;
			
			if (robot.hasStopped()) {
				throw new Exception();
			}
			return toReturn;
		}
		
	}
	
	/**
	 * this is helper method to drive robot to exit based on current general strategy
	 * role: gives the total energy used for the drive so far, will get called on by drive2exit() to check aggregate
	 * @return float to represent total energy used
	 */
	@Override
	public float getEnergyConsumption() {
		// this is helper method to drive robot to exit based on current general strategy
		// role: gives the total energy used for the drive so far, will get called on by drive2exit() to check aggregate
		// how calculate: difference between energy level at start position to end at ecit, can utilize getPathLength() to find
		// returns float to represent total energy used
		// new strategy: have drives update the energyUsed instance var, return this var
		// warning: may need to adjust if need energyConsumption for individual drives not piece by piece
		// consider using robot's val
		return energyUsed;
	}
	
	/**
	 * this is helper method to drive robot to exit based on current general strategy
	 * role: gives the total length of journey in cells visited at end of whole drive
	 * @return int for total length as an int
	 */
	@Override
	public int getPathLength() {
		// this is helper method to drive robot to exit based on current general strategy
		// role: gives the total length of journey in cells visited at end of whole drive
		// returns total length as an int
		// consider; splitting into helper method for each step not jsut whole drive
		// new strategy: have drives update the cellsTravelled instance var, return this var
		// consider using robot's val

		return robot.getOdometerReading();
	}
	
	/**
	 * this is helper method to drive robot to exit by doing the final crossover to the exit
	 * role: akes the last step by doing any needed rotations and then crossing into the exit
	 * @return int[] position curPos which is supposed to represetn the robot's current position which it will check against
	 * @throws Exception if input is not the exit or if robot dies here
	 */
	public void finalDrive2End(int[] curPos) throws Exception {
		// this is helper method to drive robot to exit by doing the final crossover to the exit
		// role: makes the last step by doing any needed rotations and then crossing into the exit
		// takes the current position (which should be right at the exit) as a parameter
		// parameter: takes the position curPos which is supposed to represetn the robot's current position which it will check against
		// throws error if not the exit
		if ((curPos[0] != maze.getExitPosition()[0]) || (curPos[1] != maze.getExitPosition()[1])) {
			throw new Exception();
		}
		
//		while(maze.hasWall(curPos[0], curPos[1], robot.getCurrentDirection()) || (robot.getCurrentDirection() == CardinalDirection.North && (curPos[1] - 1 < 0)) || (robot.getCurrentDirection() == CardinalDirection.West && (curPos[0] - 1 < 0)) || (robot.getCurrentDirection() == CardinalDirection.South && (curPos[1] + 1 >= maze.getHeight())) || (robot.getCurrentDirection() == CardinalDirection.East && (curPos[0] + 1 >= maze.getWidth())) ) {
		while(!(robot.canSeeThroughTheExitIntoEternity(Robot.Direction.FORWARD))) {
			int counter = 0;
			if (counter > 4) {
				throw new Exception();
			}
			robot.rotate(Robot.Turn.LEFT);
			energyUsed = energyUsed + 3;
			counter = counter + 1;
			
			
		}
		energyUsed = energyUsed + 6;
		if (energyUsed >= 3500) {
			throw new Exception();
		} else {
			robot.move(1);
		}
		
		
	}

}
