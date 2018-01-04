package game;

import java.util.ArrayList;
import java.util.List;

import Genetics.Individual;
import cabinet.GameDisplay;
import cabinet.GameState;
import cabinet.Plugin;
import cabinet.PluginInfo;

/**
 * API all Player plugins must implement
 * @author bsea
 *
 */
public class Player implements Plugin{
	private String name;
	
	private volatile boolean exit;
	
	// Lock to wait for Human interaction
	private Object lock;
	private List<Move> moveTo;
	
	// User Interaction Interface; 
	// null = no display
	private GameDisplay display;
	
	
	public static PluginInfo getInfo() {
		PluginInfo pi = new PluginInfo(){

			@Override
			public String name() {
				return "Human";
			}

			@Override
			public String description() {
				return "Interactive Human Player";
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
	 * Creates a player with the name of "Human"
	 */
	public Player(){
		this("Human");
	}
	
	/**
	 * Creates a new player with a specific name
	 * @param name the name of the player
	 */
	public Player( String name ){
		setName(name);
		moveTo = new ArrayList<Move>();
		lock = new Object();
	}
	
	/**
	 * Set the graphical display this player is using; Could be null
	 * @param d the display to use or null if no display is used
	 */
	public void setDisplay( GameDisplay d ){
		this.display = d;
	}
	
	/**
	 * Sets the name of the Player
	 * @param n the new name of the player;  This name must be non-null and non-empty
	 */
	public void setName( String n ){
		if( n == null ){
			throw new IllegalArgumentException("Players must have names");
		}
		n = n.trim();
		if( n.length() == 0 ){
			throw new IllegalArgumentException("Player names cannot be blank");
		}
		this.name = n;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * Set the exit condition of the player
	 * @param b true if the player should stop what it's doing, false otherwise
	 */
	public final void setExit(boolean b ){
		exit = b;
	}
	
	/**
	 * Get the exit condition for the player
	 * @return true if the player should stop what it's doing and exit, false otherwise
	 */
	public final boolean exit(){
		return exit;
	}
		
	public GameDisplay getDisplay() {
		return this.display;
	}
	
	/**
	 * Ask the player to make a move
	 * @param gs the current state of the game
	 * @param to a move object that must be filled with valid moves, in order, for the player to make
	 */
	public void makeMove( GameState gs, List<Move> to ){
		HumanMover hm = new HumanMover();
		display.addGameDisplayListener(hm);
		try {
			//System.err.println("Waiting for Human");
			synchronized(lock) {
				display.startMove(gs, moveTo);
				lock.wait();
				display.endMove(gs, moveTo);
				
				to.clear();
				for( Move m : moveTo ) {
					to.add(new Move(m));
				}
				moveTo.clear();
			}
		//	System.err.println("Human moved: " + to.toString());
		} catch (InterruptedException e) {
		}
		finally{
			display.removeGameDisplayListener(hm);
		}
	}
	
	private class HumanMover implements GameDisplayListener{

		@Override
		public boolean moveMade( List<Move> m ) {
			synchronized(lock) {
				moveTo = m;
				lock.notify();
			}
			return false;
		}
	}

	public Individual getIndividual() {
		// TODO Auto-generated method stub
		return null;
	}
}
