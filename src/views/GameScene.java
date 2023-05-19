package views;

import java.awt.Point;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

import engine.Game;
import exceptions.GameActionException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.characters.Character;
import model.characters.Direction;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import model.characters.Zombie;
import model.collectibles.Collectible;
import model.collectibles.Vaccine;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;


/**
 * A Helper class that creates the game scene. It has only one attribute:
 * <ul>
 * <li>currentHero</li>
 * </ul>
 * 
 * @author Belal Abouraya
 * @author Rafael Samuel
 */
public class GameScene {
	Hero currentHero = Game.heroes.get(0);
	VBox Heroes ;

	/**
	 * The method called by the Main class to get the game scene. It creates a Scene
	 * object with all the required elements and logic of the game
	 * 
	 * @return the finished game scene object
	 */
	public Scene gameScene() {
		
		
		BorderPane root = new BorderPane();
		Scene gameScene = new Scene(root);
		
		// TODO Belal
		GridPane grid = new GridPane();
		updateGrid(Game.map, grid);
		
		// TODO Rafael
		Heroes = new VBox();
		updateHeroesStack(Game.heroes, Heroes);
		
		Label updates = new Label();
		BorderPane bottom = new BorderPane();
		// updates.setAlignment(Pos.BASELINE_LEFT);
		Button endTurn = new Button("End turn");
		endTurn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Game.endTurn();
				updateGrid(Game.map, grid);
				updateHeroesStack(Game.heroes,Heroes);
				if (Game.checkGameOver())
					grid.fireEvent(new GameEvent(GameEvent.GAME_OVER));
				else if (Game.checkWin())
					grid.fireEvent(new GameEvent(GameEvent.WIN));
			}
		});
		// endTurn.setAlignment(Pos.BASELINE_RIGHT);
		bottom.setLeft(updates);
		bottom.setRight(endTurn);
	

		root.setCenter(grid);
		root.setBottom(bottom);
		root.setLeft(Heroes);

		/*
		 * handles the game controls. W, A, S, D for movement Q for cure R for using the
		 * special action
		 */
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				Direction d = null;
				switch (e.getCode()) {
				case W -> d = Direction.UP;
				case A -> d = Direction.LEFT;
				case S -> d = Direction.DOWN;
				case D -> d = Direction.RIGHT;

				case Q -> {
					try {
						currentHero.cure();
					} catch (GameActionException e1) {
						updates.setText(e1.getMessage());
					}
				}
				case R -> {
					try {
						currentHero.useSpecial();
					} catch (GameActionException e1) {
						updates.setText(e1.getMessage());
					}
				}
				}

				if (d != null) {
					try {
						currentHero.move(d);
					} catch (GameActionException e1) {
						updates.setText(e1.getMessage());
					}
				}
				updateGrid(Game.map, grid);
				updateHeroesStack(Game.heroes,Heroes);

				if (Game.checkGameOver())
					grid.fireEvent(new GameEvent(GameEvent.GAME_OVER));
				else if (Game.checkWin())
					grid.fireEvent(new GameEvent(GameEvent.WIN));
			}
		});

		// handles mouse inputs. Left click for attack
		gameScene.setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				try {
					currentHero.attack();
				} catch (GameActionException e1) {
					updates.setText(e1.getMessage());
				}
				updateGrid(Game.map, grid);
				if (Game.checkGameOver())
					grid.fireEvent(new GameEvent(GameEvent.GAME_OVER));
				else if (Game.checkWin())
					grid.fireEvent(new GameEvent(GameEvent.WIN));
			}
		});

		return gameScene;
	}

	private void updateGrid(Cell[][] map, GridPane grid) {
		grid.getChildren().clear();
		for (int i = map.length - 1; i >= 0; i--) {
			for (int j = map[i].length - 1; j >= 0; j--) {
				int x = j;
				int y = getCoord(i);
				StackPane tmp = null;
				if (map[i][j].isVisible()) {
					if (map[i][j] instanceof CharacterCell) {
						tmp = characterStackPane(((CharacterCell) map[i][j]).getCharacter());
					} else if (map[i][j] instanceof CollectibleCell)
						tmp = collectibleStackPane(((CollectibleCell) map[i][j]).getCollectible());
					else
						tmp = characterStackPane(null);
				} else
					tmp = invisibleStackPane();
				grid.add(tmp, x, y);
 
			}
		}
	}

	private StackPane characterStackPane(Character c) {
		StackPane res = emptyStackPane();
		// handles setting the target
		res.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.SECONDARY) {
				currentHero.setTarget(c);
			}
		});

		// handles styling
		Rectangle front = null;
		if (c instanceof Hero) {
			front = new Rectangle(15, 15, Color.BLUE);
		} else if (c instanceof Zombie) {
			front = new Rectangle(15, 15, Color.RED);
		} else
			front = new Rectangle(15, 15, Color.WHITE);
		res.getChildren().add(front);
		return res;
	}

	private StackPane collectibleStackPane(Collectible s) {
		StackPane res = emptyStackPane();

		// handles styling
		Rectangle front = null;
		if (s instanceof Vaccine) {
			front = new Rectangle(15, 15, Color.GREEN);
		} else {
			front = new Rectangle(15, 15, Color.BROWN);
		}
		res.getChildren().add(front);
		return res;
	}

	private StackPane emptyStackPane() {
		StackPane res = new StackPane();
		Rectangle front = new Rectangle(15, 15, Color.WHITE);
		res.getChildren().add(front);
		return res;
	}

	private StackPane invisibleStackPane() {
		StackPane res = emptyStackPane();

		// handles styling
		Rectangle front = new Rectangle(15, 15, Color.BLACK);
		res.getChildren().add(front);
		return res;
	}

	private int getCoord(int x) {
		return 15 - x;
	}

	//create a stack of hero cards
	private void updateHeroesStack(ArrayList<Hero> h , VBox stack) {
		stack.getChildren().clear();
		for(Hero x : h) {
			StackPane card = heroCard(x);
			stack.getChildren().add(card);
		}
		
		
	}
	 
	//create a card for a hero
	private StackPane heroCard(Hero h) {
		HBox card = new HBox();
		card.setSpacing(3);
		
		
		// VBox  to contian the hero info 
		VBox info = new VBox();
		
		//Getting the hero info
		String name = h.getName();
		int currentHp = h.getCurrentHp();
		int noSupplies = h.getSupplyInventory().size();
		int noVaccines = h.getVaccineInventory().size();
		int attackDmg = h.getAttackDmg();
		int actionsAvailable = h.getActionsAvailable();
		boolean usedSpeacialAction = h.isSpecialAction();
		
		
		
		//Setting the health bar.
		double healthBarWidth = 150;
		double maxHp = h.getMaxHp();
		double iconSize = 12 ;
		GridPane healthBar = bar("healthBar",healthBarWidth, currentHp, maxHp, iconSize, "src/images/healthicon.png");

		 
		
		//Setting the action points bar
		double actionsBarWidth = 120;
		double maxActions = h.getMaxActions();
		GridPane actionsBar = bar("actionsBar",actionsBarWidth,actionsAvailable,maxActions,iconSize,"src/images/actionpointsicon.png");
       
     
        
        //Setting the collictibleview
        GridPane collectibles =  collectibles(noSupplies, noVaccines);
       
        
		
		// specify the type of the hero.
		String type ="Balabizak yasta";
		if(h instanceof Medic) {
			type ="Medic";
		} else if(h instanceof Fighter) {
			type = " Fighter";
		} else {
			type = "Explorer";
		}
		
		

	
		Label l = new Label("Attack damage: " + attackDmg);


		
		info.setSpacing(2);
		info.getChildren().addAll(healthBar,actionsBar,collectibles);
		
		
		//getting the hero image
		VBox img = heroImage(h,type +" " + name);
		img.setAlignment(Pos.TOP_LEFT);	
		
		
		card.getChildren().addAll(img,info);

		int width = 350;
		int height = 140;
		StackPane res = new StackPane();
		Rectangle rec = new Rectangle(width,height);
		rec.setArcHeight(35);
		rec.setArcWidth(35);
		if(h.equals( currentHero) )
			rec.setFill(Color.BEIGE.brighter());
		else
			rec.setFill(Color.GREY);
		res.getChildren().add(rec);
		card.setMaxWidth(width - 10);
		card.setMaxHeight(height - 10);
		
		res.getChildren().add(card);
		
		

		
		//setting a listener to the card
		res.setOnMouseEntered(e -> {
			rec.setFill(Color.BEIGE.brighter());
		});
		res.setOnMouseExited(e -> {
			if(h != currentHero)
				rec.setFill(Color.GRAY);

		});
		res.setOnMouseClicked(e -> {
			
			currentHero = h ;
			updateHeroesStack(Game.heroes,Heroes );
			
			
		});
		return res;
	
	}
	
	private static VBox heroImage(Hero h , String txt) {
		StackPane photo = new StackPane();
		ImageView imageView = new ImageView(h.getImage());
		int width = 95;
		int height = 100;
		imageView.setFitHeight(height);
		imageView.setFitWidth(width);
		
		photo.getChildren().add(imageView);
		


        // Create a Rectangle as the Clip shape with round edges
        Rectangle clipShape = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        clipShape.setArcWidth(30);
        clipShape.setArcHeight(30);
        imageView.setClip(clipShape);
        
        Label name = new Label();
        name.setText(txt);
		
        VBox res = new VBox(10);
        res.getChildren().addAll(photo, name);
		return res;
	}
	
	
	
	private static GridPane collectibles(int supplies,int vaccines) {
		GridPane grid = new GridPane();
		grid.setHgap(5);
		StackPane vaccineIcon = icon(15,"src/images/vaccineIcon.png");
		StackPane supplyeIcon = icon(15,"src/images/supplyIcon.png");
		
		Label noVaccines = new Label();
		noVaccines.setText("  "+vaccines);
		
		//vaccineIcon.getChildren().add(noVaccines);
		
		Label noSupplies = new Label();
		noSupplies.setText("     "+supplies);
		
		//supplyeIcon.getChildren().add(noSupplies);
		
		
		grid.add(vaccineIcon, 0, 0);
		grid.add(noVaccines, 1, 0);
		grid.add(supplyeIcon, 2,0);
		grid.add(noSupplies, 3, 0);
		return grid;
	}
	private static StackPane icon(double r , String path)  {
		StackPane res = new StackPane();
		Circle circle = new Circle(r);
        circle.setCenterX(r);
        circle.setCenterY(r);
		
		try {
			Image image = new Image(new File(path).toURI().toURL().toExternalForm());
			ImageView imageView = new ImageView(image);
			imageView.setFitWidth(r * 2);
		    imageView.setFitHeight(r * 2);
			imageView.setClip(circle);
			res.getChildren().add(imageView);
			
			
		}catch(Exception e) {
			System.out.println("Missing image");
			circle.setFill(Color.BLACK);
			res.getChildren().add(circle);
			
		}
		 return res;
	}
	
	
	private static GridPane bar(String type,double width,double current, double max , double iconSize,String path) {
		GridPane res = new GridPane();
		res.setHgap(20);
		
		ProgressBar bar = new ProgressBar();
		
		bar.setProgress(current / max);
		String style = "";
		if(type.equals("healthBar")) {
			style ="-fx-padding: 2px; -fx-background-insets: 2px;-fx-pref-height: 23px;";
		if( current >= 0.75 * max)
			style += "-fx-accent: green;" ;
		else if (current >= 0.5 * max)
			style += "-fx-accent: yellow;" ;
		else if (current >= 0.25 * max)
			style += "-fx-accent: orange;" ; 
		else
			style += "-fx-accent: red;" ;
		} else {
			style ="-fx-accent: Blue;-fx-padding: 2px; -fx-background-insets: 2px;-fx-pref-height: 20px;";
		
		}
		bar.setStyle(style);
		bar.setPrefWidth(width); 
		
		
		StackPane icon = icon(iconSize , path);
		
		res.add(bar, 1, 0);
		res.add(icon, 0, 0);
		
		return res;
		
	}		



}
