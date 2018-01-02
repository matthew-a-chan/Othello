package game;

import java.util.List;

import cabinet.GameState;

/**
 * A Listener that is notified when a GameState changes
 * @author bsea
 *
 */
public interface GameStateListener {
	enum MSG_LVL {
		MSG_INFO("Info"),
		MSG_STATUS("Status"),
		MSG_ERROR("Error"),
		MSG_FATAL("FATAL");
		
		private String condition;
		
		private MSG_LVL(String name){
			condition = name;
		}
		
		public String toString() {
			return condition;
		}
	}
	

	/**
	 * A player has joined the game
	 * @param g the current GameState
	 * @param p the player that joined
	 */
	public void playerJoined( GameState g, Player p);
	
	/**
	 * A player has left the game
	 * @param g the current GameState
	 * @param p the player that left
	 */
	public void playerLeft( GameState g, Player p );
	
	/**
	 * A Move was made in the GameState
	 * @param g the GameState after the move was made
	 * @param move the move made
	 */
	public void moveMade( GameState g, List<Move> move);
	
	/**
	 * The GameState has finished its setup phase
	 * @param g the current state of the game
	 */
	public void setup( GameState g );
	
	/**
	 * The GameState has completed its start phase
	 * @param g the current state of the game
	 */
	public void start( GameState g );
	
	/**
	 * The GameState has completed its stop phase
	 * @param g the current state of the game
	 */
	public void stop( GameState g );
	
	/**
	 * The GameState has completed its shutdown phase
	 * @param g the final state of the game
	 */
	public void shutdown( GameState g );
	
	/**
	 * The Game has just been reset
	 * @param g the current state of the game
	 */
	public void reset( GameState g );
	
	/**
	 * The Game has issued a message
	 * @param level the level of importance of the message
	 * @param msg the message sent
	 */
	public void message(MSG_LVL level, String msg);
}
