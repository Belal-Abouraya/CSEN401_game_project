package views;

import java.io.IOException;
import java.util.ArrayList;

import engine.Game;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.characters.Hero;

/**
 * SecondScene is the class represents the second scene in the game.
 * The second scene is when the player is asked to choose his hero to start the game.
 * The class contains the images of all heroes and their abilities.
 * 
 * @author Ahmed Hussein
 *
 */

public class SecondScene {

	private StackPane root = new StackPane();
	private BorderPane borderPane = new BorderPane();
	private StackPane heroesContainer = new StackPane();
	private GridPane heroes = new GridPane();
	private int d1 = 4, d2 = 2;
	private int row = 0, column = 0;
	private Hero[][] map = new Hero[d1][d2];

	private Color brightColor = Color.DARKGRAY.brighter();
	private Color darkColor = Color.DARKGRAY;
	private StackPane[][] mapPane = new StackPane[d1][d2];

	private double RectangleWidth;
	private double RectangleHeight;
	private ImageView wallpaper;
	private ImageView model = new ImageView();
	private Label selectYourHero;
	private Label info = new Label();
	private VBox vbox = new VBox(Main.height / 3.7);
	private HBox button = new HBox(6);
	private Timeline timeLine = new Timeline();

	private MediaPlayer select = Main.loadEffect("select");
	private MediaPlayer hover = Main.loadEffect("hover");
	private double textSize = 23;
	private double factor = 23;

	public SecondScene() {
		try {
			Game.loadHeroes("assets/" + Game.mode + "/heroes.csv");
		} catch (IOException e1) {
		}
		if (Main.mediaPlayer.getStatus() != Status.PLAYING)
			Main.mediaPlayer.play();
		Main.mediaPlayer.setCycleCount(Timeline.INDEFINITE);
		createBackGround();
		createSelectYourHeroLabel();
		createHeroes();
		button = createButton("h", "Help", textSize, 28);

		root.getChildren().addAll(wallpaper, borderPane, selectYourHero, model);

		info.setId("info");
		info.setVisible(false);
		info.setMinSize(RectangleWidth * 2, RectangleHeight * 3);
		vbox.setAlignment(Pos.BOTTOM_LEFT);
		borderPane.setBackground(Background.fill(new Color(0, 0, 0, 0)));
		borderPane.setFocusTraversable(true);
		Platform.runLater(() -> heroes.requestFocus());

		model.setVisible(false);
		heroesContainer.getChildren().add(heroes);
		borderPane.setRight(heroesContainer);
		vbox.getChildren().addAll(info, button);
		borderPane.setLeft(vbox);
		root.setOnKeyPressed(e -> keyboardHandle(e));
		borderPane.widthProperty().addListener((observable, oldWidth, newWidth) -> {
			double nw = (double) newWidth;
			Main.width = nw;
			wallpaper.setFitWidth(nw + 20);
			updateLabelSize(selectYourHero, nw, Main.height, 30);
			model.setFitHeight((1920 / 3) * Math.pow(Main.height / 720, 0.85));
			model.setFitWidth((1480 / 3) * Math.sqrt(Main.width / 1280));
			RectangleHeight = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.98;
			RectangleWidth = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.98;
			updateMapWidth();
			updateLabel();
			updateVBox(button, textSize, factor);
		});
		borderPane.heightProperty().addListener((observable, oldHeight, newHeight) -> {
			double nh = (double) newHeight;
			vbox.setSpacing(Main.height / 3.7);
			Main.height = nh;
			wallpaper.setFitHeight(nh + 20);
			updateLabelSize(selectYourHero, Main.width, nh, 30);
			model.setFitHeight((1920 / 3) * Math.pow(Main.height / 720, 0.85));
			model.setFitWidth((1480 / 3) * Math.sqrt(Main.width / 1280));
			RectangleHeight = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.98;
			RectangleWidth = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.98;
			updateMapHeight();
			updateLabel();
			updateVBox(button, textSize, factor);
		});
	}

	public StackPane getRoot() {
		return root;
	}

	private void createBackGround() {
		wallpaper = new ImageView(Main.loadImage("wallpapers/secondscene.jpeg"));
		wallpaper.setFitHeight(Main.height);
		wallpaper.setFitWidth(Main.width);
	}

	private void createHeroes() {
		ArrayList<Hero> allHeroes = Game.availableHeroes;
		int count = 0;
		for (int i = 0; i < d1; i++) {
			for (int j = 0; j < d2; j++) {
				map[i][j] = allHeroes.get(count);
				StackPane tmp = hero(allHeroes.get(count++), i, j);
				heroes.add(tmp, j, i);
				mapPane[i][j] = tmp;
			}
		}
		heroes.setAlignment(Pos.CENTER_RIGHT);
		heroes.setTranslateX(-2);
		heroes.setHgap(2);
		heroes.setVgap(2);
	}

	static HBox createButton(String letter, String text, double textSize, double factor) {
		HBox button = new HBox(6);
		Image hKey = Hero.loadIcon(letter);
		ImageView imageView = new ImageView(hKey);
		double size = Math.min(Main.height, Main.width) / factor;
		imageView.setFitHeight(size);
		imageView.setFitWidth(size);
		Label label = new Label(text);
		label.setStyle("-fx-font-size : " + textSize + "px;");
		button.getChildren().addAll(imageView, label);
		return button;
	}

	static void updateVBox(HBox button, double prev, double factor) {
		double size = Math.min(Main.height, Main.width) / factor;
		((ImageView) button.getChildren().get(0)).setFitHeight(size);
		((ImageView) button.getChildren().get(0)).setFitWidth(size);
		updateLabelSize((Label) button.getChildren().get(1), Main.width, Main.height, prev);
	}

	private StackPane hero(Hero h, int x, int y) {
		StackPane res = new StackPane();
		RectangleHeight = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.98;
		RectangleWidth = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.98;
		Rectangle back = new Rectangle(RectangleWidth, RectangleHeight);
		back.setArcHeight(10);
		back.setArcWidth(10);
		back.setFill(darkColor);
		back.setOpacity(0.2);
		ImageView icon = new ImageView(h.getIcon());
		icon.setFitHeight(RectangleHeight - 5);
		icon.setFitWidth(RectangleWidth - 5);
		res.getChildren().addAll(back, icon);
		res.setOnMouseEntered(e -> mouseHandle(e, x, y));
		res.setOnMouseClicked(e -> {
			Game.startGame(h);
			Main.mediaPlayer.stop();
			select.play();
			Main.window.getScene().setRoot((new GameScene()).getRoot());
		});

		return res;
	}

	private void createSelectYourHeroLabel() {
		selectYourHero = new Label("Select Your Hero");
		selectYourHero.setId("SelectHeroLabel");
		Timeline timeLine = new Timeline(
				new KeyFrame(Duration.seconds(0), new KeyValue(selectYourHero.opacityProperty(), 0)),
				new KeyFrame(Duration.seconds(1), new KeyValue(selectYourHero.opacityProperty(), 1)),
				new KeyFrame(Duration.seconds(2), new KeyValue(selectYourHero.opacityProperty(), 0)));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
	}

	private String createInfo(Hero h) {
		return h.getName() + "\n" + "\n" + h.getType() + "\n" + "\n" + "Health : " + h.getMaxHp() + "\n"
				+ "Actions per Turn : " + h.getMaxActions() + "\n" + "Attack Damage : " + h.getAttackDmg();
	}

	static void updateLabelSize(Label label, double width, double height, double prev) {
		Platform.runLater(() -> {
			double size = prev * Math.sqrt((width * height) / (720 * 1280));
			label.setStyle("-fx-text-fill: white;-fx-font-size : " + size + " ;");
		});

	}

	private void updateMapHeight() {
		for (int i = 0; i < d1; i++) {
			for (int j = 0; j < d2; j++) {
				double width = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.98;
				((Rectangle) mapPane[i][j].getChildren().get(0)).setWidth(width);
				((ImageView) mapPane[i][j].getChildren().get(1)).setFitWidth(width - 5);
				double height = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.98;
				((Rectangle) mapPane[i][j].getChildren().get(0)).setHeight(height);
				((ImageView) mapPane[i][j].getChildren().get(1)).setFitHeight(height - 5);
			}
		}
	}

	private void updateMapWidth() {
		for (int i = 0; i < d1; i++) {
			for (int j = 0; j < d2; j++) {
				double width = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.98;
				((Rectangle) mapPane[i][j].getChildren().get(0)).setWidth(width);
				((ImageView) mapPane[i][j].getChildren().get(1)).setFitWidth(width - 5);
				double height = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.98;
				((Rectangle) mapPane[i][j].getChildren().get(0)).setHeight(height);
				((ImageView) mapPane[i][j].getChildren().get(1)).setFitHeight(height - 5);
			}
		}
	}

	private void updateLabel() {
		Platform.runLater(() -> {
			info.setMinHeight(RectangleWidth * 2);
			info.setMinWidth(RectangleHeight * 3);
			double size = 15 * Math.pow((Main.height * Main.width) / (1280 * 720), 1.0 / 3);
			info.setStyle("-fx-font-size : " + size + " ;");
		});

	}

	private void keyboardHandle(KeyEvent e) {
		boolean isValid = false;
		switch (e.getCode()) {
		case W:
		case A:
		case S:
		case D:
		case H:
		case ENTER:
			isValid = true;
		default:
			break;
		}
		if (isValid) {
			if (e.getCode() != KeyCode.ENTER && e.getCode() != KeyCode.H) {
				Main.play(hover);
				((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(darkColor);
				((Rectangle) mapPane[row][column].getChildren().get(0)).setOpacity(0.2);
			}
			switch (e.getCode()) {
			case W -> row = Math.max(row - 1, 0);
			case A -> column = Math.max(column - 1, 0);
			case S -> row = Math.min(row + 1, d1 - 1);
			case D -> column = Math.min(column + 1, d2 - 1);
			case H -> {
				Main.play(select);
				Main.window.getScene().setRoot((new TutorialScene(root)).getRoot());
			}
			case ENTER -> {
				Main.mediaPlayer.stop();
				select.play();
				Game.startGame(map[row][column]);
				Main.window.getScene().setRoot((new GameScene()).getRoot());
			}
			}
			handleHelper(row, column);
		}
	}

	private void mouseHandle(MouseEvent e, int x, int y) {
		hover.seek(Duration.ZERO);
		hover.play();
		handleHelper(x, y);
	}

	private void handleHelper(int x, int y) {
		info.setVisible(true);
		model.setVisible(true);
		selectYourHero.setVisible(false);
		((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(darkColor);
		timeLine.stop();
		((Rectangle) mapPane[row][column].getChildren().get(0)).setOpacity(0.2);
		row = x;
		column = y;
		((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(brightColor);
		timeLine = new Timeline(
				new KeyFrame(Duration.seconds(0),
						new KeyValue(((Rectangle) mapPane[row][column].getChildren().get(0)).opacityProperty(), 0.2)),
				new KeyFrame(Duration.seconds(1),
						new KeyValue(((Rectangle) mapPane[row][column].getChildren().get(0)).opacityProperty(), 0.05)),
				new KeyFrame(Duration.seconds(2),
						new KeyValue(((Rectangle) mapPane[row][column].getChildren().get(0)).opacityProperty(), 0.2)));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
		info.setText(createInfo(map[row][column]));
		model.setImage(map[row][column].getModel());
		model.setFitHeight((1920 / 3) * Math.pow(Main.height / 720, 0.85));
		model.setFitWidth((1480 / 3) * Math.sqrt(Main.width / 1280));
	}
}