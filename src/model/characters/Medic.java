package model.characters;

import exceptions.InvalidTargetException;
import exceptions.NoAvailableResourcesException;

/**
 * represents the medics in the game.
 *
 * @author Ahmed Hussein
 */

public class Medic extends Hero {

	/**
	 * constructor for the medic class that initializes the name, the maximum Hp,
	 * the attack damage and the maximum number of actions in a turn.
	 *
	 * @param name
	 * @param maxHp
	 * @param attackDmg
	 * @param maxActions
	 */

	public Medic(String name, int maxHp, int attackDmg, int maxActions) {
		super(name, maxHp, attackDmg, maxActions);
	}

	@Override
	public void useSpecial() throws NoAvailableResourcesException, InvalidTargetException {
		if (getTarget() instanceof Hero && isAdjacent(getTarget())) {
			super.useSpecial();
			getTarget().setCurrentHp(getTarget().getMaxHp());
			setSpecialAction(false);
		} else
			throw new InvalidTargetException();
	}

}
