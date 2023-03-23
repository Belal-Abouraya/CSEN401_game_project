package model.characters;

import model.collectibles.Supply;
import model.collectibles.Vaccine;
import java.util.ArrayList;

/**
 * Hero is the abstract base class of all heroes in the game. it contains all
 * common attributes of heros in the game such as :
 * <ul>
 * <li>number of available actions in a turn</li>
 * <li>maximum number of actions in a turn</li>
 * <li>a boolean to check whether the special action is used</li>
 * <li>list of vaccines collected</li>
 * <li>list of supplies collected</li>
 * </ul>
 *
 * @author Ahmed Hussein
 */

public abstract class Hero extends Character {

	private int actionsAvailable;
	private int maxActions;
	private boolean specialAction;
	private ArrayList<Vaccine> vaccineInventory;
	private ArrayList<Supply> supplyInventory;

	/**
	 * Constructor that initializes the name, maximum Hp, attack damage and maximum
	 * number of actions.
	 *
	 * @pram name
	 * @pram maxHp
	 * @pram attackDmg
	 * @pram maxActions
	 */

	public Hero(String name, int maxHp, int attackDmg, int maxActions) {
		super(name, maxHp, attackDmg);
		maxActions = Math.max(maxActions, 0);
		this.maxActions = maxActions;
		this.actionsAvailable = maxActions;
		this.specialAction = false;
		this.vaccineInventory = new ArrayList<>();
		this.supplyInventory = new ArrayList<>();
	}

	/**
	 *
	 * @return the number of the available actions.
	 */
	public int getActionsAvailable() {
		return actionsAvailable;
	}

	/**
	 *
	 * @param actionsAvailable the number of available actions to set.
	 */
	public void setActionsAvailable(int actionsAvailable) {
		actionsAvailable = Math.max(actionsAvailable, 0);
		this.actionsAvailable = actionsAvailable;
	}

	/**
	 *
	 * @return the maximum number of actions in a turn.
	 */
	public int getMaxActions() {
		return maxActions;
	}

	/**
	 *
	 * @return whether the special action is used.
	 */
	public boolean isSpecialAction() {
		return specialAction;
	}

	/**
	 *
	 * @param specialAction the special action usage state to set.
	 */
	public void setSpecialAction(boolean specialAction) {
		this.specialAction = specialAction;
	}

	/**
	 *
	 * @return the list of vaccines collected.
	 */

	public ArrayList<Vaccine> getVaccineInventory() {
		return vaccineInventory;
	}

	/**
	 *
	 * @return the list of supplies collected.
	 */
	public ArrayList<Supply> getSupplyInventory() {
		return supplyInventory;
	}

}
