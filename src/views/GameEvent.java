package views;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 * A class that models events that can occur in a game it has only two
 * eventTypes:
 * <ul>
 * <li>GAME_OVER
 * <li>WIN
 * </ul>
 * 
 * @author Belal Abouraya
 */
public class GameEvent extends Event {

	final static EventType<GameEvent> GAME_OVER = new EventType<>("GAME_OVER");
	final static EventType<GameEvent> WIN = new EventType<>("WIN");

	/**
	 * simply calls the super class contructor
	 */
	public GameEvent(EventType<? extends Event> arg0) {
		super(arg0);
	}
}
