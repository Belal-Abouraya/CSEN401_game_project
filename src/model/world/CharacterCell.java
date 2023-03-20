package model.world;

import model.characters.Character;

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
     * constructor for the CharacterCell class that initializes the character and isSafe variables
     */
	

	public CharacterCell(boolean isSafe , Character character) {
		this.character = character;
		this.isSafe = isSafe;
	}
	
	/**
	* Constructor for the CharacterCell class that accepts only one arguement
	*/
	
	public CharacterCell(Character character) {
		this(false , character);
	}
	
	
	/**
     * the default construcor of the class
     */

	public CharacterCell() {
		
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
