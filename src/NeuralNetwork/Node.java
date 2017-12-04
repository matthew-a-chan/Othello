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
	
	int currentConnection=0;
	public Connection[] outConnections;
	
	public Node(int ConnectionNumber) {
		ID=count++;
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
		System.out.println(ID+"::"+value);
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
