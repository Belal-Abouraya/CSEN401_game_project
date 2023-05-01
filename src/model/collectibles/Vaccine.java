package model.collectibles;

import exceptions.NoAvailableResourcesException;
import model.characters.Hero;

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
	}

	@Override
	public void pickUp(Hero owner) {
		owner.getVaccineInventory().add(this);
	}

	@Override
	public void use(Hero owner){
		owner.getVaccineInventory().remove(this);
	}
}
