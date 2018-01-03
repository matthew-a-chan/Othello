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
		return "A Random Walrus";
	}
	
	public String getDescription(){
		return "It's a walrus. What's so difficult?";
	}

	public String getType(){
		return "AI";
	}

	public Move makeMove(GameState gs,Move m){
		MMtree.getMove(gs,m);
		return m;
	}
}