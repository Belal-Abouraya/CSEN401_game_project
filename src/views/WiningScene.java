package views;

import javafx.scene.Group;
import javafx.scene.Scene;

public class WiningScene {
	
	public Scene getScene() {
		return new Scene(new Group() , Main.width , Main.height);
	}
}
