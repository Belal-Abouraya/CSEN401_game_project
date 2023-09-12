package views;

import java.awt.Point;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import engine.Game;
import exceptions.GameActionException;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.characters.Character;
import model.characters.Direction;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import model.characters.Zombie;
import model.collectibles.Collectible;
import model.collectibles.Supply;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;

/**
 * A class that renders the main game scene. It handles taking input from the
 * user, doing the appropriate game actions, displaying the results to the user
 * and handling all possilbe exceptions.
 * 
 * @author Belal Abouraya
 * @author Rafael Samuel
 */
public class GameScene {
	private Hero currentHero = Game.heroes.get(0);
	private VBox Heroes;

	private GridPane grid;
	private Label updates;
	FadeTransition ft;
	final public static double SCENEWIDTH = 1552, SCENEHEIGHT = 873;
	final private static double CELLHEIGHT = 55, CELLWIDTH = 76, BOTTOMFONT = 18, UPDATESHEIGHT = 35,
			HEROCARDWIDTH = 350, HEROCARDHEIGHT = 125, HEROIMAGEWIDTH = 85, HEROIMAGEHEIGHT = 90, HEALTHBARWIDTH = 170,
			ICONHEIGHT = 20, ICONWIDTH = 20, DIVWIDTH = 13, DIVHEIGHT = 10;
	private double cellHeight = CELLHEIGHT, cellWidth = CELLWIDTH, bottomFont = BOTTOMFONT,
			updatesHeight = UPDATESHEIGHT, heroCardWidth = HEROCARDWIDTH, heroCardHeight = HEROCARDHEIGHT,
			heroImageWidth = HEROIMAGEWIDTH, heroImageHeight = HEROIMAGEHEIGHT, healthBarWidth = HEALTHBARWIDTH,
			iconHeight = ICONHEIGHT, iconWidth = ICONWIDTH, divWidth = DIVWIDTH, divHeight = DIVHEIGHT;
	private StackPane[][] cells = new StackPane[15][15];
	private Region heroBorder, targetBorder;
	private BorderPane root, menu;
	private ImageView wallpaper;
	private Image invisible, empty, vaccineModel, vaccineIcon, supplyModel, supplyIcon, actionIcon, healthIcon,
			attackDamageIcon;
	private MediaPlayer attackSound, cureSound, errorSound, explorerSound, fighterSound, hoverSound, medicSound,
			selectSound, supplySound, trapSound, vaccineSound, zombieSound;

	public GameScene() {
		loadAssets();
		root = new BorderPane();
		root.getChildren().add(wallpaper);
		grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setMinSize(0, 0);
		createGrid();
		updateGrid();

		createUpdates();

		createHeroesStack();
		updateHeroesStack();

		menu = new BorderPane();
		menu.setBottom(updates);
		menu.setLeft(Heroes);
		root.setCenter(grid);
		root.setLeft(menu);
		Platform.runLater(() -> root.requestFocus());
		root.setFocusTraversable(true);
		root.setOnKeyPressed(e -> Keyboardcontrols(e));
		grid.setOnMouseClicked(e -> mouseControls(e));
		root.widthProperty().addListener((obs, OldWidth, newWidth) -> resizeWidth(obs, OldWidth, newWidth));
		root.heightProperty().addListener((obs, OldHeight, newHeight) -> resizeHeight(obs, OldHeight, newHeight));
		root.setMinSize(0, 0);
	}

	private void loadAssets() {
		wallpaper = new ImageView(Main.loadImage("wallpapers/secondscene.jpeg"));
		wallpaper.setFitWidth(SCENEWIDTH);
		wallpaper.setFitHeight(SCENEHEIGHT);

		// loading icons
		vaccineModel = Character.LoadModel("vaccine");
		vaccineIcon = Hero.loadIcon("vaccine");
		supplyModel = Character.LoadModel("supply");
		supplyIcon = Hero.loadIcon("supply");
		actionIcon = Hero.loadIcon("action");
		healthIcon = Hero.loadIcon("health");
		attackDamageIcon = Hero.loadIcon("attackDamage");
		invisible = Hero.loadIcon("invisible");
		empty = Hero.loadIcon("empty");

		// loading sounds
		attackSound = Main.loadEffect("attack");
		cureSound = Main.loadEffect("cure");
		errorSound = Main.loadEffect("error");
		explorerSound = Main.loadEffect("explorer");
		fighterSound = Main.loadEffect("fighter");
		hoverSound = Main.loadEffect("hover");
		medicSound = Main.loadEffect("medic");
		selectSound = Main.loadEffect("select");
		supplySound = Main.loadEffect("supply");
		trapSound = Main.loadEffect("trap");
		vaccineSound = Main.loadEffect("vaccine");
		zombieSound = Main.loadEffect("zombie");
	}

	public BorderPane getRoot() {
		return root;
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
				Main.play(cureSound);
				animate(i, j, Color.BLUE);
			} catch (GameActionException e1) {
				handleException(e1);
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
					Main.play(medicSound);
					animate(i, j, Color.GREEN);
				} else if (currentHero instanceof Fighter) {
					Main.play(fighterSound);
				} else {
					Main.play(explorerSound);
				}
			} catch (GameActionException e1) {
				handleException(e1);
			}
		}
		case R -> {
			display("Zombies turn!");
			endTurn();
		}
		}

		if (d != null) {
			int type = -1;
			try {
				type = currentHero.move(d);
				switch (type) {
				case 1 -> {
					display("Trap cell!");
					Main.play(trapSound);
					animate(currentHero.getLocation().x, currentHero.getLocation().y, Color.RED);
				}
				case 2 -> {
					Main.play(supplySound);
				}
				case 3 -> {
					Main.play(vaccineSound);
				}
				}
			} catch (GameActionException e1) {
				handleException(e1);
			}
		}

		updateScene();
		if (Game.checkGameOver()) {
			cureSound.stop();
			zombieSound.stop();
			grid.fireEvent(new GameEvent(GameEvent.GAME_OVER));
		} else if (Game.checkWin()) {
			cureSound.stop();
			grid.fireEvent(new GameEvent(GameEvent.WIN));
		}

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
				Main.play(attackSound);
				animate(i, j, Color.RED);
			} catch (GameActionException e1) {
				handleException(e1);
			}
			updateScene();
			if (Game.checkGameOver())
				grid.fireEvent(new GameEvent(GameEvent.GAME_OVER));
			else if (Game.checkWin())
				grid.fireEvent(new GameEvent(GameEvent.WIN));
		}
	}

	private void updateScene() {
		updateGrid();
		updateHeroesStack();
	}

	private void createUpdates() {
		updates = new Label();
		updates.setMinSize(heroCardWidth, updatesHeight);
		updates.setPrefSize(heroCardWidth, updatesHeight);
		updates.setMaxSize(heroCardWidth, updatesHeight);
		updates.setStyle("-fx-font-size: " + bottomFont);
		updates.setAlignment(Pos.CENTER);
		ft = new FadeTransition(Duration.millis(700), updates);
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.setCycleCount(2);
		ft.setAutoReverse(true);
	}

	private void createGrid() {
		heroBorder = new Region();
		heroBorder.setMinSize(cellWidth, cellHeight);
		heroBorder.setPrefSize(cellWidth, cellHeight);
		heroBorder.setMaxSize(cellWidth, cellHeight);
		heroBorder.setId("hero");

		targetBorder = new Region();
		targetBorder.setMinSize(cellWidth, cellHeight);
		targetBorder.setPrefSize(cellWidth, cellHeight);
		targetBorder.setMaxSize(cellWidth, cellHeight);

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
						cells[i][j].setOnMouseClicked(e -> {
							if (e.getButton() == MouseButton.SECONDARY) {
								currentHero.setTarget(c);
								updateScene();
							}
						});
					}

					else if (map[i][j] instanceof CollectibleCell) {
						Collectible c = ((CollectibleCell) map[i][j]).getCollectible();
						Image model = null;
						if (c instanceof Supply) {
							model = supplyModel;
							content.setFitWidth(cellWidth / 2);
							content.setFitHeight(cellHeight / 2);
						} else {
							model = vaccineModel;
							content.setFitWidth(cellWidth);
							content.setFitHeight(cellHeight);
						}
						content.setImage(model);

					}
				} else {
					content.setImage(invisible);
					content.setFitWidth(cellWidth * 1.05);
					content.setFitHeight(cellHeight * 1.05);
				}
				cells[i][j].getChildren().add(1, content);
			}
		}

		int x = currentHero.getLocation().x, y = currentHero.getLocation().y;
		if (!cells[x][y].getChildren().contains(heroBorder))
			cells[x][y].getChildren().add(heroBorder);

		if (targetBorder.getParent() != null)
			((StackPane) targetBorder.getParent()).getChildren().remove(targetBorder);

		if (currentHero.getTarget() != null) {
			x = currentHero.getTarget().getLocation().x;
			y = currentHero.getTarget().getLocation().y;
			cells[x][y].getChildren().add(targetBorder);
			if (currentHero.getTarget() instanceof Zombie)
				targetBorder.setId("zombie-target");
			else
				targetBorder.setId("hero-target");
		}
	}

	private StackPane base() {
		StackPane res = new StackPane();
		ImageView base = new ImageView(empty);
		base.setFitHeight(cellHeight * 1.05);
		base.setFitWidth(cellWidth * 1.05);
		res.getChildren().addAll(base, new Rectangle(), new Rectangle(cellWidth, cellHeight, Color.TRANSPARENT));
		res.setPrefSize(cellWidth, cellHeight);
		res.setMinHeight(cellHeight);
		res.setMaxHeight(cellHeight);
		res.setMinWidth(cellWidth);
		res.setMaxWidth(cellWidth);
		return res;
	}

	private void createHeroesStack() {
		Heroes = new VBox(10);
		Heroes.setStyle("-fx-background-color : transparent;");

		for (int i = 0; i < 6; i++) {
			BorderPane result = new BorderPane();
			result.setPrefSize(heroCardWidth, heroCardHeight);
			result.setMinHeight(heroCardHeight);
			result.setMaxHeight(heroCardHeight);
			result.setMinWidth(heroCardWidth);
			result.setMaxWidth(heroCardWidth);
			result.setVisible(false);
			Heroes.getChildren().add(result);
		}
	}

	// create a stack of hero cards
	private void updateHeroesStack() {
		ArrayList<Hero> h = Game.heroes;
		VBox stack = Heroes;
		for (int i = 0; i < 6; i++) {
			Heroes.getChildren().get(i).setVisible(false);
		}
		for (int i = 0; i < h.size(); i++) {
			heroCard(h.get(i), (BorderPane) stack.getChildren().get(i));
		}
	}

	// create a card for a hero
	private void heroCard(Hero h, BorderPane result) {
		result.getChildren().clear();
		result.setPrefSize(heroCardWidth, heroCardHeight);
		result.setMinHeight(heroCardHeight);
		result.setMaxHeight(heroCardHeight);
		result.setMinWidth(heroCardWidth);
		result.setMaxWidth(heroCardWidth);
		result.setVisible(true);

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
		info.setTranslateY(5);

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
			Main.play(hoverSound);
		});
		result.setOnMouseExited(e -> {
			if (h != currentHero) {
				result.setId("OtherHero");
			}

		});
		result.setOnMouseClicked(e -> {
			Main.play(selectSound);
			currentHero = h;
			updateScene();
		});

		Label Name = new Label(name);
		Name.setPrefHeight(heroCardHeight * 0.1);
		Name.setPadding(new Insets(2));
		Name.setStyle("-fx-alignment:center;-fx-font-size: " + (bottomFont * 0.7));

		img.getChildren().add(Name);
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
		noVaccines.setStyle("-fx-font-size: " + bottomFont * 0.9);

		Label noSupplies = new Label();
		noSupplies.setText("  " + supplies);
		noSupplies.setStyle("-fx-font-size: " + bottomFont * 0.9);

		Label attackDamage = new Label();
		attackDamage.setText("  " + attackDmg);
		attackDamage.setStyle("-fx-font-size: " + bottomFont * 0.9);

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
		scale /= SCENEHEIGHT;
		DecimalFormat d = new DecimalFormat("#.##");
		scale = Double.parseDouble(d.format(scale));

		cellHeight = CELLHEIGHT * scale;
		updatesHeight = UPDATESHEIGHT * scale;
		heroCardHeight = scale * HEROCARDHEIGHT;
		heroImageHeight = scale * HEROIMAGEHEIGHT;
		iconHeight = scale * ICONHEIGHT;
		divHeight = DIVHEIGHT * scale;
		bottomFont = Math.max(scale, 0.3) * BOTTOMFONT;
		updates.setMinHeight(updatesHeight);
		updates.setMaxHeight(updatesHeight);
		updates.setPrefHeight(updatesHeight);
		updates.setStyle("-fx-font-size: " + bottomFont + ";");
		wallpaper.setFitHeight((double) newHeight);
		menu.setMinHeight((double) newHeight);
		menu.setPrefHeight((double) newHeight);
		menu.setMaxHeight((double) newHeight);

		createGrid();
		updateScene();
	}

	private void resizeWidth(ObservableValue<? extends Number> obs, Number oldWidth, Number newWidth) {
		double scale = (double) newWidth;
		scale /= SCENEWIDTH;
		DecimalFormat d = new DecimalFormat("#.##");
		scale = Double.parseDouble(d.format(scale));

		cellWidth = CELLWIDTH * scale;
		heroCardWidth = HEROCARDWIDTH * scale;
		heroImageWidth = scale * HEROIMAGEWIDTH;
		healthBarWidth = scale * HEALTHBARWIDTH;
		divWidth = DIVWIDTH * scale;
		iconWidth = ICONWIDTH * scale;
		bottomFont = Math.max(scale, 0.3) * BOTTOMFONT;
		updates.setMinWidth(heroCardWidth);
		updates.setMaxWidth(heroCardWidth);
		updates.setPrefWidth(heroCardWidth);
		updates.setStyle("-fx-font-size: " + bottomFont + ";");
		wallpaper.setFitWidth((double) newWidth);
		menu.setMinWidth(heroCardWidth);
		menu.setPrefWidth(heroCardWidth);
		menu.setMaxWidth(heroCardWidth);
		createGrid();
		updateScene();
	}

	private void animate(int i, int j, Color c) {
		animate(i, j, c, 150);
	}

	private void animate(int i, int j, Color c, int period) {
		Rectangle rect = (Rectangle) cells[i][j].getChildren().get(2);
		rect.setFill(c);
		FadeTransition ft1 = new FadeTransition(Duration.millis(period), rect);
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

	private void handleException(GameActionException e) {
		display(e.getMessage());
		Main.play(errorSound);
	}

	private void endTurn() {
		ArrayList<Point> locations = Game.endTurn();
		for (Point p : locations) {
			animate(p.x, p.y, Color.RED, 300);
		}
		if (locations.size() > 0) {
			root.setOnKeyPressed(null);
			grid.setOnMouseClicked(null);
			Main.play(zombieSound);

			Timer t = new Timer();
			t.schedule(new TimerTask() {

				@Override
				public void run() {
					root.setOnKeyPressed(e -> Keyboardcontrols(e));
					grid.setOnMouseClicked(e -> mouseControls(e));
				}
			}, 1000);
		}
	}
}
