package views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import model.characters.Hero;
import engine.Game;

public class SecondScene {
	
	private ImageView wallpaper ;
	
	public Scene getScene() {
		wallpaper = new ImageView();
		GridPane gridPane = new GridPane();
		int size = Game.availableHeroes.size();
		int count = 0 ;
		for(int i = 0 ; i < 2 ; i++) {
			for(int j = 0 ; j < size/2 + (i == 0 ? 0 : size%2) ; j++) {
				gridPane.add(hero(Game.availableHeroes.get(count++)), j, i);
			}
		}
		gridPane.setAlignment(Pos.BOTTOM_CENTER);
		StackPane pane = new StackPane();
		pane.getChildren().addAll(wallpaper,gridPane);
		Scene scene = new Scene(pane,1280,720);
		return scene;
	}
	
	private StackPane hero(Hero h) {
		StackPane res = new StackPane();
		res.setBackground(new Background(new BackgroundImage(h.getImage(), null, null, null, null)));
		res.setOnMouseEntered(e -> wallpaper.setImage(h.getWallpaper()));
		res.setOnMouseClicked(e -> Main.window.setScene((new GameScene()).gameScene()));
		return res ;
	}
}
