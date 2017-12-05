/**

 * @author Stephen C.
 * 
 * 
 * 
 */
package NeuralNetwork;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * @author Stephen C.
 *
 */
public class Connection {

	private static int count=0;
	private int ID;

	private Node inputNode;
	private Node outputNode;

	private double weight=0;

	public Connection(Node input,Node output) {
		ID=count++;
		inputNode=input;
		outputNode=output;
		weight=Math.random()-0.5;
	}

	public void calculateConnection(double input){
		double value=Math.tanh(input);
		value*=weight;
		outputNode.add(value);
	}

	public int getID() {
		return ID;
	}

	public void setWeight(double newWeight) {
		weight=newWeight;
	}

	
	
	
	
	public void draw(Graphics2D g) {
		if(weight>0.4) {
			g.setColor(Color.GREEN);
			g.drawLine(inputNode.x, inputNode.y, outputNode.x, outputNode.y);

		}
		else if(weight<-0.4) {
			g.setColor(Color.RED);
			g.drawLine(inputNode.x, inputNode.y, outputNode.x, outputNode.y);

		}
		else {
			//g.setColor(Color.BLACK);
		}
	}

}
