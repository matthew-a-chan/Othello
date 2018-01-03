/**
 * @authors Stephen C. & Jon Wu
 */
package AI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import NeuralNetwork.Network;
import cabinet.*;
import game.Location;
import game.Player;


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
		String[] weights=s.split(" ");
		network=new Network();
		network.train(weights);
	}

	public double calculate(GameState gs) {
		int[] Inputs=new int[64];
		//GET THE INPUT ARRAY FROM THE GAMESTATE
		Location location=new Location();
		for(int r=0;r<8;r++) {
			for(int c=0;c<8;c++) {
				location.x=c;
				location.y=r;
				if(gs.getOwner(location).isEmpty()) {//If unowned
					Inputs[8*r+c]=0;
				}
				else if(gs.getOwner(location).get(0).get(0)==thisPlayer) {//If owned by AI -- happy
					Inputs[8*r+c]=1;
				}
				else {//If owned by non-AI -- sad
					Inputs[8*r+c]=-1;
				}
			}
		}

		//System.err.println(network.calculate(Inputs));
		for(int i:Inputs) {
		//	System.out.print(i+" ");
		}
		return network.calculate(Inputs);
	}

}
//als;dkfj
