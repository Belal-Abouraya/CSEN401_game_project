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

/**
 * WinningScene represents the last scene in the game in case the player wins.
 * WinningScene gratulates the player on his winning. The class gives the player
 * the choice to quit the game or to play again.
 * 
 * @author Ahmed Hussein
 * @author Belal Abouraya
 */

public class WinningScene {

	private MediaPlayer mediaPlayer;
	private Label label = new Label();
	private VBox vbox = new VBox(6);
	private double size = 23;
	private double factor = 23;
	private MediaPlayer select = Main.loadEffect("select");
	private StackPane root;

	/**
	 * creates the root of the winning scene. creates the WallPaper, replay and quit
	 * buttons, statistics label. plays the winning music. handles the resizing.
	 * 
	 */
	public WinningScene() {
		ImageView imageView = Main.createImageView("wallpapers/winnigscene.jpg");
		root = new StackPane();
		createLabel();
		createVBox();
		root.getChildren().addAll(imageView, label, vbox);
		vbox.setAlignment(Pos.BOTTOM_LEFT);
		getMediaPlayer();
		root.setFocusTraversable(true);
		Platform.runLater(() -> root.requestFocus());
		root.setOnKeyPressed(e -> {
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

		root.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			Main.width = (double) newWidth;
			imageView.setFitWidth((double) newWidth);
			Main.updateLabelSize(label, Main.width, Main.height, 30);
			updateVbox();
		});
		root.heightProperty().addListener((obs, oldHeight, newHeight) -> {
			Main.height = (double) newHeight;
			imageView.setFitHeight((double) newHeight);
			Main.updateLabelSize(label, Main.width, Main.height, 30);
			updateVbox();
		});
	}

	/**
	 * @return root
	 */

	public StackPane getRoot() {
		return root;
	}

	/**
	 * loads the winning music.
	 */

	private void getMediaPlayer() {
		mediaPlayer = Main.loadMusic("winningcene");
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.setCycleCount(Timeline.INDEFINITE);
	}

	/**
	 * creates statistics label.
	 */

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

	/**
	 * creates replay and quit buttons.
	 */

	private void createVBox() {
		HBox quit = Main.createButton("q", "Quit", size, factor);
		HBox play = Main.createButton("r", "Replay", size, factor);
		vbox.getChildren().addAll(play, quit);
	}

	/**
	 * updates the size of the buttons when resizing takes place.
	 */

	private void updateVbox() {
		for (Node node : vbox.getChildren()) {
			HBox currentButton = (HBox) node;
			SecondScene.updateHBox(currentButton, size, factor);
		}
	}
}
