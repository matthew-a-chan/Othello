package game;

/**
 * Represents a location in a game space
 * @author bsae
 *
 */
public class Location {
	
	/**
	 * The x, y, and z coordinates in space
	 */
	public int x;
	public int y;
	public int z;
	
	/**
	 * Initialize the move to the minimum possible values;
	 */
	public Location() {
		x = y = z = Integer.MIN_VALUE;
	}
	
	/**
	 * Initialize a move to be a duplicate of another
	 * @param other the object to copy
	 */
	public Location( Location other ){
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}
	
	public boolean setX( int x ) {
		this.x = x;
		return true;
	}
	
	public boolean setY( int y ) {
		this.y = y;
		return true;
	}
	
	public boolean setZ( int z ) {
		this.z = z;
		return true;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getZ() {
		return z;
	}
	
	public String toString() {
		return "X: " + getX() + " Y: " + getY() + " Z: " + getZ();
	}
}
