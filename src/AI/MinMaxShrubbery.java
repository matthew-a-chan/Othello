package AI;
import java.io.File;

import cabinet.GameState;
import game.*;
import game.Move;

public class MinMaxShrubbery
{
	File f;
	Heuristic a=null;
	
	public MinMaxShrubbery (File f)
	{
		this.f = f;
	}
	
	public void getMove(GameState gs,Move m)
	{
		Move move=new Move();
		int depth=1;
		m.setZ(0);
		Integer player=0;
		for(int i=0;i<gs.getPlayers().size();i++){
			if(gs.getPlayer(i).getName().equals("A Random Walrus")){
				player=i;
			}
		}
		
		if(a==null) {
			a=new Heuristic(f,gs.getPlayer(player));
		}
		
		while(depth<100 && !Thread.interrupted())
		{
			depth++;
			MaxNode root=new MaxNode(gs,Integer.MIN_VALUE,Integer.MAX_VALUE,depth, move, player, 1, a);
			//root.setDepth(depth);
			move=root.returnMove();
			System.err.println("Depth:"+depth+" X:"+move.getX()+" Y:"+move.getY()+" Z:"+move.getZ());
			m.setX(move.getX());
			m.setY(move.getY());
		}
	}
}