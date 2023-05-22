package views;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

import engine.Game;
import exceptions.GameActionException;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.characters.Character;
import model.characters.Direction;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import model.collectibles.Collectible;
import model.collectibles.Supply;
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
 * @author Rafael Samuel
 */
public class GameScene {
	private Hero currentHero = Game.heroes.get(0);
	private VBox Heroes;
	private double cellHeight = 56, cellWidth = 75, bottomFont = 17, updatesHeight = 35, updatesWidth = 100;
	private StackPane[][] cells = new StackPane[15][15];
	EventHandler<MouseEvent>[][] events = new EventHandler[15][15];
	Image invisible, empty, vaccineModel = Character.LoadModel("vaccine"), vaccineIcon = Hero.loadIcon("vaccine"),
			supplyModel = Character.LoadModel("supply"), supplyIcon = Hero.loadIcon("supply");

	/**
	 * The method called by the Main class to get the game scene. It creates a Scene
	 * object with all the required elements and logic of the game
	 * 
	 * @return the finished game scene object
	 */
	public Scene gameScene() {
		try {
			invisible = new Image(new File("assets/" + Game.mode + "/images/wallpapers/" + "invisible" + ".png").toURI()
					.toURL().toExternalForm());
			empty = new Image(new File("assets/" + Game.mode + "/images/wallpapers/" + "empty" + ".jpg").toURI().toURL()
					.toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		BorderPane root = new BorderPane();
		Scene gameScene = new Scene(root);

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		createGrid(Game.map, grid);
		updateGrid(Game.map, grid);

		Heroes = new VBox();
		updateHeroesStack(Game.heroes, Heroes);

		Label updates = new Label();
		updates.setMinSize(updatesWidth, updatesHeight);
		updates.setStyle("-fx-font-size: " + bottomFont + ";");
		BorderPane bottom = new BorderPane();
		updates.setAlignment(Pos.CENTER);
		Button endTurn = new Button("End turn");
		endTurn.setMinHeight(updatesHeight);
		endTurn.setStyle("-fx-font-size: " + bottomFont + ";");
		FadeTransition ft = new FadeTransition(Duration.millis(1000), updates);
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.setCycleCount(2);
		ft.setAutoReverse(true);

		endTurn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Game.endTurn();
				updateScene(Game.map, grid, Game.heroes, Heroes);
				if (Game.checkGameOver())
					grid.fireEvent(new GameEvent(GameEvent.GAME_OVER));
				else if (Game.checkWin())
					grid.fireEvent(new GameEvent(GameEvent.WIN));
			}
		});

		BorderPane menu = new BorderPane();
		bottom.setCenter(updates);
		bottom.setRight(endTurn);
		bottom.setStyle("-fx-background-color: red;");
		menu.setBottom(bottom);
		menu.setLeft(Heroes);
		bottom.setMinHeight(35);
		root.setCenter(grid);
		root.setLeft(menu);

		/*
		 * handles the game controls. W, A, S, D for movement Q for cure E for using the
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
						int i = 0, j = 0;
						if (currentHero.getTarget() != null) {
							i = currentHero.getTarget().getLocation().x;
							j = currentHero.getTarget().getLocation().y;
						}
						currentHero.cure();
						Rectangle rect = (Rectangle) cells[i][j].getChildren().get(2);
						rect.setFill(Color.BLUE);
						FadeTransition ft1 = new FadeTransition(Duration.millis(150), rect);
						ft1.setFromValue(0);
						ft1.setToValue(0.3);
						ft1.setCycleCount(2);
						ft1.setAutoReverse(true);
						ft1.play();
					} catch (GameActionException e1) {
						updates.setText(e1.getMessage());
					}
				}
				case E -> {
					try {
						currentHero.useSpecial();
						if (currentHero instanceof Medic) {
							int i = currentHero.getTarget().getLocation().x;
							int j = currentHero.getTarget().getLocation().y;
							Rectangle rect = (Rectangle) cells[i][j].getChildren().get(2);
							rect.setFill(Color.GREEN);
							FadeTransition ft1 = new FadeTransition(Duration.millis(150), rect);
							ft1.setFromValue(0);
							ft1.setToValue(0.3);
							ft1.setCycleCount(2);
							ft1.setAutoReverse(true);
							ft1.play();
						}
					} catch (GameActionException e1) {
						updates.setText(e1.getMessage());
						ft.play();
					}
				}
				}

				if (d != null) {
					try {
						currentHero.move(d);
					} catch (GameActionException e1) {
						updates.setText(e1.getMessage());
						ft.play();
					}
				}
				updateScene(Game.map, grid, Game.heroes, Heroes);

				if (Game.checkGameOver())
					grid.fireEvent(new GameEvent(GameEvent.GAME_OVER));
				else if (Game.checkWin())
					grid.fireEvent(new GameEvent(GameEvent.WIN));
			}
		});

		// handles mouse inputs. Left click for attack
		grid.setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				try {
					int i = 0, j = 0;
					if (currentHero.getTarget() != null) {
						i = currentHero.getTarget().getLocation().x;
						j = currentHero.getTarget().getLocation().y;
					}
					currentHero.attack();
					Rectangle rect = (Rectangle) cells[i][j].getChildren().get(2);
					rect.setFill(Color.RED);
					FadeTransition ft1 = new FadeTransition(Duration.millis(150), rect);
					ft1.setFromValue(0);
					ft1.setToValue(0.3);
					ft1.setCycleCount(2);
					ft1.setAutoReverse(true);
					ft1.play();
				} catch (GameActionException e1) {
					updates.setText(e1.getMessage());
					ft.play();
				}
				updateScene(Game.map, grid, Game.heroes, Heroes);
				if (Game.checkGameOver())
					grid.fireEvent(new GameEvent(GameEvent.GAME_OVER));
				else if (Game.checkWin())
					grid.fireEvent(new GameEvent(GameEvent.WIN));
			}
		});

		gameScene.getStylesheets().add(this.getClass().getResource(Game.mode + ".css").toExternalForm());
		return gameScene;
	}

	/**
	 * 
	 * @param map
	 * @param grid
	 * @param h
	 * @param stack
	 */
	private void updateScene(Cell[][] map, GridPane grid, ArrayList<Hero> h, VBox stack) {
		updateGrid(map, grid);
		updateHeroesStack(h, stack);
	}

	/**
	 * Initializes all the grid cells.
	 * 
	 * @param map  two dimensional array conataining the game state
	 * @param grid the grid to be updated
	 */
	private void createGrid(Cell[][] map, GridPane grid) {
		for (int i = map.length - 1; i >= 0; i--) {
			for (int j = map[i].length - 1; j >= 0; j--) {
				int x = j;
				int y = 15 - i;
				StackPane tmp = base();
				cells[i][j] = tmp;
				grid.add(tmp, x, y);
			}
		}

	}

	/**
	 * Updates the displayed grid.
	 * 
	 * @param map  two dimensional array conataining the game state
	 * @param grid the grid to be updated
	 */
	private void updateGrid(Cell[][] map, GridPane grid) {
		for (int i = map.length - 1; i >= 0; i--) {
			for (int j = map[i].length - 1; j >= 0; j--) {
				cells[i][j].getChildren().remove(1);
				ImageView content = new ImageView();
				if (events[i][j] != null)
					cells[i][j].removeEventHandler(MouseEvent.ANY, events[i][j]);
				if (map[i][j].isVisible()) {
					if (map[i][j] instanceof CharacterCell) {
						Character c = ((CharacterCell) map[i][j]).getCharacter();
						content.setFitWidth(cellWidth);
						content.setFitHeight(cellHeight);
						if (c != null)
							content.setImage(c.getModel());
						EventHandler<MouseEvent> e = new EventHandler<MouseEvent>() {

							@Override
							public void handle(MouseEvent arg0) {
								currentHero.setTarget(c);
							}
						};
						cells[i][j].setOnMouseClicked(e);

					}

					else if (map[i][j] instanceof CollectibleCell) {
						Collectible c = ((CollectibleCell) map[i][j]).getCollectible();
						Image model = null;
						if (c instanceof Supply)
							model = supplyModel;
						else
							model = vaccineModel;
						content.setImage(model);
						content.setFitWidth(cellWidth / 1.5);
						content.setFitHeight(cellHeight / 1.5);
					}
				} else {
					content.setImage(invisible);
					content.setFitWidth(cellWidth);
					content.setFitHeight(cellHeight);
				}
				cells[i][j].getChildren().add(1, content);
			}
		}

	}

	private StackPane base() {
		StackPane res = new StackPane();
		ImageView base = new ImageView(empty);
		base.setFitHeight(cellHeight);
		base.setFitWidth(cellWidth);
		res.getChildren().addAll(base, new Rectangle(), new Rectangle(cellWidth, cellHeight, Color.TRANSPARENT));
		return res;
	}

	// create a stack of hero cards
	private void updateHeroesStack(ArrayList<Hero> h, VBox stack) {
		stack.getChildren().clear();
		for (Hero x : h) {
			StackPane card = heroCard(x);
			stack.getChildren().add(card);
		}

	}

	// create a card for a hero
	private StackPane heroCard(Hero h) {
		HBox card = new HBox();
		card.setSpacing(3);

		// VBox to contian the hero info
		VBox info = new VBox();

		// Getting the hero info
		String name = h.getName();
		int currentHp = h.getCurrentHp();
		int noSupplies = h.getSupplyInventory().size();
		int noVaccines = h.getVaccineInventory().size();
		int attackDmg = h.getAttackDmg();
		int actionsAvailable = h.getActionsAvailable();
		boolean usedSpeacialAction = h.isSpecialAction();

		// Setting the health bar.
		double healthBarWidth = 150;
		double maxHp = h.getMaxHp();
		double iconSize = 12;
		GridPane healthBar = bar("healthBar", healthBarWidth, currentHp, maxHp, iconSize, "src/images/healthicon.png");

		// Setting the action points bar
		double actionsBarWidth = 120;
		double maxActions = h.getMaxActions();
		GridPane actionsBar = bar("actionsBar", actionsBarWidth, actionsAvailable, maxActions, iconSize,
				"src/images/actionpointsicon.png");

		// Setting the collictibleview
		GridPane collectibles = collectibles(noSupplies, noVaccines);

		// specify the type of the hero.
		String type = "Balabizak yasta";
		if (h instanceof Medic) {
			type = "Medic";
		} else if (h instanceof Fighter) {
			type = " Fighter";
		} else {
			type = "Explorer";
		}

		Label l = new Label("Attack damage: " + attackDmg);

		info.setSpacing(2);
		info.getChildren().addAll(healthBar, actionsBar, collectibles);

		// getting the hero image
		VBox img = heroImage(h, type + " " + name);
		img.setAlignment(Pos.TOP_LEFT);

		card.getChildren().addAll(img, info);

		int width = 350;
		int height = 140;
		StackPane res = new StackPane();
		Rectangle rec = new Rectangle(width, height);
		rec.setArcHeight(35);
		rec.setArcWidth(35);
		if (h.equals(currentHero))
			rec.setFill(Color.BEIGE.brighter());
		else
			rec.setFill(Color.GREY);
		res.getChildren().add(rec);
		card.setMaxWidth(width - 10);
		card.setMaxHeight(height - 10);

		res.getChildren().add(card);

		// setting a listener to the card
		res.setOnMouseEntered(e -> {
			rec.setFill(Color.BEIGE.brighter());
		});
		res.setOnMouseExited(e -> {
			if (h != currentHero)
				rec.setFill(Color.GRAY);

		});
		res.setOnMouseClicked(e -> {

			currentHero = h;
			updateHeroesStack(Game.heroes, Heroes);

		});
		return res;

	}

	private static VBox heroImage(Hero h, String txt) {
		StackPane photo = new StackPane();
		ImageView imageView = new ImageView(h.getIcon());
		int width = 95;
		int height = 100;
		imageView.setFitHeight(height);
		imageView.setFitWidth(width);

		photo.getChildren().add(imageView);

		// Create a Rectangle as the Clip shape with round edges
		Rectangle clipShape = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
		clipShape.setArcWidth(30);
		clipShape.setArcHeight(30);
		imageView.setClip(clipShape);

		Label name = new Label();
		name.setText(txt);

		VBox res = new VBox(10);
		res.getChildren().addAll(photo, name);
		return res;
	}

	private static GridPane collectibles(int supplies, int vaccines) {
		GridPane grid = new GridPane();
		grid.setHgap(5);
		StackPane vaccineIcon = icon(15, "src/images/vaccineIcon.png");
		StackPane supplyeIcon = icon(15, "src/images/supplyIcon.png");

		Label noVaccines = new Label();
		noVaccines.setText("  " + vaccines);

		// vaccineIcon.getChildren().add(noVaccines);

		Label noSupplies = new Label();
		noSupplies.setText("     " + supplies);

		// supplyeIcon.getChildren().add(noSupplies);

		grid.add(vaccineIcon, 0, 0);
		grid.add(noVaccines, 1, 0);
		grid.add(supplyeIcon, 2, 0);
		grid.add(noSupplies, 3, 0);
		return grid;
	}

	private static StackPane icon(double r, String path) {
		StackPane res = new StackPane();
		Circle circle = new Circle(r);
		circle.setCenterX(r);
		circle.setCenterY(r);

		try {
			Image image = new Image(new File(path).toURI().toURL().toExternalForm());
			ImageView imageView = new ImageView(image);
			imageView.setFitWidth(r * 2);
			imageView.setFitHeight(r * 2);
			imageView.setClip(circle);
			res.getChildren().add(imageView);

		} catch (Exception e) {
			System.out.println("Missing image");
			circle.setFill(Color.BLACK);
			res.getChildren().add(circle);

		}
		return res;
	}

	private static GridPane bar(String type, double width, double current, double max, double iconSize, String path) {
		GridPane res = new GridPane();
		res.setHgap(20);

		ProgressBar bar = new ProgressBar();

		bar.setProgress(current / max);
		String style = "";
		if (type.equals("healthBar")) {
			style = "-fx-padding: 2px; -fx-background-insets: 2px;-fx-pref-height: 23px;";
			if (current >= 0.75 * max)
				style += "-fx-accent: green;";
			else if (current >= 0.5 * max)
				style += "-fx-accent: yellow;";
			else if (current >= 0.25 * max)
				style += "-fx-accent: orange;";
			else
				style += "-fx-accent: red;";
		} else {
			style = "-fx-accent: Blue;-fx-padding: 2px; -fx-background-insets: 2px;-fx-pref-height: 20px;";

		}
		bar.setStyle(style);
		bar.setPrefWidth(width);

		StackPane icon = icon(iconSize, path);

		res.add(bar, 1, 0);
		res.add(icon, 0, 0);

		return res;

	}

}
