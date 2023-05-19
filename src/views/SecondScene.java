package views;

import java.util.ArrayList;

import engine.Game;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import model.characters.Hero;

/**
 * class dealing with the Second Scene.
 * it has attributes :
 * <ul>
 * <li>wallpaper which represents the wallpaper used in this scene</li>
 * <li>label which contains the info of the hero currently selected</li>
 * <li>selectYourHero label which displays "select your hero" message</li>
 * <li>row and column represent the location of the currently selected hero</li>
 * <li>dimension1 and dimension2 are the dimensions of the grid (used for testing)</li>
 * <li>mapPane is a matrix contains all the stackPanes used</li>
 * </ul>
 *  
 * @author Ahmed Hussein
 *
 */

public class SecondScene {

	private static ImageView wallpaper;
	private static Label label = new Label();
	private static Label selectYourHero ;
	private static int row = 0 ;
	private static int column = 0;
	private static int dimension1 = 1 ;
	private static int dimension2 = 8 ;
	private static StackPane [][] mapPane = new StackPane [dimension1][dimension2] ; 

	/**
	 * the controller method in the class.
	 * 
	 * {@link createWallpaper}
	 * {@link updateLabel}
	 * {@link createSelectYourHeroLabel}
	 * {@link createGridAndMap}
	 * {@link createScene}
	 * 
	 * @return the secondScene
	 */
	
	public Scene getScene() {
		
		createWallpaper();
		updateLabel(170,170,"San Francisco");
		
		//gridPane
		GridPane gridPane = new GridPane();
		gridPane.setHgap(6);
		gridPane.setVgap(4);
		gridPane.setAlignment(Pos.BOTTOM_CENTER);
		gridPane.setTranslateY(-0.01*Main.height);
		
		//map
		Hero [][] map = new Hero [dimension1][dimension2];
		
		createSelectYourHeroLabel();
		createGridAndMap(map,gridPane);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setBottom(gridPane);
		borderPane.setRight(label);
		
		label.setTranslateY(0.8*Main.height);
		label.setTranslateX(-0.01*Main.width);
		
		// the root of the scene
		StackPane root = new StackPane();
		root.getChildren().addAll(wallpaper, borderPane , selectYourHero);
		
		return createScene(root, map);
	}
	
	/**
	 * A helper method that create a stackPane containing the hero image.
	 * 
	 * 
	 * @param h the hero
	 * @param x the x_location of the stackPane on the grid
	 * @param y the y_location of the stackPane on the grid
	 * @return stackPane
	 */

	private static StackPane hero(Hero h , int x , int y) {
		StackPane res = new StackPane();
		ImageView tmp = new ImageView(h.getImage());
		Rectangle back = new Rectangle(85,85);
		back.setFill(Color.GRAY);
		res.getChildren().add(back);
		tmp.setFitHeight(80);
		tmp.setFitWidth(80);
		res.getChildren().add(tmp);
		res.setOnMouseEntered(e -> {
			( (Rectangle) mapPane[row][column].getChildren().get(0) ).setFill(Color.GRAY); 
			back.setFill(Color.BEIGE.brighter());
			wallpaper.setImage(h.getWallpaper());
			label.setText(getHeroInfo(h));
			row = x ; column = y;
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
	
	/**
	 * An initializer method
	 * <p>
	 * 		it creates the grid that contains all the heroes images and the map that 
	 * 		contains the heroes in the same location as the grid so we can have direct 
	 * 		access to them.
	 * </p>
	 * 
	 * @param map
	 * @param gridPane
	 */
	
	private static void createGridAndMap(Hero [][] map , GridPane gridPane) {
		ArrayList<Hero> allHeroes = Game.availableHeroes ;
		int count = 0 ;
		for(int i = 0 ; i < dimension1 ; i++) {
			for(int j = 0 ; j < dimension2 ; j++) {
				map[i][j] = allHeroes.get(count);
				StackPane tmp = hero(allHeroes.get(count++) , i , j) ;
				gridPane.add(tmp, j, i);
				mapPane[i][j] = tmp ;
			}
		}
	}
	
	/**
	 * A helper method called to get the information if a given hero.
	 * 
	 * @param h
	 * @return string contains all required info
	 */
	
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
	
	/**
	 * An initializer method 
	 * <p>
	 * 		initializes and creates the wallpaper imageView and give it the proper
	 * 		dimensions and gives it an initial value.
	 * </p>
	 */
	
	private static void createWallpaper () {
		wallpaper = new ImageView();
		wallpaper.setFitHeight(Main.height);
		wallpaper.setFitWidth(Main.width);
		wallpaper.setImage(Game.availableHeroes.get(0).getWallpaper());
	}
	
	/**
	 * Another initializer method.
	 * <p>
	 * 		updates the label size and font.
	 * </p>
	 * 
	 * still requires styling.
	 * 
	 * @param width
	 * @param height
	 * @param font
	 */
	
	private static void updateLabel (int width , int height , String font) {
		label.setMaxHeight(width);
		label.setMaxWidth(height);
		label.setFont(new Font(font , Math.min(width, height)/9.5));
		label.setTextFill(Color.GHOSTWHITE);
		label.setText(getHeroInfo(Game.availableHeroes.get(0)));
		label.setTextAlignment(TextAlignment.CENTER);
	}
	
	/**
	 * An initializer method.
	 * <p>
	 * 		creates the selectYourHero label and its animation.
	 * </p>
	 */
	
	private static void createSelectYourHeroLabel() {
		selectYourHero = new Label("Select Your Hero");
		selectYourHero.setFont(new Font("Impact", 34*Main.height*Main.width / (1280*720)));
		selectYourHero.setTextFill(Color.GAINSBORO);
		Timeline timeLine = new Timeline(
				new KeyFrame(Duration.seconds(0), new KeyValue(selectYourHero.opacityProperty(), 0)),
				new KeyFrame(Duration.seconds(1), new KeyValue(selectYourHero.opacityProperty(), 1)),
				new KeyFrame(Duration.seconds(2), new KeyValue(selectYourHero.opacityProperty(), 0)));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
	}
	
	/**
	 * A helper method used to create the scene and its actions.
	 * 
	 * 
	 * @param root the stackPane containing all items 
	 * @param map contains all heros
	 * @return the final scene
	 */
	
	private static Scene createScene (StackPane root , Hero [][] map) {
		Scene scene = new Scene(root, Main.width, Main.height);
		
		scene.setOnKeyPressed(e -> {
			( (Rectangle) mapPane[row][column].getChildren().get(0)).setFill(Color.GRAY);
			switch(e.getCode()) {
				case W -> row = Math.max(row-1, 0);
				case A -> column = Math.max(column-1, 0);
				case S -> row = Math.min(row+1, dimension1-1);
				case D -> column = Math.min(column+1, dimension2-1);
				case ENTER ->{
					Game.startGame(map[row][column]);
					Main.window.setScene((new GameScene()).gameScene());
				}
			}
			( (Rectangle) mapPane[row][column].getChildren().get(0))
			.setFill(Color.BEIGE.brighter());
			label.setText(getHeroInfo(map[row][column]));
			wallpaper.setImage(map[row][column].getWallpaper());
		});
		
		scene.widthProperty().addListener((observable , oldWidth , newWidth) -> {
			Main.width = (double) newWidth ;
			wallpaper.setFitWidth((double) newWidth);
			selectYourHero.setFont(new Font("Impact", 34*Math.min(Main.height , Main.width) / 720));
			//label.setTranslateX((double)newWidth - (double)oldWidth);
		});
		
		scene.heightProperty().addListener((observable , oldHeight , newHeight) -> {
			Main.height = (double) newHeight ;
			wallpaper.setFitHeight((double) newHeight);
			selectYourHero.setFont(new Font("Impact", 34*Math.min(Main.height , Main.width) / 720));
			//label.setTranslateY((double)newHeight - (double)oldHeight);
		});
		
		return scene;
	}
	
}