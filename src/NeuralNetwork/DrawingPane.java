package NeuralNetwork;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

public class DrawingPane extends JPanel{

	
	Network network;
	public DrawingPane(Network networkThing) {
		network=networkThing;
		JFrame thing=new JFrame();
		thing.add(this);
		thing.setVisible(true);
		thing.setSize(1500, 1000);
	}


	public void paintComponent( Graphics g1 ){
		Graphics2D g = (Graphics2D) g1;
		
		for(Connection a:network.Connections) {
			//a.draw(g);
		}
		
		repaint();
	}


	public void reset(){
		repaint();
	}
}
