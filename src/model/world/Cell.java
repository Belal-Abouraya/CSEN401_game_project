package model.world;

/**
 * Cell is the abstract base class of all cell types in the game map. It's only
 * attribute indicates whether the cell is visible or not
 * 
 * @author Belal Abouraya
 */
public abstract class Cell {
	private boolean isVisible;

	public Cell() {
		isVisible = false;
	}

	/**
	 * @return the isVisible
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * @param isVisible the isVisible to set
	 */
    public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}
