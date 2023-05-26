package views;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardEndHandler;

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
import model.characters.Hero;

/**
 * 
 */
public class Main extends Application {
	static Stage window;
//<<<<<<< HEAD
//	static Scene firstScene;
//	static double width = 1280;
//	static double height = 720;
//=======
	static double width = 1280;
	static double height = 720;
	static Scene scene = new Scene(new Group());
	static MediaPlayer mediaPlayer;

	@Override
	public void init() {
		try {
			Font.loadFont(new File("assets/classic/fonts/Orbitron-Regular.ttf").toURI().toURL().toExternalForm(), 10);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) throws MalformedURLException {
		window = primaryStage;
		window.getIcons().add(Hero.loadIcon("icon"));
		getMediaPlayer();
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
		window.setFullScreenExitHint("Press F to toggle fullscreen mode");
		window.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.F)
				window.setFullScreen(!window.isFullScreen());

		});
		window.addEventHandler(GameEvent.WIN, e -> window.getScene().setRoot((new WinningScene()).getRoot()));
		window.addEventHandler(GameEvent.GAME_OVER, e -> window.getScene().setRoot((new LosingScene()).getRoot()));
		window.show();
	}

	public static void main(String[] args) {
		try {
			Game.loadHeroes("assets/" + Game.mode + "/heroes.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		launch(args);
	}

	private void getMediaPlayer() {
		String path = "assets/" + Game.mode + "/audio/music/firstscene.mp3";
		Media firstSceneMusic = new Media(new File(path).toURI().toString());
		mediaPlayer = new MediaPlayer(firstSceneMusic);
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.setCycleCount(Timeline.INDEFINITE);
	}

//	public static void main(String[] args) {
//		launch(args);
//	}
//
//	@Override
//	public void start(Stage primaryStage) throws FileNotFoundException, IOException {
//		Game.loadHeroes("assets/classic/heroes.csv");
//		Game.startGame(Game.availableHeroes.get(0), "classic");
//		GameScene g = new GameScene();
////		TutorialScene t = new TutorialScene();
//		primaryStage.setScene(new Scene(g.getRoot()));
//		primaryStage.setFullScreen(true);
//		primaryStage.show();
//	}
}