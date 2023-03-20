package exceptions;

/**
* A subclass of GameActionException representing an exception that occurs when a
*character tries to make an invalid movement
 *
 *@author Rafael Samuel
 */
public class MovementException extends GameActionException {
	
	/**
	 * The Default constructor of the class.
	 */
		public MovementException() {
			super();
		}
		
		/**
		 * Constructor that initializes the exception with the given message
		 * 
		 * @param s Custom message to display for the exception
		 */
		public MovementException(String s) {
			super(s);
		}

}
