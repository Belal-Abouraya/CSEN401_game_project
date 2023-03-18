package engine;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import model.characters.Hero;
import model.characters.Zombie;
import model.world.Cell;

/**
 * The Game class represents the main engine of the game, and ensures all game
 * rules are followed
 * 
 * @author Belal
 */
public class Game {
	public static ArrayList<Hero> availableHeros;
	public static ArrayList<Hero> heros;
	public static ArrayList<Zombie> zombies;
	static Cell[][] map;

	/**
	 * Reads the CSV file with filePath and loads the Heros into the availableHeros
	 * ArrayList. The .csv file format should be follows:
	 * <ul>
	 * <li>Each linerepresents the information of a single Hero.
	 * <li>The data has no header, i.e. the first line represents the first Hero.
	 * <li>The parameters are separated by a comma (,).
	 * <li>The line represents the Heros’s data as follows: name, type, max Hp, max
	 * actions, attack damage .
	 * <li>The type represents the type of Hero:- • FIGH for Fighter • EXP for
	 * Explorer • MED for Medic
	 * </ul>
	 * 
	 * @param filePath The path of the file containg the heros' information.
	 * @throws FileNotFoundException, IOException
	 */
	public static void loadHeros(String filePath) throws FileNotFoundException, IOException {
		FileReader fr = new FileReader(filePath);
		Scanner sc = new Scanner(fr);
		while (sc.hasNextLine()) {
			String[] tmp = sc.nextLine().split(",");
			String name = tmp[0];
			String type = tmp[1];
			int maxHp = Integer.parseInt(tmp[2]);
			int maxActions = Integer.parseInt(tmp[3]);
			int attackDmg = Integer.parseInt(tmp[4]);
			// todo
			// add the appropriate constructors when the subclasses are finished
			switch (type) {
			case "FIGH":

				break;
			case "EXP":

				break;
			case "MED":

				break;
			}
			System.out.println(Arrays.toString(tmp));
		}
		fr.close();
	}

}
