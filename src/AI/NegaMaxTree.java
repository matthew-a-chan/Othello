package AI;
import java.util.ArrayList;
import java.util.List;

import Genetics.Playground;
import cabinet.*;
import game.Move;

public class NegaMaxTree {

	GameState gs;
	Move move;
	List<Move> moves;
	Integer player;
	Integer color; //1 for AI -1 for opponent
	//Double bestMove;
	Heuristic h;

	public NegaMaxTree(GameState gs,Move move,Integer player, Heuristic h){
		this.gs=gs;
		this.player=player;
		this.h = h;
		this.move=move;
		makeMoves(gs, 3, Double.MIN_VALUE, Double.MAX_VALUE, 1,true);
	}

	public double makeMoves(GameState gs, Integer depth,double A,double B, Integer color,boolean isRoot)
	{
		System.out.println(isRoot);
		if (depth == 0 || gs.isGameOver())
		{
			return color * h.calculate(gs); //value
		}

		moves=gs.getValidMoves();
		double bestMove = -1000000000.0;
		while(!moves.isEmpty()) //modify beta and alpha values
		{
			Move move=moves.remove(0);
			GameState newgs=gs.copyInstance();
			ArrayList<Move> moves=new ArrayList<Move>();
			moves.add(move);
			newgs.makeMove(moves);

			double m = -makeMoves(newgs, depth-1,-B,-A, -color,false);

			if(m>bestMove) {
				System.out.println("BEST MOVE YET"+move.to.x+"::"+move.to.y+"::"+isRoot);
				bestMove=m;
				if(isRoot) {
					System.err.println("CHANGING");
					this.move.to.x=move.to.x;
					this.move.to.y=move.to.y;
				}
			}
			A=Math.max(A, bestMove);

			if(A>=B) {
				break;
			}
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