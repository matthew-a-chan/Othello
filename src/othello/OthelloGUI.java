package othello;

import java.util.ArrayList;
import java.util.List;

import cabinet.GameDisplay;
import cabinet.GameState;
import cabinet.Plugin;
import cabinet.PluginInfo;
import game.GameDim;
import game.GameDisplayAdapter;
import game.GameStateListener;
import game.Location;
import game.Move;
import game.Player;
import game.Team;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

public class OthelloGUI extends GameDisplayAdapter{

	private OthelloGUIPiece[][] board; 
	private ArrayList<Label> playerScores;

	private BorderPane root;
	private ButtonListener boardlistener;
	private GSListener listener;

	public static PluginInfo getInfo() {
		PluginInfo pi = new PluginInfo() {

			@Override
			public String name() {
				return "JavaFX Othello GUI";
			}

			@Override
			public String description() {
				return "Othello GUI in Java FX";
			}

			@Override
			public Class<? extends Plugin> type() {
				return GameDisplay.class;
			}

			@Override
			public List<Class<? extends GameState>> supportedGames() {
				ArrayList<Class<? extends GameState>> sg = new ArrayList<Class<? extends GameState>>();
				sg.add(Othello.class);
				return sg;
			}

		};
		return pi;
	}


	public OthelloGUI(){
		root = new BorderPane();
		playerScores = new ArrayList<Label>();

		root.setStyle("-fx-background-color: green;");
		root.getStylesheets().add(getClass().getResource("othello.css").toExternalForm());
	}


	@Override
	public void init(GameState g) {

		GameDim dims = g.getDimensions();
		int height = dims.height();
		int width = dims.width();

		this.board = new OthelloGUIPiece[height][width];
		boardlistener = new ButtonListener();
		listener = new GSListener();

		// Create a setup initial Board
		GridPane board = new GridPane();
		board.prefWidthProperty().bind(root.widthProperty());
		board.prefHeightProperty().bind(root.heightProperty());
		for(int i = 0; i < height; i++ ){
			for( int j = 0; j < width; j++ ){
				this.board[i][j] = new OthelloGUIPiece();
				this.board[i][j].setColor(null);
				this.board[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				this.board[i][j].setId( j + " " + i );
				this.board[i][j].setOnAction(boardlistener);
				board.add(this.board[i][j], j, i);
			}
		}

		for( int i = 0; i < height; i++) {
			RowConstraints rc = new RowConstraints();
			rc.setPercentHeight(100.0/height);
			board.getRowConstraints().add(rc);
		}

		for( int i = 0; i < width; i++ ){
			ColumnConstraints cc = new ColumnConstraints();
			cc.setPercentWidth(100.0/width);
			board.getColumnConstraints().add(cc);
		}

		root.setCenter(board);
		g.addGameStateListener(listener);


		Team[] players = g.getPlayers();
		playerScores.clear();

		HBox scorebox = new HBox();
		scorebox.getStyleClass().add("scorebox");
		scorebox.setPadding(new Insets(5,10,5,10));
		
		HBox left = new HBox();
		Label leftscore = new Label();
		playerScores.add( leftscore );
		left.getChildren().addAll( new Label(players[0].getName()), leftscore);
		
		HBox right = new HBox();
		Label rightscore = new Label();
		playerScores.add(rightscore);
		right.getChildren().addAll(new Label(players[1].getName()), rightscore);
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		scorebox.getChildren().addAll(left, spacer, right);

		root.setTop(scorebox);		
		render(g);
	}	


	private void gameOver( GameState overState ) {
		List<Team> winner = overState.getWinners();

		if( winner.size() > 1 ){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Game Over");
			alert.setContentText("It's a tie!");
			alert.show();
		}
		else{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Game Over");
			alert.setContentText(winner.get(0).getName() + " wins!");
			alert.show();
		}

		shutdown(overState);
	}



	@Override
	public void render(GameState g) {

		GameDim dims = g.getDimensions();
		int height = dims.height();
		int width = dims.width();

		Location m = new Location();
		Team[] players = g.getPlayers();

		for(int i = 0; i < height; i++ ){
			for( int j = 0; j < width; j++ ){				
				m.x = j;
				m.y = i;
				List<Team> p = g.getOwner(m);

				Color c = null;
				for(int k = 0;k < players.length;k++)
				{
					if( p.contains(players[k]) ){
						switch( k ){
						case 0: 
							c = Color.BLACK;
							break;
						case 1:
							c = Color.WHITE;
							break;
						}
						break;
					}
				}
				this.board[i][j].setColor(c);
			}
		}

		for( int i = 0; i < players.length; i++ ){
			playerScores.get(i).setText(" x"+g.getScore(players[i]));
		}
	}

	@Override
	public void shutdown( GameState g ) {

	}

	@Override
	public String getName( Player p ){
		TextInputDialog dialog = new TextInputDialog();
		dialog.setContentText("Enter the name for ");
		dialog.showAndWait();
		p.setName(dialog.getResult());
		return dialog.getResult();
	}

	@Override
	public Scene getGraphic() {
		Scene scene = new Scene(root);
		return scene;
	}

	@Override
	public void startMove( GameState gs, List<Move> m) {

		Platform.runLater(new Runnable() {
			public void run() {
				render(gs);

				for( int i = 0; i < board.length; i++ ){
					for( int j = 0; j < board[0].length; j++ ){
						board[i][j].setDisable(true);
					}
				}

				List<Move> valids = gs.getValidMoves();
				for( Move move : valids ){
					board[move.to.y][move.to.x].setDisable(false);
					board[move.to.y][move.to.x].setColor(Color.YELLOW);			
				}

				if( valids.size() == 0 ) {
					// No legal moves, so we pass and force a move
					moveMade(new ArrayList<Move>());
				}
			}
		});
	}

	@Override
	public void endMove(GameState gs, List<Move> m) {
		if( gs.isGameOver() ) {
			gameOver( gs );
		}
		else {
			for( int i = 0; i < board.length; i++ ){
				for( int j = 0; j < board[0].length; j++ ){
					board[i][j].setDisable(true);
					//board[i][j].setColor(Color.GREEN);
				}
			}			
		}
	}

	private class GSListener implements GameStateListener{

		@Override
		public void playerJoined(GameState g, Player p) {
		}

		@Override
		public void playerLeft(GameState g, Player p) {
		}

		@Override
		public void moveMade(GameState g, List<Move> moves) {
			
			Platform.runLater(new Runnable() {
				public void run() {
					render(g);
					if( moves.size() > 0 ) {
						Move move = moves.get(0);
						if( move.to.x >= 0 && move.to.y >= 0 ){
							board[move.to.y][move.to.x].setColor(Color.RED);
						}
					}
					endMove(g, moves);				
				}
			});
		}

		@Override
		public void start(GameState g) {

		}

		@Override
		public void stop(GameState g ){

		}

		@Override
		public void shutdown(GameState g) {
			System.out.println("STOP FROM GUI");
		}

		@Override
		public void reset(GameState g) {

		}

		@Override
		public void message(MSG_LVL level, String msg) {

		}

		@Override
		public void setup(GameState g) {			
		}
	}

	private class ButtonListener implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e){
			Object src = e.getSource();
			for( int i = 0; i < board.length; i++ ){
				for( int j = 0; j < board[0].length; j++ ){
					if( board[i][j] == src ){
						Move m = new Move();
						m.to.x = j;
						m.to.y = i;

						List<Move> moves = new ArrayList<Move>();
						moves.add(m);
						Platform.runLater(new Runnable() {
							public void run() {
								moveMade(moves);
							}
						});
						break;
					}
				}
			}
		}
	}

	private class OthelloGUIPiece extends Button{
		private Color c;

		public OthelloGUIPiece() {
			c = Color.GREEN;
		}
		public boolean setColor( Color c1 ){
			this.c = (c1 == null) ? Color.GREEN : c1;

			String style = "-fx-background-radius:50%;"+
					"-fx-background-insets: 5px;"+
					"-fx-border-color: white;"+
					"-fx-border-width: 2px;";
			String hex = String.format( "#%02X%02X%02X",
					(int)( this.c.getRed() * 255 ),
					(int)( this.c.getGreen() * 255 ),
					(int)( this.c.getBlue() * 255 ) );
			this.setStyle(style+"-fx-background-color:"+hex+";");
			return true;
		}
	}
}
