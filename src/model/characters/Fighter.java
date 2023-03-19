package model.characters;

/**
 * represents fighters in the game.
 *
 * @author Ahmed Hussein
 */

public class Fighter extends Hero{


    /**
     * constructor for the fighter class which initializes the name, the maximum Hp, the attack damage
     * and the maximum number of actions in a turn.
     *
     * @param name
     * @param maxHp
     * @param attackDmg
     * @param maxActions
     */
    public Fighter(String name , int maxHp , int attackDmg , int maxActions){
        super(name , maxHp , attackDmg , maxActions);
    }


}
