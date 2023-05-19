package views;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import engine.Game;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 
 */
public class Main extends Application {
	static Stage window;
	static Scene firstScene;
	static double width = 1280;
	static double height = 720;

	@Override
	public void start(Stage primaryStage) throws MalformedURLException {
		window = primaryStage;
		window.setTitle("Last of Us : Legacy");
		createFirstScene();
		firstScene.getStylesheets().add(this.getClass().getResource("classic.css").toExternalForm());
		window.setScene(firstScene);
//		window.setScene((new FirstScene()).getScene());
		window.addEventHandler(GameEvent.WIN, e-> window.setScene((new WiningScene()).getScene()));
		window.addEventHandler(GameEvent.GAME_OVER, e-> window.setScene((new LosingScene()).getScene()));
		window.show();
	}

	public static void main(String[] args) {
		try {
			Game.loadHeroes("src/engine/Heros.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		launch(args);
	}

	private static void createFirstScene() {
		StackPane stackPane = new StackPane();
		Image wallpaper = null;
		try {
			wallpaper = new Image(new File("src/FirstScene.jpg").toURI().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageView imageView = new ImageView(wallpaper);
		imageView.setFitHeight(height);
		imageView.setFitWidth(width);
		
		Label label = new Label("Press Enter to Start");
		label.setId("StartLabel");
		label.setTranslateY(Math.floor(0.4 * height));
		Timeline timeLine = new Timeline(
				new KeyFrame(Duration.seconds(0), new KeyValue(label.opacityProperty(), 0)),
				new KeyFrame(Duration.seconds(1), new KeyValue(label.opacityProperty(), 1)),
				new KeyFrame(Duration.seconds(2), new KeyValue(label.opacityProperty(), 0)));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
		stackPane.getChildren().addAll(imageView, label);
		
		firstScene = new Scene(stackPane, width, height);
		firstScene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				window.setScene((new SecondScene()).getScene());
			}
//			if(e.getCode() == KeyCode.F) {
//				height = 1080 ;
//				width = 1960 ;
//				window.setFullScreen(true);
//				isFullScreen = true ;
//			}
//			if(e.getCode() == KeyCode.ESCAPE) {
//				height = 720 ;
//				width = 1280 ;
//				isFullScreen = false ;
//				window.setFullScreen(false);
//			}
		});
		firstScene.widthProperty().addListener((observable , oldWidth , newWidth) -> {
			width = (double) newWidth ;
			imageView.setFitWidth((double) newWidth);
			double newSize = 30 * Math.min(width, height) / 720 ;
			label.setStyle("-fx-font-size: "+newSize+";");
		});
		firstScene.heightProperty().addListener((observable , oldHeight , newHeight) -> {
			height = (double) newHeight ;
			imageView.setFitHeight((double) newHeight);
			double newSize = 30 * Math.min(width, height) / 720 ;
			label.setStyle("-fx-font-size: "+newSize+";");
			label.setTranslateY(Math.floor(0.4 * height));
		});
	}
}
//package views;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
//import engine.Game;
//import javafx.application.Application;
//import javafx.event.Event;
//import javafx.event.EventHandler;
//import javafx.scene.image.Image;
//import javafx.stage.Stage;
//import junit.framework.Test;
//import model.characters.Explorer;
//
///**
// * 
// */
//public class Main extends Application {
//	@Override
//	public void init() {
//	}
//
//	@Override
//	public void start(Stage primaryStage) throws FileNotFoundException, IOException {
//
//		Game.loadHeroes("src\\engine\\Heros.csv");
//		// Image i = new Image(
//		// getClass().getResourceAsStream("D:\\eclipse-workspace\\CSEN401_game_project\\src\\images\\Bill.jpg"));
//
//		// Image i = new Image(new FileInputStream("C:\\Users\\Belal\\Downloads\\
//		// billtlou.jpeg"));
//		Image image1 = new Image(new File("C:/Users/Belal/Downloads/billtlou.jpeg").toURI().toURL().toExternalForm());
//		Game.startGame(Game.availableHeroes.get(0));
//		GameScene g = new GameScene();
//		primaryStage.addEventHandler(GameEvent.GAME_OVER, new EventHandler<Event>() {
//
//			@Override
//			public void handle(Event arg0) {
//				System.out.println("You lose");
//			}
//		});
//		primaryStage.addEventHandler(GameEvent.WIN, new EventHandler<Event>() {
//
//			@Override
//			public void handle(Event arg0) {
//				System.out.println("You win");
//
//			}
//		});
//		primaryStage.setScene(g.gameScene());
//		primaryStage.show();
//	}
//
//	public static void main(String[] args) {
//		launch(args);
//	}
//}
