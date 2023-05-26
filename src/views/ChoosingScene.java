package views;

import java.io.File;
import java.util.ArrayList;

import engine.Game;
import model.characters.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.control.Label;

public class ChoosingScene {

	private StackPane sceneBack = new StackPane();
	private GridPane modes = new GridPane();
	private int d1 = 1, d2 = 2;
	private int row = 0, column = 0;

	private Color brightColor = Color.DARKGRAY.brighter();
	private Color darkColor = Color.DARKGRAY;
	private StackPane[][] mapPane = new StackPane[d1][d2];

	private double RectangleWidth;
	private double RectangleHeight;
	private static ImageView wallpaper;
	private Label chooseGameMode;
	private Timeline timeLine = new Timeline();
	
	private Image classic ;
	private Image bonus ;

	private MediaPlayer select = loadMedia("select");
	private MediaPlayer hover = loadMedia("hover");

	public StackPane getRoot() {
		Main.mediaPlayer.play();
		Main.mediaPlayer.setCycleCount(Timeline.INDEFINITE);
		createImages();
		createBackGround();
		createSelectYourHeroLabel();
		createMap();
		sceneBack.getChildren().add(wallpaper);
		sceneBack.setFocusTraversable(true);
		Platform.runLater(() -> sceneBack.requestFocus());

		sceneBack.getChildren().addAll(chooseGameMode, modes);
		sceneBack.setOnKeyPressed(e -> {
			boolean isValid = false;
			switch (e.getCode()) {
			case A -> isValid = true;
			case D -> isValid = true;
			case ENTER -> isValid = true;
			}
			if (isValid) {
				int res = row + column ;
				if (e.getCode() != KeyCode.ENTER) {
					hover.stop();
					hover.play();
				}
				((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(darkColor);
				timeLine.stop();
				((Rectangle) mapPane[row][column].getChildren().get(0)).setOpacity(0.8);
				switch (e.getCode()) {
				case A -> column = Math.max(column - 1, 0);
				case D -> column = Math.min(column + 1, 1);
				case ENTER -> {
					select.play();
					Main.window.getScene().getStylesheets().clear();
					if(res == 0) {
						Main.mode = "classic";
						Main.window.getScene().getStylesheets().add(this.getClass().
								getResource("classic.css").toExternalForm());
						Main.window.getScene().setRoot((new SecondScene()).getRoot());
					}else {
						Main.mode = "bonus";
						Main.window.getScene().getStylesheets().add(this.getClass().
								getResource("bonus.css").toExternalForm());
						//Main.window.getScene().setRoot((new SecondScene()).getRoot());
					}
				}
				}
				((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(brightColor);
				timeLine = new Timeline(
						new KeyFrame(Duration.seconds(0),
								new KeyValue(((Rectangle) mapPane[row][column].getChildren().get(0)).opacityProperty(),
										0)),
						new KeyFrame(Duration.seconds(1),
								new KeyValue(((Rectangle) mapPane[row][column].getChildren().get(0)).opacityProperty(),
										0.8)),
						new KeyFrame(Duration.seconds(2), new KeyValue(
								((Rectangle) mapPane[row][column].getChildren().get(0)).opacityProperty(), 0.001)));
				timeLine.setCycleCount(Timeline.INDEFINITE);
				timeLine.play();
				wallpaper.setImage(res == 0 ? classic : bonus);
			}
		});
		sceneBack.widthProperty().addListener((observable, oldWidth, newWidth) -> {
			double nw = (double) newWidth;
			Main.width = nw;
			wallpaper.setFitWidth(nw);
			updateLabelSize(chooseGameMode, nw, Main.height, 30);
			RectangleHeight = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.98;
			RectangleWidth = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.98;
			updateMapWidth();
		});
		sceneBack.heightProperty().addListener((observable, oldHeight, newHeight) -> {
			double nh = (double) newHeight;
			Main.height = nh;
			wallpaper.setFitHeight(nh);
			updateLabelSize(chooseGameMode, Main.width, nh, 30);
			RectangleHeight = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.98;
			RectangleWidth = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.98;
			updateMapHeight();
		});
		return sceneBack;
	}

	private void createBackGround() {
		wallpaper = new ImageView(classic);
	}
	
	private Image getImage(int x) {
		String path = null ;
		if(x == 0) {
			path = "assets/classic/images/icons/classic.jpg";
		}
		else {
			path = "assets/bonus/images/icons/bonus.png";
		}
		Image image = null ;
		try {
			image = new Image(new File(path).toURI().toURL().toExternalForm());
		}catch(Exception e) {}
		return image ;
	}
	
	private void createImages() {
		String path1 = "assets/classic/images/wallpapers/choosingscene.png";
		String path2 = "assets/bonus/images/wallpapers/choosingscene.jpg";
		try {
			classic = new Image(new File(path1).toURI().toURL().toExternalForm());
			bonus = new Image(new File(path2).toURI().toURL().toExternalForm());
		}catch(Exception e) {}
	}

	private void createMap() {
		int count = 0;
		for (int i = 0; i < d1; i++) {
			for (int j = 0; j < d2; j++) {
				StackPane tmp = image(count, i, j);
				modes.add(tmp, j, i);
				mapPane[i][j] = tmp;
			}
		}
		modes.setAlignment(Pos.BOTTOM_CENTER);
		modes.setHgap(50);
		modes.setVgap(2);
	}

	private StackPane image(int count, int x, int y) {
		StackPane res = new StackPane();
		RectangleHeight = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.6;
		RectangleWidth = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.6;

		Rectangle back = new Rectangle(RectangleWidth, RectangleHeight);
		back.setArcHeight(10);
		back.setArcWidth(10);
		back.setFill(darkColor);
		back.setOpacity(0.2);
		ImageView icon = new ImageView(getImage(count));
		icon.setFitHeight(RectangleHeight - 5);
		icon.setFitWidth(RectangleWidth - 5);
		res.getChildren().addAll(back, icon);

		res.setOnMouseEntered(e -> {
			((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(darkColor);
			timeLine.stop();
			((Rectangle) mapPane[row][column].getChildren().get(0)).setOpacity(0.2);
			back.setFill(brightColor);
			hover.seek(Duration.ZERO);
//			hover.play();
//			hover.setOnEndOfMedia(() -> hover.stop());
			row = x ; column = y ;
			timeLine = new Timeline(new KeyFrame(Duration.seconds(0), new KeyValue(back.opacityProperty(), 0)),
					new KeyFrame(Duration.seconds(1), new KeyValue(back.opacityProperty(), 0.8)),
					new KeyFrame(Duration.seconds(2), new KeyValue(back.opacityProperty(), 0.001)));
			timeLine.setCycleCount(Timeline.INDEFINITE);
			timeLine.play();
			wallpaper.setImage(count == 0 ? classic : bonus);
		});
//		res.setOnMouseExited(e -> hover.stop());
		res.setOnMouseClicked(e -> {
			select.play();
			Main.window.getScene().getStylesheets().clear();
			if(count == 0) {
				Main.mode = "classic";
				Main.window.getScene().getStylesheets().add(this.getClass().
						getResource("classic.css").toExternalForm());
				Main.window.getScene().setRoot((new SecondScene()).getRoot());
			}else {
				Main.mode = "bonus";
				Main.window.getScene().getStylesheets().add(this.getClass().
						getResource("bonus.css").toExternalForm());
				//Main.window.getScene().setRoot((new SecondScene()).getRoot());
			}
		});

		return res;
	}

	private MediaPlayer loadMedia(String name) {
		String path = "assets/" + Main.mode + "/audio/effects/" + name + ".wav";
		Media tmp = new Media(new File(path).toURI().toString());
		MediaPlayer player = new MediaPlayer(tmp);
		return player;
	}

	private void createSelectYourHeroLabel() {
		chooseGameMode = new Label("Choose the Game Mode");
		chooseGameMode.setId("SelectHeroLabel");
		Timeline timeLine = new Timeline(
				new KeyFrame(Duration.seconds(0), new KeyValue(chooseGameMode.opacityProperty(), 0)),
				new KeyFrame(Duration.seconds(1), new KeyValue(chooseGameMode.opacityProperty(), 1)),
				new KeyFrame(Duration.seconds(2), new KeyValue(chooseGameMode.opacityProperty(), 0)));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
	}

	private void updateLabelSize(Label label, double width, double height, double prev) {
		double size = prev * Math.sqrt((width * height) / (720 * 1280));
		label.setStyle("-fx-font-size : " + size + " ;");
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
				double width = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.6;
				((Rectangle) mapPane[i][j].getChildren().get(0)).setWidth(width);
				((ImageView) mapPane[i][j].getChildren().get(1)).setFitWidth(width - 5);
				double height = Math.pow(Main.height * Main.width, 1.0 / 3) / 0.6;
				((Rectangle) mapPane[i][j].getChildren().get(0)).setHeight(height);
				((ImageView) mapPane[i][j].getChildren().get(1)).setFitHeight(height - 5);
			}
		}
	}

	private void updateLabel() {
		chooseGameMode.setMinHeight(RectangleWidth * 2);
		chooseGameMode.setMinWidth(RectangleHeight * 3);
		double size = 30 * Math.pow((Main.height * Main.width) / (1280 * 720), 1.0 / 3);
		chooseGameMode.setStyle("-fx-font-size : " + size + " ;");
	}

}
