package game;

/**
 * Represents the dimensions of an object; These dimensions could be of
 * many different units and need not start at zero
 * @author bsea
 *
 */
public class GameDim {
	
	/**
	 * Known units
	 * @author bsea
	 *
	 */
	public enum UNITS {
		GAMEDIM_PX("pixels"), 
		GAMEDIM_IN("inches"),
		GAMEDIM_CM("centimeters"),
		GAMEDIM_SPACES("spaces");
		
		private final String name;
		private UNITS( String s ) {
			name = s;
		}
		public String toString() {
			return this.name;
		}
	}
	
	/**
	 * The width, height, and depth of the an object in relative coordinates
	 */
	public Range<Integer> width;
	public Range<Integer> height;
	public Range<Integer> depth;
	public UNITS units = UNITS.GAMEDIM_SPACES;
	
	/**
	 * Creates a new dimension object with zero width, height, and depth
	 */
	public GameDim() {
		width = new Range<Integer>(0,0);
		height = new Range<Integer>(0,0);
		depth = new Range<Integer>(0,0);
	}
	
	/**
	 * Copies a dimensions object into a newly created one (copy constructor)
	 * @param other the object to copy
	 */
	public GameDim( GameDim other ) {
		width = new Range<Integer>(other.width.min(), other.width.max());
		height = new Range<Integer>(other.height.min(), other.height.max());
		depth = new Range<Integer>(other.depth.min(), other.depth.max());
		units = other.units;
	}
	
	/**
	 * Calculate the sizes of this object
	 * @return an array with [ width, height, depth ] in order
	 */
	public Integer[] size() {
		Integer[] sizes = new Integer[3];
		sizes[0] = width();
		sizes[1] = height();
		sizes[2] = depth();
		
		return sizes;
	}
	
	/**
	 * A convenience method to calculate the width 
	 * @return the width of the current dimensions
	 */
	public Integer width() {
		return width.max() - width.min();
	}

	/**
	 * A convenience method to calculate the height 
	 * @return the height of the current dimensions
	 */
	public Integer height() {
		return height.max() - height.min();
	}

	/**
	 * A convenience method to calculate the depth 
	 * @return the depth of the current dimensions
	 */
	public Integer depth() {
		return depth.max() - depth.min();
	}
}
