package views;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.shape.Rectangle;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;

import java.util.ArrayList;

import engine.Game;

public class SecondScene {

	private static ImageView wallpaper;
	private static int row = 0 ;
	private static int column = 0;
	private static int diminsion1 = 1 ;
	private static int diminsion2 = 8 ;

	public Scene getScene() {
		wallpaper = new ImageView();
		wallpaper.setFitHeight(Main.height);
		wallpaper.setFitWidth(Main.width);
		GridPane gridPane = new GridPane();
		Hero [][] map = new Hero [diminsion1][diminsion2];
//		Rectangle [][] rectangles = new Rectangle [diminsion1][diminsion2] ;
		gridPane.setHgap(4);
		createGridAndMap(map,gridPane);
		wallpaper.setImage(Game.availableHeroes.get(0).getWallpaper());
		gridPane.setAlignment(Pos.BOTTOM_CENTER);
		gridPane.setTranslateY(-4);
		StackPane pane = new StackPane();
		pane.getChildren().addAll(wallpaper, gridPane);
		Scene scene = new Scene(pane, Main.width, Main.height);
//		scene.setOnKeyPressed(e -> {
////			rectangles[row][column].setFill(Color.GRAY);
//			switch(e.getCode()) {
//				case W -> row = Math.max(row-1, 0);
//				case A -> column = Math.max(column-1, 0);
//				case S -> row = Math.min(row+1, diminsion1-1);
//				case D -> column = Math.min(column+1, diminsion2-1);
//				case ENTER ->{
//					Game.startGame(map[row][column]);
//					Main.window.setScene((new GameScene()).gameScene());
//				}
//			}
////			rectangles[row][column].setFill(Color.BEIGE.brighter());
//			wallpaper.setImage(map[row][column].getWallpaper());
//		});
		return scene;
	}

	private static StackPane hero(Hero h) {
		StackPane res = new StackPane();
		ImageView tmp = new ImageView(h.getImage());
		Rectangle back = new Rectangle(85,85);
		back.setFill(Color.GRAY);
		res.getChildren().add(back);
		tmp.setFitHeight(80);
		tmp.setFitWidth(80);
		res.getChildren().add(tmp);
		res.setOnMouseEntered(e -> {
			back.setFill(Color.BEIGE.brighter());
			wallpaper.setImage(h.getWallpaper());
		});
		res.setOnMouseExited(e -> {
			back.setFill(Color.GRAY);
		});
		res.setOnMouseClicked(e -> {
			Game.startGame(h);
			Main.window.setScene((new GameScene()).gameScene());
		});
		return res;
	}
	
	private static void createGridAndMap(Hero [][] map , GridPane gridPane) {
		ArrayList<Hero> allHeroes = Game.availableHeroes ;
		int count = 0 ;
		for(int i = 0 ; i < diminsion1 ; i++) {
			for(int j = 0 ; j < diminsion2 ; j++) {
				map[i][j] = allHeroes.get(count);
//				rectangles[i][j] = new Rectangle(85,85);
//				rectangles[i][j].setFill(Color.GRAY);
				gridPane.add(hero(allHeroes.get(count++)), j, i);
			}
		}
	}
}