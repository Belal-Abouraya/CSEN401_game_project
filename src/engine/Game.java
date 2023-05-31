package engine;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import exceptions.GameActionException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;
import javafx.scene.image.Image;
import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Medic;
import model.characters.Hero;
import model.characters.Zombie;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;

/**
 * The Game class represents the main engine of the game, and ensures all game
 * rules are followed
 * 
 * @author Belal Abouraya
 * @author Rafael Smauel
 * @author Ahmed Hussein
 */
public class Game {
	public static ArrayList<Hero> availableHeroes = new ArrayList<>();
	public static ArrayList<Hero> heroes = new ArrayList<>();
	public static ArrayList<Zombie> zombies = new ArrayList<>(10);
	public static Cell[][] map = new Cell[15][15];
	public static Hero currentHero;
	public static String mode = "classic";
	public static int deadZombies, deadHeroes, turns;
	public static long startTime, endTime;

	public static void main(String[] args) throws Exception {
	}

	/**
	 * Reads the CSV file with filePath and loads the Heroes into the
	 * availableHeroes ArrayList. The .csv file format should be follows:
	 * <ul>
	 * <li>Each line represents the information of a single Hero.
	 * <li>The data has no header, i.e. the first line represents the first Hero.
	 * <li>The parameters are separated by a comma (,).
	 * <li>The line represents the Heroes's data as follows: name, type, max Hp, max
	 * actions, attack damage .
	 * <li>The type represents the type of Hero: FIGH for Fighter EXP for Explorer
	 * MED for Medic
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

	/**
	 * Clears the cell on the map by making a new cell in its location and adding
	 * the location to the emptyCells ArrayList
	 * 
	 * @param x the x coordinate of the cell
	 * @param y the y coordinate of the cell
	 */
	public static void clearCell(int x, int y) {
		((CharacterCell) map[x][y]).setCharacter(null);
	}

	/**
	 * Spawns Cells at a random location on the map.
	 * 
	 * @param c the type of the cell to be spawn
	 */
	public static void spawnCell(Cell c) {
		ArrayList<ArrayList<Integer>> emptyCells = new ArrayList<>();
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[i].length; j++)
				if (map[i][j] == null)
					emptyCells.add(new ArrayList<>(Arrays.asList(i, j)));
				else if (map[i][j] instanceof CharacterCell && ((CharacterCell) map[i][j]).getCharacter() == null) {
					emptyCells.add(new ArrayList<>(Arrays.asList(i, j)));
				}

		int idx = (int) (Math.random() * emptyCells.size());
		int x = emptyCells.get(idx).get(0);
		int y = emptyCells.get(idx).get(1);
		map[x][y] = c;
		if (c instanceof CharacterCell) {
			((CharacterCell) c).getCharacter().setLocation(new Point(x, y));
		}
	}

	/**
	 * This method is called to handle the initial game setup, as such, it spawns
	 * the necessary collectibles (5 Vaccines, 5 Supplies), spawns 5 traps randomly
	 * around the map, spawns 10 zombies randomly around the map, adds the hero to
	 * the controllable heroes pool and removing from the availableHeroes, and
	 * finally allocating the hero to the bottom left corner of the map and making
	 * the adjacent cells visible.
	 * 
	 * @param h
	 */
	public static void startGame(Hero h) {
		startTime = System.currentTimeMillis();
		deadZombies = deadHeroes = turns = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j] = new CharacterCell();
			}
		}
		map[0][0] = new CharacterCell(h);
		h.setLocation(new Point(0, 0));
		availableHeroes.remove(h);
		heroes.add(h);
		for (int i = 0; i < 5; i++) {
			spawnCell(new CollectibleCell(new Vaccine()));
			spawnCell(new CollectibleCell(new Supply()));
			spawnCell(new TrapCell());
		}
		for (int i = 0; i < 10; i++) {
			Zombie z = new Zombie();
			zombies.add(z);
			spawnCell(new CharacterCell(z));
		}
		Hero.makeAllAdjacentVisible(0, 0);
	}

	/**
	 * This method checks the win conditions for the game and updates the endTime
	 * variable if the player wins.
	 */
	public static boolean checkWin() {
		if (heroes.size() >= 5) {
			for (int i = 0; i < map.length; i++)
				for (int j = 0; j < map[i].length; j++)
					if (map[i][j] instanceof CollectibleCell) {
						if (((CollectibleCell) map[i][j]).getCollectible() instanceof Vaccine)
							return false;
					}
			for (Hero h : heroes)
				if (h.getVaccineInventory().size() > 0)
					return false;

			endTime = System.currentTimeMillis();
			return true;
		}
		return false;
	}

	/**
	 * This method checks the conditions for the game to end.
	 */
	public static boolean checkGameOver() {
		if (heroes.size() == 0)
			return true;
		if (heroes.size() < 5) {
			for (int i = 0; i < map.length; i++)
				for (int j = 0; j < map[i].length; j++)
					if (map[i][j] instanceof CollectibleCell) {
						if (((CollectibleCell) map[i][j]).getCollectible() instanceof Vaccine)
							return false;
					}
			for (Hero h : heroes)
				if (h.getVaccineInventory().size() > 0)
					return false;
			return true;
		} else
			return false;
	}

	/**
	 * This method is called when the player decides to end the turn. it makes all
	 * the zombies in the game attack an adjacent Hero ( if exists ) , and reset
	 * each hero's actions, target, and special, end update the map visibility.
	 * 
	 * @throws NotEnoughActionsException
	 * @throws InvalidTargetException
	 */

	public static ArrayList<Point> endTurn() {
		ArrayList<Point> res = new ArrayList<>();
		Zombie z1 = null;
		z1 = new Zombie();
		spawnCell(new CharacterCell(z1));

		for (int i = 0; i < zombies.size(); i++) {
			Zombie z = zombies.get(i);
			Hero h = z.getAdjacentTarget();
			if (h != null) {
				try {
					res.add(h.getLocation());
					z.attack();
				} catch (GameActionException e) {
					System.out.println("Balabizo");
				}
			}
			z.setTarget(null);
		}
		for (Hero h : heroes) {
			h.reset();
		}
		updateMapVisibility();
		if (z1 != null)
			zombies.add(z1);
		turns++;
		return res;
	}

	/**
	 * A helper method that updates the map visibility in the game such that only
	 * cells adjacent to heroes are visible
	 */

	private static void updateMapVisibility() {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				Game.map[i][j].setVisible(false);
			}
		}

		for (int i = 0; i < heroes.size(); i++) {
			Hero h = heroes.get(i);
			Hero.makeAllAdjacentVisible((int) h.getLocation().getX(), (int) h.getLocation().getY());
		}
	}

}
