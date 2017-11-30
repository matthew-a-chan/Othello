/**
 * @author Stephen C.
 */
package NeuralNetwork;

/**
 * @author Stephen C.
 *
 */
public class Connection {
	
	private static int count=0;
	private int ID;
	
	private Node inputNode;
	private Node outputNode;
	
	private float weight;
	
	public Connection(Node input,Node output) {
		ID=count++;
		inputNode=input;
		outputNode=output;
	}
	
	calculateConnection(double input){
		double value=Math.tanh(input);
		value*=weight;
		outputNode.add(value);
	}

}
