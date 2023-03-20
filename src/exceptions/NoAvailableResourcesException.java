package exceptions;
/**
*A subclass of GameActionException representing an exception that occurs when a
*character tries to use a Collectible he does not have.
*@author Rafael Samuel
*/
public class NoAvailableResourcesException extends GameActionException{
	
	/**
	 * The default constructor of the class.
	 */
	public NoAvailableResourcesException() {
		super();
	}
	
	/**
	 * Constructor that initializes the exception with the given message
	 * 
	 * @param s Custom message to display for the exception
	 */
	public NoAvailableResourcesException(String s) {
		super(s);
	}

}
