package views;

import engine.Game;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;

/**
 * 
 */
public class Main extends Application {
	
	static Stage window ;
	static Scene firstScene ;
	static Scene secondScene ;
	static ImageView secondSceneWallpaper ;

	@Override
	public void init() {

	}

	@Override
	public void start(Stage primaryStage) {
		window = primaryStage;
		window.setTitle("Last of Us : Legacy");
		createFirstScene();
		createSecondScene();
		window.setScene(firstScene);
		window.show();
	}

	public static void main(String[] args) {
		//loading heroes manually
		Hero [] arr = new Hero [8] ;
		arr[0] = new Fighter("Joel Miller", 140, 5, 30);
		arr[1] = new Medic("Ellie Williams", 110, 6, 15);
		arr[2] = new Explorer("Tess", 80, 6, 20);
		arr[3] = new Explorer("Riley Abel", 90, 5, 25);
		arr[4] = new Explorer("Tommy Miller", 95, 5, 25);
		arr[5] = new Medic("Bill" , 100,7,10);
		arr[6] = new Fighter("David", 150, 4, 35);
		arr[7] = new Medic("Henry Burell", 105, 6, 15);
		for(int i =0 ; i < 8 ; i++ ) {
			Game.availableHeroes.add(arr[i]);
		}
		
		launch(args);
	}
	
	private static void createFirstScene() {
		StackPane stackPane = new StackPane();
		Image wallpaper = new Image("FirstScene.jpg");
		ImageView imageView = new ImageView(wallpaper);
		imageView.setFitHeight(720);
		imageView.setFitWidth(1280);
		Label label = new Label("Press Enter to Start");
		label.setFont(new Font("Courier New" , 46));
		label.setTextFill(Color.CORAL);
		label.setTranslateY(265);
		Timeline timeLine = new Timeline(
				new KeyFrame(Duration.seconds(0), new KeyValue(label.opacityProperty(),0)),
				new KeyFrame(Duration.seconds(1), new KeyValue(label.opacityProperty(),1)),
				new KeyFrame(Duration.seconds(2), new KeyValue(label.opacityProperty(),0))
				);
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
		stackPane.getChildren().addAll(imageView,label);
		firstScene = new Scene(stackPane,1280,720);
		firstScene.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER) {
				window.setScene(secondScene);
			}
		});
	}
	
	private static void createSecondScene() {
		StackPane pane = new StackPane();
		secondSceneWallpaper = new ImageView();
		secondSceneWallpaper.setFitHeight(720);
		secondSceneWallpaper.setFitWidth(1280);
		GridPane gridPane = new GridPane();
		gridPane.setHgap(8);
		gridPane.setVgap(8);
		int size = Game.availableHeroes.size();
		int count = 0 ;
		for(int i = 0 ; i < size/2 ; i++) {
			gridPane.add(hero(Game.availableHeroes.get(count++)), i, 0);
		}
		for(int i = 0 ; i < size/2 + size%2 ; i++) {
			gridPane.add(hero(Game.availableHeroes.get(count++)), i, 1);
		}
		gridPane.setAlignment(Pos.BOTTOM_CENTER);
		pane.getChildren().addAll(secondSceneWallpaper,gridPane);
		secondScene = new Scene(pane,1280,720);
	}
	
	private static StackPane hero(Hero h) {
		StackPane res = new StackPane();
		ImageView tmp = new ImageView(h.getImage());
		tmp.setFitHeight(100);
		tmp.setFitWidth(100);
		res.getChildren().add(tmp);
		res.setOnMouseEntered(e -> secondSceneWallpaper.setImage(h.getWallpaper()));
		res.setOnMouseClicked(e -> {
			Game.startGame(h);
			window.setScene((new GameScene()).gameScene());
		});
		return res ;
	}
}
