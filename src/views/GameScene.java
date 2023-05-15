package views;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/**
 * @author Belal Abouraya
 */
public class GameScene {

	public GameScene() {

	}

	public Scene gameScene() {
		BorderPane root = new BorderPane();

		return new Scene(root);
	}
}
