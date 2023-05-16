package views;

import engine.Game;
import javafx.application.Application;
import javafx.stage.Stage;
import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Hero;

/**
 * 
 */
public class Main extends Application {

	@Override
	public void init() {

	}

	@Override
	public void start(Stage primaryStage) {
		Game.startGame(new Explorer("sds", 14, 20, 30));
		GameScene g = new GameScene();
		primaryStage.setScene(g.gameScene());
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
