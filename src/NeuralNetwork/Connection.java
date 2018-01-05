package NeuralNetwork;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * @author Stephen C. & Jon Wu
 * the connection between nodes on the neural network
 */
public class Connection {

	private Node outputNode;

	private double weight=0;

	/**
	 * @author Jon Wu & Stephen Chern
	 * 
	 * Initializes a new connection with an output node
	 *
	 * @param output The output node
	 */
	public Connection(Node output) {
		outputNode=output;
	}

	/**
	 * @author Jon Wu & Stephen Chern
	 *
	 * Feeds input through activation function, then pushes to output node
	 *
	 * @param input
	 */
	public void calculateConnection(double input){
		double value=Math.tanh(input);
		value*=weight;
		outputNode.add(value);
	}

	/**
	 * @author Jon Wu & Stephen Chern
	 * 
	 * Sets the weight of the connection
	 *
	 * @param newWeight The new weight of the connection
	 */
	public void setWeight(double newWeight) {
		weight=newWeight;
	}
	
}
