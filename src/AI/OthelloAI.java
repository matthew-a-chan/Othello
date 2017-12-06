package AI;
import java.util.List;

import gamecabinet.*;

public class OthelloAI extends Player{
	File f = new File();

	MinMaxShrubbery MMtree=new MinMaxShrubbery(f);

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