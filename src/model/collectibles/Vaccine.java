package model.collectibles;

import java.awt.Point;

import engine.Game;
import exceptions.InvalidTargetException;
import model.characters.Character;
import model.characters.Hero;
import model.characters.Zombie;
import model.world.CharacterCell;

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
		Point targetLocation = owner.getTarget().getLocation();
		int x = targetLocation.x, y = targetLocation.y;
		int idx = (int) (Math.random() * Game.heroes.size());
		Hero newHero = Game.availableHeroes.get(idx);
		Hero.makeAllAdjacentVisible(x, y);
		newHero.setLocation(new Point(x, y));
		Game.availableHeroes.remove(idx);
		Game.heroes.add(newHero);
		((CharacterCell) Game.map[x][y]).setCharacter(newHero);
		Game.zombies.remove((Zombie) owner.getTarget());
		owner.setTarget(null);
		Game.getVaccines().remove(this);
	}
}
