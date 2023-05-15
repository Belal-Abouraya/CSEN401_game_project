package views;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 * @author Belal Abouraya
 */
public class GameOverEvent extends Event {

	/**
	 * @param arg0
	 */
	public GameOverEvent(EventType<? extends Event> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public GameOverEvent(Object arg0, EventTarget arg1, EventType<? extends Event> arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

}
