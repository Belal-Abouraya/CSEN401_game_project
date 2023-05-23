package views;

import java.io.File;
import java.util.ArrayList;

import engine.Game;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.characters.Hero;

public class SecondScene {

	private static StackPane sceneBack = new StackPane();
	private static GridPane heroes = new GridPane();
	private static int d1 = 2, d2 = 4;
	private static int row = 0, column = 0;
	private static Hero[][] map = new Hero[d1][d2];

	private static Color brightColor = Color.DARKGRAY.brighter();
	private static Color darkColor = Color.DARKGRAY;
	private static StackPane[][] mapPane = new StackPane[d1][d2];

	private static double RectangleWidth;
	private static double RectangleHeight;
	private static VBox vbox = new VBox();
	private static ImageView wallpaper;
	private static Label selectYourHero;
	private static double scaleFactor ;

	public Scene getScene() {
		createBackGround();
		createSelectYourHeroLabel();
		createHeroes();
		sceneBack.getChildren().add(wallpaper);

//		 initial state
		vbox = createModel(map[0][0]);
		((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(brightColor);
		vbox.setAlignment(Pos.CENTER_LEFT);
		sceneBack.getChildren().addAll(selectYourHero, vbox, heroes);
		Scene scene = new Scene(sceneBack, Main.width, Main.height);
		scene.getStylesheets().add(this.getClass().getResource(Game.mode + ".css").toExternalForm());
		scene.setOnKeyPressed(e -> {
			((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(darkColor);
			switch (e.getCode()) {
			case W -> row = Math.max(row - 1, 0);
			case A -> column = Math.max(column - 1, 0);
			case S -> row = Math.min(row + 1, d1 - 1);
			case D -> column = Math.min(column + 1, d2 - 1);
			case ENTER -> {
				Main.mediaPlayer.stop();
				Game.startGame(map[row][column], Main.mode);
				Main.window.setScene((new GameScene()).getScene());
			}
			case MINUS -> Main.mediaPlayer.stop();
			case EQUALS -> Main.mediaPlayer.play();
			}
			((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(brightColor);
			sceneBack.getChildren().clear();
			vbox = createModel(map[row][column]);
			vbox.setAlignment(Pos.CENTER_LEFT);
			sceneBack.getChildren().addAll(wallpaper, selectYourHero, vbox, heroes);
		});
		scene.widthProperty().addListener((observable, oldWidth, newWidth) -> {
//			double nw = (double) newWidth;
//			Main.width = nw;
//			wallpaper.setFitWidth(nw);
//			double newSize = 30 * Math.min(Main.width, Main.height) / 720;
//			selectYourHero.setStyle("-fx-font-size: " + newSize + ";");
//			resizeWidth(observable , oldWidth , newWidth) ;
		});
		scene.heightProperty().addListener((observable, oldHeight, newHeight) -> {
//			double nh = (double) newHeight;
//			Main.height = nh;
//			wallpaper.setFitHeight(nh);
//			double newSize = 30 * Math.min(Main.width, Main.height) / 720;
//			selectYourHero.setStyle("-fx-font-size: " + newSize + ";");
//			resizeHeight(observable , oldHeight , newHeight) ;
		});
		return scene;
	}

	private static void createBackGround() {
		Image image = null;
		try {
			String path = "assets/" + Main.mode + "/images/wallpapers/secondscene.jpeg";
			image = new Image(new File(path).toURI().toURL().toExternalForm());
		} catch (Exception e) {
		}
		wallpaper = new ImageView(image);
		wallpaper.setFitHeight(Main.height);
		wallpaper.setFitWidth(Main.width);
	}

	private static void createHeroes() {
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
		heroes.setAlignment(Pos.BOTTOM_CENTER);
		heroes.setTranslateX(-2);
		heroes.setHgap(2);
		heroes.setVgap(2);
	}

	private static StackPane hero(Hero h, int x, int y) {
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

		res.setOnMouseEntered(e -> {
			((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(darkColor);
			back.setFill(brightColor);
			row = x;
			column = y;
			sceneBack.getChildren().clear();
			vbox = createModel(map[row][column]);
			vbox.setAlignment(Pos.CENTER_LEFT);
			sceneBack.getChildren().addAll(wallpaper, selectYourHero, vbox, heroes);
		});
		res.setOnMouseClicked(e -> {
			Game.startGame(h, Main.mode);
			Main.mediaPlayer.stop();
			Main.window.setScene((new GameScene()).getScene());
		});

		return res;
	}

	private static VBox createModel(Hero h) {
		Image image = h.getModel();
		ImageView imageView = new ImageView(image);

		double maxImageSize = Math.max(Main.height / 3, Main.width / 3);
		double imageWidth = image.getWidth();
		double imageHeight = image.getHeight();
		scaleFactor = 1;

		if (imageWidth > maxImageSize || imageHeight > maxImageSize) {
			double widthRatio = maxImageSize / imageWidth;
			double heightRatio = maxImageSize / imageHeight;
			scaleFactor = Math.min(widthRatio, heightRatio);
		}

		double width = imageWidth * scaleFactor;
		double height = imageHeight * scaleFactor;

		imageView.setFitWidth(width);
		imageView.setFitHeight(height);

		Label info = new Label(h.getName() + "\n" + h.getType() + "\n" + "Health : " + h.getMaxHp() + "\n"
				+ "Actions per Turn : " + h.getMaxActions() + "\n" + "Attack Damage : " + h.getAttackDmg());
//		Label info = new Label();
		info.setTranslateX(width / 5);

		VBox res = new VBox(4);
		res.getChildren().addAll(imageView, info);
		res.setTranslateY((Main.height - res.getHeight()) / 10);
		return res;
	}

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

	private static String createInfo(Hero h) {
		return h.getName() + "\n" + h.getType() + "\n" + "Health : " + h.getMaxHp() + "\n" + "Actions per Turn : "
				+ h.getMaxActions() + "\n" + "Attack Damage : " + h.getAttackDmg();
	}
	
	private void resizeWidth(ObservableValue<? extends Number> obs, Number oldWidth, Number newWidth) {
		double nw = (double) oldWidth ;
		Main.width = nw ;
		wallpaper.setFitWidth(nw);
		sceneBack.getChildren().clear();
		RectangleWidth = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.98;
		double newSize = 30 * Math.min(Main.width, Main.height) / 720;
		for(Node node : heroes.getChildren()) {
			( (Rectangle) ((StackPane) node).getChildren().get(0)).setWidth(RectangleWidth);
			ImageView imageView = ((ImageView) ((StackPane) node).getChildren().get(1));
			double maxImageSize = Math.max(Main.height / 3, Main.width / 3);
			Image image = imageView.getImage();
			double imageWidth = image.getWidth();
			double imageHeight = image.getHeight();
			scaleFactor = 1;

			if (imageWidth > maxImageSize || imageHeight > maxImageSize) {
				double widthRatio = maxImageSize / imageWidth;
				double heightRatio = maxImageSize / imageHeight;
				scaleFactor = Math.min(widthRatio, heightRatio);
			}

			double width = imageWidth * scaleFactor;
			double height = imageHeight * scaleFactor;

			((ImageView) ((StackPane) node).getChildren().get(1)).setFitWidth(width);
			((ImageView) ((StackPane) node).getChildren().get(1)).setFitHeight(height);
		}
		vbox = createModel(map[row][column]);
		vbox.setAlignment(Pos.CENTER_LEFT);
		selectYourHero.setStyle("-fx-font-size : " + newSize +"px ;");
		sceneBack.getChildren().addAll(wallpaper, selectYourHero, vbox, heroes);
	}
	
	private void resizeHeight(ObservableValue<? extends Number> obs, Number oldHeight, Number newHeight) {
		double nh = (double) newHeight ;
		Main.height = nh ;
		wallpaper.setFitHeight(nh);
		sceneBack.getChildren().clear();
		RectangleHeight = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.98;
		double newSize = 30 * Math.min(Main.width, Main.height) / 720;
		for(Node node : heroes.getChildren()) {
			( (Rectangle) ((StackPane) node).getChildren().get(0)).setHeight(RectangleWidth);
			ImageView imageView = ((ImageView) ((StackPane) node).getChildren().get(1));
			double maxImageSize = Math.max(Main.height / 3, Main.width / 3);
			Image image = imageView.getImage();
			double imageWidth = image.getWidth();
			double imageHeight = image.getHeight();
			scaleFactor = 1;

			if (imageWidth > maxImageSize || imageHeight > maxImageSize) {
				double widthRatio = maxImageSize / imageWidth;
				double heightRatio = maxImageSize / imageHeight;
				scaleFactor = Math.min(widthRatio, heightRatio);
			}

			double width = imageWidth * scaleFactor;
			double height = imageHeight * scaleFactor;

			((ImageView) ((StackPane) node).getChildren().get(1)).setFitWidth(width);
			((ImageView) ((StackPane) node).getChildren().get(1)).setFitHeight(height);
		}
		selectYourHero.setStyle("-fx-font-size : " + newSize +"px ;");
		vbox = createModel(map[row][column]);
		vbox.setAlignment(Pos.CENTER_LEFT);
		sceneBack.getChildren().addAll(wallpaper, selectYourHero, vbox, heroes);
	}
	

}
