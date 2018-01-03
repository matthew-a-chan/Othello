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
		int depth=10;
		m.to.z=0;
		Integer player=0;
		for(int i=0;i<gs.getPlayers().length;i++){
			if(gs.getPlayers()[i].get(0).getName().equals("A Random Walrus")){
				player=i;
			}
		}

		NegaMaxTree root=new NegaMaxTree(gs, move, player, NN);
		//move=root.returnMove();
		System.err.println("Depth:"+depth+" X:"+move.to.x+" Y:"+move.to.y+" Z:"+move.to.z);
		m.to.x=move.to.x;
		m.to.y=move.to.y;

	}
}