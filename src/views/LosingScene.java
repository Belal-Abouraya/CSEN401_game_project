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

public class LosingScene {

	Label yes = new Label("YES");
	Label no = new Label("NO");
	ImageView imageView;
	MediaPlayer mediaPlayer;
	private MediaPlayer select = Main.loadEffect("select");
	private MediaPlayer hover = Main.loadEffect("hover");

	public StackPane getRoot() {

		getImageView();
		getMediaPlayer();
		yes.setId("yes");
		no.setId("no");
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

		StackPane stackPane = new StackPane();
		stackPane.getChildren().addAll(imageView, vbox);
		stackPane.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			double width = (double) newWidth;
			Main.width = width;
			imageView.setFitWidth(width);
			hbox.setSpacing(50 * Main.width / 1280);
			updateLabelSize(tryAgain, Main.width, Main.height, 25);
			updateLabelSize(yes, Main.width, Main.height, 15);
			updateLabelSize(no, Main.width, Main.height, 15);
		});
		stackPane.heightProperty().addListener((obs, oldHeight, newHeight) -> {
			double height = (double) newHeight;
			Main.width = height;
			imageView.setFitHeight(height);
			vbox.setTranslateY(120 * Main.height * Main.height / 518400);
		});
		return stackPane;
	}

	private void getMediaPlayer() {
		mediaPlayer = Main.loadMusic("losingscene");
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.setCycleCount(Timeline.INDEFINITE);
	}

	private void getImageView() {
		imageView = new ImageView(Main.loadImage("wallpapers/losingscene.jpg"));
		imageView.setFitHeight(Main.height);
		imageView.setFitWidth(Main.width);
	}

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

	private void updateLabel(Label label) {
		Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(0), new KeyValue(label.opacityProperty(), 0)),
				new KeyFrame(Duration.seconds(1), new KeyValue(label.opacityProperty(), 1)),
				new KeyFrame(Duration.seconds(2), new KeyValue(label.opacityProperty(), 0)));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		label.setOnMouseEntered(e -> {
			hover.play();
			timeLine.play();
		});
		label.setOnMouseExited(e -> {
			hover.stop();
			timeLine.stop();
			label.setOpacity(1);
		});
	}

	private void updateLabelSize(Label label, double width, double height, double prev) {
		double size = prev * Math.sqrt((width * height) / (720 * 1280));
		label.setStyle("-fx-font-size : " + size + " ;");
	}

}
