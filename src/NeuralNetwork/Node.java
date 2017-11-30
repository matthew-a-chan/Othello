/**
 * @author Stephen C.
 */
package NeuralNetwork;

/**
 * @author Stephen C.
 *
 */
public class Node {
	
	private static int count=0;
	private int ID;
	
	private double value;//THIS VALUE IS PREREGULATED (Sum of the TanH of all inputs, often > 1 -- Is TanH in Connection.calculate
	
	public Node() {
		ID=count++;
	}
	
	public void add(double addend) {
		value+=addend;
	}
	
	public void propagate() {
		for(Connection out:outConnections) {
			out.calculateConnection(value);
		}
	}
}
