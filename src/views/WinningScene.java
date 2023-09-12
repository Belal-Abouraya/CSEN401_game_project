package views;

import engine.Game;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;

public class WinningScene {

	MediaPlayer mediaPlayer;
	Label label = new Label();
	VBox vbox = new VBox(6);
	double size = 23;
	double factor = 23;
	private MediaPlayer select = Main.loadEffect("select");

	public StackPane getRoot() {
		ImageView imageView = getImageView();
		StackPane stackPane = new StackPane();
		createLabel();
		createVBox();
		stackPane.getChildren().addAll(imageView, label, vbox);
		vbox.setAlignment(Pos.BOTTOM_LEFT);
		getMediaPlayer();
		stackPane.setFocusTraversable(true);
		Platform.runLater(() -> stackPane.requestFocus());
		stackPane.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.Q) {
				select.play();
				Main.window.close();
			}
			if (e.getCode() == KeyCode.R) {
				select.play();
				mediaPlayer.stop();
				Main.window.getScene().setRoot((new SecondScene()).getRoot());
			}
		});

		stackPane.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			Main.width = (double) newWidth;
			imageView.setFitWidth((double) newWidth);
			SecondScene.updateLabelSize(label, Main.width, Main.height, 30);
			updateVbox();
		});
		stackPane.heightProperty().addListener((obs, oldHeight, newHeight) -> {
			Main.height = (double) newHeight;
			imageView.setFitHeight((double) newHeight);
			SecondScene.updateLabelSize(label, Main.width, Main.height, 30);
			updateVbox();
		});
		return stackPane;
	}

	private void getMediaPlayer() {
		mediaPlayer = Main.loadMusic("winningscene");
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.setCycleCount(Timeline.INDEFINITE);
	}

	private ImageView getImageView() {
		ImageView imageView = new ImageView(Main.loadImage("wallpapers/winningscene.jpg"));
		imageView.setFitHeight(Main.height);
		imageView.setFitWidth(Main.width);
		return imageView;
	}

	private void createLabel() {
		label.setId("winnigLabel");
		long totalTime = Game.endTime - Game.startTime;
		totalTime /= 1000;
		long minutes = totalTime / 60;
		long seconds = totalTime % 60;
		String text = "Total time played : " + minutes + ":" + seconds + "\n" + "\n" + "Number of Turns played : "
				+ Game.turns + "\n" + "\n" + "Number of Zombies killed : " + Game.deadZombies + "\n" + "\n"
				+ "Number of Heroes lost : " + Game.deadHeroes + "\n";
		label.setText(text);
	}

	private void createVBox() {
		HBox quit = SecondScene.createButton("q", "Quit", size, factor);
		HBox play = SecondScene.createButton("r", "Replay", size, factor);
		vbox.getChildren().addAll(play, quit);
	}

	private void updateVbox() {
		for (Node node : vbox.getChildren()) {
			HBox currentButton = (HBox) node;
			SecondScene.updateVBox(currentButton, size, factor);
		}
	}
}
