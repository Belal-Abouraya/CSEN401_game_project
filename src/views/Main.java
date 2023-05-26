package views;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import engine.Game;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * 
 */
public class Main extends Application {
	static Stage window;
	static double width = 1280;
	static double height = 720;
	static Scene scene = new Scene(new Group());
	static String mode = "classic";
	static boolean isFullScreen;
	static MediaPlayer mediaPlayer;

	@Override
	public void start(Stage primaryStage) throws MalformedURLException {
		window = primaryStage;
		getMediaPlayer();
		window.setTitle("Last of Us : Legacy");
		scene.setRoot((new FirstScene()).getRoot());
		scene.getStylesheets().add(this.getClass().getResource(Game.mode + ".css").toExternalForm());
		window.setScene(scene);
		window.setFullScreen(true);
		window.setFullScreenExitHint("");
		window.addEventHandler(GameEvent.WIN, e -> window.getScene().setRoot((new WinningScene()).getRoot()));
		window.addEventHandler(GameEvent.GAME_OVER, e -> window.getScene().setRoot((new LosingScene()).getRoot()));
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
//		primaryStage.setScene(new Scene(g.getRoot()));
//		primaryStage.setFullScreen(true);
//		primaryStage.show();
//	}
}