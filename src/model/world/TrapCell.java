package model.world;

/**
 * TrapCell is a special type of cells.
 * it contains one attribute indicates how much damage is achieved by the cell.
 *
 * @author Ahmed Hussein
 */

public class TrapCell extends Cell{


    private int trapDamage ;

    /**
     * constructor for the TrapCell class that initializes the trap damage with a random value 10, 20 or 30
     */

    public TrapCell(){
        super();
        trapDamage = 10 * ((int)(Math.random() * 3) + 1); // gets 10, 20 or 30 only
    }

    /**
     *
     * @return the damage achieved by the cell
     */
    public int getTrapDamage() {
        return trapDamage;
    }

}
