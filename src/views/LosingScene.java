package views;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * LosingScene represents the last scene in the game in case the player looses.
 * LosingScene should inform the player that they lost. The class gives the
 * player the choice to quit the game or to play again.
 * 
 * @author Ahmed Hussein
 *
 */

public class LosingScene {

	private Label yes = new Label("YES");
	private Label no = new Label("NO");
	private ImageView imageView = Main.createImageView("wallpapers/losingscene.jpg");
	private MediaPlayer mediaPlayer;
	private MediaPlayer select = Main.loadEffect("select");
	private MediaPlayer hover = Main.loadEffect("hover");
	private StackPane root;
	private double opacity = 0.5;

	/**
	 * creates the root of the loosing scene. creates the WallPaper, yes and no
	 * buttons. plays the loosing music. handles the resizing.
	 */
	public LosingScene() {
		getMediaPlayer();
		getLabels();

		HBox hbox = new HBox(50 * Main.width / 1280);
		Label tryAgain = new Label("Try Again?");
		tryAgain.setId("tryAgain");

		hbox.getChildren().addAll(yes, no);
		hbox.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(5);
		vbox.getChildren().addAll(tryAgain, hbox);
		vbox.setAlignment(Pos.CENTER);
		vbox.setTranslateY(120 * Main.height * Main.height / 518400);

		root = new StackPane();
		root.getChildren().addAll(imageView, vbox);
		root.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			double width = (double) newWidth;
			Main.width = width;
			imageView.setFitWidth(width);
			hbox.setSpacing(50 * Main.width / 1280);
			Main.updateLabelSize(tryAgain, Main.width, Main.height, 25, "white", "-fx-opacity: " + opacity + ";");
			Main.updateLabelSize(yes, Main.width, Main.height, 15, "white", "-fx-opacity: " + opacity + ";");
			Main.updateLabelSize(no, Main.width, Main.height, 15, "white", "-fx-opacity: " + opacity + ";");
		});
		root.heightProperty().addListener((obs, oldHeight, newHeight) -> {
			double height = (double) newHeight;
			Main.width = height;
			imageView.setFitHeight(height);
			vbox.setTranslateY(120 * Main.height * Main.height / 518400);
		});
	}

	/**
	 * @return root
	 */

	public StackPane getRoot() {
		return root;
	}

	/**
	 * loads the loosing music.
	 */

	private void getMediaPlayer() {
		mediaPlayer = Main.loadMusic("losingscene");
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.setCycleCount(Timeline.INDEFINITE);
	}

	/**
	 * creates the yes and no buttons.
	 */

	private void getLabels() {
		updateLabel(yes);
		updateLabel(no);
		yes.setOnMouseClicked(e -> {
			select.play();
			mediaPlayer.stop();
			Main.window.getScene().setRoot((new SecondScene()).getRoot());
		});
		no.setOnMouseClicked(e -> {
			select.play();
			Main.window.close();
		});
	}

	/**
	 * deals with fading animation.
	 * 
	 * @param label
	 */

	private void updateLabel(Label label) {
		Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(0), new KeyValue(label.opacityProperty(), 0)),
				new KeyFrame(Duration.seconds(1), new KeyValue(label.opacityProperty(), opacity)),
				new KeyFrame(Duration.seconds(2), new KeyValue(label.opacityProperty(), 0)));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		label.setOnMouseEntered(e -> {
			hover.play();
			timeLine.play();
		});
		label.setOnMouseExited(e -> {
			hover.stop();
			timeLine.stop();
			label.setOpacity(opacity);
		});
	}
}
