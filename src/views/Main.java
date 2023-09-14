package views;

import java.io.File;
import java.net.MalformedURLException;

import engine.Game;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.characters.Hero;

/**
 * The runner class that handles launching the game. It contains some helper
 * methods that are used in other classes
 * 
 * @author Ahmed Hussein
 * @author Belal Abouraya
 */

public class Main extends Application {
	static Stage window;
	static double width = 1280;
	static double height = 720;
	static Scene scene = new Scene(new Group());
	static MediaPlayer mediaPlayer;

	/**
	 * loads the font used in the game.
	 */

	@Override
	public void init() {
		try {
			Font.loadFont(new File("assets/classic/fonts/Orbitron-Bold.ttf").toURI().toURL().toExternalForm(), 10);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * starts the game, creates the window, calls the first scene, handles the
	 * winning and loosing events.
	 */

	@Override
	public void start(Stage primaryStage) throws MalformedURLException {
		window = primaryStage;
		window.getIcons().add(Hero.loadIcon("icon"));
		mediaPlayer = loadMusic("firstscene");
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.setCycleCount(Timeline.INDEFINITE);
		window.setTitle("Last of Us : Legacy");
		scene.setRoot((new FirstScene()).getRoot());
		scene.getStylesheets().add(this.getClass().getResource(Game.mode + ".css").toExternalForm());
		Image image = null;
		try {
			String path = "assets/" + Game.mode + "/images/icons/cursorImage.png";
			image = new Image(new File(path).toURI().toURL().toExternalForm());
		} catch (Exception e) {
		}
		ImageCursor imageCursor = new ImageCursor(image);
		scene.setCursor(imageCursor);
		window.setScene(scene);
		window.setFullScreen(true);
		window.setFullScreenExitHint("");
		window.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.F)
				window.setFullScreen(!window.isFullScreen());

		});
		window.addEventHandler(GameEvent.WIN, e -> window.getScene().setRoot((new WinningScene()).getRoot()));
		window.addEventHandler(GameEvent.GAME_OVER, e -> window.getScene().setRoot((new LosingScene()).getRoot()));
		window.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * responsible for loading images.
	 * 
	 * @param image name
	 * @return image
	 */

	public static Image loadImage(String name) {
		Image i = null;
		try {
			i = new Image(new File("assets/" + Game.mode + "/images/" + name).toURI().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * responsible for loading sound files.
	 * 
	 * @param file name
	 * @return sound file
	 */

	private static MediaPlayer loadMedia(String name) {
		String path = "assets/" + Game.mode + "/audio/" + name;
		Media res = new Media(new File(path).toURI().toString());
		return new MediaPlayer(res);
	}

	/**
	 * loads sound files from the "music" folder.
	 * 
	 * @param file name
	 * @return mediaPlayer
	 */

	static MediaPlayer loadMusic(String name) {
		return loadMedia("music/" + name + ".mp3");
	}

	/**
	 * loads sound files from the "effects" folder.
	 * 
	 * @param file name
	 * @return mediaPlayer
	 */

	static MediaPlayer loadEffect(String name) {
		return loadMedia("effects/" + name + ".wav");
	}

	/**
	 * plays the sound file from the beginning.
	 * 
	 * @param mediaPlayer
	 */

	static void play(MediaPlayer m) {
		m.seek(Duration.ZERO);
		m.play();
	}

	/**
	 * creates an ImageView that contains the image and set its dimensions.
	 * 
	 * @param image name
	 * @return ImageView
	 */

	static ImageView createImageView(String name) {
		ImageView imageView = new ImageView(Main.loadImage(name));

		imageView.setFitWidth(width);
		imageView.setFitHeight(height);

		return imageView;
	}

	/**
	 * does the resizing of the label and changes the size of the text inside it.
	 * it also handles the style of the label.
	 * 
	 * @param label
	 * @param width
	 * @param height
	 * @param prev
	 * @param color
	 * @param additionalStyle
	 */
	static void updateLabelSize(Label label, double width, double height, double prev ,
			String color , String additionalStyle) {
		Platform.runLater(() -> {
			double size = prev * Math.sqrt((width * height) / (720 * 1280));
			label.setStyle("-fx-text-fill: "+color+";-fx-font-size : " + size + " ;"+additionalStyle);
		});
	}

	/**
	 * creates a letterButton.
	 * 
	 * @param letter
	 * @param text
	 * @param textSize
	 * @param factor
	 * @return letterButton
	 */

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

}