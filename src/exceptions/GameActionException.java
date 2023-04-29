package exceptions;

/**
 * GameActionException represents a generic exception that can occur during the
 * gameplay. These exceptions arise from any invalid action that is performed.
 * 
 * @author Belal Abouraya
 */
public abstract class GameActionException extends Exception {
	/**
	 * Default constructor for the class.
	 */
	GameActionException() {
		super();
	}

	/**
	 * Constructor that initializes the exception with the given message
	 * 
	 * @param s Custom message to display for the exception
	 */
	GameActionException(String s) {
		super(s);
	}
}
