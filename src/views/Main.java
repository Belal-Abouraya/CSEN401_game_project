package views;

import java.io.File;
import java.net.MalformedURLException;

import engine.Game;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.characters.Hero;

public class Main extends Application {
	static Stage window;
	static double width = 1280;
	static double height = 720;
	static Scene scene = new Scene(new Group());
	static MediaPlayer mediaPlayer;

	@Override
	public void init() {
		try {
			Font.loadFont(new File("assets/classic/fonts/Orbitron-Bold.ttf").toURI().toURL().toExternalForm(), 10);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

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

	public static Image loadImage(String name) {
		Image i = null;
		try {
			i = new Image(new File("assets/" + Game.mode + "/images/" + name).toURI().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return i;
	}

	private static MediaPlayer loadMedia(String name) {
		String path = "assets/" + Game.mode + "/audio/" + name;
		Media res = new Media(new File(path).toURI().toString());
		return new MediaPlayer(res);
	}

	static MediaPlayer loadMusic(String name) {
		return loadMedia("music/" + name + ".mp3");
	}

	static MediaPlayer loadEffect(String name) {
		return loadMedia("effects/" + name + ".wav");
	}

	static void play(MediaPlayer m) {
		m.seek(Duration.ZERO);
		m.play();
	}
//	public static void main(String[] args) {
//		launch(args);
//	}
//
//	@Override
//	public void start(Stage primaryStage) throws FileNotFoundException, IOException {
//		Game.loadHeroes("assets/classic/heroes.csv");
//		Game.startGame(Game.availableHeroes.get(0));
//		WinningScene g = new WinningScene();
////		TutorialScene t = new TutorialScene();
//		primaryStage.setScene(new Scene(g.getRoot()));
//		primaryStage.getScene().getStylesheets().add(this.getClass().getResource(Game.mode + ".css").toExternalForm());
//		primaryStage.setFullScreen(true);
//		primaryStage.show();
//	}
}