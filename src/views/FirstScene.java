package views;

import java.io.File;

import engine.Game;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class FirstScene {

	public Scene getScene() {
		ImageView wallpaper = createImageView();
		Label label = getStartGameLabel();
		StackPane stackPane = new StackPane();
		stackPane.getChildren().addAll(wallpaper,label);
		Scene scene = new Scene(stackPane , Main.width , Main.height);
		scene.getStylesheets().add(this.getClass().getResource(Main.mode + ".css").toExternalForm());
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				Main.window.setScene((new SecondScene()).getScene());
			}
			if(e.getCode() == KeyCode.MINUS) {
				Main.mediaPlayer.stop();
			}
			if(e.getCode() == KeyCode.EQUALS) {
				Main.mediaPlayer.play();
			}
		});
		scene.widthProperty().addListener((observable, oldWidth, newWidth) -> {
			Main.width = (double) newWidth;
			wallpaper.setFitWidth((double) newWidth);
			double newSize = 30 * Math.min(Main.width, Main.height) / 720;
			label.setStyle("-fx-font-size: " + newSize + ";");
		});
		scene.heightProperty().addListener((observable, oldHeight, newHeight) -> {
			Main.height = (double) newHeight;
			wallpaper.setFitHeight((double) newHeight);
			double newSize = 30 * Math.min(Main.width, Main.height) / 720;
			label.setStyle("-fx-font-size: " + newSize + ";");
			label.setTranslateY(Math.floor(0.4 * Main.height));
		});
		return scene;
	}

	private ImageView createImageView() {
		Image image = null;
		try {
			String path = "assets/" + Main.mode + "/images/wallpapers/firstscene.jpg";
			image = new Image(new File(path).toURI().toURL().toExternalForm());
		}catch(Exception e) {}
		ImageView imageView = new ImageView(image);

		imageView.setFitWidth(Main.width);
		imageView.setFitHeight(Main.height);
		
		return imageView;
	}
	
	private Label getStartGameLabel () {
		Label label = new Label("Press Enter to Start");
		label.setId("StartLabel");
		label.setTranslateY(Math.floor(0.4 * Main.height));
		Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(0), new KeyValue(label.opacityProperty(), 0)),
				new KeyFrame(Duration.seconds(1), new KeyValue(label.opacityProperty(), 1)),
				new KeyFrame(Duration.seconds(2), new KeyValue(label.opacityProperty(), 0)));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
		return label;
	}
	
}
