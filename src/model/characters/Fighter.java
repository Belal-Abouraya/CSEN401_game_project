package model.characters;

import exceptions.InvalidTargetException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;

/**
 * represents fighters in the game.
 *
 * @author Ahmed Hussein
 * @author Belal Abouraya
 */

public class Fighter extends Hero {


	/**
	 * constructor for the fighter class which initializes the name, the maximum Hp,
	 * the attack damage and the maximum number of actions in a turn.
	 *
	 * @param name
	 * @param maxHp
	 * @param attackDmg
	 * @param maxActions
	 */
	public Fighter(String name, int maxHp, int attackDmg, int maxActions) {

		super(name, maxHp, attackDmg, maxActions);
	}

	/**
	 * 
	 * checks the specialAction parameter after calling the super class method. if
	 * it is true it increments the action points available to compensate for the
	 * decrease by the superclass method
	 * 
	 * @throws NotEnoughActionsException
	 * @throws InvalidTargetException
	 */
	@Override
	public void attack() throws InvalidTargetException, NotEnoughActionsException {
		super.attack();
		if (isSpecialAction())
			setActionsAvailable(1 + getActionsAvailable());
	}

}
