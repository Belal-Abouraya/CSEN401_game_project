package engine;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Medic;
import model.characters.Hero;
import model.characters.Zombie;
import model.world.Cell;

/**
 * The Game class represents the main engine of the game, and ensures all game
 * rules are followed
 * 
 * @author Belal Abouraya
 */
public class Game {
	public static ArrayList<Hero> availableHeroes = new ArrayList<>();
	public static ArrayList<Hero> heroes = new ArrayList<>();
	public static ArrayList<Zombie> zombies = new ArrayList<>();
	public static Cell[][] map = new Cell[15][15];

	/**
	 * Reads the CSV file with filePath and loads the Heroes into the availableHeroes
	 * ArrayList. The .csv file format should be follows:
	 * <ul>
	 * <li>Each line represents the information of a single Hero.
	 * <li>The data has no header, i.e. the first line represents the first Hero.
	 * <li>The parameters are separated by a comma (,).
	 * <li>The line represents the Heroes’s data as follows: name, type, max Hp, max
	 * actions, attack damage .
	 * <li>The type represents the type of Hero:- • FIGH for Fighter • EXP for
	 * Explorer • MED for Medic
	 * </ul>
	 * 
	 * @param filePath The path of the file containg the heros' information.
	 * @throws FileNotFoundException, IOException
	 */
	public static void loadHeroes(String filePath) throws FileNotFoundException, IOException {
		FileReader fr = new FileReader(filePath);
		Scanner sc = new Scanner(fr);
		while (sc.hasNextLine()) {
			String[] tmp = sc.nextLine().split(",");
			String name = tmp[0];
			String type = tmp[1];
			int maxHp = Integer.parseInt(tmp[2]);
			int maxActions = Integer.parseInt(tmp[3]);
			int attackDmg = Integer.parseInt(tmp[4]);
			switch (type) {
			case "FIGH":
				availableHeroes.add(new Fighter(name, maxHp, attackDmg, maxActions));
				break;
			case "EXP":
				availableHeroes.add(new Explorer(name, maxHp, attackDmg, maxActions));
				break;
			case "MED":
				availableHeroes.add(new Medic(name, maxHp, attackDmg, maxActions));
				break;
			}
		}
		fr.close();
		sc.close();
	}

}
