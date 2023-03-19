package model.characters;

/**
 * represents the explorers in the game.
 *
 * @author Ahmed Hussein
 */

public class Explorer extends Hero{


    /**
     * constructor for the explorer class that initializes the name, the maximum Hp, the attack damage
     * and the maximum number of actions in a turn.
     *
     * @param name
     * @param maxHp
     * @param attackDmg
     * @param maxActions
     */
    public Explorer(String name , int maxHp , int attackDmg , int maxActions){
        super(name , maxHp , attackDmg , maxActions);
    }


}