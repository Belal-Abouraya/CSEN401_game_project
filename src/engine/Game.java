package engine;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import exceptions.GameActionException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;
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
	public static ArrayList<Zombie> zombies = new ArrayList<>();
	public static ArrayList<ArrayList<Integer>> emptyCells = new ArrayList<>();
	public static Cell[][] map = new Cell[15][15];

	/**
	 * Reads the CSV file with filePath and loads the Heroes into the
	 * availableHeroes ArrayList. The .csv file format should be follows:
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

	/**
	 * Clears the cell on the map by making a new cell in its location and adding
	 * the location to the emptyCells ArrayList
	 * 
	 * @param x the x coordinate of the cell
	 * @param y the y coordinate of the cell
	 */
	public static void clearCell(int x, int y) {
		map[x][y] = new CharacterCell();
		emptyCells.add(new ArrayList<>(Arrays.asList(x, y)));
	}

	/**
	 * Spawns Cells at a random location on the map.
	 * 
	 * @param c the type of the cell to be spawn
	 */
	public static void spawnCell(Cell c) {
		if (emptyCells.isEmpty())
			return;
		int idx = (int) (Math.random() * emptyCells.size());
		int x = emptyCells.get(idx).get(0);
		int y = emptyCells.get(idx).get(1);
		map[x][y] = c;
		if (c instanceof CharacterCell) {
			((CharacterCell) c).getCharacter().setLocation(new Point(x, y));
		}
		emptyCells.remove(idx);
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
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				emptyCells.add(new ArrayList<>(Arrays.asList(i, j)));
			}
		}
		emptyCells.remove(new ArrayList<>(Arrays.asList(0, 0)));
		map[0][0] = new CharacterCell(h);
		h.setLocation(new Point(0, 0));
		availableHeroes.remove(h);
		heroes.add(h);
		Hero.makeAllAdjacentVisible(0, 0);
		for (int i = 0; i < 5; i++) {
			spawnCell(new CollectibleCell(new Vaccine()));
			spawnCell(new CollectibleCell(new Supply()));
			spawnCell(new TrapCell());
		}
		for (int i = 0; i < 10; i++) {
			spawnCell(new CharacterCell(new Zombie()));
		}
	}

	/**
	 * This method checks the win conditions for the game.
	 */
	public static boolean checkWin() {
		return (Vaccine.getUsedVaccine() == 5 && heroes.size() >= 5);
	}

	/**
	 * This method checks the conditions for the game to end.
	 */
	public static boolean checkGameOver() {
		return (heroes.size() == 0 || Vaccine.getUsedVaccine() == 5 && heroes.size() < 5);
	}

	/**
	 * This method is called when the player decides to end the turn. it makes all
	 * the zombies in the game attack an adjacent Hero ( if exists ) , and reset
	 * each hero’s actions, target, and special, end update the map visibility.
	 * 
	 * @throws NotEnoughActionsException
	 * @throws InvalidTargetException
	 */

	public static void endTurn() {

		for (Zombie z : zombies) {
			if (z.getAdjacentTarget() != null) {
				Hero h = z.getAdjacentTarget();
				z.setTarget(h);
				try {
					z.attack();
				} catch (GameActionException e) {
					System.out.println("Balabizo");
				}
				z.setTarget(null);
			}
		}
		for (Hero h : heroes) {
			h.reset();
		}
		updateMapVisibility();
		spawnCell(new CharacterCell(new Zombie()));
	}

	/**
	 * ِِِA helper method that updates the map visibility in the game such that only
	 * cells adjacent to heroes are visible
	 */

	private static void updateMapVisibility() {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				Game.map[i][j].setVisible(false);
			}
		}

		for (Hero h : Game.heroes) {
			Hero.makeAllAdjacentVisible((int) h.getLocation().getX(), (int) h.getLocation().getY());
		}
	}
}
