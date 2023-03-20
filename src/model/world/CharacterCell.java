package model.world;

/**
 * CharacterCell is a special type of cells.
 * it contains two attributes which are:
 * <ul>
 * <li>character
 * <li>isSafe 
 * </ul>
 *
 * @author Rafael Samuel
 */
public class CharacterCell extends Cell{
	
	private Character character;
	private boolean isSafe;
	
	  /**
     * the default construcor of the class
     */

	public CharacterCell() {
		
	}
	  /**
     * constructor for the CharacterCell class that initializes the character and isSafe variables
     */

	public CharacterCell(Character character , boolean isSafe) {
		this.character = character;
		this.isSafe = isSafe;
	}
	
	/**
    *
    * @return the character in the cell
    */
	public Character getCharacter() {
		return character;
	}
	
	/**
	 * @param character the character to set
	 */
	public void setCharacter(Character character) {
		this.character = character;
	}
	
	/**
    *
    * @return a boolean the states whether the cell is safe or not.
    */
	public boolean isSafe() {
		return isSafe;
	}
	
	/**
	 * @param isSafe the isSafe to set
	 */
	public void setSafe(boolean isSafe) {
		this.isSafe = isSafe;
	}
	
	

}
