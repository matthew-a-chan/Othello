package AI;
import java.util.ArrayList;
import java.util.List;
import gamecabinet.*;

public class MinNode {
	ArrayList<MaxNode> child=new ArrayList<MaxNode>(0);
	Integer depth;
	GameState gs;
	Integer A;
	Integer B;
	Move move;
	List<Move> moves;
	Integer player;

	public MinNode(GameState gs,Integer A,Integer B,Integer depth,Move move,Integer player){
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
			MaxNode newNode;
			while(!moves.isEmpty()&&B>A){
				Move move=moves.get(0);
				moves.remove(0);
				GameState newgs=gs.copyInstance();
				newgs.makeMove(move, move);
				newNode=new MaxNode(newgs,A,B,depth-1,move,player);
				if(swap(newNode.getHeuristicValue())){
					this.move=move;
				}
				child.add(newNode);
			}
		}
	}

	public boolean swap(Integer B){
		if(B<this.B){
			this.B=B;
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
			value+=(10*yourScore*((turn-16)/48))+Math.abs(10*yourScore*((turn-16)/48));
			value-=((10*notYourScore*((turn-16)/48)))+Math.abs((10*notYourScore*((turn-16)/48)));
			value+=(20*moves.size()*((64-turn)/48)+Math.abs(20*moves.size()*((64-turn)/48)));
			Move move=new Move();
			for(int i=0;i<=7;i+=7){
				for(int k=0;k<=7;k+=7){
					move.setX(i);
					move.setY(k);
					move.setZ(0);
					if(gs.getOwner(move)==you){
						value+=31;
					}
					if(gs.getOwner(move)==notYou){
						value-=31;
					}
				}
			}
			return (int)value;
		}
		return B;
	}
}