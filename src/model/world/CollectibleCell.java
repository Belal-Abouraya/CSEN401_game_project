package model.world;
import model.collectibles.Collectible;
/**
 * A subclass of the cell class defining the CollectibleCell type
 * with only one attribute for this collectible.
 * @author  Rafael Samuel.
 */


public class CollectibleCell extends Cell{
	private Collectible collectible;
	/**
	 * The default constructor for the class.
	 */
	public CollectibleCell() {
		super();
	}
	 /**
     * constructor for the CollectibleCell class that initializes the collectible variable.
     */
	public CollectibleCell(Collectible collectible) {
		super();
		this.collectible = collectible;
	}
	
	/**
    *
    * @return collectible
    */
	public Collectible getCollectible() {
		return collectible;
	}

	

}
