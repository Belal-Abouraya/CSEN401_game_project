package views;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import engine.Game;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * 
 */
public class Main extends Application {
	static Stage window;
	static Scene firstScene;
	static double width = 1280;
	static double height = 720;
	static String mode = "classic";
	static boolean isFullScreen;
	static MediaPlayer mediaPlayer;

	@Override
	public void init() {
		try {
			File f = new File("assets/classic/fonts/Orbitron-Regular.ttf");
			Font.loadFont(new File("assets/classic/fonts/Orbitron-Regular.ttf").toURI().toURL().toExternalForm(), 10);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) throws MalformedURLException {
		window = primaryStage;
		// TODO: maximize window
		// window.setMaximized(true);
		window.setTitle("Last of Us : Legacy");
		getMediaPlayer();
		window.setScene((new FirstScene()).getScene());
		window.addEventHandler(GameEvent.WIN, e -> window.setScene((new WinningScene()).getScene()));
		window.addEventHandler(GameEvent.GAME_OVER, e -> window.setScene((new LosingScene()).getScene()));
		window.show();
	}

	public static void main(String[] args) {
		try {
			Game.loadHeroes("assets/" + mode + "/heroes.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		launch(args);
	}

	private void getMediaPlayer() {
		String path = "assets/" + Main.mode + "/audio/music/firstscene.mp3";
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