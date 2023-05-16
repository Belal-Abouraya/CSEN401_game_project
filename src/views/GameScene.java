package views;

import engine.Game;
import exceptions.GameActionException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.characters.Character;
import model.characters.Direction;
import model.characters.Hero;
import model.characters.Zombie;
import model.collectibles.Collectible;
import model.collectibles.Vaccine;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;

/**
 * A Helper class that creates the game scene. It has only one attribute:
 * <ul>
 * <li>currentHero</li>
 * </ul>
 * 
 * @author Belal Abouraya
 */
public class GameScene {

	Hero currentHero = Game.heroes.get(0);

	/**
	 * The method called by the Main class to get the game scene. It creates a Scene
	 * object with all the required elements and logic of the game
	 * 
	 * @return the finished game scene object
	 */
	public Scene gameScene() {
		BorderPane root = new BorderPane();
		Scene gameScene = new Scene(root);
		// TODO Belal
		GridPane grid = new GridPane();

		updateGrid(Game.map, grid);

		Label updates = new Label();
		BorderPane bottom = new BorderPane();
		// updates.setAlignment(Pos.BASELINE_LEFT);
		Button endTurn = new Button("End turn");
		endTurn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Game.endTurn();
				updateGrid(Game.map, grid);
				if (Game.checkGameOver())
					grid.fireEvent(new GameEvent(GameEvent.GAME_OVER));
				else if (Game.checkWin())
					grid.fireEvent(new GameEvent(GameEvent.WIN));
			}
		});
		// endTurn.setAlignment(Pos.BASELINE_RIGHT);
		bottom.setLeft(updates);
		bottom.setRight(endTurn);

		// TODO Rafael
		VBox Heroes = new VBox();

		root.setCenter(grid);
		root.setBottom(bottom);
		root.setLeft(Heroes);

		/*
		 * handles the game controls. W, A, S, D for movement Q for cure R for using the
		 * special action
		 */
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				Direction d = null;
				switch (e.getCode()) {
				case W -> d = Direction.UP;
				case A -> d = Direction.LEFT;
				case S -> d = Direction.DOWN;
				case D -> d = Direction.RIGHT;

				case Q -> {
					try {
						currentHero.cure();
					} catch (GameActionException e1) {
						updates.setText(e1.getMessage());
					}
				}
				case R -> {
					try {
						currentHero.useSpecial();
					} catch (GameActionException e1) {
						updates.setText(e1.getMessage());
					}
				}
				}

				if (d != null) {
					try {
						currentHero.move(d);
					} catch (GameActionException e1) {
						updates.setText(e1.getMessage());
					}
				}
				updateGrid(Game.map, grid);
				if (Game.checkGameOver())
					grid.fireEvent(new GameEvent(GameEvent.GAME_OVER));
				else if (Game.checkWin())
					grid.fireEvent(new GameEvent(GameEvent.WIN));
			}

		});

		// handles mouse inputs. Left click for attack
		gameScene.setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				try {
					currentHero.attack();
				} catch (GameActionException e1) {
					updates.setText(e1.getMessage());
				}
				updateGrid(Game.map, grid);
				if (Game.checkGameOver())
					grid.fireEvent(new GameEvent(GameEvent.GAME_OVER));
				else if (Game.checkWin())
					grid.fireEvent(new GameEvent(GameEvent.WIN));
			}
		});

		return gameScene;
	}

	private void updateGrid(Cell[][] map, GridPane grid) {
		grid.getChildren().clear();
		for (int i = map.length - 1; i >= 0; i--) {
			for (int j = map[i].length - 1; j >= 0; j--) {
				int x = j;
				int y = getCoord(i);
				StackPane tmp = null;
				if (map[i][j].isVisible()) {
					if (map[i][j] instanceof CharacterCell) {
						tmp = characterStackPane(((CharacterCell) map[i][j]).getCharacter());
					} else if (map[i][j] instanceof CollectibleCell)
						tmp = collectibleStackPane(((CollectibleCell) map[i][j]).getCollectible());
					else
						tmp = characterStackPane(null);
				} else
					tmp = invisibleStackPane();
				grid.add(tmp, x, y);

			}
		}
	}

	private StackPane characterStackPane(Character c) {
		StackPane res = emptyStackPane();
		// handles setting the target
		res.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.SECONDARY) {
				currentHero.setTarget(c);
			}
		});

		// handles styling
		Rectangle front = null;
		if (c instanceof Hero) {
			front = new Rectangle(15, 15, Color.BLUE);
		} else if (c instanceof Zombie) {
			front = new Rectangle(15, 15, Color.RED);
		} else
			front = new Rectangle(15, 15, Color.WHITE);
		res.getChildren().add(front);
		return res;
	}

	private StackPane collectibleStackPane(Collectible s) {
		StackPane res = emptyStackPane();

		// handles styling
		Rectangle front = null;
		if (s instanceof Vaccine) {
			front = new Rectangle(15, 15, Color.GREEN);
		} else {
			front = new Rectangle(15, 15, Color.BROWN);
		}
		res.getChildren().add(front);
		return res;
	}

	private StackPane emptyStackPane() {
		StackPane res = new StackPane();
		Rectangle front = new Rectangle(15, 15, Color.WHITE);
		res.getChildren().add(front);
		return res;
	}

	private StackPane invisibleStackPane() {
		StackPane res = emptyStackPane();

		// handles styling
		Rectangle front = new Rectangle(15, 15, Color.BLACK);
		res.getChildren().add(front);
		return res;
	}

	private int getCoord(int x) {
		return 15 - x;
	}

}
