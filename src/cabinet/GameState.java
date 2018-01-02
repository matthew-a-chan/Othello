package cabinet;

import java.util.List;

import game.GameDim;
import game.GameStateListener;
import game.Location;
import game.Move;
import game.Player;
import game.Range;
import game.Team;

/**
 * API all game plugins must implement
 * @author bsea
 *
 */

public interface GameState extends Plugin{

	/**
	 * Defines the width, height, and depth of the game space in units that
	 * are defined by the game itself (rows, pixels, inches, etc.).
	 * @return the game dimensions in specified units
	 */
	public GameDim getDimensions();
	
	/**
	 * Copies the current instance
	 * @return a new object which is a copy of this
	 */
	public GameState copyInstance();
	
	/* Player Management */
	/**
	 * The number of players required to play the game
	 * @return the minimum and maximum number of players needed for the game
	 */
	public Range<Integer> numPlayersAllowed();

	/**
	 * Attempt to add a player.  The concrete game takes care of team assignments
	 * @param p the player to add
	 * @return true if the player has been successfully added, false otherwise
	 */
	public boolean addPlayer( Player p );
	
	/**
	 * Attempt to remove a player.  The concrete games takes care of team assignments
	 * @param p the player to remove
	 * @return true if the player was successfully removed, false otherwise
	 */
	public boolean removePlayer( Player p );
	
	/**
	 * Remove all players from the game
	 * @return true if all players could be removed without issue, false otherwise
	 */
	public boolean removeAllPlayers();
	
	/* Player Discovery */
	
	/**
	 * Get the number of players in the game.  Each index of the array returned has the number of 
	 * players on that team.  The last element of the array returned is total of all teams.
	 * @return the number of players on each team
	 */
	public int[] numPlayers();
	
	/**
	 * Get the players on each team
	 * @return players separated by team
	 */
	public Team[] getPlayers();
	
	/**
	 * Find out whose turn it is
	 * @return all teams who are currently taking their turn, each team contains the specific players
	 */
	public List<Team> whoseTurn();
	
	/**
	 * Discover the owner(s) of a specific location
	 * @param space the location to query
	 * @return all teams that currently own the space
	 */
	public List<Team> getOwner(Location space);
	
	/* Move Discovery */
	
	/**
	 * Get all the valid moves given the current state of the game
	 * @return a list of valid moves
	 */
	public List<Move> getValidMoves();
	
	/**
	 * Returns a move that represents an invalid move in the game
	 * @return an invalid move
	 */
	public Move getInvalidMove();
	
	/**
	 * Checks to see if a given Move is valid given the current state of the game
	 * @param m the move chain to check
	 * @return true if m is valid, false otherwise
	 */
	public boolean isValidMove( List<Move> m );
	
	/**
	 * Makes a move(s) on the current game.  This method changes the state of the current game.
	 * Moves are made in sequential order.
	 * @param m the move to make
	 * @return true if m is a valid move, false otherwise
	 */
	public boolean makeMove( List<Move> m );
	
	/* Game State Discovery */
	
	/**
	 * Check to see if the game is over
	 * @return true if the game is over, false otherwise
	 */
	public boolean isGameOver();
	
	/**
	 * Figure out the number of turns in the game
	 * @return a new object where min is the current turn number and max is the max number of turns
	 */
	public Range<Integer> numTurns();
	
	/**
	 * Find the score of a player given the current state of the game
	 * @param p the player to get the score of
	 * @return an integer representation of the score for p
	 */
	public int getScore( Player p );
	
	/**
	 * Find the score for an entire team given the current state of the game
	 * @param t the team to get the score of
	 * @return an integer representation of the score of t
	 */
	public int getScore( Team t );
	
	/**
	 * See who won
	 * @return A list of winners. The list is empty if there is no winner yet or the game is a tie
	 */
	public List<Team> getWinners();
	
	/**
	 * Add an object to be notified if the game changes state
	 * @param gsl the listener to add
	 */
	public void addGameStateListener( GameStateListener gsl );
	
	/**
	 * Remove a listener from the game
	 * @param gsl the listener to remove
	 * @return true if the listener was found and remove, false otherwise
	 */
	public boolean removeGameStateListener( GameStateListener gsl );
	
	/**
	 * Clears all listeners off the current game
	 */
	public void removeAllGameStateListeners();
	
	/* Game State Management */
	
	/**
	 * Game States are managed in the following stages:
	 * 0. Init -- the game should initialize field in the constructor
	 * 1. Setup -- the game initializes and sets up the game
	 * 2. Start -- the game starts accepting input
	 * 3. Stop -- the game should stop accepting input
	 * 4. Shutdown -- the game cleans up resources and closes down
	 */
	
	/**
	 * Setup the game
	 */
	public void setup();
	
	/**
	 * Start the game
	 */
	public void start();
	/**
	 * Stop the game
	 */
	public void stop();
	/** 
	 * Reset the game
	 */
	public void reset();
	/**
	 * Shutdown and cleanup
	 */
	public void shutdown();

}
