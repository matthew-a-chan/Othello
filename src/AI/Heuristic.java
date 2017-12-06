/**
 * @authors Stephen C. & Jon Wu
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
	Player thisPlayer;

	public Heuristic(File f, Player P) {
		thisPlayer=P;
		BufferedReader r;
		String s = null;
		try {
			r = new BufferedReader(new FileReader(f));
			s=r.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] weights=s.split(",");
		network=new Network();
		network.train(weights);
	}

	public double calculate(GameState gs) {
		int[] Inputs=new int[64];
		//GET THE INPUT ARRAY FROM THE GAMESTATE
		Move move=new Move();
		for(int r=0;r<8;r++) {
			for(int c=0;c<8;c++) {
				move.x=c;
				move.y=r;
				if(gs.getOwner(move)==null) {//If unowned
					Inputs[8*r+c]=0;
				}
				else if(gs.getOwner(move)==thisPlayer) {//If owned by AI -- happy
					Inputs[8*r+c]=1;
				}
				else {//If owned by non-AI -- sad
					Inputs[8*r+c]=-1;
				}
			}
		}



		return network.calculate(Inputs);
	}

}
//als;dkfj
