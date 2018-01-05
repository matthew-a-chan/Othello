
package NeuralNetwork;

/**
 * @author Stephen C. & Jon Wu
 * a node (Input, Hidden, Output) in the neural network
 */
public class Node {
	
	
	private double value;//THIS VALUE IS PREREGULATED (Sum of the TanH of all inputs, often > 1 -- Is TanH in Connection.calculate
	
	int currentConnection=0;
	public Connection[] outConnections;
	
	public Node(int ConnectionNumber) {
		outConnections=new Connection[ConnectionNumber];
	}
	
	/**
	 * connects this node to the parameter other, other being the output node
	 */
	public Connection addConnection(Node other) {
		Connection connection=new Connection(other);
		outConnections[currentConnection]=connection;
		currentConnection++;
		return connection;
	}
	
	public void add(double addend) {
		value+=addend;
	}
	
	/**
	 * sums up all the values from the input nodes to a hidden node, or the hidden nodes to an output node
	 */
	public void propagate() {
		for(int i=0;i<outConnections.length;i++){//for(Connection out:outConnections) 
			outConnections[i].calculateConnection(value);
		}
	}

	public void clear() {
		value=0;
	}
	
	public double getValue() {
		return value;
	}
	
	
	
	//public int x;
	//public int y;
}
