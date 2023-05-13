package model.characters;

import java.awt.Point;
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
		if (getTarget() instanceof Zombie)
			throw new InvalidTargetException("Cannot heal a zombie!");
		if(!isAdjacent(getTarget()) && !sameCell(getTarget()))
			throw new InvalidTargetException("The Hero is not close enough!");
		if(getTarget() == null)
			throw new InvalidTargetException("Target is not set yet!");
		super.useSpecial();
		getTarget().setCurrentHp(getTarget().getMaxHp());
		setSpecialAction(false);
	}
	
	/**
	 * A helper method checks whether two characters share the same cell
	 * 
	 * @param c a character
	 * @return true if the two characters are in the same cell and false otherwise
	 */
	private boolean sameCell(Character c) {
		Point cellLocation = c.getLocation();
		return cellLocation.x == this.getLocation().x &&
				cellLocation.y == this.getLocation().y ;
	}

}
