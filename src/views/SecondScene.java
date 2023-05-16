package views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import engine.Game;

public class SecondScene {

	private static ImageView wallpaper;
	private static Label text = new Label();

	public Scene getScene() {
		wallpaper = new ImageView();
		wallpaper.setFitHeight(720);
		wallpaper.setFitWidth(1280);
		GridPane gridPane = new GridPane();
		gridPane.setHgap(8);
		text.setMinSize(250, 500);
		text.setBackground(new Background(new BackgroundFill(Color.BEIGE, null, null)));
		text.setAlignment(Pos.CENTER_RIGHT);
		int count = 0;
		for (Hero h : Game.availableHeroes) {
			gridPane.add(hero(h), count++, 0);
		}
		gridPane.setAlignment(Pos.BOTTOM_CENTER);
		StackPane pane = new StackPane();
		pane.getChildren().addAll(wallpaper, gridPane, text);
		Scene scene = new Scene(pane, 1280, 720);
		return scene;
	}

	private StackPane hero(Hero h) {
		StackPane res = new StackPane();
		ImageView tmp = new ImageView(h.getImage());
		tmp.setFitHeight(80);
		tmp.setFitWidth(80);
		res.getChildren().add(tmp);
		res.setOnMouseEntered(e -> {
			wallpaper.setImage(h.getWallpaper());
			String type = "";
			if (h instanceof Fighter)
				type = "Fighter";
			else if (h instanceof Medic)
				type = "Medic";
			else
				type = "Explorer";
			text.setText("Name : " + h.getName() + "\nType : " + type + "\nAttack Damage : " + h.getAttackDmg()
					+ "\nMax number of Actions per Turn :" + h.getMaxActions() + "\nMax Health Points : "
					+ h.getMaxHp());
		});
		res.setOnMouseClicked(e -> {
			Game.startGame(h);
			Main.window.setScene((new GameScene()).gameScene());
		});
		return res;
	}
}