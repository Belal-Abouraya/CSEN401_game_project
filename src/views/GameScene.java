package views;

import engine.Game;
import exceptions.GameActionException;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.characters.Character;
import model.characters.Direction;
import model.characters.Hero;
import model.characters.Zombie;
import model.collectibles.Collectible;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;

/**
 * @author Belal Abouraya
 */
public class GameScene {

	public GameScene() {

	}

	public Scene gameScene() {
		Hero currentHero = Game.heroes.get(0);
		BorderPane root = new BorderPane();
		// TODO Belal
		GridPane grid = new GridPane();

		createGrid(Game.map, grid);

		Label updates = new Label();
		HBox bottom = new HBox();
		updates.setAlignment(Pos.BOTTOM_LEFT);
		Button endTurn = new Button("End turn");
		endTurn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Game.endTurn();
				updateGrid(Game.map, grid);
			}
		});
		endTurn.setAlignment(Pos.BASELINE_RIGHT);
		bottom.getChildren().add(updates);
		bottom.getChildren().add(endTurn);

		// TODO Rafael
		VBox Heroes = new VBox();

		root.setCenter(grid);
		root.setBottom(bottom);
		root.setLeft(Heroes);

		Scene gameScene = new Scene(root);
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
					;
				else if (Game.checkWin())
					;

			}

		});

		// handles mouse inputs. Left click for attack
		gameScene.setOnMouseClicked(e -> {

		});

		return gameScene;
	}

	public void createGrid(Cell[][] map, GridPane grid) {
		for (int i = map.length - 1; i >= 0; i--) {
			for (int j = map[i].length - 1; j >= 0; j--) {
				int x = i;// getCoord(i);
				int y = getCoord(j);
				StackPane tmp = null;
				if (map[i][j].isVisible()) {
					if (map[i][j] instanceof CharacterCell) {
						tmp = characterStackPane(((CharacterCell) map[i][j]).getCharacter());
					} else if (map[i][j] instanceof CollectibleCell)
						tmp = collectibleStackPane(((CollectibleCell) map[i][j]).getCollectible());
					else
						tmp = trapStackPane();
				} else
					tmp = invisibleStackPane();
				grid.add(tmp, x, y);

			}
		}
//        final int numCols = 50 ;
//        final int numRows = 50 ;
//        for (int i = 0; i < numCols; i++) {
//            ColumnConstraints colConst = new ColumnConstraints();
//            colConst.setPercentWidth(100.0 / numCols);
//            root.getColumnConstraints().add(colConst);
//        }
//        for (int i = 0; i < numRows; i++) {
//            RowConstraints rowConst = new RowConstraints();
//            rowConst.setPercentHeight(100.0 / numRows);
//            root.getRowConstraints().add(rowConst);         
//        }
	}

	public void updateGrid(Cell[][] map, GridPane grid) {
		for (int i = map.length - 1; i >= 0; i--) {
			for (int j = map[i].length - 1; j >= 0; j--) {
				int x = i;
				int y = getCoord(j);
				StackPane tmp = null;
				if (map[i][j].isVisible()) {
					if (map[i][j] instanceof CharacterCell) {
						tmp = characterStackPane(((CharacterCell) map[i][j]).getCharacter());
					} else if (map[i][j] instanceof CollectibleCell)
						tmp = collectibleStackPane(((CollectibleCell) map[i][j]).getCollectible());
					else
						tmp = trapStackPane();
				} else
					tmp = invisibleStackPane();
				grid.add(tmp, x, y);

			}
		}
	}

	/*
	 * creates a button for the hero cells
	 */
	public StackPane characterStackPane(Character c) {
		StackPane res = new StackPane();
		// hanles setting the target
		res.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.SECONDARY) {

			}
		});

		// handles styling
		Rectangle back = new Rectangle();
		Rectangle front = null;
		if (c instanceof Hero) {
			front = new Rectangle(15, 15, Color.BLUE);
		} else if (c instanceof Zombie) {
			front = new Rectangle(15, 15, Color.RED);
		} else
			front = new Rectangle(15, 15, Color.WHITE);
		res.getChildren().add(back);
		res.getChildren().add(front);
		return res;
	}

	public StackPane collectibleStackPane(Collectible s) {
		StackPane res = new StackPane();

		// handles styling
		Rectangle back = new Rectangle();
		Rectangle front = null;
		if (s instanceof Vaccine) {
			front = new Rectangle(15, 15, Color.GREEN);
		} else {
			front = new Rectangle(15, 15, Color.BROWN);
		}
		res.getChildren().add(back);
		res.getChildren().add(front);
		return res;
	}

	public StackPane trapStackPane() {
		StackPane res = new StackPane();

		// handles styling
		Rectangle back = new Rectangle();
		Rectangle front = new Rectangle(15, 15, Color.WHITE);

		res.getChildren().add(back);
		res.getChildren().add(front);
		return res;
	}

	public StackPane invisibleStackPane() {
		StackPane res = new StackPane();

		// handles styling
		Rectangle back = new Rectangle();
		Rectangle front = new Rectangle(15, 15, Color.BLACK);
		res.getChildren().add(back);
		res.getChildren().add(front);
		return res;
	}

	public int getCoord(int x) {
		return 15 - x;
	}

}
