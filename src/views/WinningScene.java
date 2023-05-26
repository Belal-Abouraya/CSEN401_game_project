package views;

import java.io.File;

import engine.Game;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class WinningScene {

	MediaPlayer mediaPlayer;

	public StackPane getRoot() {
		ImageView imageView = getImageView();
		StackPane stackPane = new StackPane();
		stackPane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		Label label = new Label();
		label.setId("winnigLabel");
		stackPane.getChildren().addAll(label);
		getMediaPlayer();
		Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			// Set the text and start the fade transition
			label.setText("The World is safe!");
			label.setOpacity(1);// initially set opacity to 1
			FadeTransition fadeTransition = new FadeTransition(Duration.seconds(4), label);
			fadeTransition.setFromValue(1);
			fadeTransition.setToValue(0);
			fadeTransition.play();
		}), new KeyFrame(Duration.seconds(3), e -> {
			// Set the new text and start the fade transition
			label.setText("You can rest now!");
			label.setOpacity(1); // reset the opacity to 1
			FadeTransition fadeTransition = new FadeTransition(Duration.seconds(4), label);
			fadeTransition.setFromValue(1);
			fadeTransition.setToValue(0);
			fadeTransition.play();
		}), new KeyFrame(Duration.seconds(6), e -> {
			// Start the fade transition on the image view
			stackPane.getChildren().addAll(imageView);
			FadeTransition fadeTransition = new FadeTransition(Duration.seconds(4), imageView);
			fadeTransition.setFromValue(0);
			fadeTransition.setToValue(1);
			fadeTransition.play();
		}));
		timeline.play();

		stackPane.setFocusTraversable(true);
		Platform.runLater(() -> stackPane.requestFocus());
		stackPane.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.Q)
				Main.window.close();
		});
		stackPane.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			imageView.setFitWidth((double) newWidth);
		});
		stackPane.heightProperty().addListener((obs, oldHeight, newHeight) -> {
			imageView.setFitHeight((double) newHeight);
		});
		return stackPane;
	}

	private ImageView getImageView() {
		Image image = null;
		try {
			String path = "assets/" + Game.mode + "/images/wallpapers/winnigscene.jpg";
			image = new Image(new File(path).toURI().toURL().toExternalForm());
		} catch (Exception e) {
		}
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(Main.height);
		imageView.setFitWidth(Main.width);
		return imageView;
	}

	private void getMediaPlayer() {
		String path = "assets/" + Game.mode + "/audio/music/winningcene.mp3";
		Media firstSceneMusic = new Media(new File(path).toURI().toString());
		mediaPlayer = new MediaPlayer(firstSceneMusic);
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.setCycleCount(Timeline.INDEFINITE);
	}
}
