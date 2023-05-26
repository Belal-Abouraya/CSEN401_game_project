package views;

import java.io.File;
import java.text.DecimalFormat;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.characters.Hero;

/**
 * @author Belal Abouraya
 */
public class TutorialScene {
	private StackPane root;
	private VBox vBox;
	final private static double FONTSIZE = 30, IMAGEWIDTH = 50, IMAGEHEIGHT = 50;
	private double fontSize = FONTSIZE, imageWidth = IMAGEWIDTH, imageHeight = IMAGEHEIGHT;
	private Image wKey = Hero.loadIcon("w"), aKey = Hero.loadIcon("a"), sKey = Hero.loadIcon("s"),
			dKey = Hero.loadIcon("d"), qKey = Hero.loadIcon("q"), eKey = Hero.loadIcon("e"), rKey = Hero.loadIcon("r"),
			fKey = Hero.loadIcon("f"), hKey = Hero.loadIcon("h"), rClick = Hero.loadIcon("rightclick"),
			lClick = Hero.loadIcon("leftclick");

	public TutorialScene() {
		root = new StackPane();
		Image image = null;
		try {
			String path = "assets/" + Main.mode + "/images/wallpapers/tutorialscene.jpg";
			image = new Image(new File(path).toURI().toURL().toExternalForm());
		} catch (Exception e) {
		}
		ImageView imageView = new ImageView(image);

		imageView.setFitWidth(GameScene.SCENEWIDTH);
		imageView.setFitHeight(GameScene.SCENEHEIGHT);
		imageView.setFitHeight(873);

		root.getChildren().add(imageView);
		createStack();

		root.widthProperty().addListener((obs, OldWidth, newWidth) -> resizeWidth(obs, OldWidth, newWidth));
		root.heightProperty().addListener((obs, OldHeight, newHeight) -> resizeHeight(obs, OldHeight, newHeight));
		root.getStylesheets().add(this.getClass().getResource(Main.mode + ".css").toExternalForm());
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

		root.getChildren().add(vBox);
	}

	public StackPane getRoot() {
		return root;
	}

	private void create(Image image, String message) {
		HBox hBox = new HBox(20);
		Label l = new Label(message);
		l.setStyle("-fx-font-size:" + fontSize);
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(imageWidth);
		imageView.setFitHeight(imageWidth);
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