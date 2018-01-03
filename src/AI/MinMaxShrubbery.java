package AI;
import java.io.File;

import cabinet.GameState;
import game.*;
import game.Move;

public class MinMaxShrubbery
{

	Heuristic a=null;


	public void getMove(GameState gs,Move m)
	{
		Move move=new Move();
		int depth=10;
		m.to.setZ(0);
		Integer player=0;
		for(int i=0;i<gs.numPlayers().length;i++){
			if(gs.numPlayers()[i].getName().equals("A Random Walrus")) //find way to get names of players in a game
			{
				player=i;
			}
		}

		if(a==null) {
			a=new Heuristic(gs.getPlayer(individual.getFile(),player));
		}

		MaxNode root=new MaxNode(gs, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, move, player, 1, a);
		//root.setDepth(depth);
		move=root.returnMove();
		System.err.println("Depth:"+depth+" X:"+move.to.getX()+" Y:"+move.to.getY()+" Z:"+move.to.getZ());
		m.to.setX(move.to.getX());
		m.to.setY(move.to.getY());

	}
}