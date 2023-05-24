package views;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

import engine.Game;
import exceptions.GameActionException;
import javafx.animation.FadeTransition;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
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

	// private double heroCardWidth = 350;
	// private double heroCardHeight = 140;

	private GridPane grid;
	private Label updates;
	FadeTransition ft;
	final private static double CELLHEIGHT = 55, CELLWIDTH = 76, BOTTOMFONT = 18, UPDATESHEIGHT = 35,
			HEROCARDWIDTH = 350, HEROCARDHEIGHT = 140, HEROIMAGEWIDTH =95 , HEROIMAGEHEIGHT =100,
			HEALTHBARWIDTH = 170,ACTIONBARWIDTH = 120,ICONSIZE = 13;
	private double cellHeight = 54, cellWidth = 75, bottomFont = 20, updatesHeight = 35,
			heroCardWidth = 350, heroCardHeight = 140,heroImageWidth = 95,heroImageHeight = 100, healthBarWidth = 170, actionsBarWidth = 120,iconSize =13; ; 
	private static StackPane[][] cells = new StackPane[15][15];
	private Image invisible, empty, vaccineModel = Character.LoadModel("vaccine"),
			vaccineIcon = Hero.loadIcon("vaccine"), supplyModel = Character.LoadModel("supply"),
			supplyIcon = Hero.loadIcon("supply"), actionIcon = Hero.loadIcon("action"), healthIcon = Hero.loadIcon("health");

	/**
	 * The method called by the Main class to get the game scene. It creates a Scene
	 * object with all the required elements and logic of the game
	 * 
	 * @return the finished game scene object
	 */
	public Scene getScene() {
		try {
			invisible = new Image(new File("assets/" + Game.mode + "/images/wallpapers/" + "invisible" + ".png").toURI()
					.toURL().toExternalForm());
			empty = new Image(new File("assets/" + Game.mode + "/images/wallpapers/" + "empty" + ".jpg").toURI().toURL()
					.toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		StackPane back = new StackPane();
		String path = "assets/" + Game.mode + "/images/wallpapers/secondscene.jpeg";
		Image i = null;
		try {
			i = new Image(new File(path).toURI().toURL().toExternalForm());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ImageView wallpaper = new ImageView(i);
		wallpaper.setFitWidth(1920);
		wallpaper.setFitHeight(1080);
		back.getChildren().add(wallpaper);
		//back.setStyle("-fx-background-color : black;");
		BorderPane root = new BorderPane();
		//root.setBackground(new BackgroundImage(empty, null, null, null, null));
		back.getChildren().add(root);
		Scene gameScene = new Scene(back);

		grid = new GridPane();
		grid.setStyle("-fx-background-color : transparent;");
		grid.setAlignment(Pos.CENTER);
		createGrid();
		updateGrid();

		Heroes = new VBox(10);
		
		// Heroes.setBackground(new background);
		Heroes.setStyle("-fx-background-color : transparent;");
		updateHeroesStack();

		updates = new Label();
		updates.setMinSize(heroCardWidth, updatesHeight);
		// updates.setText("Heroes can not attack each other!");
		updates.setStyle("-fx-font-size: " + bottomFont + ";");
		StackPane bottom = new StackPane();
		updates.setAlignment(Pos.CENTER);
		ft = new FadeTransition(Duration.millis(1000), updates);
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.setCycleCount(2);
		ft.setAutoReverse(true);

		BorderPane menu = new BorderPane();
		bottom.getChildren().add(updates);
		bottom.setStyle("-fx-background-color: transparent;");
		menu.setBottom(bottom);
		menu.setLeft(Heroes);
		root.setCenter(grid);
		root.setLeft(menu);

		gameScene.setOnKeyPressed(e -> Keyboardcontrols(e));
		grid.setOnMouseClicked(e -> mouseControls(e));

		gameScene.widthProperty().addListener((obs, OldWidth, newWidth) -> resizeWidth(obs, OldWidth, newWidth));
		gameScene.heightProperty().addListener((obs, OldHeight, newHeight) -> resizeHeight(obs, OldHeight, newHeight));
		gameScene.getStylesheets().add(this.getClass().getResource(Game.mode + ".css").toExternalForm());
		return gameScene;
	}

	/**
	 * handles the game controls. W, A, S, D for movement Q for cure E for using the
	 * special action and R for ending the turn.
	 */
	private void Keyboardcontrols(KeyEvent e) {
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
				int i = 0, j = 0;
				if (currentHero.getTarget() != null) {
					i = currentHero.getTarget().getLocation().x;
					j = currentHero.getTarget().getLocation().y;
				}
				currentHero.useSpecial();
				if (currentHero instanceof Medic) {
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
		case R -> {
			Game.endTurn();
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

		updateScene();
		if (Game.checkGameOver())
			grid.fireEvent(new GameEvent(GameEvent.GAME_OVER));
		else if (Game.checkWin())
			grid.fireEvent(new GameEvent(GameEvent.WIN));

	}

	/**
	 * handles mouse inputs. Left click for attack
	 */
	private void mouseControls(MouseEvent e) {
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
			updateScene();
			if (Game.checkGameOver())
				grid.fireEvent(new GameEvent(GameEvent.GAME_OVER));
			else if (Game.checkWin())
				grid.fireEvent(new GameEvent(GameEvent.WIN));
		}
		// System.out.println(sce);
	}

	/**
	 * 
	 */
	private void updateScene() {
		updateGrid();
		updateHeroesStack();
	}

	/**
	 * Initializes all the grid cells.
	 */
	private void createGrid() {
		Cell map[][] = Game.map;
		grid.getChildren().clear();
		grid.setPadding(new Insets(5));
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
	 */
	private void updateGrid() {
		Cell map[][] = Game.map;
		for (int i = map.length - 1; i >= 0; i--) {
			for (int j = map[i].length - 1; j >= 0; j--) {
				cells[i][j].getChildren().remove(1);
				ImageView content = new ImageView();
				cells[i][j].setOnMouseClicked(null);
				if (map[i][j].isVisible()) {
					if (map[i][j] instanceof CharacterCell) {
						Character c = ((CharacterCell) map[i][j]).getCharacter();
						content.setFitWidth(cellWidth);
						content.setFitHeight(cellHeight);
						if (c != null)
							content.setImage(c.getModel());
						cells[i][j].setOnMouseClicked(e -> currentHero.setTarget(c));

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
					content.setFitWidth(cellWidth * 1.05);
					content.setFitHeight(cellHeight * 1.05);
				}
				cells[i][j].getChildren().add(1, content);
			}
		}

	}

	private StackPane base() {
		StackPane res = new StackPane();
		ImageView base = new ImageView(empty);
		base.setFitHeight(cellHeight * 1.05);
		base.setFitWidth(cellWidth * 1.05);
		res.getChildren().addAll(base, new Rectangle(), new Rectangle(cellWidth, cellHeight, Color.TRANSPARENT));
		res.setPrefSize(cellWidth, cellHeight);
		// res.setBackground(Background.fill(Color.BLUE));
		res.setMinHeight(cellHeight);
		res.setMaxHeight(cellHeight);
		res.setMinWidth(cellWidth);
		res.setMaxWidth(cellWidth);
		return res;
	}

	// create a stack of hero cards
	private void updateHeroesStack() {
		ArrayList<Hero> h = Game.heroes;
		VBox stack = Heroes;

		stack.getChildren().clear();
		for (Hero x : h) {
			StackPane card = heroCard(x);
			stack.getChildren().add(card);
		}

	}

	// create a card for a hero
	private StackPane heroCard(Hero h) {
		HBox card = new HBox();
		card.setMinHeight(heroCardHeight);
		card.setMaxHeight(heroCardHeight);
		card.setPrefHeight(heroCardHeight);
		
		card.setMinWidth(heroCardWidth);
		card.setMaxWidth(heroCardWidth);
		card.setPrefWidth(heroCardWidth);
		
		
	
		if (h.equals(currentHero))
			card.setId("CurrentHero");
		else
			card.setId("OtherHero");

		card.setSpacing(3);

		StackPane result = new StackPane();
		Rectangle frame = new Rectangle(heroCardWidth, heroCardHeight);
		frame.setFill(Color.TRANSPARENT);
		result.getChildren().add(frame);
		result.getChildren().add(card);

		// VBox to contian the hero info
		VBox info = new VBox();
		info.setTranslateY(10);

		// Getting the hero info
		String name = h.getName();
		int currentHp = h.getCurrentHp();
		int noSupplies = h.getSupplyInventory().size();
		int noVaccines = h.getVaccineInventory().size();
		int actionsAvailable = h.getActionsAvailable();
		boolean usedSpeacialAction = h.isSpecialAction();

		///// Setting the health bar.
		
		double maxHp = h.getMaxHp();
//		double iconSize = 12;
		GridPane healthBar = bar("healthBar", healthBarWidth, currentHp, maxHp, iconSize,
				healthIcon);

		///// Setting the action points bar
		
		double maxActions = h.getMaxActions();
		GridPane actionsBar = bar("actionsBar", actionsBarWidth, actionsAvailable, maxActions, iconSize,
				actionIcon);

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

		info.setSpacing(2);
		info.getChildren().addAll(healthBar, actionsBar, collectibles);

		// getting the hero image
		VBox img = heroImage(h, type + " " + name);
		img.setAlignment(Pos.TOP_LEFT);

		card.getChildren().addAll(img, info);

		// setting a listener to the card
		result.setOnMouseEntered(e -> {
			card.setId("CurrentHero");
		});
		result.setOnMouseExited(e -> {
			if (h != currentHero) {
				card.setId("OtherHero");
			}

		});
		result.setOnMouseClicked(e -> {

			currentHero = h;
			updateHeroesStack();

		});
		return result;

	}

	private VBox heroImage(Hero h, String txt) {
		StackPane photo = new StackPane();
		ImageView imageView = new ImageView(h.getIcon());
		
		imageView.setFitHeight(heroImageHeight);
		imageView.setFitWidth(heroImageWidth);

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
	
	private GridPane collectibles(int supplies, int vaccines) {
		GridPane grid = new GridPane();
		grid.setHgap(20);
		StackPane vaccine = icon(iconSize, vaccineIcon);
		StackPane supply = icon(iconSize, supplyIcon);

		Label noVaccines = new Label();
		noVaccines.setText("  " + vaccines);

		// vaccineIcon.getChildren().add(noVaccines);

		Label noSupplies = new Label();
		noSupplies.setText("     " + supplies);

		// supplyeIcon.getChildren().add(noSupplies);

		grid.add(vaccine, 0, 0);
		grid.add(noVaccines, 1, 0);
		grid.add(supply, 2, 0);
		grid.add(noSupplies, 3, 0);
		return grid;
	}

	private StackPane icon(double r, Image icon) {
		StackPane res = new StackPane();
		Circle circle = new Circle(r);
		circle.setCenterX(r);
		circle.setCenterY(r);

		try {
			ImageView imageView = new ImageView(icon);
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

	private GridPane bar(String type, double width, double current, double max, double iconSize, Image i) {
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

		StackPane icon = icon(iconSize, i);

		res.add(bar, 1, 0);
		res.add(icon, 0, 0);
		return res;

	}

	private void resizeHeight(ObservableValue<? extends Number> obs, Number oldHeight, Number newHeight) {
		double scale = (double) newHeight;
		scale /= 860;
		cellHeight = CELLHEIGHT * scale;
		updatesHeight = UPDATESHEIGHT * scale;

		// updatesWidth = UPDATESWIDTH * scale;
		heroCardHeight = scale * HEROCARDHEIGHT;
		
		heroImageHeight = scale * HEROIMAGEHEIGHT;
		iconSize = scale * ICONSIZE ;		

		bottomFont = scale * BOTTOMFONT;
		bottomFont = Math.max(bottomFont, 0.4 * BOTTOMFONT);
		updates.setMinHeight(updatesHeight);
		updates.setMaxHeight(updatesHeight);
		updates.setPrefHeight(updatesHeight);
		updates.setStyle("-fx-font-size: " + bottomFont + ";");
		createGrid();
		updateScene();

		// System.out.println("height " + updatesHeight);
		// System.out.println("old height : " + oldHeight + "new height: " + newHeight);

	}

	private void resizeWidth(ObservableValue<? extends Number> obs, Number oldWidth, Number newWidth) {
		double scale = (double) newWidth;
		scale /= 1520;
		cellWidth = CELLWIDTH * scale;
		heroCardWidth = HEROCARDWIDTH * scale;
		heroImageWidth = scale * HEROIMAGEWIDTH;
		healthBarWidth = scale * HEALTHBARWIDTH;
		actionsBarWidth = scale * ACTIONBARWIDTH;
		bottomFont = scale * cellHeight / CELLHEIGHT * bottomFont;
		bottomFont = Math.max(bottomFont, 0.4 * BOTTOMFONT);
		updates.setMinWidth(heroCardWidth);
		updates.setMaxWidth(heroCardWidth);
		updates.setPrefWidth(heroCardWidth);
		updates.setStyle("-fx-font-size: " + bottomFont + ";");
		createGrid();
		updateScene();

		// System.out.println("width " + updatesWidth);
		// System.out.println("old width : " + oldWidth + "new width: " + newWidth);

	}
}
