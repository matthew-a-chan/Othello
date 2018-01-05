package AI;
import java.io.File;

import com.sun.corba.se.spi.ior.MakeImmutable;

import cabinet.GameState;
import game.*;
import game.Move;

/**
 * @author Jon Wu & Stephen Chern
 * 
 * makes a NegaMaxTree and passes in current gamestate, as well as depth and neural network
 * makes the heuristic off the given file
 * if no file, then uses the hard-coded heuristic
 */
public class MinMaxShrubbery
{

	Heuristic NN=null;
	File file;
	Player player;
	private boolean isEvolving;

	/**
	 * @author Jon Wu & Stephen Chern
	 *
	 * @param f The file to create the NegaMax Heuristic off of
	 * @param p The player containing this tree
	 */
	public MinMaxShrubbery(File f,Player p) {
		this.file=f;
		this.player=p;
		isEvolving=true;
		NN=new Heuristic(file,player);
	}

	/**
	 * @author Jon Wu & Stephen Chern
	 *
	 * @param p The player containing this tree
	 */
	public MinMaxShrubbery(Player p) {
		this.player=p;
		isEvolving=false;
		NN=new Heuristic(player);
	}

	
	/**
	 * @author Jon Wu & Stephen Chern
	 *
	 * @param gs The current GameState
	 * @param m The move to be returned after calculating from NegaMax
	 */
	public void getMove(GameState gs,Move m)
	{
		Move move=new Move();
		m.to.z=0;

		if(isEvolving) {
			NegaMaxTree root=new NegaMaxTree(gs,move,NN);
		}
		else {
			NegaMaxTree root=new NegaMaxTree(gs, 6, move, NN);
		}
		//move=root.returnMove();
		//System.err.println("X:"+move.to.x+" Y:"+move.to.y+" Z:"+move.to.z);
		m.to.x=move.to.x;
		m.to.y=move.to.y;
		//System.err.println("X:"+m.to.x+" Y:"+m.to.y+" Z:"+m.to.z);
	}
}