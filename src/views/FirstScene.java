package views;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class FirstScene {

	public double currHeight = Main.height, currWidth = Main.width;

	public Scene getScene() {
		ImageView wallpaper = createImageView();
		Label label = getStartGameLabel();
		StackPane stackPane = new StackPane();
		stackPane.getChildren().addAll(wallpaper, label);
		Scene scene = new Scene(stackPane, currWidth, currHeight);
		scene.getStylesheets().add(this.getClass().getResource(Main.mode + ".css").toExternalForm());
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				Main.window.setScene((new SecondScene()).getScene());
			}
			if (e.getCode() == KeyCode.MINUS) {
				Main.mediaPlayer.stop();
			}
			if (e.getCode() == KeyCode.EQUALS) {
				Main.mediaPlayer.play();
			}
		});
		scene.widthProperty().addListener((observable, oldWidth, newWidth) -> {
			currWidth = (double) newWidth;
			Main.width = currWidth;
			wallpaper.setFitWidth(currWidth);
			updateLabelSize(label, currWidth, currHeight);
		});
		scene.heightProperty().addListener((observable, oldHeight, newHeight) -> {
			currHeight = (double) newHeight;
			Main.height = currHeight;
			wallpaper.setFitHeight(currHeight);
			label.setTranslateY(0.4 * currHeight);
			updateLabelSize(label, currWidth, currHeight);
		});
		return scene;
	}

	private ImageView createImageView() {
		Image image = null;
		try {
			String path = "assets/" + Main.mode + "/images/wallpapers/firstscene.jpg";
			image = new Image(new File(path).toURI().toURL().toExternalForm());
		} catch (Exception e) {
		}
		ImageView imageView = new ImageView(image);

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
