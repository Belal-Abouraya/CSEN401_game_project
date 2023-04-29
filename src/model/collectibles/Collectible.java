package model.collectibles;

import model.characters.Hero;

/**
 * contains the methods available to all Collectible objects within the game map
 * such as Vaccines and Supplies
 * 
 * @author Belal Abouraya
 */
public interface Collectible {
	/**
	 * Adds the collectible picked by the hero to the corresponding Arraylist
	 * 
	 * @param owner
	 */
	void pickUp(Hero owner);

	/**
	 * Removes the used collectible by the hero from the corresponding Arraylist
	 * 
	 * @param owner
	 */
	void use(Hero owner);
}