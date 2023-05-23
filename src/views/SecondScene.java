package views;

import java.util.ArrayList;

import engine.Game;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import model.characters.Hero;

/**
 * class dealing with the Second Scene. it has attributes :
 * <ul>
 * <li>wallpaper which represents the wallpaper used in this scene</li>
 * <li>selectYourHero label which displays "select your hero" message</li>
 * <li>row and column represent the location of the currently selected hero</li>
 * <li>dimension1 and dimension2 are the dimensions of the grid (used for
 * testing)</li>
 * <li>mapPane is a matrix contains all the stackPanes used</li>
 * </ul>
 * 
 * @author Ahmed Hussein
 *
 */

public class SecondScene {

	private static ImageView wallpaper;
	private static Label selectYourHero;
	private static int row = 0;
	private static int column = 0;
	private static int dimension1 = 4;
	private static int dimension2 = 2;
	private static StackPane[][] mapPane = new StackPane[dimension1][dimension2];
	private static VBox heroInfo ;
	private static Color brightColor = Color.DARKGRAY.brighter();
	private static Color darkColor = Color.DARKGRAY;
	private static String mode;

	public SecondScene(String mode) {
		this.mode = mode;
	}

	/**
	 * the controller method in the class.
	 * 
	 * {@link createWallpaper} {@link createSelectYourHeroLabel}
	 * {@link createGridAndMap} {@link createScene}
	 * 
	 * @return the secondScene
	 */

	public Scene getScene() {

		createWallpaper();

		// gridPane
		GridPane gridPane = new GridPane();
		gridPane.setHgap(4);
		gridPane.setVgap(4);
		gridPane.setAlignment(Pos.BOTTOM_CENTER);
		gridPane.setTranslateY(0.14 * Main.height);

		// map
		Hero[][] map = new Hero[dimension1][dimension2];

		createSelectYourHeroLabel();
		createGridAndMap(map, gridPane);
		
		// VBox
		heroInfo = new VBox();
		
		// BorderPane 
		BorderPane borderPane = new BorderPane();
		borderPane.setLeft(heroInfo);
		borderPane.setRight(gridPane);
		borderPane.setTop(selectYourHero);

		// the root of the scene
		StackPane root = new StackPane();
		root.getChildren().addAll(wallpaper, borderPane);

		return createScene(root, map);
	}

	/**
	 * A helper method that create a stackPane containing the hero image.
	 * 
	 * 
	 * @param h the hero
	 * @param x the x_location of the stackPane on the grid
	 * @param y the y_location of the stackPane on the grid
	 * @return stackPane
	 */

	private static StackPane hero(Hero h, int x, int y) {
		StackPane res = new StackPane();
		
		Rectangle back = new Rectangle(100, 100);
		back.setArcHeight(10);
		back.setArcWidth(10);
		back.setFill(darkColor);
		res.getChildren().add(back);
		
		ImageView model = new ImageView(h.getModel());
		
		res.setOnMouseEntered(e -> {
			((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(darkColor);
			mapPane[row][column].setTranslateY(0.0009 * Main.height);
			back.setFill(brightColor);
			row = x;
			column = y;
			res.setTranslateY(-0.14 * Main.height);
		});
		res.setOnMouseExited(e -> {
			back.setFill(darkColor);
			res.setTranslateY(0.0009 * Main.height);
		});
		res.setOnMouseClicked(e -> {
			Game.startGame(h , mode);
			Main.window.setScene((new GameScene()).gameScene());
		});
		return res;
	}

	/**
	 * An initializer method
	 * <p>
	 * it creates the grid that contains all the heroes images and the map that
	 * contains the heroes in the same location as the grid so we can have direct
	 * access to them.
	 * </p>
	 * 
	 * @param map
	 * @param gridPane
	 */

	private static void createGridAndMap(Hero[][] map, GridPane gridPane) {
		ArrayList<Hero> allHeroes = Game.availableHeroes;
		int count = 0;
		for (int i = 0; i < dimension1; i++) {
			for (int j = 0; j < dimension2; j++) {
				map[i][j] = allHeroes.get(count);
				StackPane tmp = hero(allHeroes.get(count++), i, j);
				gridPane.add(tmp, j, i);
				mapPane[i][j] = tmp;
			}
		}
	}

	/**
	 * An initializer method
	 * <p>
	 * initializes and creates the wallpaper imageView and give it the proper
	 * dimensions and gives it an initial value.
	 * </p>
	 */

	private static void createWallpaper() {
		wallpaper = new ImageView();
		wallpaper.setFitHeight(Main.height);
		wallpaper.setFitWidth(Main.width);
		wallpaper.setImage(Game.availableHeroes.get(0).getWallpaper());
	}

	/**
	 * An initializer method.
	 * <p>
	 * creates the selectYourHero label and its animation.
	 * </p>
	 */

	private static void createSelectYourHeroLabel() {
		selectYourHero = new Label("Select Your Hero");
		selectYourHero.setId("SelectHeroLabel");
		Timeline timeLine = new Timeline(
				new KeyFrame(Duration.seconds(0), new KeyValue(selectYourHero.opacityProperty(), 0)),
				new KeyFrame(Duration.seconds(1), new KeyValue(selectYourHero.opacityProperty(), 1)),
				new KeyFrame(Duration.seconds(2), new KeyValue(selectYourHero.opacityProperty(), 0)));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
	}

	/**
	 * A helper method that creates a VBox containing the image of the hero and all
	 * attributes
	 * 
	 * @param h the hero we want to get its VBox
	 * @return VBox of hero's information
	 */

	private static VBox getHeroCell(Hero h) {
		ImageView image = new ImageView(h.getImage());
		image.setFitHeight(80);
		image.setFitWidth(90);
		String heroName = h.getName().split(" ").length == 1 ? h.getName() : h.getName().split(" ")[0];
		Label name = new Label(heroName);
		name.setId("NameLabel");
		Label type = new Label(h.getType());
		Label attackDmg = new Label("Attack Damage : " + h.getAttackDmg());
		Label actionPoints = new Label("Action Points : " + h.getMaxActions());
		Label health = new Label("Health Points : " + h.getMaxHp());
		VBox res = new VBox(3);
		res.getChildren().addAll(image, name, type, attackDmg, health, actionPoints);
		return res;
	}

	/**
	 * A helper method used to create the scene and its actions.
	 * 
	 * 
	 * @param root the stackPane containing all items
	 * @param map  contains all heros
	 * @return the final scene
	 */

	private Scene createScene(StackPane root, Hero[][] map) {
		Scene scene = new Scene(root, Main.width, Main.height);
		mapPane[0][0].setTranslateY(-0.14 * Main.height);
		((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(brightColor);
		scene.setOnKeyPressed(e -> {
			((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(darkColor);
			KeyCode key = e.getCode();
			boolean isValid = key == KeyCode.W || key == KeyCode.A || key == KeyCode.S || key == KeyCode.D;
			if (isValid)
				mapPane[row][column].setTranslateY(0.0009 * Main.height);
			switch (e.getCode()) {
			case W -> {
				row = Math.max(row - 1, 0);
			}
			case A -> {
				column = Math.max(column - 1, 0);
			}
			case S -> {
				row = Math.min(row + 1, dimension1 - 1);
			}
			case D -> {
				column = Math.min(column + 1, dimension2 - 1);
			}
			case ENTER -> {
				Game.startGame(map[row][column]);
				Main.window.setScene((new GameScene()).gameScene());
			}
			}
			if (isValid)
				mapPane[row][column].setTranslateY(-0.14 * Main.height);
			((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(brightColor);
			wallpaper.setImage(map[row][column].getWallpaper());
		});

		scene.widthProperty().addListener((observable, oldWidth, newWidth) -> {
			Main.width = (double) newWidth;
			wallpaper.setFitWidth((double) newWidth);
			double newSize = 30 * Math.min(Main.height, Main.width) / 720;
			selectYourHero.setStyle("-fx-font-size: " + newSize + ";");
		});

		scene.heightProperty().addListener((observable, oldHeight, newHeight) -> {
			Main.height = (double) newHeight;
			wallpaper.setFitHeight((double) newHeight);
			double newSize = 30 * Math.min(Main.height, Main.width) / 720;
			selectYourHero.setStyle("-fx-font-size: " + newSize + ";");
		});

		scene.getStylesheets().add(getClass().getResource("classic.css").toExternalForm());

		return scene;
	}

}