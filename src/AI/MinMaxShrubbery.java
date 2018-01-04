package AI;
import java.io.File;

import com.sun.corba.se.spi.ior.MakeImmutable;

import cabinet.GameState;
import game.*;
import game.Move;

public class MinMaxShrubbery
{

	Heuristic NN=null;
	File file;
	Player player;


	public MinMaxShrubbery(File f,Player p) {
		this.file=f;
		this.player=p;
		NN=new Heuristic(file,player);
	}


	public void getMove(GameState gs,Move m)
	{
		Move move=new Move();
		m.to.z=0;
		
		NegaMaxTree root=new NegaMaxTree(gs, move, NN);
		//move=root.returnMove();
		//System.err.println("X:"+move.to.x+" Y:"+move.to.y+" Z:"+move.to.z);
		m.to.x=move.to.x;
		m.to.y=move.to.y;
		//System.err.println("X:"+m.to.x+" Y:"+m.to.y+" Z:"+m.to.z);
	}
}