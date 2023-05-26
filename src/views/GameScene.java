package views;

import java.io.File;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
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
	// private BorderPane root;
	private GridPane grid;
	private Label updates;
	FadeTransition ft;
	final private static double SCENEWIDTH = 860, SCENEHEIGHT = 1520, CELLHEIGHT = 55, CELLWIDTH = 76, BOTTOMFONT = 18,
			UPDATESHEIGHT = 35, HEROCARDWIDTH = 350, HEROCARDHEIGHT = 125, HEROIMAGEWIDTH = 85, HEROIMAGEHEIGHT = 90,
			HEALTHBARWIDTH = 170, ICONHEIGHT = 20, ICONWIDTH = 20, DIVWIDTH = 13, DIVHEIGHT = 10;

	private double cellHeight = CELLHEIGHT, cellWidth = CELLWIDTH, bottomFont = BOTTOMFONT,
			updatesHeight = UPDATESHEIGHT, heroCardWidth = HEROCARDWIDTH, heroCardHeight = HEROCARDHEIGHT,
			heroImageWidth = HEROIMAGEWIDTH, heroImageHeight = HEROIMAGEHEIGHT, healthBarWidth = HEALTHBARWIDTH,
			iconHeight = ICONHEIGHT, iconWidth = ICONWIDTH, divWidth = DIVWIDTH, divHeight = DIVHEIGHT;
	private static StackPane[][] cells = new StackPane[15][15];
	private Image invisible, empty, vaccineModel, vaccineIcon, supplyModel, supplyIcon, actionIcon, healthIcon,
			attackDamageIcon;
	private MediaPlayer attackSound, cureSound, errorSound, explorerSound, fighterSound, hoverSound, medicSound,
			selectSound, supplySound, trapSound, vaccineSound;

	public GameScene() {
		try {
			invisible = new Image(new File("assets/" + Game.mode + "/images/wallpapers/" + "invisible" + ".png").toURI()
					.toURL().toExternalForm());
			empty = new Image(new File("assets/" + Game.mode + "/images/wallpapers/" + "empty" + ".png").toURI().toURL()
					.toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// loading icons
		vaccineModel = Character.LoadModel("vaccine");
		vaccineIcon = Hero.loadIcon("vaccine");
		supplyModel = Character.LoadModel("supply");
		supplyIcon = Hero.loadIcon("supply");
		actionIcon = Hero.loadIcon("action");
		healthIcon = Hero.loadIcon("health");
		attackDamageIcon = Hero.loadIcon("attackdamage");

		// loading sounds
		attackSound = new MediaPlayer(loadMedia("attack"));
		cureSound = new MediaPlayer(loadMedia("cure"));
		errorSound = new MediaPlayer(loadMedia("error"));
		explorerSound = new MediaPlayer(loadMedia("explorer"));
		fighterSound = new MediaPlayer(loadMedia("fighter"));
		hoverSound = new MediaPlayer(loadMedia("hover"));
		medicSound = new MediaPlayer(loadMedia("medic"));
		selectSound = new MediaPlayer(loadMedia("select"));
		supplySound = new MediaPlayer(loadMedia("supply"));
		trapSound = new MediaPlayer(loadMedia("trap"));
		vaccineSound = new MediaPlayer(loadMedia("vaccine"));
	}

	/**
	 * The method called by the Main class to get the game scene. It creates a Scene
	 * object with all the required elements and logic of the game
	 * 
	 * @return the finished game scene object
	 */
	public StackPane getRoot() {
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
		// back.setStyle("-fx-background-color : black;");
		BorderPane root = new BorderPane();
		// root.setBackground(new BackgroundImage(empty, null, null, null, null));
		back.getChildren().add(root);

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

		root.setFocusTraversable(true);
		root.setOnKeyPressed(e -> Keyboardcontrols(e));
		root.setOnMouseClicked(e -> mouseControls(e));
		root.widthProperty().addListener((obs, OldWidth, newWidth) -> resizeWidth(obs, OldWidth, newWidth));
		root.heightProperty().addListener((obs, OldHeight, newHeight) -> resizeHeight(obs, OldHeight, newHeight));
		root.setMinHeight(0);
		root.setMinWidth(0);
		root.getStylesheets().add(this.getClass().getResource(Game.mode + ".css").toExternalForm());

		return back;
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
				cureSound.seek(Duration.ZERO);
				cureSound.play();
				animate(i, j, Color.BLUE);
			} catch (GameActionException e1) {
				display(e1.getMessage());
				errorSound.seek(Duration.ZERO);
				errorSound.play();
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
				animate(currentHero.getLocation().x, currentHero.getLocation().y, Color.ORANGE);
				display("Activated special action.");
				if (currentHero instanceof Medic) {
					medicSound.seek(Duration.ZERO);
					medicSound.play();
					animate(i, j, Color.GREEN);
				} else if (currentHero instanceof Fighter) {
					fighterSound.seek(Duration.ZERO);
					fighterSound.play();
				} else {
					explorerSound.seek(Duration.ZERO);
					explorerSound.play();
				}
			} catch (GameActionException e1) {
				display(e1.getMessage());
				errorSound.seek(Duration.ZERO);
				errorSound.play();
			}
		}
		case R -> {
			Game.endTurn();
			selectSound.seek(Duration.ZERO);
			selectSound.play();
		}
		}

		if (d != null) {
			int type = -1;
			try {
				type = currentHero.move(d);
				switch (type) {
				case 1 -> {
					display("Trap cell!");
					trapSound.seek(Duration.ZERO);
					trapSound.play();
					animate(currentHero.getLocation().x, currentHero.getLocation().y, Color.RED);
				}
				case 2 -> {
					supplySound.seek(Duration.ZERO);
					supplySound.play();
				}
				case 3 -> {
					vaccineSound.seek(Duration.ZERO);
					vaccineSound.play();
				}
				}

			} catch (GameActionException e1) {
				display(e1.getMessage());
				errorSound.seek(Duration.ZERO);
				errorSound.play();
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
				attackSound.seek(Duration.ZERO);
				attackSound.play();
				animate(i, j, Color.RED);
			} catch (GameActionException e1) {
				display(e1.getMessage());
				errorSound.seek(Duration.ZERO);
				errorSound.play();
			}
			updateScene();
			if (Game.checkGameOver())
				grid.fireEvent(new GameEvent(GameEvent.GAME_OVER));
			else if (Game.checkWin())
				grid.fireEvent(new GameEvent(GameEvent.WIN));
		}
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
				cells[i][j].setStyle("-fx-border-width: 50; -fx-border-color: red");
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
			BorderPane card = heroCard(x);
			stack.getChildren().add(card);
		}

	}

	// create a card for a hero
	private BorderPane heroCard(Hero h) {

		BorderPane result = new BorderPane();
		result.setPrefSize(heroCardWidth, heroCardHeight);
		result.setMinHeight(heroCardHeight);
		result.setMaxHeight(heroCardHeight);
		result.setMinWidth(heroCardWidth);
		result.setMaxWidth(heroCardWidth);

		HBox card = new HBox();

		card.setAlignment(Pos.TOP_LEFT);
		card.setPrefSize(heroCardWidth, heroCardHeight * 0.8);
		card.setSpacing(5);

		card.setMinHeight(heroCardHeight * 0.8);
		card.setMaxHeight(heroCardHeight * 0.8);

		card.setMinWidth(heroCardWidth);
		card.setMaxWidth(heroCardWidth);

		if (h.equals(currentHero))
			result.setId("CurrentHero");
		else
			result.setId("OtherHero");

		result.setTop(card);

		// Getting the hero info
		String name = h.getName();
		String type = "Balabizak yasta";
		if (h instanceof Medic) {
			type = "Medic";
		} else if (h instanceof Fighter) {
			type = " Fighter";
		} else {
			type = "Explorer";
		}
		int currentHp = h.getCurrentHp();
		int noSupplies = h.getSupplyInventory().size();
		int noVaccines = h.getVaccineInventory().size();
		int actionsAvailable = h.getActionsAvailable();

		int attackDmg = h.getAttackDmg();

		// VBox to contian the hero info
		VBox info = new VBox();
		info.setSpacing(3);
		info.setTranslateY(7);

		// Setting the health bar.
		double maxHp = h.getMaxHp();
		GridPane healthBar = createHealthBar(currentHp, maxHp);

		// Setting the action points bar
		double maxActions = h.getMaxActions();
		GridPane actionsBar = createActionPintsBar(actionsAvailable);

		// Setting the collictibleview
		HBox collectibles = collectibles(noSupplies, noVaccines, attackDmg);

		// Setting the attackDamge view

		Label heroType = new Label(type);
		heroType.setPrefHeight(heroCardHeight * 0.1);
		heroType.setAlignment(Pos.BOTTOM_CENTER);
		heroType.setStyle("-fx-alignment:center;");
		heroType.setPadding(new Insets(5));
		heroType.setStyle(" -fx-alignment:center;-fx-font-size: " + (bottomFont * 0.7));

		info.getChildren().addAll(healthBar, actionsBar, collectibles, heroType);

		// getting the hero image
		VBox img = heroImage(h);
		img.setAlignment(Pos.TOP_LEFT);

		card.getChildren().addAll(img, info);

		// setting a listener to the card
		result.setOnMouseEntered(e -> {
			result.setId("CurrentHero");
		});
		result.setOnMouseExited(e -> {
			if (h != currentHero) {
				result.setId("OtherHero");
			}

		});
		result.setOnMouseClicked(e -> {

			currentHero = h;
			updateHeroesStack();

		});

		Label Name = new Label(name);
		Name.setPrefHeight(heroCardHeight * 0.1);
		Name.setPadding(new Insets(5));
		Name.setStyle(" -fx-alignment:center;-fx-font-size: " + (bottomFont * 0.7));

		img.getChildren().add(Name);
		return result;

	}

	private VBox heroImage(Hero h) {
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

		VBox res = new VBox(10);
		res.getChildren().addAll(photo);
		return res;
	}

	private HBox collectibles(int supplies, int vaccines, int attackDmg) {
		HBox res = new HBox(heroCardWidth * 0.05);
		res.setAlignment(Pos.CENTER);
		StackPane vaccine = icon(vaccineIcon);
		StackPane supply = icon(supplyIcon);
		StackPane attack = icon(attackDamageIcon);

		Label noVaccines = new Label();
		noVaccines.setText("  " + vaccines);

		Label noSupplies = new Label();
		noSupplies.setText("  " + supplies);

		Label attackDamage = new Label();
		attackDamage.setText("  " + attackDmg);

		res.getChildren().addAll(attack, attackDamage, vaccine, noVaccines, supply, noSupplies);

		return res;
	}

	private StackPane icon(Image icon) {
		StackPane res = new StackPane();
		Rectangle rec = new Rectangle(iconWidth, iconHeight);

		try {
			ImageView imageView = new ImageView(icon);
			imageView.setFitWidth(iconWidth);
			imageView.setFitHeight(iconHeight);
			imageView.setClip(rec);
			res.getChildren().add(imageView);

		} catch (Exception e) {
			System.out.println("Missing image");
			rec.setFill(Color.BLACK);
			res.getChildren().add(rec);

		}
		return res;
	}

	private GridPane createHealthBar(double current, double max) {
		GridPane res = new GridPane();
		res.setHgap(healthBarWidth * 0.05);
		ProgressBar bar = new ProgressBar();
		bar.setProgress(current / max);

		StackPane icon = icon(healthIcon);

		String color = "Balabizo";
		if (current >= 0.75 * max)
			color = "-fx-accent: green;";
		else if (current >= 0.5 * max)
			color = "-fx-accent: yellow;";
		else if (current >= 0.25 * max)
			color = "-fx-accent: orange;";
		else
			color = "-fx-accent: red;";

		bar.setStyle("-fx-padding: 2px; -fx-background-insets: 2px;-fx-pref-height: 23px;" + color);
		bar.setPrefWidth(healthBarWidth);
		res.add(bar, 1, 0);
		res.add(icon, 0, 0);
		return res;

	}

	private GridPane createActionPintsBar(int x) {
		GridPane res = new GridPane();
		res.setHgap(healthBarWidth * 0.07);
		HBox bar = new HBox(2);
		bar.setAlignment(Pos.CENTER);
		// bar.setStyle("-fx-border-color:red;");

		while (x-- > 0) {
			Rectangle rec = new Rectangle(divWidth, divHeight);
			rec.setStyle(
					"-fx-fill: linear-gradient(to bottom, rgba(0, 0, 255, 0.5), rgba(0, 0, 255, 1)); -fx-border-radius:5px;-fx-backgound-radius:5px;");
			bar.getChildren().add(rec);
		}
		StackPane icon = icon(actionIcon);
		res.add(bar, 1, 0);
		res.add(icon, 0, 0);
		return res;

	}

	private void resizeHeight(ObservableValue<? extends Number> obs, Number oldHeight, Number newHeight) {
		double scale = (double) newHeight;
		scale /= SCENEWIDTH;
		DecimalFormat d = new DecimalFormat("#.##");
		scale = Double.parseDouble(d.format(scale));

		cellHeight = CELLHEIGHT * scale;
		updatesHeight = UPDATESHEIGHT * scale;
		heroCardHeight = scale * HEROCARDHEIGHT;
		heroImageHeight = scale * HEROIMAGEHEIGHT;
		iconHeight = scale * ICONHEIGHT;
		divHeight = DIVHEIGHT * scale;
		bottomFont = Math.max(scale, 0.8) * BOTTOMFONT;
		bottomFont = Math.max(bottomFont, 0.4 * BOTTOMFONT);
		updates.setMinHeight(updatesHeight);
		updates.setMaxHeight(updatesHeight);
		updates.setPrefHeight(updatesHeight);
		updates.setStyle("-fx-font-size: " + bottomFont + ";");
		createGrid();
		updateScene();
	}

	private void resizeWidth(ObservableValue<? extends Number> obs, Number oldWidth, Number newWidth) {
		double scale = (double) newWidth;
		scale /= SCENEHEIGHT;
		DecimalFormat d = new DecimalFormat("#.##");
		scale = Double.parseDouble(d.format(scale));

		cellWidth = CELLWIDTH * scale;
		heroCardWidth = HEROCARDWIDTH * scale;
		heroImageWidth = scale * HEROIMAGEWIDTH;
		healthBarWidth = scale * HEALTHBARWIDTH;
		divWidth = DIVWIDTH * scale;
		iconWidth = ICONWIDTH * scale;
		bottomFont = Math.max(scale, 0.8) * BOTTOMFONT;
		bottomFont = Math.max(bottomFont, 0.4 * BOTTOMFONT);
		updates.setMinWidth(heroCardWidth);
		updates.setMaxWidth(heroCardWidth);
		updates.setPrefWidth(heroCardWidth);
		updates.setStyle("-fx-font-size: " + bottomFont + ";");
		createGrid();
		updateScene();
	}

	private Media loadMedia(String name) {
		String path = "assets/" + Game.mode + "/audio/effects/" + name + ".wav";
		Media res = new Media(new File(path).toURI().toString());
		return res;
	}

	private void animate(int i, int j, Color c) {
		Rectangle rect = (Rectangle) cells[i][j].getChildren().get(2);
		rect.setFill(c);
		FadeTransition ft1 = new FadeTransition(Duration.millis(150), rect);
		ft1.setFromValue(0);
		ft1.setToValue(0.3);
		ft1.setCycleCount(2);
		ft1.setAutoReverse(true);
		ft1.play();
	}

	private void display(String s) {
		updates.setText(s);
		ft.play();
	}
}
