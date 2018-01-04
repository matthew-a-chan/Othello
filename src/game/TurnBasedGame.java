package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import Genetics.Playground;
import cabinet.GameState;

public abstract class TurnBasedGame implements GameState{

	protected ArrayList<Team> players;
	protected ArrayList<Team> playing;

	private ArrayList<GameStateListener> listeners;
	private GameThread thread;

	public TurnBasedGame() {
		players = new ArrayList<Team>();
		playing = new ArrayList<Team>();
		listeners = new ArrayList<GameStateListener>();
	}
	public TurnBasedGame( TurnBasedGame g ) {
		this(g, false);
	}

	public TurnBasedGame( TurnBasedGame g, boolean copyListeners){
		this();
		players.addAll(g.players);
		playing.addAll(g.playing );
		if( copyListeners ){
			listeners.addAll( g.listeners );
		}
	}

	protected void sendMessage( GameStateListener.MSG_LVL lvl, String msg ){
		for( GameStateListener gsl : listeners ){
			gsl.message(lvl, msg);
		}
	}

	protected void playTurn() {
		for( Team t : playing ){
			for( Player p : t ){
				//final Move m = getInvalidMove();
				final List<Move> m = new ArrayList<Move>();
				GameState g = this.copyInstance();

				ExecutorService exec = Executors.newFixedThreadPool(2);
				Thread playerMove = new Thread() {
					public void run() {
						p.makeMove(g, m);
					}
				};

				Future<?> task = exec.submit(playerMove);
				exec.shutdown();
				try {
					//if(p.getClass() == Player.class ){
						task.get();
					//}
					//else {
					//	task.get(7, TimeUnit.SECONDS);
					//}
				}
				catch( InterruptedException e ){
				}
				catch( ExecutionException e ){
					System.err.print(p.getName() +": caused an exception -- ");
					System.err.println(e.getCause());
					e.printStackTrace();
				}/* catch (TimeoutException e) {
					System.err.println(p.getName() + ": Player exeeded allotted time!");
					p.setExit(true);
					task.cancel(true);
				}*/

				// Wait for all unfinished tasks for 2 secs
				try{
					if( !exec.awaitTermination(2, TimeUnit.SECONDS)){
						exec.shutdownNow();
					}
				}
				catch( InterruptedException e ){
				}

				if( !isValidMove(m) ){
					System.err.print(p.getName() + ": Invalid Move -- Using Random Move");
					System.err.println("-- Move was: " + m.toString());
					List<Move> legals = getValidMoves();
					Random rand = new Random();
					if( legals.size() > 0  ) {
						int n = rand.nextInt(legals.size() );
						m.add(new Move( legals.get(n)));
					}
				}

				// Make the move
				p.setExit(false);
				makeMove( m );
			}
		}
	}

	@Override
	public int[] numPlayers(){
		int[] numplayers = new int[ players.size()+1 ];
		int total = 0;
		int spot = 0;
		for( Team t : players ){
			numplayers[ spot ] = t.size();
			total += t.size();
			spot++;
		}
		numplayers[numplayers.length - 1] = total;
		return numplayers;
	}


	@Override
	public Team[] getPlayers(){
		return players.toArray(new Team[0]);
	}

	@Override
	public List<Team> whoseTurn(){
		return Collections.unmodifiableList(this.playing);
	}


	@Override
	public void addGameStateListener( GameStateListener gsl ){
		if( !this.listeners.contains(gsl) ){
			this.listeners.add(gsl);
		}
	}
	@Override
	public boolean removeGameStateListener( GameStateListener gsl ){
		return listeners.remove(gsl);
	}
	@Override
	public void removeAllGameStateListeners(){
		this.listeners.clear();
	}

	/* Game State Management */
	@Override
	public void setup(){
		for( GameStateListener gsl : listeners ){
			gsl.setup(this);
		}
	}
	@Override
	public void start(){
		if( players.size() > 0 && players.get(0).size() > 0 ) {

			setup();

			thread = new GameThread();
			thread.start();
			for( GameStateListener gsl : listeners ){
				gsl.start(this);
			}
		}
	}
	@Override
	public void stop(){
		if( thread != null ) {
			thread.exit();
			for( GameStateListener gsl : listeners ){
				gsl.stop( this );
			}
		}
	}
	@Override
	public void reset(){
		for( GameStateListener gsl : listeners ){
			gsl.reset(this);
		}
	}
	@Override
	public void shutdown(){
		System.out.println("STOP FROM TBG");
		if( thread != null ) {
			thread.exit();
			thread.interrupt();
		}
		for( GameStateListener gsl : listeners ){
			gsl.shutdown(this);
		}
	}
	@Override
	public boolean addPlayer( Player p ){
		boolean rtn = false;
		for( Team t : players ){
			Range<Integer> cap = t.teamCaps();
			if( t.size() < cap.max() ){
				t.add(p);
				rtn = true;
				for( GameStateListener gsl : listeners ){
					gsl.playerJoined(this, p);
				}
				break;
			}
		}


		return rtn;
	}
	
	@Override
	public boolean removePlayer( Player p ){
		boolean rtn = false;
		for( Team t : players ){
			if( t.contains(p) ){
				t.remove(p);
				for( Team tp: playing ) {
					if(tp.contains(p) ) {
						tp.remove(p);
					}
				}
				rtn = true;
				for( GameStateListener gsl : listeners ){
					gsl.playerLeft(this, p);
				}
			}
		}
		return rtn;
	}
	@Override
	public boolean removeAllPlayers() {
		playing.clear();
		for( Team t : players ){
			for( Player p : t ){
				removePlayer(p);
			}
		}
		players.clear();
		return true;
	}
	@Override
	public boolean makeMove( List<Move> moves ){
		for( GameStateListener gsl : listeners ){
			gsl.moveMade(this, moves);
		}
		return true;
	}

	private class GameThread extends Thread{

		private volatile boolean exit;

		public void exit() {
			exit = true;
		}

		@Override
		public void run() {
			while( !isGameOver() && !exit ){
				playTurn();
			}
			
			ArrayList<Player> activePlayers=new ArrayList<Player>();
			
			for( Team t : players ){
				for(Player p : t) {
					activePlayers.add(p);
				}
			}
		}
	}
}
