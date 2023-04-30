package model.characters;

import java.awt.Point;

import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;
import model.world.CharacterCell;
import engine.Game;

/**
 * Character is the abstract base class of all characters available in the game.
 * it contains all the common attributes of all characters such as:
 * <ul>
 * <li>name
 * <li>location
 * <li>maximum HP
 * <li>current HP
 * <li>attack damage
 * <li>target character
 * </ul>
 * 
 * @author Belal Abouraya
 */
public abstract class Character {
	private String name;
	private Point location;
	private int maxHp;
	private int currentHp;
	private int attackDmg;
	private Character target;

	/**
	 * Constructor for the character class which initializes the name, maximum HP
	 * and attack damage. it sets other parameters to their default values.
	 * 
	 * @param name
	 * @param maxHp
	 * @param attackDmg
	 */
	Character(String name, int maxHp, int attackDmg) {
		maxHp = Math.max(maxHp, 0);
		attackDmg = Math.max(attackDmg, 0);
		this.name = name;
		this.maxHp = maxHp;
		this.attackDmg = attackDmg;
		this.target = null;
		this.location = null;
		this.currentHp = maxHp;
	}

	/**
	 * Checks wether a character is adjacent to another one
	 */
	public boolean isAdjacent(Character c) {
		if (c == null)
			return false;
		Point tmp = c.getLocation();
		int dx = Math.abs(tmp.x - location.x);
		int dy = Math.abs(tmp.y - location.y);
		if (dy == 1) {
			if (dx <= 1)
				return true;
		}

		if (dx == 1) {
			if (dy <= 1)
				return true;
		}
		return false;
	}

	/**
	 * Handles applying the logic of an attack on the character’s target.
	 * <p>
	 * First checks if the target is adjacent to the character's or not where
	 * adjacenct cells are defined as cells within radius 1 of the character's cell
	 * in all directions: Horizontal, Vertical, and Diagonal. if it is not it throws
	 * an exception, otherwise it calls {@link defend} then decreases the target's
	 * HP; finally it checks the target's HP if it is zero it sets target to null.
	 * </p>
	 * 
	 * @throws InvalidTargetException
	 * @throws NotEnoughActionsException
	 */
	void attack() throws InvalidTargetException, NotEnoughActionsException {
		if (!isAdjacent(target))
			throw new InvalidTargetException();
		if((this instanceof Hero && target instanceof Hero) ||
				(this instanceof Zombie && target instanceof Zombie))
			throw new InvalidTargetException();
		target.defend(this);
		target.setCurrentHp(target.getCurrentHp() - attackDmg);
		if (target.getCurrentHp() == 0)
			target = null;
	}

	/**
	 * This method is only called whenever a character has been attacked by another
	 * character. A defending character causes half his attackDmg to the attacking
	 * character
	 * 
	 * @param target
	 */
	void defend(Character target) {
		target.setCurrentHp(target.getCurrentHp() - attackDmg / 2);
	}

	/**
	 * This method should is called whenever a character’s currentHp reaches zero.
	 * It handles removing the character from the game and updating the map
	 */
	void onCharacterDeath() {
		((CharacterCell) Game.map[location.x][location.y]).setCharacter(null);
	}

	/**
	 * @return the location
	 */
	Point getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * @return the currentHp
	 */
	int getCurrentHp() {
		return currentHp;
	}

	/**
	 * updates the current HP and calls {@link onCharacterDeath} if it reaches zero.
	 * 
	 * @param currentHp the currentHp to set
	 */
	void setCurrentHp(int currentHp) {
		currentHp = Math.max(currentHp, 0);
		this.currentHp = Math.min(currentHp, maxHp);
		if (currentHp == 0)
			onCharacterDeath();
	}

	/**
	 * @return the target
	 */
	Character getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	void setTarget(Character target) {
		this.target = target;
	}

	/**
	 * @return the name
	 */
	String getName() {
		return name;
	}

	/**
	 * @return the maxHP
	 */
	int getMaxHp() {
		return maxHp;
	}

	/**
	 * @return the attackDmg
	 */
	int getAttackDmg() {
		return attackDmg;
	}

}
