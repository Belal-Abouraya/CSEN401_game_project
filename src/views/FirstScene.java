package views;

import java.io.File;
import java.net.MalformedURLException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class FirstScene {
	
	public Scene getScene() {
		ImageView wallpaper = createImageView();
		Label label = new Label("Press Enter to Start");
		label.setFont(new Font("" , 30));
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(wallpaper);
		StackPane stackPane = new StackPane();
		stackPane.getChildren().add(label);
		borderPane.setBottom(stackPane);
		Scene scene = new Scene(borderPane, Main.width, Main.height);
		scene.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER) {
				Main.window.setScene((new SecondScene()).getScene());
			}
		});
		return scene;
	}
	
	private static ImageView createImageView() {
		Image image = null ;
		try {
			image = new Image(new File("src/FirstScene.jpg").toURI().toURL().toExternalForm());
		}catch(Exception e) {}
		ImageView wallpaper = new ImageView(image);
		wallpaper.setFitHeight(Main.height);
		wallpaper.setFitWidth(Main.width);
		return wallpaper;
	}

}
