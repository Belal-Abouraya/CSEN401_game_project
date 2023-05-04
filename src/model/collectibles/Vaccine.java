package model.collectibles;

import engine.Game;
import exceptions.InvalidTargetException;
import model.characters.Character;
import model.characters.Hero;
import model.characters.Zombie;

/**
 * represents Vaccines in the game
 *
 * @author Ahmed Hussein
 */

public class Vaccine implements Collectible {

	/**
	 * Default constructor for the class.
	 */
	public Vaccine() {
		Game.getVaccines().add(this);
	}

	@Override
	public void pickUp(Hero owner) {
		owner.getVaccineInventory().add(this);
	}

	@Override
	public void use(Hero owner) {
		owner.getVaccineInventory().remove(this);
		Game.getVaccines().remove(this);
	}
}
