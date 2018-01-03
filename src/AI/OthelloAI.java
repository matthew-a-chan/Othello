package AI;
import java.io.File;
import java.util.List;

import Genetics.Individual;
import cabinet.GameState;
import game.*;

public class OthelloAI extends Player{
	
	File f;
	Individual i=null;

	MinMaxShrubbery MMtree;

	public OthelloAI(File file) {
		MMtree=new MinMaxShrubbery(file,(Player)this);
	}
	
	public OthelloAI(File file,Individual indiv) {
		this(file);
		this.i=indiv;
	}
	
	public Individual getIndividual() {
		return i;
	}

	public boolean isHuman(){
		return false;
	}

	public String getName(){
		return "DaOthelloAI";
	}
	
	public String getDescription(){
		return "This is probably not going to work";
	}

	public String getType(){
		return "AI";
	}

	@Override
	public void makeMove(GameState gs,List<Move> m){
		Move move=null;
		m.add(move=new Move());
		MMtree.getMove(gs,move);
		if(move.to.x<0||move.to.y<0) {
			move=gs.getValidMoves().get(0);
			System.err.println("COULDNT SEARCH:: RANDOM MOVE");
		}
		//return m.get(0);
	}
}