package othello;

import java.util.ArrayList;
import java.util.List;

import cabinet.GameState;
import cabinet.Plugin;
import cabinet.PluginInfo;
import game.GameDim;
import game.Location;
import game.Move;
import game.Player;
import game.Range;
import game.Team;
import game.TurnBasedGame;

public class Othello extends TurnBasedGame{

	private int numTurn;
	private int turn;
	private int[][] board;
	private boolean gameOver;
	private boolean passed;

	public static PluginInfo getInfo() {
		PluginInfo pi = new PluginInfo() {

			@Override
			public String name() {
				return "Othello";
			}

			@Override
			public String description() {
				return "Original Othello Game";
			}

			@Override
			public Class<? extends Plugin> type() {
				return GameState.class;
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

	
	
	
	public Othello(){
		turn = -1;
		numTurn = 0;
		passed = false;
		
		GameDim gd = getDimensions();
		board = new int[gd.height()][gd.width()];
		
		/* Setup teams */
		String[] teamNames = {"Black", "White"};
		for( String s : teamNames ){
			Team t = new Team();
			Range<Integer> teamcap = new Range<Integer>(1,1);
			t.setPlayerRange(teamcap);
			t.setName(s);
			players.add(t);
		}
		
	}

	// Copy an Othello Game State
	public Othello( Othello other ){
		super(other);

		passed = other.passed;
		turn = other.turn;
		numTurn = other.numTurn;
		gameOver = other.gameOver;
		
		board = new int[other.board.length][other.board[0].length];
		for( int i = 0; i < board.length; i++ ){
			for( int j = 0; j < board[0].length; j++ ){
				board[i][j] = other.board[i][j];
			}
		}
		reset();
	}
	
	@Override
	public GameDim getDimensions() {
		GameDim gd = new GameDim();
		gd.width = new Range<Integer>(0,8);
		gd.height = new Range<Integer>(0,8);
		return gd;
	}

	public Othello copyInstance() {
		Othello copy = new Othello(this);
		return copy;
	}
	
	@Override
	public Range<Integer> numPlayersAllowed() {
		return new Range<Integer>(2,2);
	}
	
	@Override
	public List<Team> getOwner(Location m) {
		List<Team> owners = new ArrayList<Team>();
		
		if( m.y >= 0 && m.y < board.length ){
			if( m.x >= 0 && m.x < board[0].length ){
				
				if( board[m.y][m.x] == -1 ){
					owners.add( players.get(0) );
				}
				else if( board[m.y][m.x] == 1 ){
					owners.add(players.get(1));
				}
			}
		}
		return owners;
	}
	
	@Override
	public List<Move> getValidMoves() {
		ArrayList<Move> validMoves = new ArrayList<Move>();

		Move check = new Move();
		for( int i = 0; i < board.length; i++ ){
			for( int j = 0; j < board[0].length; j++ ){
				check.to.x = j;
				check.to.y = i;
				
				List<Move> moves = new ArrayList<Move>();
				moves.add(check);
				if( isValidMove(moves)){
					validMoves.add(new Move(check));
				}
			}
		}

		return validMoves;
	}
	
	@Override
	public Move getInvalidMove() {
		Move m = new Move();
		m.to.x = -1;
		m.to.y = -1;
		m.to.z = -1;
		return m;
	}
	

	
	@Override
	public boolean makeMove(List<Move> moves) {
		boolean rtn = false;
		
		
		if(isGameOver()) {
			return false;
		}
		
		/*
		 * Attempt to pass; Player can only pass if there are no valid moves
		 * Two consecutive passes means the game is over.
		 */
		if( moves.size() == 0 && getValidMoves().size() == 0 ) {	
			if( passed ) {
				gameOver = true;
			}
			else {
				// Count the pass and move to the next player
				passed = true;
				this.turn *= -1;
				int next = (this.turn > 0 ) ? 1 : 0;
				playing.set(0, players.get(next));
			}
			super.makeMove(moves);
			return true;
		}
		else if(moves.size() == 0 ){
			return false;
		}
		
		passed = false;
		
		rtn = isValidMove( moves );
		if( rtn && players.size() > 0 )
		{
			Move to = moves.get(0);
			int row = to.to.y;
			int col = to.to.x;
			int turn = this.toTurn(whoseTurn().get(0));

			GameDim dims = getDimensions();
			int width = dims.width();
			int height = dims.height();

			// Place Piece
			board[row][col] = turn;

			// Find Next pieces
			// Array format {rowmove, colmove, numfound}
			int[][] find = {
					{ 0, 1, 1 }, //right
					{ 0, -1, 1 }, // left
					{ 1, 0, 1 }, // down
					{ -1, 0, 1 }, // up
					{ 1, 1, 1 }, // dia down right
					{ 1, -1, 1 }, // dia down left
					{ -1, 1, 1 }, // dia up right
					{ -1, -1, 1 } // dia up left
			};


			// Create the "lines" from the Move to the first surrounding piece

			for( int i = 0; i < find.length; i++ )
			{
				int crow = row + find[i][0];
				int ccol = col + find[i][1];

				// Make sure we don't off the board
				// Make sure we don't off the board
				while( crow >=0 && crow < height && ccol >=0 && ccol < width )
				{

					// the piece isn't ours or and its not empty
					// so we add one to the line and keep going
					//
					// Empty piece, so we stop this direction
					if( board[crow][ccol] == 0  )
					{
						break;
					}
					else if( board[crow][ccol] == turn )
					{
						// the piece is ours!
						// "lock" the line by making it negative
						find[i][2] *= -1;
						break;
					}
					else {
						// valid line
						find[i][2]++;
					}
					crow += find[i][0];
					ccol += find[i][1];
				}

			}

			for( int i = 0; i < find.length; i++ )
			{
				// unlock the line length
				// added side-effect: if we actually had an incomplete  line
				// above, then it becomes "locked" and we don't travel it!
				find[i][2] = -find[i][2];
				int crow = row + find[i][0];
				int ccol = col + find[i][1];
				for( int j = 0; j < find[i][2]; j++ )
				{
					board[crow][ccol] = turn;
					crow += find[i][0];
					ccol += find[i][1];
				}
			}

			this.turn *= -1;
			numTurn++;	
			
			int next = (this.turn > 0 ) ? 1 : 0;
			playing.set(0, players.get(next));
			
			super.makeMove(moves);			
		}

		return rtn;
	}

	public Range<Integer> numTurns(){
		GameDim dims = getDimensions();
		
		int totalTurns = dims.width.max() - dims.width.min();
		totalTurns *= dims.height.max() - dims.height.min();
		
		Range<Integer> range = new Range<Integer>(numTurn, totalTurns);
		return range;
	}

	@Override
	public void setup() {		
		Team t = new Team();
		Range<Integer> range = new Range<Integer>(1,1);
		t.setPlayerRange(range);

		t.add(players.get(0).get(0));
		playing.add(t);
		
		for( int i = 0; i < board.length; i++ ){
			for( int j = 0; j < board[0].length; j++ ){
				board[i][j] = 0;
			}
		}

		board[3][3] = 1;
		board[3][4] = -1;
		board[4][3] = -1;
		board[4][4] = 1;

		numTurn = 0;
		turn = -1;
	
		super.setup();
		
	}
	
	private int toTurn( Team p ){
		if(p.containsAll(players.get(0))){
			return -1;
		}
		else if(p.containsAll(players.get(1))){
			return 1;
		}
		return 0;
	}
	
	@Override
	public boolean isValidMove( List<Move> moves) {
		
		if( moves.size() == 0 ) {
			return true;
		}
		
		Move to = moves.get(0);
		int row = to.to.y;
		int col = to.to.x;
		
		List<Team> currentTurn = whoseTurn();
		int turn = this.toTurn(currentTurn.get(0));
		
		GameDim gd = getDimensions();
		int width = gd.width();
		int height = gd.height();

		boolean valid = false;

		if( row < 0 || col < 0 ){
			return false;
		}
		// Spot is taken... not a valid move!
		if( board[row][col] != 0 ){
			return false;
		}
		
		// Find Next pieces
		// Array format {rowmove, colmove, numfound}
		int[][] find = {
				{ 0, 1, 1 }, //right
				{ 0, -1, 1 }, // left
				{ 1, 0, 1 }, // down
				{ -1, 0, 1 }, // up
				{ 1, 1, 1 }, // dia down right
				{ 1, -1, 1 }, // dia down left
				{ -1, 1, 1 }, // dia up right
				{ -1, -1, 1 } // dia up left
		};


		// Create the "lines" from the Move to the first surrounding piece
		for( int i = 0; i < find.length; i++ )
		{
			int crow = row + find[i][0];
			int ccol = col + find[i][1];

			// Make sure we don't off the board
			while( crow >= 0 && crow < height && ccol >= 0 && ccol < width )
			{

				// the piece isn't ours or and its not empty
				// so we add one to the line and keep going
				//
				// Empty piece, so we stop this direction
				if( board[crow][ccol] == 0  )
				{
					break;
				}
				else if( board[crow][ccol] == turn )
				{
					// the piece is ours!
					// "lock" the line by making it negative and stop
					find[i][2] *= -1;
					break;
				}
				else {
					// valid line
					find[i][2]++;
				}
				crow += find[i][0];
				ccol += find[i][1];
			}
		}

		for( int i = 0; i < find.length; i++ )
		{
			if( find[i][2] < -1 )
			{
				valid = true;
				break;
			}
		}

		return valid && !isGameOver();
	}
	
	@Override
	public boolean isGameOver() {
		return gameOver;
	}

	@Override
	public int getScore(Player p) {
		int cnt = 0;
		Location l = new Location();
		for( int i = 0; i < board.length; i++ ){
			for( int j = 0; j < board[0].length; j++ ){
				l.x = j;
				l.y = i;
				
				List<Team> owners = getOwner(l);
				for( Team t : owners ) {
					if( t.contains(p)) {
						cnt++;
						break;
					}
				}
			}
		}

		return cnt;
	}
	
	@Override
	public int getScore( Team t ) {
		int cnt = 0;
		for( Player p : t ) {
			cnt += getScore( p );
		}
		return cnt;
	}

	@Override
	public List<Team> getWinners() {

		Team[] players = getPlayers();
		ArrayList<Team> winners = new ArrayList<Team>();
		int max = -1;
		for( Team p : players ){
			int cnt = getScore(p);
			if( cnt > max ){
				winners.clear();
				winners.add(p);
				max = cnt;
			}
			else if( cnt == max ){
				winners.add(p);
			}
		}
		return winners;
	}
}
