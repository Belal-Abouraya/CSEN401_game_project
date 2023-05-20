package model.collectibles;

import javafx.scene.image.Image;
import model.characters.Character;
import model.characters.Hero;

/**
 * contains the methods available to all Collectible objects within the game map
 * as well as the model of the object.
 * 
 * @author Belal Abouraya
 */
public abstract class Collectible {
	Image model;
	Image icon;

	/**
	 * Initializes the collectible icon and model.
	 */
	public Collectible(String name) {
		this.model = Character.LoadModel(name);
		this.icon = Hero.loadIcon(name);
	}

	/**
	 * @return the icon
	 */
	public Image getIcon() {
		return icon;
	}

	/**
	 * @return the model
	 */
	public Image getModel() {
		return model;
	}

	/**
	 * Adds the collectible picked by the hero to the corresponding Arraylist
	 * 
	 * @param owner
	 */
	public abstract void pickUp(Hero owner);

	/**
	 * Removes the used collectible by the hero from the corresponding Arraylist
	 * 
	 * @param owner
	 */
	public abstract void use(Hero owner);
}