package AI;
import java.util.ArrayList;
import java.util.List;
import gamecabinet.*;

public class MaxNode {
	ArrayList<MinNode> child=new ArrayList<MinNode>(0);
	Integer depth;
	GameState gs;
	Integer A;
	Integer B;
	Move move;
	List<Move> moves;
	Integer player;

	public MaxNode(GameState gs,Integer A,Integer B,Integer depth,Move move,Integer player){
		this.gs=gs;
		this.A=A;
		this.B=B;
		this.depth=depth;
		this.player=player;
		makeMoves();
	}

	public void makeMoves(){
		if(depth>0){
			moves=gs.getValidMoves();
			MinNode newNode;
			while(!moves.isEmpty()&&B>A){
				Move move=moves.get(0);
				moves.remove(0);
				GameState newgs=gs.copyInstance();
				newgs.makeMove(move, move);
				newNode=new MinNode(newgs,A,B,depth-1,move,player);
				if(swap(newNode.getHeuristicValue())){
					this.move=move;
				}
				child.add(newNode);
			}
		}
	}

	public boolean swap(Integer A){
		if(A>this.A){
			this.A=A;
			return true;
		}
		return false;
	}

	public Move returnMove(){
		return move;
	}

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
		if(depth==0){//raise moves
			
			
			
			
			return (int)value;
		}
		return A;
	}
}