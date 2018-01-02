package game;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a collection of players with a team name and caps
 * @author bsea
 *
 */
public class Team extends ArrayList<Player>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4994447610359332042L;
	private String name;
	private Range<Integer> rangePlayers;
	
	/**
	 * Creates an empty team that cannot be added to
	 */
	public Team() {
		name = "";
		rangePlayers = new Range<Integer>(0,0);
	}
	
	/**
	 * Change the range limits of the team capacity. The range's
	 * min and max represent the minimum and maximum number of players
	 * allowed on the team
	 * @param range the minimum and maximum number of the players allowed on the team
	 * @return true if range is non-null, false otherwise
	 */
	public boolean setPlayerRange( Range<Integer> range ) {
		boolean rtn = false;
		if( range != null ) {
			this.rangePlayers = range;
			rtn = true;
		}
		return rtn;
	}
	
	/**
	 * Get the caps of the team (min and max number of players)
	 * @return the caps on this team's enrollment
	 */
	public Range<Integer> teamCaps() {
		return rangePlayers;
	}
	
	/**
	 * Provide a team name
	 * @param name the new name of the team
	 * @return true if <em>name</em> is non-null, false otherwise
	 */
	public boolean setName( String name ) {
		boolean rtn = false;
		if( name != null ) {
			this.name = name.trim();
			rtn = true;
		}
		return rtn;
	}
	public String getName() {
		return name;
	}
	
	@Override
	/**
	 * Add a player to team if it has room
	 */
	public boolean add( Player p ){	
		boolean rtn = false;
		if( rangePlayers.contains(size()+1)) {
			rtn = super.add(p);
		}
		
		return rtn;
	}
	
	@Override
	/**
	 * Replace a team member
	 */
	public void add( int index, Player p ){
		if( rangePlayers.contains(index)){
			super.add(index, p);
		}
	}
	
	@Override
	/**
	 * Add lots of players to the team at once
	 */
	public boolean addAll( Collection<? extends Player> c ) {
		boolean rtn = false;
		if( rangePlayers.contains(size()+c.size() )){
			rtn = super.addAll( c );
		}
		return rtn;
	}
	
	@Override
	/**
	 * Add lots of players to the team at a specific point, if possible with caps
	 */
	public boolean addAll( int index, Collection<? extends Player> c ){
		boolean rtn = false;
		if( rangePlayers.contains(size()+c.size()) ){
			rtn = super.addAll(index, c );
		}
		return rtn;
	}
	
}
