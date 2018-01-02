package game;

/**
 * Represents a move in the Game; Currently, you can only select a placement
 * @author bsea
 *
 */
public class Move {
	
	/**
	 * Where to move to and from
	 */
	public Location to;
	public Location from;
	
	/**
	 * Game specific movement option(s)
	 * Use of multiple options should be OR'd together
	 */
	public int specialMove;
	
	public Move() {
		to = new Location();
		from = new Location();
		specialMove = 0;
	}
	
	/**
	 * Copies another move object into a newly created object (copy constructor)
	 * @param other the object to copy
	 */
	public Move( Move other ){
		to = new Location( other.to );
		from = new Location( other.from );
		specialMove = other.specialMove;
	}
	
	/**
	 * Copies an object into this object (overwriting it)
	 * @param other the object to copy
	 * @return itself
	 */
	public Move copy( Move other ){
		this.to.x = other.to.x;
		this.to.y = other.to.y;
		this.to.z = other.to.z;
		
		this.from.x = other.from.x;
		this.from.y = other.from.y;
		this.from.z = other.from.z;
		
		this.specialMove = other.specialMove;
		
		return this;
	}
	
	public String toString() {
		return "Move -- ("+to.x+","+to.y+")"; 
	}
}
