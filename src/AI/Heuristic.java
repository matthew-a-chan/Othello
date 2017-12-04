/**
 * @author Stephen C.
 */
package AI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import NeuralNetwork.Network;
import gamecabinet.*;


/**
 * @author Stephen C.
 *
 */
public class Heuristic {
	
	Network network;
	
	public Heuristic(File f) {
		BufferedReader r;
		String s = null;
		try {
			r = new BufferedReader(new FileReader(f));
			s=r.readLine();
		} catch (IOException e) {
			
		}
		String[] weights=s.split(",");
		network=new Network();
		network.train(weights);
	}

	public double calculate(GameState gs) {
		int[] Inputs=new int[64];
		//GET THE INPUT ARRAY FROM THE GAMESTATE
		
		return network.calculate(Inputs);
	}
	
}
//als;dkfj
