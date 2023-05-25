package views;

import java.io.File;
import java.util.ArrayList;

import engine.Game;
import model.characters.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.control.Label;

public class SecondScene {
	
	private StackPane sceneBack = new StackPane();
	private GridPane heroes = new GridPane();
	private int d1 = 4 , d2 = 2 ;
	private int row = 0 , column = 0 ;
	private Hero [][] map = new Hero [d1][d2] ;
	
	private Color brightColor = Color.DARKGRAY.brighter();
	private Color darkColor = Color.DARKGRAY;
	private StackPane[][] mapPane = new StackPane[d1][d2];
	
	private double RectangleWidth ;
	private double RectangleHeight ;
	private ImageView wallpaper ;
	private ImageView model = new ImageView();
	private Label selectYourHero ;
	private Label info = new Label();
	private HBox hbox = new HBox(Main.width/7.05);
	
	
	public Scene getScene() {
		Main.mediaPlayer.play();
		Main.mediaPlayer.setCycleCount(Timeline.INDEFINITE);
		createBackGround();
		createSelectYourHeroLabel();
		createHeroes();
		sceneBack.getChildren().add(wallpaper);
		info.setTranslateX(4);
		info.setId("info");
		info.setMinSize(RectangleWidth*2, RectangleHeight*3);
		hbox.setAlignment(Pos.CENTER_LEFT);
		hbox.setTranslateY(Main.height/10);
		sceneBack.getChildren().addAll( selectYourHero , heroes);
		Scene scene = new Scene(sceneBack , Main.width , Main.height);
		scene.getStylesheets().add(this.getClass().getResource(Game.mode + ".css").toExternalForm());
		scene.setOnKeyPressed(e -> {
			((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(darkColor);
			switch (e.getCode()) {
				case W -> row = Math.max(row - 1, 0);
				case A -> column = Math.max(column - 1, 0);
				case S -> row = Math.min(row + 1, d1 - 1);
				case D -> column = Math.min(column + 1, d2 - 1);
				case ENTER -> {
					Game.startGame(map[row][column], Main.mode);
					Main.mediaPlayer.stop();
					Main.window.setScene((new GameScene()).getScene());
				}
			}
			((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(brightColor);
			sceneBack.getChildren().clear();
			hbox.getChildren().clear();
			info.setText(createInfo(map[row][column]));
			hbox.getChildren().addAll(info);
			model.setImage(map[row][column].getModel());
			model.setFitHeight( (1920 / 3) * Math.pow(Main.height/ 720 , 0.85) );
			model.setFitWidth( (1480 / 3) * Math.sqrt(Main.width / 1280) );
			sceneBack.getChildren().addAll(wallpaper , model , hbox , heroes);
		});
		scene.widthProperty().addListener((observable , oldWidth , newWidth) -> {
			double nw = (double) newWidth ;
			Main.width = nw ;
			wallpaper.setFitWidth(nw);
			updateLabelSize(selectYourHero, nw, Main.height, 30);
			model.setFitHeight( (1920 / 3) * Math.pow(Main.height/ 720 , 0.85) );
			model.setFitWidth( (1480 / 3) * Math.sqrt(Main.width / 1280) );
			RectangleHeight =  Math.pow(Main.height * Main.width , 1.0/3) / 0.98 ;
			RectangleWidth =  Math.pow(Main.height * Main.width , 1.0/3) / 0.98 ;
			updateMapWidth();
			updateLabel();
		});
		scene.heightProperty().addListener((observable , oldHeight , newHeight) -> {
			double nh = (double) newHeight ;
			Main.height = nh ;
			wallpaper.setFitHeight(nh);
			updateLabelSize(selectYourHero, Main.width, nh, 30);
			hbox.setTranslateY(Main.height/10);
			model.setFitHeight( (1920 / 3) * Math.pow(Main.height/ 720 , 0.85) );
			model.setFitWidth( (1480 / 3) * Math.sqrt(Main.width / 1280) );
			RectangleHeight =  Math.pow(Main.height * Main.width , 1.0/3) / 0.98 ;
			RectangleWidth =  Math.pow(Main.height * Main.width , 1.0/3) / 0.98 ;
			updateMapHeight();
			updateLabel();
		});
		return scene ;
	}
	
	private void createBackGround() {
		Image image = null;
		try {
			String path = "assets/" + Main.mode + "/images/wallpapers/secondscene.jpeg";
			image = new Image(new File(path).toURI().toURL().toExternalForm());
		}catch(Exception e) {}
		wallpaper = new ImageView(image);
		wallpaper.setFitHeight(Main.height);
		wallpaper.setFitWidth(Main.width);
	}
	
	private void createHeroes() {
		ArrayList<Hero> allHeroes = Game.availableHeroes;
		int count = 0;
		for (int i = 0; i < d1; i++) {
			for (int j = 0; j < d2; j++) {
				map[i][j] = allHeroes.get(count);
				StackPane tmp = hero(allHeroes.get(count++), i, j);
				heroes.add(tmp, j, i);
				mapPane[i][j] = tmp;
			}
		}
		heroes.setAlignment(Pos.CENTER_RIGHT);
		heroes.setTranslateX(-2);
		heroes.setHgap(2);
		heroes.setVgap(2);
	}
	
	private StackPane hero(Hero h, int x, int y) {
		StackPane res = new StackPane();
		RectangleHeight =  Math.pow(Main.height * Main.width , 1.0/3) / 0.98 ;
		RectangleWidth =  Math.pow(Main.height * Main.width , 1.0/3) / 0.98 ;
		
		Rectangle back = new Rectangle(RectangleWidth, RectangleHeight);
		back.setArcHeight(10);
		back.setArcWidth(10);
		back.setFill(darkColor);
		back.setOpacity(0.2);
		ImageView icon = new ImageView(h.getIcon());
		icon.setFitHeight(RectangleHeight-5);
		icon.setFitWidth(RectangleWidth-5);
		res.getChildren().addAll(back , icon);
		
		res.setOnMouseEntered(e -> {
			((Rectangle) mapPane[row][column].getChildren().get(0)).setFill(darkColor);
			back.setFill(brightColor);
			row = x; column = y;
			sceneBack.getChildren().clear();
			hbox.getChildren().clear();
//			vbox = createModel(map[row][column]);
			model.setImage(map[row][column].getModel());
			model.setFitHeight( (1920 / 3) * Math.pow(Main.height/ 720 , 0.85) );
			model.setFitWidth( (1480 / 3) * Math.sqrt(Main.width / 1280) );
			info.setText(createInfo(map[row][column]));
			hbox.getChildren().addAll(info);
			sceneBack.getChildren().addAll(wallpaper , model , hbox , heroes);
		});
		res.setOnMouseClicked(e -> {
			Game.startGame(h , Main.mode);
			Main.window.setScene((new GameScene()).getScene());
		});
		
		return res;
	}
	
//	private static VBox createModel(Hero h) {
//		Image image = h.getModel();
//		ImageView imageView = new ImageView(image);
//		
//		imageView.setFitHeight((1920/3.0) * (Main.height / 720));
//		imageView.setFitWidth((1480/3.0) * (Main.width / 1280));
//		
////		info.setTranslateX(Main.width/5);
//		
//		VBox res = new VBox(4);
//		res.getChildren().addAll(imageView);
//		
////		res.setTranslateY((Main.height-res.getHeight())/10);
//		
//		return res ;
//	}
	
	private void createSelectYourHeroLabel() {
		selectYourHero = new Label("Select Your Hero");
		selectYourHero.setId("SelectHeroLabel");
		Timeline timeLine = new Timeline(
				new KeyFrame(Duration.seconds(0), new KeyValue(selectYourHero.opacityProperty(), 0)),
				new KeyFrame(Duration.seconds(1), new KeyValue(selectYourHero.opacityProperty(), 1)),
				new KeyFrame(Duration.seconds(2), new KeyValue(selectYourHero.opacityProperty(), 0)));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
	}
	
	private String createInfo (Hero h) {
		return h.getName() + "\n" + "\n" +
				h.getType() + "\n" + "\n" +
				"Health : "+ h.getMaxHp() + "\n" +
				"Actions per Turn : "+h.getMaxActions() + "\n" + 
				"Attack Damage : "+h.getAttackDmg() ;
	}
	
	private void updateLabelSize(Label label , double width , double height , double prev) {
		double size = prev * Math.sqrt((width*height) / (720*1280)) ;
		label.setStyle("-fx-font-size : "+size+ " ;");
	}
	
	private void updateVBox() {
		hbox.setSpacing(Main.width/7.05);
		hbox.setTranslateY(Main.height/8);
	}
	
	private void updateMapHeight () {
		for (int i = 0 ; i < d1 ; i++) {
			for(int j = 0 ; j < d2 ; j++) {
				double width =  Math.pow(Main.height * Main.width , 1.0/3) / 0.98 ;
				((Rectangle) mapPane[i][j].getChildren().get(0)).setWidth(width);
				((ImageView) mapPane[i][j].getChildren().get(1)).setFitWidth(width-5);
				double height =  Math.pow(Main.height * Main.width , 1.0/3) / 0.98 ;
				((Rectangle) mapPane[i][j].getChildren().get(0)).setHeight(height);
				((ImageView) mapPane[i][j].getChildren().get(1)).setFitHeight(height-5);
			}
		}
	}
	
	private void updateMapWidth () {
		for (int i = 0 ; i < d1 ; i++) {
			for(int j = 0 ; j < d2 ; j++) {
				double width =  Math.pow(Main.height * Main.width , 1.0/3) / 0.98 ;
				((Rectangle) mapPane[i][j].getChildren().get(0)).setWidth(width);
				((ImageView) mapPane[i][j].getChildren().get(1)).setFitWidth(width-5);
				double height =  Math.pow(Main.height * Main.width , 1.0/3) / 0.98 ;
				((Rectangle) mapPane[i][j].getChildren().get(0)).setHeight(height);
				((ImageView) mapPane[i][j].getChildren().get(1)).setFitHeight(height-5);
			}
		}
	}
	
	private void updateLabel() {
		info.setMinHeight(RectangleWidth *2);
		info.setMinWidth(RectangleHeight *3);
		double size = 15 * Math.pow( (Main.height * Main.width) / (1280*720) , 1.0/3);
		info.setStyle("-fx-font-size : " + size + " ;");
	}

}
