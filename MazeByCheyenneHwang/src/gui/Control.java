package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import generation.*;

import gui.ColorTheme.ColorThemeSelection;
import gui.Constants.UserInput;
import gui.Robot.Direction;

/**
 * Class handles the user interaction through the different stages of the game.
 * 
 * It implements a state pattern where this class is the Context, 
 * State is the state interface and its implementing state classes
 * all carry a prefix State in its name.
 * The transition from one state to another is distributed across states.
 *  
 * The game currently has 4 states.
 * StateTitle: the starting state where the user can pick the skill-level
 * StateGenerating: the state in which the factory computes the maze to play
 * and the screen shows a progress bar.
 * StatePlaying: the state in which the user plays the game and
 * the screen shows the first person view and the map view.
 * StateWinning: the finish screen that shows the winning message.
 * 
 * This class implements an automaton with states for the different stages of the game.
 * It has state-dependent behavior that controls the display and reacts to key board input from a user.
 * To handle user keyboard input it implements a key listener, digests the input 
 * and delegates the responsibility to respond to its state object.
 *
 * This code is refactored code from Maze.java by Paul Falstad, 
 * www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 * 
 * @author Peter Kemper
 */
public class Control extends JFrame implements KeyListener {

	// not used, just to make the compiler, static code checker happy
	private static final long serialVersionUID = 1L;
	
	// developments vs production version
	// for development it is more convenient if we produce the same maze over an over again
	// by setting the following constant to false, the maze will only vary with skill level and algorithm
	// but not on its own
	// for production version it is desirable that we never play the same maze 
	// so even if the algorithm and skill level are the same, the generated maze should look different
	// which is achieved with some random initialization
	private static final boolean DEVELOPMENT_VERSION_WITH_DETERMINISTIC_MAZE_GENERATION = false;
	// rooms are an additional feature that generalizes the text book maze generation algorithms
	// for development it can be useful to turn of the room generation to focus on the standard algorithm
	private static final boolean DEVELOPMENT_VERSION_MAZE_GENERATION_WITHOUT_ROOMS = false;
	
	/**
	 * The logger is used to track execution and report issues.
	 * Collaborators are the UI and the MazeFactory.
	 * Level INFO: logs mitigated issues such as unexpected user input
	 * Level FINE: logs information flow.
	 */
	private static final Logger LOGGER = Logger.getLogger(Control.class.getName());

	/**
     * Specifies if the maze is perfect, i.e., it has
     * no loops, which is guaranteed by the absence of 
     * rooms and the way the generation algorithms work.
     */
    boolean perfect;
    /**
     * In the deterministic setting (true), 
     * the same random maze will be generated over 
     * and over again for the same settings of skill level, 
     * builder, and perfect.
     * If false, a different maze will be generated each
     * and every time, even if settings of skill level, 
     * builder, and perfect remain the same.
     */
    boolean deterministic;
    
	/**
	 * The current state of the controller and the game.
	 * All state objects share the same interface and can be
	 * operated in the same way, although the behavior is 
	 * vastly different.
	 * currentState is never null and updated from the 
	 * state objects themselves when they hand over 
	 * control to the next state.
	 * The game goes through 4 states: 
	 * 1: show the title screen, wait for user input for skill level
	 * 2: show the generating screen with the progress bar during 
	 * maze generation
	 * 3: show the playing screen, have the user or robot driver
	 * play the game
	 * 4: show the finish screen with the winning/loosing message
	 * 
	 * The initial state is the title state.
	 */
	State currentState;
    /**
     * The panel is used to draw on the screen for the UI.
     * It can be set to null for dry-running the controller
     * for testing purposes but otherwise panel is never null.
     */
    MazePanel panel;

    /**
     * Default constructor
     */
	public Control() {
		currentState = new StateTitle(); // initial state is the title state
		((StateTitle)currentState).setBuilder(Order.Builder.DFS);
		
		panel = new MazePanel(); 
		
		// determine if a maze can have rooms or not
		// a perfect maze has no rooms and matches a spanning tree
		perfect = DEVELOPMENT_VERSION_MAZE_GENERATION_WITHOUT_ROOMS? true: false;
		
		
		// can decide if user repeatedly plays the same mazes or 
	    // if mazes are different each and every time
	    // set to true for testing purposes
	    // set to false for playing the game
		deterministic = DEVELOPMENT_VERSION_WITH_DETERMINISTIC_MAZE_GENERATION? true : false;
	}
	/**
	 * Sets the current state to the given one.
	 * The method is prescribed in the State pattern as the one that helps
	 * updating the context class to the next state.
	 * All specific state classes use this to hand over control to the 
	 * next state once they are done.
	 * 
	 * @param state the state that should become the current state of the game
	 */
    public void setState(State state) {
    	currentState = state;
    }
    
    /**
     * Informs if the current mode of operation is deterministic.
     * This means that the same maze can be generated repeatedly.
     * @return true if mode is deterministic, false otherwise
     */
    public boolean isDeterministic() { 
    	return deterministic;
    }
    /**
     * Informs if the current mode of operation such that the maze is perfect and can't have rooms.
     * This means that the maze matches with a spanning tree.
     * @return true if maze is perfect, false otherwise
     */
    public boolean isPerfect() { 
    	return perfect;
    }

    /**
     * Gets a reference to the panel to draw on.
     * @return the panel
     */
    public MazePanel getPanel() {
        return panel;
    }
    
    /**
     * Starts the controller and begins the game 
     * with the title screen.
     */
    public void start() { 
		add(panel) ;
		// Control digests keyboard input, need to add it to the JFrame to receive inputs that feeds keyboard input into the controller
		addKeyListener(this) ;
		// set the frame to a fixed size for its width and height and put it on display
		setSize(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT+22) ;
		setVisible(true) ;
		// focus should be on the JFrame of Control and not on the maze panel
		// such that Control as the keyboard listener is used
		setFocusable(true) ;
		
		LOGGER.config("Starting in determistic mode? " + deterministic + ", room generation is turned off? " + perfect);
		
		// start the game, hand over control to the StateTitle
        currentState.start(this, panel);
    }
 
 
    /**
     * Turns of graphics to dry-run controller for testing purposes.
     * This is irreversible and subsequent calls to getPanel() will return null. 
     */
    public void turnOffGraphics() {
    	LOGGER.config("Turning graphics permanently off, makes sense for unit testing.");
    	panel = null;
    }
    
    //// Extension in preparation for Project 3: robot and robot driver //////
    // TODO: decide if this should move to StatePlaying 
    /**
     * Generic robot that interacts with the controller
     */
    Robot robot;
    /**
     * Generic driver that interacts with the robot starting from P3
     */
    RobotDriver driver;
    
    
    
    /**
     * Sets the robot and robot driver
     * @param robot the robot that is used for the automated playing mode
     * @param robotdriver the driver that is used for the automated playing mode
     */
    public void setRobotAndDriver(Robot robot, RobotDriver robotdriver) {
    	//setting the robot type is equivalent to setting the sensor type
    	if (robot.getRobotType().equals("reliable")) {
        	this.robot = (ReliableRobot) robot;
        	//IDK if this is needed
        } else {
        	assert robot.getRobotType().equals("unreliable") : "This is the only other option for a robot/sensor setting";
        	this.robot = (UnreliableRobot) robot;


        }
        
        if (robotdriver != null) {
        	driver = robotdriver;

        	
        } else {
        	assert robotdriver == null : "The only remaining option for the robot driver is the manual mode";
        }
        
    }
    /**
     * The robot that is used in the automated playing mode.
     * Null if run in the manual mode.
     * @return the robot, may be null
     */
    public Robot getRobot() {
        return robot;
    }
    /**
     * The robot driver that is used in the automated playing mode.
     * Null if run in the manual mode.
     * @return the driver, may be null
     */
    public RobotDriver getDriver() {
        return driver;
    }
    /**
     * Provides access to the maze. 
     * This is needed for a robot to be able to recognize walls
     * for the distance to walls calculation, to see if it 
     * is in a room or at the exit. 
     * Note that the current position is stored by the 
     * controller. The maze itself is not changed during
     * the game.
     * This method should only be called in the playing state.
     * @return the maze
     */
    
    public Maze getMaze() {
        return ((StatePlaying)currentState).getMaze();
    }
    
    /**
     * Provides access to the current position.
     * The controller keeps track of the current position
     * while the maze holds information about walls.
     * This method should only be called in the playing state.
     * @return the current position as [x,y] coordinates, 
     * {@code 0 <= x < width, 0 <= y < height}
     */
    
    public int[] getCurrentPosition() {
        return ((StatePlaying)currentState).getCurrentPosition();
    }
    
    /**
     * Provides access to the current direction.
     * The controller keeps track of the current position
     * and direction while the maze holds information about walls.
     * This method should only be called in the playing state.
     * @return the current direction
     */
    
    public CardinalDirection getCurrentDirection() {
        return ((StatePlaying)currentState).getCurrentDirection();
    }
	
	////////////// end of P3 specific additions //////////////////



	private void handleCommandLineInput(String[] args) {
		// command line input can specify the builder algorithm to use
	    // or a filename to load an already computed maze from
	    
	    // possible builder algorithms are  (Prim, Kruskal, Eller, Boruvka)
	    
	    // command line input only applies to the first round of the game
	    // so we can directly apply it to the currentState 
//		String[] args = new String[6];
//		
//		for (int i = 0; i < argumentFull.length - 1; i++) {
//			if (argumentFull[0].substring(i, i++).equalsIgnoreCase("-g")) {
//				args[0] = "-g";
//				
//			} 
//		}
		
		
	    if (args.length == 0) { 
	    	// no command line input
	    	LOGGER.fine("No command line input: maze will be generated with a randomized algorithm.");
	    	return;
	    }
	    String parameter = args[0];
	    String msg = "Error in handling command line input: " + parameter; // message for feedback
	    
	    int argCounter = 0;
	    
	    while (argCounter < args.length) {
	    	parameter = args[argCounter];
	    	
	    	if (parameter.equals("-g")){ // for setting maze builder type
	    		
	    		argCounter++;
	    		String algo = args[argCounter];
	    		
	    		File f = new File(algo);
	    		if (algo == null) {
	    			msg = "Unknown command line parameter: " + parameter + " ignored, operating in default mode. \n";
	    		} else if (algo.equalsIgnoreCase("-d")) {
	    			msg = "No comman line input detected for maze generation: generating random maze with default settings.\n";
	    			argCounter--;
	    		} else if (algo.equalsIgnoreCase("Prim")){
	    			msg = "Command line input detected: generating random maze with Prim's algorithm. \n";
	    	        ((StateTitle)currentState).setBuilder(Order.Builder.Prim);
	    		} else if (algo.equalsIgnoreCase("Boruvka")) {
	    			msg = "Command line input detected: generating random maze with Boruvka's algorithm. \n";
	    	        ((StateTitle)currentState).setBuilder(Order.Builder.Boruvka);
	    		} else if (algo.equalsIgnoreCase("DFS")) {
	    			msg = "Command line input detected: generating random maze with default settings.\n";
	    		} else if (f.exists() && f.canRead()) {
	    			 msg = "Detected file descriptor on command line, loading maze from this file: " + parameter +". \n";
	 	            ((StateTitle)currentState).setFileName(parameter);
	    		} else {
	    			msg = "Unknown command line parameter: " + parameter + " ignored, operating in default mode. \n";
	    		}
	    		
	    		argCounter++;
	    		
	    	} else if (parameter.equals("-d")) { // for setting robot driver type
	    		
	    		argCounter++;
	    		String robotDriver = args[argCounter];
	    		
	    		if (robotDriver == null) {
	    			msg += "No robot driver input detected, switching to manual mode.\n";
	    		} else if (robotDriver.equalsIgnoreCase("-r")){
	    			msg += "No robot driver input detected, switching to manual mode.\n";
	    			argCounter--;// to counter the auto increment
	    		} else if (robotDriver.equalsIgnoreCase("Wizard")){
	    			msg += "Using Wizard robot driver to try solving the maze.\n"; 
	    			driver = new Wizard();
	    		} else if (robotDriver.equalsIgnoreCase("SmartWizard")){
	    			msg += "Using SmartWizard robot driver to try solving the maze.\n"; 
	    			driver = new SmartWizard();
	    		} else if (robotDriver.equalsIgnoreCase("WallFollower")){
	    			msg += "Using Wall Follower robot driver to try solving the maze.\n";
	    			driver = new WallFollower();
	    		} else if (robotDriver.equalsIgnoreCase("SmartWallFollower")){
	    			msg += "Using Smart Wall Follower robot driver to try solving the maze.\n";
	    			driver = new SmartWallFollower();
	    		}else if (robotDriver.equalsIgnoreCase("Manual")){
	    			msg += "Using manual robot driver to solve the maze.\n";
	    		}
	    		
	    		argCounter++;

	    	} else if (parameter.equals("-r")) {//for setting robot sensor/robot type
	    		
	    		argCounter++;
	    		if (argCounter >= args.length) {
	    			msg += "No robot specified or robot specification incomplete, using Reliable Robot as default mode.\n";
	    			
	    			robot = new ReliableRobot();
    				
    				ReliableSensor forwardSensor = new ReliableSensor();
    				forwardSensor.setSensorDirection(Direction.FORWARD);
	                robot.addDistanceSensor(forwardSensor, Direction.FORWARD);
	                
	                ReliableSensor leftSensor = new ReliableSensor();
					leftSensor.setSensorDirection(Direction.LEFT);
	                robot.addDistanceSensor(leftSensor, Direction.LEFT);
	                
	                ReliableSensor rightSensor = new ReliableSensor();
					rightSensor.setSensorDirection(Direction.RIGHT);
	                robot.addDistanceSensor(rightSensor, Direction.RIGHT);
	                
	                ReliableSensor backSensor = new ReliableSensor();
					backSensor.setSensorDirection(Direction.BACKWARD);
	                robot.addDistanceSensor(backSensor, Direction.BACKWARD);
	    		} else {
	    			String sensorSetting = args[argCounter];
	    			
	    			int forwardSensorSpec; // 1 represents a reliable setting, 0 is unreliable
		    		int leftSensorSpec;
		    		int backSensorSpec;
		    		int rightSensorSpec;
		    		
		    		if (args[argCounter].substring(0, 1).equals("1")) {// forward sensor setting
	    				forwardSensorSpec = 1;
	    				
	    			} else {
	    				assert args[argCounter].substring(0, 1).equals("0") : "Invalid input for sensor specification";
	    				
	    				forwardSensorSpec = 0;
	    			} 
	    			
	    			if (sensorSetting.substring(1, 2).equals("1")) {//left sensor setting
	    				leftSensorSpec = 1;
	    			} else {
	    				assert sensorSetting.substring(1, 2).equals("0") : "Invalid input for sensor specification";
	    				
	    				leftSensorSpec = 0;
	    			}
	    			
	    			if (sensorSetting.substring(2, 3).equals("1")) {//right sensor setting
	    				rightSensorSpec = 1;
	    			} else {
	    				assert sensorSetting.substring(2, 3).equals("0") : "Invalid input for sensor specification";
	    				
	    				rightSensorSpec = 0;
	    			}
	    			
	    			if (sensorSetting.substring(3, 4).equals("1")) {//back sensor setting
	    				backSensorSpec = 1;
	    			} else {
	    				assert sensorSetting.substring(3, 4).equals("0") : "Invalid input for sensor specification";
	    				
	    				backSensorSpec = 0;
	    			}
	    			
	    			if (forwardSensorSpec == 1 && leftSensorSpec == 1 && rightSensorSpec == 1 && backSensorSpec == 1) {// case for reliable robot
	    				msg += "Using reliable robot with all sensor types as reliable.\n";
	    				robot = new ReliableRobot();
	    				
	    				ReliableSensor forwardSensor = new ReliableSensor();
	    				forwardSensor.setSensorDirection(Direction.FORWARD);
    	                robot.addDistanceSensor(forwardSensor, Direction.FORWARD);
    	                
    	                ReliableSensor leftSensor = new ReliableSensor();
    					leftSensor.setSensorDirection(Direction.LEFT);
    	                robot.addDistanceSensor(leftSensor, Direction.LEFT);
    	                
    	                ReliableSensor rightSensor = new ReliableSensor();
    					rightSensor.setSensorDirection(Direction.RIGHT);
    	                robot.addDistanceSensor(rightSensor, Direction.RIGHT);
    	                
    	                ReliableSensor backSensor = new ReliableSensor();
    					backSensor.setSensorDirection(Direction.BACKWARD);
    	                robot.addDistanceSensor(backSensor, Direction.BACKWARD);
	    				
	    				argCounter++;
	    			} else { // case for unreliable robot
	    				msg += "Using unreliable robot with specified settings for each sensor.\n";
	    				robot = new UnreliableRobot();
	    				
	    				if (forwardSensorSpec == 1) {// set the forward sensor type
	    					ReliableSensor forwardSensor = new ReliableSensor();
	    					forwardSensor.setSensorDirection(Direction.FORWARD);
	    	                robot.addDistanceSensor(forwardSensor, Direction.FORWARD);
	    	                
	    	                
	    				} else {
//	    					assert forwardSensorSpec == 0 : "The 0/Unreliable setting should be the only remaining setting";
	    					UnreliableSensor forwardSensor = new UnreliableSensor();
	    					forwardSensor.setSensorDirection(Direction.FORWARD);
	    	                robot.addDistanceSensor(forwardSensor, Direction.FORWARD);

	    				}
	    				
	    				if (leftSensorSpec == 1) {// set the left sensor type
	    					ReliableSensor leftSensor = new ReliableSensor();
	    					leftSensor.setSensorDirection(Direction.LEFT);
	    	                robot.addDistanceSensor(leftSensor, Direction.LEFT);
	    	                
	    				} else {
//	    					assert leftSensorSpec == 0 : "The 0/Unreliable setting should be the only remaining setting";
	    					UnreliableSensor leftSensor = new UnreliableSensor();
	    					leftSensor.setSensorDirection(Direction.LEFT);
	    	                robot.addDistanceSensor(leftSensor, Direction.LEFT);
	    				}
	    				
	    				if (rightSensorSpec == 1) {// set the right sensor type
	    					ReliableSensor rightSensor = new ReliableSensor();
	    					rightSensor.setSensorDirection(Direction.RIGHT);
	    	                robot.addDistanceSensor(rightSensor, Direction.RIGHT);
	    					
	    				} else {
//	    					assert rightSensorSpec == 0 : "The 0/Unreliable setting should be the only remaining setting";
	    					UnreliableSensor rightSensor = new UnreliableSensor();
	    					rightSensor.setSensorDirection(Direction.RIGHT);
	    	                robot.addDistanceSensor(rightSensor, Direction.RIGHT);
	    				}
	    				
	    				if (backSensorSpec == 1) {// set the back sensor type
	    					ReliableSensor backSensor = new ReliableSensor();
	    					backSensor.setSensorDirection(Direction.BACKWARD);
	    	                robot.addDistanceSensor(backSensor, Direction.BACKWARD);
	    	                
	    				} else {
//	    					assert backSensorSpec == 0 : "The 0/Unreliable setting should be the only remaining setting";
	    					UnreliableSensor backSensor = new UnreliableSensor();
	    					backSensor.setSensorDirection(Direction.BACKWARD);
	    	                robot.addDistanceSensor(backSensor, Direction.BACKWARD);
	    				}
	    				
	    				argCounter++;
	    			}
	
	    		}
	    	}
	    		
	    	
	    }
	    
	    if (driver != null) {
	    	if (robot != null) {
	    		driver.setRobot(robot);
	    	} else {
	    		robot = new ReliableRobot();
	    		driver.setRobot(robot);
	    	}
	    	setRobotAndDriver(robot, driver);
	    }
	    
	    
		

//	    switch (parameter) {
//	    case "Prim" :
//	    	msg = "Command line input detected: generating random maze with Prim's algorithm.";
//	        ((StateTitle)currentState).setBuilder(Order.Builder.Prim);
//	    	break;
//	    case "Kruskal":
//	    case "Eller":
//	    case "Boruvka":
//	    	// TODO: for P2 assignment, please add code to set the builder accordingly
////	    	throw new RuntimeException("Don't know anybody named " + parameter);
//	    	msg = "Command line input detected: generating random maze with Boruvka's algorithm.";
//	        ((StateTitle)currentState).setBuilder(Order.Builder.Boruvka);
//	    	break;
//	    case "Wizard": // added for wizard
//	    	msg = "Command line input detected: utilizing ReliableRobot, ReliableSensor, and Wizard";
//	    	ReliableRobot robot = new ReliableRobot();
//	    	Wizard wizard = new Wizard();
//	    	setRobotAndDriver(robot, wizard);
//	    	break;
//	    	
//	    default: // assume this is a filename
//	    	File f = new File(parameter) ;
//	        if (f.exists() && f.canRead())
//	        {
//	            msg = "Detected file descriptor on command line, loading maze from this file: " + parameter;
//	            ((StateTitle)currentState).setFileName(parameter);
//	        }
//	        else {
//	            // None of the predefined strings and not a filename either: 
//	            msg = "Unknown command line parameter: " + parameter + " ignored, operating in default mode.";
//	        }
//	    	break;
//	    }
	    
	    LOGGER.fine(msg);
	}
	/**
     * Method incorporates all reactions to keyboard input in original code. 
     * The key listener calls this method to communicate input.
     * @param userInput is the user input 
     * @param value is only used for the numerical input for the size of the maze
     */
	public void handleKeyboardInput(UserInput userInput, int value) {
		currentState.handleUserInput(userInput, value);
		repaint();
	}
	
	//////////////////////////////////////////////////////////////////////////////////
	////////// KeyListener Interface /////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////
	/**
	 * Translate keyboard input to the corresponding operation for 
	 * the handleKeyboardInput method.
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
		int key = arg0.getKeyChar();
		int code = arg0.getKeyCode();
		
		//Possible operations for UserInput based on enum
		// {ReturnToTitle, Start, 
		// Up, Down, Left, Right, Jump, 
		// ToggleLocalMap, ToggleFullMap, ToggleSolution, 
		// ZoomIn, ZoomOut };
		UserInput userInput = null;
		int value = 0;
			
		// translate keyboard input into operation for Controller
		// the switch statements encodes a map: 
		// keyboard input -> (UserInput x Size of maze)
		switch (key) {
		case ('w' & 0x1f): // Ctrl-w makes a step forward even through a wall
			userInput = UserInput.JUMP;
			break;
		case '\t': case 'm': // show local information: current position and visible walls
			// precondition for showMaze and showSolution to be effective
			// acts as a toggle switch
			userInput = UserInput.TOGGLELOCALMAP;
			break;
		case 'z': // show a map of the whole maze
			// acts as a toggle switch
			userInput = UserInput.TOGGLEFULLMAP;
			break;
		case 's': // show the solution on the map as a yellow line towards the exit
			// acts as a toggle switch
			userInput = UserInput.TOGGLESOLUTION;
			break;
		case '+': case '=': // zoom into map
			userInput = UserInput.ZOOMIN;
			break ;
		case '-': // zoom out of map
			userInput = UserInput.ZOOMOUT;
			break ;
		case KeyEvent.VK_ESCAPE: // is 27
			userInput = UserInput.RETURNTOTITLE;
			break;
		case 'h': // turn left
			userInput = UserInput.LEFT;
			break;
		case 'j': // move backward
			userInput = UserInput.DOWN;
			break;
		case 'k': // move forward
			userInput = UserInput.UP;
			break;
		case 'l': // turn right
			userInput = UserInput.RIGHT;
			break;
		case KeyEvent.CHAR_UNDEFINED: // fall back if key is undefined but code is
			// char input for 0-9, a-f skill-level
			if ((KeyEvent.VK_0 <= code && code <= KeyEvent.VK_9) || (KeyEvent.VK_A <= code && code <= KeyEvent.VK_Z)){
				if (code >= '0' && code <= '9') {
					value = code - '0';
					userInput = UserInput.START;
				}
				if (code >= 'a' && code <= 'f') {
					value = code - 'a' + 10;
					userInput = UserInput.START;
				}
			} else {
				if (KeyEvent.VK_ESCAPE == code)
					userInput = UserInput.RETURNTOTITLE;
				if (KeyEvent.VK_UP == code)
					userInput = UserInput.UP;
				if (KeyEvent.VK_DOWN == code)
					userInput = UserInput.DOWN;
				if (KeyEvent.VK_LEFT == code)
					userInput = UserInput.LEFT;
				if (KeyEvent.VK_RIGHT == code)
					userInput = UserInput.RIGHT;
			}
			break;
		default:
			// check ranges of values as possible selections for skill level
			if (key >= '0' && key <= '9') {
				value = key - '0';
				userInput = UserInput.START;
			} else
			if (key >= 'a' && key <= 'f') {
				value = key - 'a' + 10;
				userInput = UserInput.START;
			} else
				LOGGER.severe("Cannot match input key:" + key);
			break;
		}
		// don't let bad input proceed
		if (null == userInput) {
			LOGGER.fine("Ignoring unmatched keyboard input: key=" + key + " code=" + code);
			return;
		}
		
		assert (0 <= value && value <= 15);		
		// feed user input into control
		// userInput encodes what action should be triggered
		// value is only used if userInput == Start
		// value indicates the user selected size of the maze
		handleKeyboardInput(userInput, value);
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// nothing to do
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// NOTE FOR THIS TYPE OF EVENT IS getKeyCode always 0, so Escape etc is not recognized	
		// this is why we work with keyPressed
	}
	
	//////////////////////////////////////////////////////////////////////////////////
	////////// Static methods ////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////

	/**
	 * Main method to launch Maze game as a java application.
	 * The application can be operated in three ways. 
	 * 1) The intended normal operation is to provide no parameters
	 * and the maze will be generated by a randomized DFS algorithm (default). 
	 * 2) If a filename is given that contains a maze stored in xml format. 
	 * The maze will be loaded from that file. 
	 * This option is useful during development to test with a particular maze.
	 * 3) A predefined constant string is given to select a maze
	 * generation algorithm, currently supported is "Prim".
	 * @param args is optional, first string can be a fixed constant like Prim or
	 * the name of a file that stores a maze in XML format
	 */
	public static void main(String[] args) {
		// some general configurations
		// ColorTheme offers different variants, pick one
		ColorTheme.setColorTheme(ColorThemeSelection.ADVANCED);
		
		// adjust the logging to specific needs
		// development: set level to fine, finer, finest to trace execution
		// of code in specific classes
		// production: default level is INFO
		configureLogging();
		
		
		// Instantiate and start the actual app
	    Control app = new Control();
	    // configure app according to flag settings
	    // configure app according to command line parameter settings
		app.handleCommandLineInput(args);
		// proceed with game
		app.start();
		app.repaint() ;
	}

	
	private static void configureLogging() {
		// Need to adjust the logging level
		// finest: tracks method calls
		// adjust specific logger
		//
		// GUI package
		/*
		Logger logger = Logger.getLogger(ColorTheme.class.getName());
		logger.setLevel(Level.CONFIG);
		// add console handler that is configured accordingly and disable anonymous one
		logger.setUseParentHandlers( false );
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.CONFIG);
		logger.addHandler(handler);
		*/
		// configure the logging for the ColorTheme
		setLoggingLevelForIndividualLogger(ColorTheme.class,Level.CONFIG);
		// configure the logging for the DefaultOrder class
		//setLoggingLevelForIndividualLogger(DefaultOrder.class,Level.FINE);
		// configure logging for different state classes
		setLoggingLevelForIndividualLogger(StateTitle.class,Level.FINE);
		setLoggingLevelForIndividualLogger(StateGenerating.class,Level.FINE);
		setLoggingLevelForIndividualLogger(StatePlaying.class,Level.FINE);
		setLoggingLevelForIndividualLogger(StateWinning.class,Level.FINE);
		// configure logging for control
		setLoggingLevelForIndividualLogger(Control.class,Level.FINE);
		// configure logging for drawing
		//setLoggingLevelForIndividualLogger(FirstPersonView.class,Level.FINE);
		//setLoggingLevelForIndividualLogger(Map.class,Level.FINE);
		setLoggingLevelForIndividualLogger(SimpleScreens.class,Level.FINE);
		//
		// Generation package
		setLoggingLevelForIndividualLogger(BSPBranch.class,Level.FINE);
		setLoggingLevelForIndividualLogger(BSPBuilder.class,Level.FINE);
		setLoggingLevelForIndividualLogger(BSPLeaf.class,Level.FINE);
		setLoggingLevelForIndividualLogger(Wall.class,Level.FINE);
		setLoggingLevelForIndividualLogger(MazeFactory.class,Level.FINE);
		setLoggingLevelForIndividualLogger(MazeBuilder.class,Level.FINE);
		setLoggingLevelForIndividualLogger(MazeBuilderPrim.class,Level.FINE);
		setLoggingLevelForIndividualLogger(SingleRandom.class,Level.FINE);
	}
	/**
	 * Sets the logging level for an individual logger.
	 */
	private static void setLoggingLevelForIndividualLogger(Class classdescriptor, Level level) {
		Logger logger = Logger.getLogger(classdescriptor.getName());
		logger.setLevel(level);
		
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(level);
		
		logger.addHandler(handler);
	}

}
