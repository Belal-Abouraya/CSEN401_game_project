package model.characters;

import java.awt.Point;
import java.util.ArrayList;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import javafx.scene.image.Image;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;
import views.Main;

/**
 * Hero is the abstract base class of all heroes in the game. it contains all
 * common attributes of heroes in the game such as :
 * <ul>
 * <li>number of available actions in a turn</li>
 * <li>maximum number of actions in a turn</li>
 * <li>a boolean to check whether the special action is used</li>
 * <li>list of vaccines collected</li>
 * <li>list of supplies collected</li>
 * <li>the selected game mode</li>
 * </ul>
 *
 * @author Ahmed Hussein
 * @author Belal Abouraya
 * @author rafael Rafael Samuel
 */

public abstract class Hero extends Character {

	private int actionsAvailable;
	private int maxActions;
	private boolean specialAction;
	private ArrayList<Vaccine> vaccineInventory;
	private ArrayList<Supply> supplyInventory;
	private Image icon;

	/**
	 * Constructor that initializes the name, maximum Hp, attack damage, maximum
	 * number of actions and the hero icon.
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
		this.icon = loadIcon(name);
	}

	/**
	 * checks the available actions and throws exception if there are not any
	 * otherwise it calls the superclass method and decrements the available
	 * actions.
	 * 
	 * @throws NotEnoughActionsException
	 * @throws InvalidTargetException
	 */
	@Override
	public void attack() throws InvalidTargetException, NotEnoughActionsException {
		if (actionsAvailable <= 0) {
			throw new NotEnoughActionsException("Not enough actions!");
		}
		if (getTarget() == null)
			throw new InvalidTargetException("Target is not set yet!");
		if (!isAdjacent(getTarget())) {
			throw new InvalidTargetException("The target is not close enough!");
		}
		if (getTarget() instanceof Hero) {
			throw new InvalidTargetException("Heroes can not attack each other!");
		}
		super.attack();
		actionsAvailable--;
	}

	/**
	 * A method that is called when the current hero wishes to move in a particular
	 * direction.
	 *
	 *
	 * @param direction
	 * @throws MovementException
	 * @throws NotEnoughActionsException
	 */
	public int move(Direction direction) throws MovementException, NotEnoughActionsException {
		if (this.getCurrentHp() <= 0) {
			onCharacterDeath();
			return 0;
		}
		if (actionsAvailable <= 0) {
			throw new NotEnoughActionsException("Not enough actions.");
		}
		int x = getLocation().x;
		int y = getLocation().y;
		int oldX = x, oldY = y;
		switch (direction) {
		case RIGHT -> y += 1;
		case LEFT -> y -= 1;
		case DOWN -> x -= 1;
		case UP -> x += 1;
		}
		int res = 0;
		if (!isValidLocation(x, y))
			throw new MovementException("Invalid direction!");
		if (Game.map[x][y] instanceof CharacterCell) {
			if (((CharacterCell) Game.map[x][y]).getCharacter() != null)
				throw new MovementException("Cell is occupied!");
			((CharacterCell) Game.map[x][y]).setCharacter(this);
		} else if (Game.map[x][y] instanceof TrapCell) {
			int currHP = getCurrentHp();
			int damage = ((TrapCell) (Game.map[x][y])).getTrapDamage();
			Game.map[x][y] = new CharacterCell();
			setCurrentHp(currHP - damage);
			if (currHP > damage) {
				((CharacterCell) Game.map[x][y]).setCharacter(this);
			}
			res = 1;
		} else { // CollectibleCell case
			((CollectibleCell) Game.map[x][y]).getCollectible().pickUp(this);
			if (((CollectibleCell) Game.map[x][y]).getCollectible() instanceof Supply)
				res = 2;
			else
				res = 3;
			Game.map[x][y] = new CharacterCell();
			((CharacterCell) Game.map[x][y]).setCharacter(this);
		}
		actionsAvailable--;
		Game.clearCell(oldX, oldY);
		makeAllAdjacentVisible(x, y);
		setLocation(new Point(x, y));
		return res;
	}

	/**
	 * A helper method used for making all adjacent cells visible
	 *
	 * @param x the x component of the location
	 * @param y the y component of the location
	 */

	public static void makeAllAdjacentVisible(int x, int y) {
		for (int i = x - 1; i < x + 2; i++) {
			for (int j = y - 1; j < y + 2; j++) {
				if (isValidLocation(i, j) && Game.map[i][j] != null) {
					Game.map[i][j].setVisible(true);
				}
			}
		}
	}

	/**
	 * A helper method used for checking whether a point is inside the game grid.
	 *
	 *
	 * @param x the x component of the location
	 * @param y the y component of the location
	 * @return boolean indicating the answer
	 */

	public static boolean isValidLocation(int x, int y) {
		return (x >= 0 && x <= 14 && y >= 0 && y <= 14);
	}

	/**
	 * A method that is called when the current hero wants to use its special
	 * action.
	 *
	 * @throws NoAvailableResourcesException
	 * @throws InvalidTargetException
	 */
	public void useSpecial() throws NoAvailableResourcesException, InvalidTargetException {
		if (specialAction)
			return;
		if (supplyInventory.isEmpty()) {
			throw new NoAvailableResourcesException("The Supply inventory is empty!");
		}
		Supply supply = supplyInventory.get(0);
		supply.use(this);
	}

	/**
	 * A method that is called when the current hero wants to cure a zombie target
	 * using a vaccine.
	 *
	 * @throws InvalidTargetException
	 * @throws NotEnoughActionsException
	 * @throws NoAvailableResourcesException
	 */

	public void cure() throws InvalidTargetException, NotEnoughActionsException, NoAvailableResourcesException {
		if (getTarget() == null)
			throw new InvalidTargetException("Target is not set yet!");
		if (actionsAvailable <= 0)
			throw new NotEnoughActionsException("Not enough actions!");
		if (!(getTarget() instanceof Zombie))
			throw new InvalidTargetException("Heroes can only cure Zombies!");
		if (!isAdjacent(getTarget()))
			throw new InvalidTargetException("The target is not close enough!");
		if (vaccineInventory.isEmpty())
			throw new NoAvailableResourcesException("No vaccines are availabe!");
		actionsAvailable--;
		Vaccine vaccine = vaccineInventory.get(0);
		vaccine.use(this);
	}

	/**
	 * removes the dead hero from the heroes ArrayList and increments the dead
	 * Heroes variable.
	 */
	@Override
	public void onCharacterDeath() {
		super.onCharacterDeath();
		Game.heroes.remove(this);
		Game.deadHeroes++;
	}

	/**
	 * resets each hero’s actions, target, and special
	 */
	public void reset() {
		this.actionsAvailable = maxActions;
		setTarget(null);
		this.specialAction = false;
	}

	/**
	 * @return the number of the available actions.
	 */
	public int getActionsAvailable() {
		return actionsAvailable;
	}

	/**
	 * @param actionsAvailable the number of available actions to set.
	 */
	public void setActionsAvailable(int actionsAvailable) {
		actionsAvailable = Math.max(actionsAvailable, 0);
		this.actionsAvailable = actionsAvailable;
	}

	/**
	 * @return the maximum number of actions in a turn.
	 */
	public int getMaxActions() {
		return maxActions;
	}

	/**
	 * @return whether the special action is used.
	 */
	public boolean isSpecialAction() {
		return specialAction;
	}

	/**
	 * @param specialAction the special action usage state to set.
	 */
	public void setSpecialAction(boolean specialAction) {
		this.specialAction = specialAction;
	}

	/**
	 * @return the list of vaccines collected.
	 */

	public ArrayList<Vaccine> getVaccineInventory() {
		return vaccineInventory;
	}

	/**
	 * @return the list of supplies collected.
	 */
	public ArrayList<Supply> getSupplyInventory() {
		return supplyInventory;
	}

	abstract public String getType();

	/**
	 * @return the icon
	 */
	public Image getIcon() {
		return icon;
	}

	/**
	 * A helper method that loads the hero's icon based on the name and the selected
	 * game mode by the player
	 * 
	 * @return the loaded hero icon
	 */
	public static Image loadIcon(String name) {
		return Main.loadImage("icons/" + name + ".png");
	}
}
