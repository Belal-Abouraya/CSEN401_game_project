package engine;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import exceptions.GameActionException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
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
public class Game extends Application {
	public static ArrayList<Hero> availableHeroes = new ArrayList<>();
	public static ArrayList<Hero> heroes = new ArrayList<>();
	public static ArrayList<Zombie> zombies = new ArrayList<>(10);
	public static Cell[][] map = new Cell[15][15];
	public static Hero currentHero ;

	public static void main(String[] args) throws Exception {
		loadHeroes("src\\test_heros.csv");
		launch(args);
	}

	/**
	 * Reads the CSV file with filePath and loads the Heroes into the
	 * availableHeroes ArrayList. The .csv file format should be follows:
	 * <ul>
	 * <li>Each line represents the information of a single Hero.
	 * <li>The data has no header, i.e. the first line represents the first Hero.
	 * <li>The parameters are separated by a comma (,).
	 * <li>The line represents the Heroesâ€™s data as follows: name, type, max Hp,
	 * max actions, attack damage .
	 * <li>The type represents the type of Hero:- â€¢ FIGH for Fighter â€¢ EXP for
	 * Explorer â€¢ MED for Medic
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
	 * This method checks the win conditions for the game.
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

	public static void endTurn() {
		Zombie z1 = null;
		if (zombies.size() < 10) {
			z1 = new Zombie();
			spawnCell(new CharacterCell(z1));
		}
		for (Zombie z : zombies) {
			if (z.getAdjacentTarget() != null) {
				try {
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

		for (Hero h : Game.heroes) {
			Hero.makeAllAdjacentVisible((int) h.getLocation().getX(), (int) h.getLocation().getY());
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Last of US : Legacy");
		//firstScene is nothing but an image and a text waiting for the user to press Enter
		StackPane stackPane = new StackPane();
		Image wallpaper = new Image("images\\FirstScene.jpg") ;
		ImageView imageView = new ImageView(wallpaper);
		imageView.setFitHeight(720);
		imageView.setFitWidth(1280);
		Label waitingMessage = new Label("Press Enter to Start");
		waitingMessage.setFont(new Font("Impact" , 35));
		waitingMessage.setTextFill(Color.CORAL);
		waitingMessage.setTranslateY(265);
		Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(waitingMessage.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(waitingMessage.opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(2), new KeyValue(waitingMessage.opacityProperty(), 0))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
		stackPane.getChildren().add(imageView);
		stackPane.getChildren().add(waitingMessage);
		Scene firstScene = new Scene(stackPane,1280,720);
		stage.setScene(firstScene);
		//secondScene is the scene where the player chooses his first hero
		ImageView viewer = new ImageView();
		viewer.setFitHeight(720);
		viewer.setFitWidth(1280);
		StackPane pane = new StackPane();
		pane.getChildren().add(viewer);
		GridPane grid = new GridPane();
		for(int i = 0 ; i < 4 ; i++) {
			for(int j = 0 ; j < 4 ; j++) {
				Button button = new Button();
				Image tmp = availableHeroes.get(i+j).getImage();
				ImageView heroFace = new ImageView(tmp);
				heroFace.setFitHeight(60);
				heroFace.setFitWidth(60);
				button.setGraphic(heroFace);
			}
		}
		firstScene.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER)
				stage.setScene(new Scene(new Group() , 1280 , 720));
		});
		stage.show();
	}

}
