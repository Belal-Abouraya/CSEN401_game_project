package views;

import java.text.DecimalFormat;

import engine.Game;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import model.characters.Hero;

/**
 * A class resposnible for showing the game controls to the user.
 * 
 * @author Belal Abouraya
 */
public class TutorialScene {
	private StackPane root;
	private VBox vBox;
	private MediaPlayer select = Main.loadEffect("select");

	final private static double FONTSIZE = 30, IMAGEWIDTH = 50, IMAGEHEIGHT = 50;
	private double fontSize = FONTSIZE, imageWidth = IMAGEWIDTH, imageHeight = IMAGEHEIGHT;
	private Image wKey = Hero.loadIcon("w"), aKey = Hero.loadIcon("a"), sKey = Hero.loadIcon("s"),
			dKey = Hero.loadIcon("d"), qKey = Hero.loadIcon("q"), eKey = Hero.loadIcon("e"), rKey = Hero.loadIcon("r"),
			fKey = Hero.loadIcon("f"), hKey = Hero.loadIcon("h"), rClick = Hero.loadIcon("rightclick"),
			lClick = Hero.loadIcon("leftclick");

	public TutorialScene(Parent prev) {
		root = new StackPane();
		ImageView imageView = new ImageView(Main.loadImage("wallpapers/tutorialscene.jpg"));

		imageView.setFitWidth(GameScene.SCENEWIDTH);
		imageView.setFitHeight(GameScene.SCENEHEIGHT);

		root.getChildren().add(imageView);
		createStack();

		root.widthProperty().addListener((obs, OldWidth, newWidth) -> resizeWidth(obs, OldWidth, newWidth));
		root.heightProperty().addListener((obs, OldHeight, newHeight) -> resizeHeight(obs, OldHeight, newHeight));
		root.getStylesheets().add(this.getClass().getResource(Game.mode + ".css").toExternalForm());
		Platform.runLater(() -> root.requestFocus());
		root.setFocusTraversable(true);
		root.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.H) {
				Main.play(select);
				Main.scene.setRoot(prev);
			}
		});
	}

	private void createStack() {
		vBox = new VBox();

		create(wKey, "Move up");
		create(aKey, "Move left");
		create(sKey, "Move down");
		create(dKey, "Move right");
		create(qKey, "Cure");
		create(eKey, "Use special action");
		create(rKey, "End turn");
		create(fKey, "fullscreen mode");
		create(hKey, "Tutorial menu");
		create(rClick, "Set target");
		create(lClick, "Attack");

		vBox.setAlignment(Pos.CENTER);
		vBox.setPadding(new Insets(20));
		root.getChildren().add(vBox);
	}

	public StackPane getRoot() {
		return root;
	}

	private void create(Image image, String message) {
		HBox hBox = new HBox(20);
		Label l = new Label(message);
		l.setStyle("-fx-text-fill: white;-fx-font-size:" + fontSize);
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(imageWidth);
		imageView.setFitHeight(imageHeight);
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.getChildren().add(imageView);
		hBox.getChildren().add(l);
		vBox.getChildren().add(hBox);
	}

	private void resizeHeight(ObservableValue<? extends Number> obs, Number oldHeight, Number newHeight) {
		double scale = (double) newHeight;
		scale /= GameScene.SCENEHEIGHT;
		DecimalFormat d = new DecimalFormat("#.##");
		scale = Double.parseDouble(d.format(scale));

		((ImageView) root.getChildren().get(0)).setFitHeight((double) newHeight);
		fontSize = Math.max(0.5, scale) * FONTSIZE;
		imageHeight = scale * IMAGEHEIGHT;
		root.getChildren().remove(1);
		createStack();
	}

	private void resizeWidth(ObservableValue<? extends Number> obs, Number oldWidth, Number newWidth) {
		double scale = (double) newWidth;
		scale /= GameScene.SCENEWIDTH;
		DecimalFormat d = new DecimalFormat("#.##");
		scale = Double.parseDouble(d.format(scale));

		((ImageView) root.getChildren().get(0)).setFitWidth((double) newWidth);
		fontSize = Math.max(0.5, scale) * FONTSIZE;
		imageWidth = scale * IMAGEWIDTH;
		root.getChildren().remove(1);
		createStack();
	}
}