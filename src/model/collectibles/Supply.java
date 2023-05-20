package model.collectibles;

import model.characters.Hero;

/**
 * represents Supplies in the game
 *
 * @author Rafael Samuel
 * @author Belal Abouraya
 */
public class Supply extends Collectible {

	/**
	 * Default constructor for the class.
	 */
	public Supply() {
		super("supply");
	}

	@Override
	public void pickUp(Hero owner) {
		owner.getSupplyInventory().add(this);
	}

	@Override
	public void use(Hero owner) {
		owner.getSupplyInventory().remove(this);
		owner.setSpecialAction(true);
	}
}
