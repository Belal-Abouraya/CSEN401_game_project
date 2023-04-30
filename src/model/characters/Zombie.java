package model.characters;

import engine.Game;

/**
 * Zobmbie is is a class that represents the zombies int the game it contains
 * inly one attribute to count then number of zombies instantiated so far
 * 
 * @author Rafael Samuel
 * @author Belal Abouraya
 */
public class Zombie extends Character {
	private static int ZOMBIES_COUNT;

	/**
	 * Constructor that initializes the name, maximum Hp, and attack damage .
	 *
	 * @pram name
	 * @pram maxHp
	 * @pram attackDmg
	 */
	public Zombie() {

		super("Zombie " + (ZOMBIES_COUNT + 1), 40, 10);
		ZOMBIES_COUNT++;

	}

	/**
	 * removes the dead zombie from the zombies ArrayList and calls
	 * {@link spawnZombie}.
	 */
	@Override
	public void onCharacterDeath() {
		Game.zombies.remove(this);
		Game.spawnZombie();
	}

}
