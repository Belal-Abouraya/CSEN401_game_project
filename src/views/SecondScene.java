package views;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;

import java.util.ArrayList;

import engine.Game;

public class SecondScene {

	private static ImageView wallpaper;
	private static Label label = new Label();
	private static int row = 0 ;
	private static int column = 0;
	private static int diminsion1 = 1 ;
	private static int diminsion2 = 8 ;

	public Scene getScene() {
		wallpaper = new ImageView();
		wallpaper.setFitHeight(Main.height);
		wallpaper.setFitWidth(Main.width);
		label.setMaxHeight(170);
		label.setMaxWidth(170);
		label.setFont(new Font("San Francisco" , 18));
		label.setTextFill(Color.GHOSTWHITE);
		GridPane gridPane = new GridPane();
		Hero [][] map = new Hero [diminsion1][diminsion2];
		Label label1 = new Label("Select Your Hero");
		label1.setFont(new Font("Impact", 34));
		label1.setTextFill(Color.GAINSBORO);
		Timeline timeLine = new Timeline(
				new KeyFrame(Duration.seconds(0), new KeyValue(label1.opacityProperty(), 0)),
				new KeyFrame(Duration.seconds(1), new KeyValue(label1.opacityProperty(), 1)),
				new KeyFrame(Duration.seconds(2), new KeyValue(label1.opacityProperty(), 0)));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
//		Rectangle [][] rectangles = new Rectangle [diminsion1][diminsion2] ;
		gridPane.setHgap(6);
		gridPane.setVgap(4);
		createGridAndMap(map,gridPane);
		wallpaper.setImage(Game.availableHeroes.get(0).getWallpaper());
		gridPane.setAlignment(Pos.BOTTOM_CENTER);
		gridPane.setTranslateY(-0.01*Main.height);
		label.setTextAlignment(TextAlignment.CENTER);
		label.setAlignment(Pos.CENTER_RIGHT);
		StackPane pane = new StackPane();
		pane.getChildren().addAll(wallpaper, gridPane , label1 , label);
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
		scene.widthProperty().addListener((observable , oldWidth , newWidth) -> {
			Main.width = (double) newWidth ;
			wallpaper.setFitWidth((double) newWidth);
		});
		scene.heightProperty().addListener((observable , oldHeight , newHeight) -> {
			Main.height = (double) newHeight ;
			wallpaper.setFitHeight((double) newHeight);
		});
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
			label.setText(getHeroInfo(h));
		});
		res.setOnMouseExited(e -> {
			back.setFill(Color.GRAY);
			label.setText("");
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
	
	private static String getHeroInfo (Hero h) {
		String res = "";
		String name = h.getName();
		if(name.split(" ").length > 1)
			name = name.split(" ")[0] ;
		res = name + " , " + h.getType() + "\n" 
				+ "Attack Damage : " +  h.getAttackDmg()+ "\n" 
				+ "Health Points : " + h.getMaxHp() + "\n" 
				+ "Action Points : " + h.getActionsAvailable() ;
		return res ;
	}
	
}