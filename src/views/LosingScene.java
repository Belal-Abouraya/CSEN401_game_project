package views;

import java.io.File;
import java.io.IOException;

import engine.Game;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class LosingScene {
	
	Label yes = new Label("YES");
	Label no = new Label("NO");
	ImageView imageView ;
	MediaPlayer mediaPlayer ;

	public StackPane getRoot() {
		
		getImageView();
		getMediaPlayer();
		yes.setId("yes");
		no.setId("no");
		getLabels();

		HBox hbox = new HBox(50*Main.width/1280);
		Label tryAgain = new Label("Try Again?");
		tryAgain.setId("tryAgain");
		
		hbox.getChildren().addAll(yes, no);
		hbox.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(5);
		vbox.getChildren().addAll(tryAgain, hbox);
		vbox.setAlignment(Pos.CENTER);
		vbox.setTranslateY(120* Main.height*Main.height / 518400);

		StackPane stackPane = new StackPane();
		stackPane.getChildren().addAll(imageView, vbox);
		stackPane.widthProperty().addListener((obs , oldWidth , newWidth) -> {
			double width = (double) newWidth ;
			Main.width = width ;
			imageView.setFitWidth(width);
			hbox.setSpacing(50*Main.width/1280);
			updateLabelSize(tryAgain, Main.width, Main.height , 25);
			updateLabelSize(yes, Main.width, Main.height , 15);
			updateLabelSize(no, Main.width, Main.height , 15);
		});
		stackPane.heightProperty().addListener((obs , oldHeight , newHeight) -> {
			double height = (double) newHeight ;
			Main.width = height ;
			imageView.setFitHeight(height);
			vbox.setTranslateY(120* Main.height*Main.height / 518400);
		});
		return stackPane;
	}
	
	private void getMediaPlayer() {
		String path = "assets/" + Main.mode + "/audio/music/losingscene.mp3";
		Media firstSceneMusic = new Media(new File(path).toURI().toString());
		mediaPlayer = new MediaPlayer(firstSceneMusic);
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.setCycleCount(Timeline.INDEFINITE);
	}
	
	private void getImageView() {
		Image image = null;
		try {
			String path = "assets/" + Main.mode + "/images/wallpapers/losingscene.jpg";
			image = new Image(new File(path).toURI().toURL().toExternalForm());
		} catch (Exception e) {
		}
		imageView = new ImageView(image);
		imageView.setFitHeight(Main.height);
		imageView.setFitWidth(Main.width);
	}
	
	private void getLabels() {
		updateLabel(yes);
		updateLabel(no);
		yes.setOnMouseClicked(e -> {
			Game.availableHeroes.clear();
			Game.heroes.clear();
			Game.zombies.clear();
			try {
				Game.loadHeroes("assets/" + Main.mode + "/heroes.csv");
			} catch (IOException e1) {}
			mediaPlayer.stop();
			Main.window.getScene().setRoot((new SecondScene()).getRoot());
		});
		no.setOnMouseClicked(e -> Main.window.close());
	}
	
	private void updateLabel(Label label) {
		Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(0), new KeyValue(label.opacityProperty(), 0)),
				new KeyFrame(Duration.seconds(1), new KeyValue(label.opacityProperty(), 1)),
				new KeyFrame(Duration.seconds(2), new KeyValue(label.opacityProperty(), 0)));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		label.setCursor(Cursor.HAND);
		label.setOnMouseEntered(e -> {
			timeLine.play();
		});
		label.setOnMouseExited(e -> {
			timeLine.stop();
			label.setOpacity(1);
		});
	}
	
	private void updateLabelSize(Label label , double width , double height , double prev) {
		double size = prev * Math.sqrt((width*height) / (720*1280)) ;
		label.setStyle("-fx-font-size : "+size+ " ;");
	}
	
}
