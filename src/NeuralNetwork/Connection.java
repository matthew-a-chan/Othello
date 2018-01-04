/**

 * @author Stephen C.
 * 
 * new comment here!
 
 */
package NeuralNetwork;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * @author Stephen C.
 *
 */
public class Connection {

	private Node outputNode;

	private double weight=0;

	public Connection(Node output) {
		outputNode=output;
	}

	public void calculateConnection(double input){
		double value=Math.tanh(input);
		value*=weight;
		outputNode.add(value);
	}

	public void setWeight(double newWeight) {
		weight=newWeight;
	}

	
	
	
	
	/*public void draw(Graphics2D g) {
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
	}*/
	
}
