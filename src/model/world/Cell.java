package model.world;

/**
 * Cell is the abstract base class of all cell types in the game map. It's only
 * attribute indicates whether the cell is visible or not
 * 
 * @author Belal Abouraya
 */
public abstract class Cell {
	private boolean isVisible;

	Cell() {
		isVisible = false;
	}

	/**
	 * @return the isVisible
	 */
	boolean isVisible() {
		return isVisible;
	}

	/**
	 * @param isVisible the isVisible to set
	 */
	void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}
