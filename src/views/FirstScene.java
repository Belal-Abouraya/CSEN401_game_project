package views;

import java.io.File;

import engine.Game;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * FirstScene is a class represents the first scene that appear when starting the game.
 * The class only contains a wallpaper and a text.
 * The class takes the player to the second scene.
 * 
 * @author Ahmed Hussein
 *
 */

public class FirstScene {

	public double currHeight = Main.height, currWidth = Main.width;

	public StackPane getRoot() {
		ImageView wallpaper = createImageView();
		Label label = getStartGameLabel();
		StackPane stackPane = new StackPane();
		stackPane.getChildren().addAll(wallpaper, label);

		stackPane.setFocusTraversable(true);
		Platform.runLater(() -> stackPane.requestFocus());

		String path = "assets/" + Game.mode + "/audio/effects/select.wav";
		Media firstSceneMusic = new Media(new File(path).toURI().toString());
		MediaPlayer select = new MediaPlayer(firstSceneMusic);

		stackPane.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				select.play();
				Main.window.getScene().setRoot((new SecondScene()).getRoot());
			}
			if (e.getCode() == KeyCode.MINUS) {
				Main.mediaPlayer.stop();
			}
			if (e.getCode() == KeyCode.EQUALS) {
				Main.mediaPlayer.play();
			}
		});
		stackPane.widthProperty().addListener((observable, oldWidth, newWidth) -> {
			currWidth = (double) newWidth;
			Main.width = currWidth;
			wallpaper.setFitWidth(currWidth);
			updateLabelSize(label, currWidth, currHeight);
		});
		stackPane.heightProperty().addListener((observable, oldHeight, newHeight) -> {
			currHeight = (double) newHeight;
			Main.height = currHeight;
			wallpaper.setFitHeight(currHeight);
			label.setTranslateY(0.4 * currHeight);
			updateLabelSize(label, currWidth, currHeight);
		});
		return stackPane;
	}

	private ImageView createImageView() {
		ImageView imageView = new ImageView("wallpapers/firstscene.png");

		imageView.setFitWidth(currWidth);
		imageView.setFitHeight(currHeight);

		return imageView;
	}

	private Label getStartGameLabel() {
		Label label = new Label("Press Enter to Start");
		label.setId("StartLabel");
		label.setTranslateY(0.4 * currHeight);
		Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(0), new KeyValue(label.opacityProperty(), 0)),
				new KeyFrame(Duration.seconds(1), new KeyValue(label.opacityProperty(), 1)),
				new KeyFrame(Duration.seconds(2), new KeyValue(label.opacityProperty(), 0)));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
		return label;
	}

	private void updateLabelSize(Label label, double width, double height) {
		double size = 30 * Math.sqrt((width * height) / (720 * 1280));
		label.setStyle("-fx-font-size : " + size + " ;");
	}

}
