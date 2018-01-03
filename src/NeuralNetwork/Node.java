/**
 * @author Stephen C.
 */
package NeuralNetwork;

/**
 * @author Stephen C.
 *
 */
public class Node {
	
	
	private double value;//THIS VALUE IS PREREGULATED (Sum of the TanH of all inputs, often > 1 -- Is TanH in Connection.calculate
	
	int currentConnection=0;
	public Connection[] outConnections;
	
	public Node(int ConnectionNumber) {
		outConnections=new Connection[ConnectionNumber];
	}
	
	public Connection addConnection(Node other) {
		Connection connection=new Connection(this,other);
		outConnections[currentConnection]=connection;
		currentConnection++;
		return connection;
	}
	
	public void add(double addend) {
		value+=addend;
	}
	
	public void propagate() {
		for(Connection out:outConnections) {
			out.calculateConnection(value);
		}
	}

	public void clear() {
		value=0;
	}
	
	public double getValue() {
		return value;
	}
	
	
	
	public int x;
	public int y;
}
