package model.characters;

import java.awt.Point;

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

	public Character(String name, int maxHp, int attackDmg) {
		this.name = name;
		this.maxHp = maxHp;
		this.attackDmg = attackDmg;
		this.target = null;
		this.location = null;
		this.currentHp = maxHp;
	}

	/**
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * @return the currentHp
	 */
	public int getCurrentHp() {
		return currentHp;
	}

	/**
	 * @param currentHp the currentHp to set
	 */
	public void setCurrentHp(int currentHp) {
		this.currentHp = Math.min(currentHp , maxHp);
	}

	/**
	 * @return the target
	 */
	public Character getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(Character target) {
		this.target = target;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the maxHP
	 */
	public int getMaxHp() {
		return maxHp;
	}

	/**
	 * @return the attackDmg
	 */
	public int getAttackDmg() {
		return attackDmg;
	}

}
