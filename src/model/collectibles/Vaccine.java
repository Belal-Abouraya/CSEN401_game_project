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
	private static int usedVaccines = 0;

	/**
	 * Default constructor for the class.
	 */
	public Vaccine() {
	}

	@Override
	public void pickUp(Hero owner) {
		owner.getVaccineInventory().add(this);
	}

	@Override
	public void use(Hero owner) {
		usedVaccines++;
		owner.getVaccineInventory().remove(this);
	}

	public static int getUsedVaccine() {
		return usedVaccines;
	}
}
