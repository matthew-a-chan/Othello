package game;

import java.util.ArrayList;
import java.util.List;
import cabinet.GameDisplay;

public abstract class GameDisplayAdapter implements GameDisplay {

	private ArrayList<GameDisplayListener> listeners;
	
	public GameDisplayAdapter() {
		listeners = new ArrayList<GameDisplayListener>();
	}
	
	@Override
	public boolean addGameDisplayListener( GameDisplayListener gdl){
		if( !listeners.contains(gdl) ){
			listeners.add(gdl);
		}
		
		return true;
	}
	
	@Override
	public boolean removeGameDisplayListener( GameDisplayListener gdl ){
		return listeners.remove(gdl);
	}
	
	
	@Override
	public boolean removeAllGameDisplayListeners(){
		listeners.clear();
		return true;
	}
	
	public void moveMade( List<Move> m ) {
		for( GameDisplayListener gdl : listeners  ){
			gdl.moveMade(m);
		}
	}
}
