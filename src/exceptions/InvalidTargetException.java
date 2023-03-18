package exceptions;

/**
 * A subclass of GameActionEx163ception representing an exception that occurs
 * upon trying to target a wrong character with an action
 * 
 * @author Belal Abouraya
 */
public class InvalidTargetException extends GameActionException {
	/**
	 * Default constructor for the class.
	 */
	public InvalidTargetException() {
		super();
	}

	/**
	 * Constructor that initializes the exception with the given message
	 * 
	 * @param s Custom message to display for the exception
	 */
	public InvalidTargetException(String s) {
		super(s);
	}
}
