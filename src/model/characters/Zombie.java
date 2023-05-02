package model.characters;

import engine.Game;
import model.world.CharacterCell;

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
	 * Constructor that initializes the name, maximum Hp, and attack damage
	 * and adds it to zombies arrayList in the Game.
	 *
	 * @pram name
	 * @pram maxHp
	 * @pram attackDmg
	 */
	public Zombie() {
		super("Zombie " + (ZOMBIES_COUNT + 1), 40, 10);
		Game.zombies.add(this);
		ZOMBIES_COUNT++;
	}

	/**
	 * removes the dead zombie from the zombies ArrayList and calls
	 * {@link spawnZombie}.
	 */
	@Override
	public void onCharacterDeath() {
		super.onCharacterDeath();
		Game.zombies.remove(this);
		Game.spawnCell(new CharacterCell(new Zombie()));
	}
	
	/**
	 * a helper method that looks for heros in the adjacent cells (if exist) to set them as target
	 * @return Character to be set as target for the zombie.
	 */
	public Hero getAdjacentTarget() {
		int x =(int) getLocation().getX();
		int y = (int) getLocation().getY();
		
		for(int i = x  -1 ; i < x +2 ; i++) {
			for(int j = y - 1 ; j < y +2 ; j++) {
				if(Hero.isValidLocation(i, j) &&  Game.map[i][j] instanceof CharacterCell && ((CharacterCell)Game.map[i][j]).getCharacter() instanceof Hero )
					return (Hero)((CharacterCell)Game.map[i][j]).getCharacter();
			}
		}
		// if no adjacent hero was found return null.
		return null ;
	}

}
