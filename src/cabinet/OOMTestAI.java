package cabinet;

import java.util.List;

import game.Move;
import game.Player;

/**
 * Template AI class for the Framework
 * @author bsea
 *
 */
public class OOMTestAI extends Player{

	/**
	 * Every plugin must provide this static method
	 * @return a filled object describing the plugin
	 */
	public static PluginInfo getInfo() {
		PluginInfo pi = new PluginInfo() {

			@Override
			public String name() {
				return "Out Of Memory AI";
			}

			@Override
			public String description() {
				return "OOM AI";
			}

			@Override
			public Class<? extends Plugin> type() {
				return Player.class;
			}

			@Override
			public List<Class<? extends GameState>> supportedGames() {
				return null;
			}
			
		};
		
		
		return pi;
	}
	
	/**
	 * Your AI should call the super constructor and pass the AI's name
	 */
	public OOMTestAI(){
		super("OutOfMemoryAI");
	}
	
	/**
	 * This method is called when your AI needs to make a move.  It should fill in 'm'
	 * with the appropriate move.
	 * 
	 * <b>Note:</b>This method may be ended prematurely.
	 * 
	 * @param gs the current state of the game
	 * @param m the object to fill in to represent the AIs move
	 */
	@Override
	public void makeMove( GameState gs, List<Move> m ){
		makeMove(gs,m);
	}
}
