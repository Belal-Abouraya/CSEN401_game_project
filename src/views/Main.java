package views;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import engine.Game;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 
 */
public class Main extends Application {
	static Stage window;
	static Scene firstScene;
	static double width = 1280;
	static double height = 720;
	static String mode = "classic";
	static boolean isFullScreen ;

	@Override
	public void start(Stage primaryStage) throws MalformedURLException {
		window = primaryStage;
		window.setTitle("Last of Us : Legacy");
		window.setScene((new FirstScene()).getScene());
		window.addEventHandler(GameEvent.WIN, e -> window.setScene((new WinningScene()).getScene()));
		window.addEventHandler(GameEvent.GAME_OVER, e -> window.setScene((new LosingScene()).getScene()));
//		window.setFullScreen(true);
//		isFullScreen = true ;
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
	
//	public static void main(String[] args) {
//		launch(args);
//	}

//	@Override
//	public void start(Stage primaryStage) throws FileNotFoundException, IOException {
//
//		Game.loadHeroes("assets/classic/heroes.csv");
//		Game.startGame(Game.availableHeroes.get(0), "classic");
//		GameScene g = new GameScene();
//		primaryStage.setScene(g.gameScene());
//		primaryStage.setFullScreen(true);
//		primaryStage.show();
//	}
}