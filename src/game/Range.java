package game;

/**
 * Represents a range of ordered objects
 * @author bsea
 *
 * @param <T> the type this class uses to signify order
 */
public class Range<T extends Comparable<T>> {

	private T min, max;
	
	/**
	 * Create a range with a min and max
	 * @param min the smallest item of this object
	 * @param max the largest item of this object
	 */
	public Range( T min, T max ) {
		if( min.compareTo(max) > 0 ){
			T swap = min;
			min = max;
			max = swap;
		}
		
		this.min = min;
		this.max = max;
	}
	
	public T min(){
		return min;
	}
	
	public T max() {
		return max;
	}
	
	/**
	 * Determine if an element is within the range, inclusive of the min and the max
	 * @param val the element to check
	 * @return true if val is within the Range, false otherwise
	 */
	public boolean contains( T val ){
		return (min.compareTo(val) <= 0 && max.compareTo(val) >= 0);
	}
	
}
