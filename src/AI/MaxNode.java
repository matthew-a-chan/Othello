package AI;
import java.util.ArrayList;
import java.util.List;
import gamecabinet.*;

public class MaxNode {
	ArrayList<MaxNode> child=new ArrayList<MaxNode>(0);
	Integer depth;
	GameState gs;
	Integer A;
	Integer B;
	Move move;
	List<Move> moves;
	Integer player;
	Integer color; //1 for AI -1 for opponent
	Double bestMove;
	Heuristic h;

	public MaxNode(GameState gs,Integer A,Integer B,Integer depth,Move move,Integer player, Integer color, Heuristic h){
		this.gs=gs;
		this.A=A;
		this.B=B;
		this.depth=depth;
		this.player=player;
		this.color = color;
		this.h = h;
		makeMoves(gs, depth, color);
	}

	public double makeMoves(GameState gs, Integer depth, Integer color)
	{

		if (depth == 0)
		{
			return color * h.calculate(gs); //value
		}

		moves=gs.getValidMoves();  
		bestMove = Double.MIN_VALUE;
		while(!moves.isEmpty() && B>A) //modify beta and alpha values
		{
			Move move=moves.get(0);
			moves.remove(0);
			GameState newgs=gs.copyInstance();
			newgs.makeMove(move, move);
			
			double m = -makeMoves(newgs, depth--, -color);
			
			if (bestMove < m)
			{
				bestMove = m;
			}

			/*
				newgs.makeMove(move, move);
				newNode=new MaxNode(newgs,A,B,depth-1,move,player, -color);
				if(swap(newNode.getHeuristicValue()))
				{
					this.move=move;
				}
				child.add(newNode);
			 */
		}
		return bestMove;
	}
	
	public Move returnMove()
	{
		return move;
	}
	
/*
	public boolean swap(Integer A){
		if(A>this.A){
			this.A=A;
			return true;
		}
		return false;
	}
*/
	
/*
	public Integer getHeuristicValue(){
		double value=0;

		Player you=gs.getPlayer(player);
		Player notYou=gs.getPlayer(Math.abs(player-1));
		int yourScore=gs.getScore(you);
		int	notYourScore=gs.getScore(notYou);

		List<Move>moves=gs.getValidMoves();
		int turn=gs.numTurn();
		if(gs.isGameOver()){
			if(yourScore>notYourScore){
				return Integer.MAX_VALUE;
			}
			else{
				return Integer.MIN_VALUE;
			}
		}

		if(depth==0)
		{//raise moves			
			return (int)value * color;
		}
		return A;
	}
	*/
}