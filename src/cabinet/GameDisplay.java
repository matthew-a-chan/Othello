package cabinet;

import java.util.List;

import game.GameDisplayListener;
import game.Move;
import game.Player;
import javafx.scene.Scene;

public interface GameDisplay extends Plugin{
	
	/**
	 * Initialize the display
	 * @param gs the initial gamestate 
	 */
	public void init( GameState gs );
	
	/**
	 * Causes the display to redraw if necessary
	 * @param g the gamestate to use for the redraw
	 */
	public void render( GameState g );
	
	/**
	 * Cleanup and shutdown the display
	 * @param g the final gamestate if needed
	 */
	public void shutdown( GameState g );
	
	/**
	 * Called when the display needs to start accepting input
	 * @param gs the current state of the game
	 * @param m the move object that needs to be filled in with the user's move
	 */
	public void startMove(GameState gs, List<Move> m );
	
	/**
	 * Called after a move has been made and the display should stop accepting input
	 * @param gs the current state of the game
	 * @param m the move actually completed by the player
	 */
	public void endMove( GameState gs, List<Move> m );
	
	/**
	 * Called the get the name of a particular player from the display
	 * @param p the player whose name should be set
	 * @return the final name of the player
	 */
	public String getName(Player p);
	
	/**
	 * The Graphical representation of the display.  Currently, only JavaFX is supported
	 * @return a Scene which represents the graphical representation of the game
	 */
	public Scene getGraphic();
	
	/**
	 * Adding a listener to track display state changes
	 * @param gdl the listener to notify
	 * @return true if the listener can be added, false otherwise
	 */
	public boolean addGameDisplayListener( GameDisplayListener gdl );
	
	/**
	 * Remove a listener
	 * @param gdl the listener to remove
	 * @return true if the listener can be removed, false otherwise
	 */
	public boolean removeGameDisplayListener( GameDisplayListener gdl );
	
	/**
	 * Remove all listeners from this display
	 * @return
	 */
	public boolean removeAllGameDisplayListeners();
}
