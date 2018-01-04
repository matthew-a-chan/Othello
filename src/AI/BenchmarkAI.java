package AI;

import java.io.File;
import java.util.List;

import Genetics.Individual;
import cabinet.GameState;
import game.Move;
import game.Player;

public class BenchmarkAI extends OthelloAI{

	public BenchmarkAI(Individual individual) {
		super();
		this.i=individual;
	}
	
	@Override
	public void makeMove( GameState gs, List<Move> m ){
		if(gs.getValidMoves().size()>0) {
			m.add(gs.getValidMoves().get((int)(Math.random()*gs.getValidMoves().size())));
		}
	}
}
