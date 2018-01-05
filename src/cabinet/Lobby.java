package cabinet;

import game.*;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Lobby extends Application {

	// The 'database' of plugins, games and descriptions
	private PluginManager plugs;
	private HashMap<String, Class<GameState>> names;
	private HashMap<Class<GameState>, String> descriptions;
	private HashMap<String, Class<Player>> ais;

	// Selected Game
	private Class<GameState> selectedGame;
	private ArrayList<Class<Player>> players;
	private ArrayList<TextField> playerNames;
	private Integer[] minmax;

	// Changing Graphical Elements 
	private ListView<String> games;
	private VBox compatiableAIs;
	private HashMap<ComboBox<String>, TextField> aiNames;
	private Label gameDescription;
	private Button goButton;

	public Lobby() {
		plugs = new PluginManager();

		names = plugs.getAvailableGames();
		descriptions = plugs.getGameDescriptions();
		players = new ArrayList<Class<Player>>();
		playerNames = new ArrayList<TextField>();

		gameDescription = new Label();
		aiNames = new HashMap<ComboBox<String>,TextField>();

		goButton = new Button("Play Game!");
		goButton.setDisable(true);
		goButton.setOnAction(new PlayGame());

		this.compatiableAIs = new VBox();
		constructGUI();
	}

	private void setGameSelection( int selection ){
		Class<GameState> gameSelected = names.get(games.getItems().get(selection));
		
		String desc = "";
		if( gameSelected != null ){
			desc = descriptions.get(gameSelected);
			if( desc == null ){
				desc = "";
			}
			else{
				ais = plugs.getCompatibleAIs( gameSelected );
				ArrayList<String> names = new ArrayList<String>(ais.keySet());
				names.add(0,"No Player");
				ObservableList<String> namesArr = FXCollections.observableArrayList(names); 
				Integer[] minmax = plugs.getMinMaxPlayers(gameSelected);

				this.compatiableAIs.getChildren().clear();
				for( int i = 0; i < minmax[1]; i++ ){
					HBox selectPlayer = new HBox();
					TextField tf = new TextField("Player " + (i+1) );
					tf.setEditable(false);
					ComboBox<String> player = new ComboBox<String>(namesArr);
					player.setOnAction(new AISelector() );
					player.setId(""+i);
					this.players.add(null);

					tf.setId(""+i);
					playerNames.add(tf);
					
					selectPlayer.getChildren().addAll(tf, player);
					aiNames.put(player, tf);
					this.compatiableAIs.getChildren().add(selectPlayer);
				}
				this.selectedGame = gameSelected;
				this.minmax = minmax;
			}
		}
		gameDescription.setText(desc);

	}

	public BorderPane constructGUI() {

		BorderPane root = new BorderPane();

		HBox buttons = new HBox();
		Button scan = new Button("Scan Directory for Plugins ...");
		scan.setOnAction(new addDirectory());
		
		buttons.getChildren().addAll(scan);
		root.setTop( buttons );
		

		ObservableList<String> items = FXCollections.observableArrayList(names.keySet());
		games = new ListView<String>();
		games.setItems(items);

		games.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
				setGameSelection(newVal.intValue());
			}			
		});

		root.setLeft(games);
		games.prefWidthProperty().bind(root.widthProperty().divide(4));

		BorderPane playerSelection = new BorderPane();
		playerSelection.setTop(gameDescription);
		playerSelection.setCenter(this.compatiableAIs);
		playerSelection.setBottom(goButton);

		BorderPane.setAlignment(gameDescription, Pos.CENTER);
		BorderPane.setAlignment(this.compatiableAIs, Pos.CENTER_RIGHT);
		BorderPane.setAlignment(goButton, Pos.BASELINE_RIGHT);

		root.setCenter(playerSelection);
		BorderPane.setAlignment(playerSelection, Pos.CENTER);

		return root;

	}
	
	private class addDirectory implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event ) {
			DirectoryChooser fc = new DirectoryChooser();
			fc.setTitle("Select Plugin Directory");
			File selectedDirectory = fc.showDialog(null);
			plugs.addURI(selectedDirectory.toURI());
			plugs.scanPlugins();
			
			
			names=plugs.getAvailableGames();
			descriptions = plugs.getGameDescriptions();
			games.setItems(FXCollections.observableArrayList(names.keySet()));
		}
	}




	private class AISelector implements EventHandler<ActionEvent>
	{
		@Override
		@SuppressWarnings("unchecked")		
		public void handle(ActionEvent event) {
			
			ComboBox<String> src = (ComboBox<String>) event.getSource();
			int spot = Integer.parseInt(src.getId());
			boolean playgame = false;
			String selected = src.getSelectionModel().getSelectedItem();
			Class<Player> selectAI = ais.get(selected);

			if( selectAI == null ) {
				return;
			}
			
			try {
				players.set(spot, selectAI);	
				Player p = selectAI.newInstance();
				TextField tf = aiNames.get(src);
				
				if( p.getClass() == Player.class ){
					tf.setEditable(true);
					tf.requestFocus();
					tf.selectAll();
				}
				else {
					tf.setText(p.getName());
					tf.setEditable(false);
				}
			} catch (InstantiationException | IllegalAccessException e1) {
				e1.printStackTrace();
			}


			int cnt = 0;
			for( Class<Player> p : players ){
				if( p != null ){
					cnt++;
				}
			}
			if( cnt >= minmax[0] && cnt <= minmax[1] ){
				playgame = true;
			}
			goButton.setDisable(!playgame);
		}
	}


	private class PlayGame implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e) {
			ArrayList<PluginInfo> display = plugs.getCompatibleDisplays(selectedGame);
			if( display != null && display.size() > 0 ){
				try {
					GameState state = selectedGame.newInstance();//WOULD NEED TO MAKE ANOTHER ONE OF THESE FOR EACH GAME
					GameDisplay disp = (GameDisplay) display.get(0).getPluginClass().newInstance();//THEN FILL IT WITH TWO AIS
					
					int spot = 0;
					for( Class<Player> p : players ){
						Player play = p.newInstance();//REPLACE THIS WITH OTHELLO AI.NEW INSTANCE()
						play.setName( playerNames.get(spot).getText());
						play.setDisplay(disp);
						state.addPlayer(play);
						spot++;
					}					
					
					disp.init(state);//DISABLE disp TO REMOVE GUI
					Stage frame = new Stage();
					frame.setTitle("Game");
					frame.setWidth(500);
					frame.setHeight(550);
					
					Scene root = disp.getGraphic();
					frame.setScene(root);
					
					frame.setOnCloseRequest(new EventHandler<WindowEvent>() {

						@Override
						public void handle(WindowEvent we) {
							state.shutdown();
						}
						
					});
					
					frame.show();
					state.start();//THEN FINALLY START IT -- IT WILL RETURN INFO UPON COMPLETION

				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			}

		}		
	}
	/*
	private class NameChanger implements KeyListener
	{

		@Override
		public void keyTyped(KeyEvent e) {

			JTextField tf = (JTextField)e.getSource();
			int spot = Integer.parseInt(tf.getName());


		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Lobby lobby = new Lobby();
		BorderPane n = lobby.constructGUI();
		primaryStage.setScene(new Scene(n, 500,500));
		primaryStage.show();
	}

	public static void main(String[] args ){
		launch(args);
	}
}
