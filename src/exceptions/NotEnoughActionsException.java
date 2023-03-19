package exceptions;

/**
 * Exception that occurs when a character tries to take
 * an action without the sufficient action points available.
 *
 * @author Ahmed Hussein
 */

public class NotEnoughActionsException extends GameActionException{
    /**
     * Default constructor for the NotEnoughActionsException class
     */
    public NotEnoughActionsException(){
        super();
    }

    /**
     * Constructor that initializes the exception with a given message.
     *
     * @param s a Custom message to be displayed
     */

    public NotEnoughActionsException(String s){
        super(s);
    }
}
